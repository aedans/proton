package io.github.proton.plugin.java;

import io.github.proton.api.*;
import io.github.proton.api.Position;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.reactfx.EventStream;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class JavaEditor implements Editor {
    private final JavaPatternHighlighter highlighter = new JavaPatternHighlighter();
    private final VersionedTextDocumentIdentifier identifier;
    private final StringBuilder string = new StringBuilder();
    private PositionMap positions = new PositionMap(string.toString());

    public JavaEditor(File file) {
        this(new VersionedTextDocumentIdentifier(file.toURI().toString(), 0));
        TextDocumentItem textDocument = new TextDocumentItem(identifier.getUri(), "java", identifier.getVersion(), "");
        JavaLanguageServer.server.getTextDocumentService().didOpen(new DidOpenTextDocumentParams(textDocument));
    }

    public JavaEditor(VersionedTextDocumentIdentifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public void update(Change change) {
        identifier.setVersion(identifier.getVersion() + 1);
        JavaLanguageServer.server.getTextDocumentService().didChange(new DidChangeTextDocumentParams(identifier, Collections.singletonList(change.toLsp())));
        string.replace(change.range.start.index, change.range.end.index, change.inserted);
        positions = new PositionMap(string);
    }

    @Override
    public Collection<Highlight> highlights() {
        return highlighter.highlights(string);
    }

    @Override
    public EventStream<Collection<Diagnostic>> diagnostics() {
        return JavaLanguageServer.diagnostics.map(x -> x);
    }

    @Override
    public boolean triggerCompletion(String string) {
        return Character.isLetterOrDigit(string.charAt(0)) ||
            JavaLanguageServer.initializeResult.getCapabilities().getCompletionProvider().getTriggerCharacters().contains(string);
    }

    @Override
    public CompletableFuture<Collection<Completion>> completions(Position position) {
        return JavaLanguageServer.server.getTextDocumentService().completion(new CompletionParams(identifier, position.toLsp()))
            .thenApply(completions -> completions.isLeft() ? completions.getLeft() : completions.getRight().getItems())
            .thenApply(completions -> completions.stream().map(x -> new Completion(positions, x)).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<String> hover(Position position) {
        return JavaLanguageServer.server.getTextDocumentService().hover(new HoverParams(identifier, position.toLsp()))
            .thenApply(hover -> {
                StringBuilder text = new StringBuilder();
                if (hover.getContents().isRight()) {
                    text.append(hover.getContents().getRight().getValue());
                } else {
                    for (@SuppressWarnings("deprecation") Either<String, MarkedString> either : hover.getContents().getLeft()) {
                        if (either.isLeft()) {
                            text.append(either.getLeft()).append("\n\n");
                        } else {
                            text.append(either.getRight().getValue()).append("\n\n");
                        }
                    }
                }
                return text.toString().trim();
            });
    }
}
