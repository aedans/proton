package io.github.proton.ui;

import org.eclipse.lsp4j.*;

public final class LspUtil {
    public static boolean inRange(Position position, Range range) {
        return greaterThan(range.getStart(), position) && lessThan(range.getEnd(), position);
    }

    public static boolean greaterThan(Position start, Position position) {
        if (start.getLine() < position.getLine()) {
            return true;
        } else if (start.getLine() > position.getLine()) {
            return false;
        } else {
            return start.getCharacter() <= position.getCharacter();
        }
    }

    public static boolean lessThan(Position end, Position position) {
        if (end.getLine() < position.getLine()) {
            return false;
        } else if (end.getLine() > position.getLine()) {
            return true;
        } else {
            return end.getCharacter() > position.getCharacter();
        }
    }
}
