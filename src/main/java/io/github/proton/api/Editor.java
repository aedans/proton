package io.github.proton.api;

import io.github.proton.api.Highlighter.Highlight;
import org.eclipse.lsp4j.*;
import org.reactfx.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public interface Editor {
    void update(Change change);

    Collection<Highlight> highlight();

    EventStream<Collection<Diagnostic>> diagnostics();

    boolean triggerCompletion(String string);

    CompletableFuture<Collection<Completion>> completions(Position position);

    CompletableFuture<String> hover(Position position);
}
