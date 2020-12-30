package io.github.proton.ui;

import io.github.proton.api.Completion;
import javafx.scene.control.*;
import org.reactfx.EventSource;

public final class CompletionUI extends ListView<Completion> {
    public final EventSource<Completion> completed = new EventSource<>();

    public CompletionUI() {
        setOnKeyTyped(event -> {
            if (event.getCharacter().equals("\r") || event.getCharacter().equals("\n")) {
                completed.push(getSelectionModel().getSelectedItem());
            }
        });

        setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Completion completion, boolean empty) {
                super.updateItem(completion, empty);
                if (completion != null) {
                    setText(completion.label);
                } else {
                    setText("");
                }
            }
        });
    }
}
