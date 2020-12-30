package io.github.proton.api;

import io.github.proton.api.map.*;
import org.eclipse.lsp4j.CompletionItem;

import java.util.*;
import java.util.stream.Collectors;

public final class Completion {
    public final String label;
    public final Change completionChange;
    public final Collection<Change> importChanges;

    public Completion(PositionMap positionMap, CompletionItem completion) {
        this(
            completion.getLabel(),
            new Change(positionMap, completion.getTextEdit()),
            completion.getAdditionalTextEdits() == null
                ? Collections.emptyList()
                : completion.getAdditionalTextEdits().stream().map(x -> new Change(positionMap, x)).collect(Collectors.toList()));
    }

    public Completion(String label, Change completionChange, Collection<Change> importChanges) {
        this.label = label;
        this.completionChange = completionChange;
        this.importChanges = importChanges;
    }

    public Completion src(PositionMap positionMap, SourceMap sourceMap) {
        return new Completion(
            label,
            completionChange.src(positionMap, sourceMap),
            importChanges.stream().map(x -> x.src(positionMap, sourceMap)).collect(Collectors.toList()));
    }

    public Completion dest(PositionMap positionMap, SourceMap sourceMap) {
        return new Completion(
            label,
            completionChange.dest(positionMap, sourceMap),
            importChanges.stream().map(x -> x.dest(positionMap, sourceMap)).collect(Collectors.toList()));
    }
}
