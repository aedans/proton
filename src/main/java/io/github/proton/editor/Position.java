package io.github.proton.editor;

public record Position(int row, int col) {
    public Position withRow(int row) {
        return new Position(row, col);
    }

    public Position withCol(int col) {
        return new Position(row, col);
    }

    public Position withRelativeRow(int row) {
        return withRow(this.row + row);
    }

    public Position withRelativeCol(int col) {
        return withCol(this.col + col);
    }
}
