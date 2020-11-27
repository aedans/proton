package io.github.proton.api;

public final class Position {
    public final int index;
    public final int line;
    public final int character;

    public Position(int index, int line, int character) {
        this.index = index;
        this.line = line;
        this.character = character;
    }

    public org.eclipse.lsp4j.Position toLsp() {
        return new org.eclipse.lsp4j.Position(line, character);
    }

    public Position src(PositionMap positions, SourceMap sourceMap) {
        return positions.get(sourceMap.src(index));
    }

    public Position dest(PositionMap positions, SourceMap sourceMap) {
        return positions.get(sourceMap.dest(index));
    }

    @Override
    public String toString() {
        return "(" + line + "," + character + ")";
    }
}
