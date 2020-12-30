package io.github.proton.api;

import io.github.proton.api.map.*;
import org.eclipse.lsp4j.*;

public final class Change {
    public final Range range;
    public final String inserted;

    public Change(PositionMap positionMap, TextEdit edit) {
        this(new Range(positionMap, edit.getRange()), edit.getNewText());
    }

    public Change(Position start, Position end, String inserted) {
        this(new Range(start, end), inserted);
    }

    public Change(Range range, String inserted) {
        this.range = range;
        this.inserted = inserted;
    }

    public int length() {
        return range.end.index - range.start.index;
    }

    public TextDocumentContentChangeEvent toLsp() {
        return new TextDocumentContentChangeEvent(range.toLsp(), length(), inserted);
    }

    public Change src(PositionMap positionMap, SourceMap sourceMap) {
        return new Change(range.src(positionMap, sourceMap), inserted);
    }

    public Change dest(PositionMap positionMap, SourceMap sourceMap) {
        return new Change(range.dest(positionMap, sourceMap), inserted);
    }
}
