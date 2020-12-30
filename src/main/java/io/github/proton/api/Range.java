package io.github.proton.api;

import io.github.proton.api.map.*;

public final class Range {
    public final Position start;
    public final Position end;

    public Range(PositionMap positions, org.eclipse.lsp4j.Range range) {
        this(positions.get(range.getStart()), positions.get(range.getEnd()));
    }

    public Range(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public org.eclipse.lsp4j.Range toLsp() {
        return new org.eclipse.lsp4j.Range(start.toLsp(), end.toLsp());
    }

    public Range src(PositionMap positionMap, SourceMap sourceMap) {
        return new Range(start.src(positionMap, sourceMap), end.src(positionMap, sourceMap));
    }

    public Range dest(PositionMap positionMap, SourceMap sourceMap) {
        return new Range(start.dest(positionMap, sourceMap), end.dest(positionMap, sourceMap));
    }
}
