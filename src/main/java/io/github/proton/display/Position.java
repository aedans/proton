/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.display;

import java.util.Objects;

public final class Position {
    public final int col;
    public final int row;

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public Position withRow(int row) {
        return new Position(col, row);
    }

    public Position withColumn(int col) {
        return new Position(col, row);
    }

    public Position withRelativeRow(int row) {
        return withRow(this.row + row);
    }

    public Position withRelativeColumn(int col) {
        return withColumn(this.col + col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
