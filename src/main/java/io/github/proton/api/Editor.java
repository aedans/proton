package io.github.proton.api;

import org.eclipse.lsp4j.Diagnostic;
import org.reactfx.EventStream;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface Editor {
    void update(Change change);

    Collection<Highlight> highlights();

    EventStream<Collection<Diagnostic>> diagnostics();

    boolean triggerCompletion(String string);

    CompletableFuture<Collection<Completion>> completions(Position position);

    CompletableFuture<String> hover(Position position);
}
