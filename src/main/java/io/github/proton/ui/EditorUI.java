package io.github.proton.ui;

import com.sandec.mdfx.MDFXNode;
import io.github.proton.Proton;
import io.github.proton.api.*;
import io.github.proton.api.editor.Editor;
import io.github.proton.api.highlight.Highlight;
import io.github.proton.api.util.LspUtil;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.stage.Popup;
import org.eclipse.lsp4j.Diagnostic;
import org.fxmisc.richtext.*;
import org.fxmisc.richtext.event.MouseOverTextEvent;
import org.fxmisc.richtext.model.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.*;

public final class EditorUI extends GenericStyledArea<String, EditorUI.Segment, String> {
    public static final class Segment {
        public final String text;

        public Segment(String text) {
            this.text = text;
        }
    }

    public static final TextOps<EditorUI.Segment, String> textOps = new TextOps<>() {
        @Override
        public Segment create(String text) {
            return new Segment(text);
        }

        @Override
        public int length(Segment segment) {
            return segment.text.length();
        }

        @Override
        public char charAt(Segment segment, int index) {
            return segment.text.charAt(index);
        }

        @Override
        public String getText(Segment segment) {
            return segment.text;
        }

        @Override
        public Segment subSequence(Segment segment, int start, int end) {
            return new Segment(segment.text.substring(start, end));
        }

        @Override
        public Segment subSequence(Segment segment, int start) {
            return new Segment(segment.text.substring(start));
        }

        @Override
        public Optional<Segment> joinSeg(Segment currentSeg, Segment nextSeg) {
            return Optional.of(new Segment(currentSeg.text + nextSeg.text));
        }

        @Override
        public Segment createEmptySeg() {
            return new Segment("");
        }
    };

    public EditorUI(Editor editor, String content) {
        super(
            "",
            (textFlow, value) -> textFlow.getStyleClass().add(value),
            "",
            new GenericEditableStyledDocument<>("", "", textOps),
            textOps,
            false,
            (segment) -> StyledTextArea.createStyledTextNode(segment.getSegment().text, segment.getStyle(), Node::setId)
        );

        Popup diagnosticPopup = new Popup();
        setMouseOverTextDelay(Duration.ofMillis(250));

        AtomicReference<Iterable<Diagnostic>> diagnostics = new AtomicReference<>(Collections.emptyList());
        editor.diagnostics().subscribe(diagnostics::set);

        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, event -> {
            io.github.proton.api.Position position = position(this, event.getCharacterIndex());
            editor.hover(position).whenComplete((hover, throwable) -> {
                Point2D screenPosition = event.getScreenPosition();
                StringBuilder text = new StringBuilder();
                for (Diagnostic diagnostic : diagnostics.get()) {
                    if (LspUtil.inRange(position.toLsp(), diagnostic.getRange())) {
                        text.append(diagnostic.getMessage()).append("\n\n");
                    }
                }
                text.append(hover);
                if (!text.toString().isEmpty()) {
                    MDFXNode mdfx = new MDFXNode(text.toString());
                    mdfx.getStylesheets().add(Proton.class.getResource("mdfx.css").toExternalForm());
                    mdfx.setMaxWidth(500);
                    diagnosticPopup.getContent().setAll(mdfx);
                    Platform.runLater(() -> {
                        diagnosticPopup.show(this, screenPosition.getX(), screenPosition.getY());
                        diagnosticPopup.requestFocus();
                    });
                }
            });
        });

        addEventHandler(MouseEvent.MOUSE_MOVED, event -> Platform.runLater(diagnosticPopup::hide));

        Pattern whiteSpace = Pattern.compile("^\\s+");
        addEventHandler(KeyEvent.KEY_PRESSED, KE -> {
            if (KE.getCode() == KeyCode.ENTER) {
                Matcher m = whiteSpace.matcher(getParagraph(getCurrentParagraph() - 1).getSegments().get(0).text);
                if (m.find()) Platform.runLater(() -> insertText(getCaretPosition(), m.group()));
            }
        });

        Popup completionPopup = new Popup();
        CompletionUI completions = new CompletionUI();
        completionPopup.getContent().add(completions);
        completionPopup.setAutoHide(true);

        completions.setOnKeyPressed(event -> {
            if (completionPopup.isShowing() && (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT)) {
                completionPopup.hide();
                if (event.isConsumed()) {
                    fireEvent(event);
                }
            }
        });

        completions.completed.subscribe(completionItem -> {
            MultiChangeBuilder<String, Segment, String> multiChange = createMultiChange();
            if (completionItem.importChanges != null) {
                completionItem.importChanges.forEach(textEdit -> doChange(multiChange, textEdit));
            }
            doChange(multiChange, completionItem.completionChange);
            multiChange.commit();
        });

        setOnKeyTyped(event -> {
            if (!event.getCharacter().isEmpty() && editor.triggerCompletion(event.getCharacter())) {
                editor.completions(position(this, getCaretPosition()))
                    .whenComplete((items, throwable) -> Platform.runLater(() -> {
                        if (throwable != null)
                            throwable.printStackTrace();
                        completions.getItems().setAll(items);
                        if (!completions.getItems().isEmpty()) {
                            getCaretBounds().ifPresent(caretBounds -> completionPopup.show(this, caretBounds.getMinX(), caretBounds.getMaxY()));
                        } else {
                            completionPopup.hide();
                        }
                    }));
            } else {
                completionPopup.hide();
            }
        });

        setOnMouseClicked(event -> completionPopup.hide());

        AtomicReference<ReadOnlyStyledDocument<String, Segment, String>> snapshot = new AtomicReference<>(getContent().snapshot());

        multiPlainChanges().subscribe(changes -> {
            for (PlainTextChange change : changes) {
                if (change.getPosition() <= getCaretPosition() && change.getInsertionEnd() >= getCaretPosition()) {
                    getCaretSelectionBind().moveTo(change.getInsertionEnd());
                }
            }
        });

        plainTextChanges().subscribe(change -> {
            Range range = new Range(position(snapshot.get(), change.getPosition()), position(snapshot.get(), change.getRemovalEnd()));
            editor.update(new Change(range, change.getInserted()));
            snapshot.set(getContent().snapshot());
        });

        plainTextChanges().subscribe(change -> {
            Collection<Highlight> highlights = editor.highlights();

            if (highlights.isEmpty()) {
                return;
            }

            int lastEnd = 0;
            StyleSpansBuilder<String> spansBuilder = new StyleSpansBuilder<>();
            for (Highlight highlight : highlights) {
                spansBuilder.add("", highlight.start - lastEnd);
                spansBuilder.add(highlight.type.toLowerCase(), highlight.end - highlight.start);
                lastEnd = highlight.end;
            }

            clearStyle(0, getLength());

            setStyleSpans(0, spansBuilder.create());
        });

        replaceText(content);
    }

    private void doChange(MultiChangeBuilder<String, Segment, String> multiChange, Change change) {
        multiChange.replaceText(
            getAbsolutePosition(change.range.start.line, change.range.start.character),
            getAbsolutePosition(change.range.start.line, change.range.end.character),
            change.inserted);
    }

    private static io.github.proton.api.Position position(TwoDimensional t, int pos) {
        Position position = t.offsetToPosition(pos, Bias.Forward);
        return new io.github.proton.api.Position(pos, position.getMajor(), position.getMinor());
    }
}
