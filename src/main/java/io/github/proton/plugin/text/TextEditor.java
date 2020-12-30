package io.github.proton.plugin.text;

import io.github.proton.api.*;
import io.github.proton.api.Change;
import org.eclipse.lsp4j.Diagnostic;
import org.reactfx.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class TextEditor implements Editor {
    @Override
    public void update(Change change) {

    }

    @Override
    public Collection<Highlighter.Highlight> highlight() {
        return Collections.emptyList();
    }

    @Override
    public EventStream<Collection<Diagnostic>> diagnostics() {
        return new EventSource<>();
    }

    @Override
    public boolean triggerCompletion(String string) {
        return false;
    }

    @Override
    public CompletableFuture<Collection<Completion>> completions(Position position) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    @Override
    public CompletableFuture<String> hover(Position position) {
        return CompletableFuture.completedFuture("");
    }
}
