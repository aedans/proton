package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.expr.SimpleName;
import io.github.proton.editor.Text;
import io.vavr.collection.Vector;

public record JavaSimpleName(Vector<Character> chars) {
    public JavaSimpleName(String name) {
        this(new Text(name));
    }

    public JavaSimpleName(Text text) {
        this(text.chars());
    }

    public static JavaSimpleName from(SimpleName name) {
        return new JavaSimpleName(name.getIdentifier());
    }

    public static boolean isValid(char c) {
        return !Character.isWhitespace(c);
    }

    public boolean isEmpty() {
        return chars.isEmpty();
    }

    @Override
    public String toString() {
        return new Text(chars).toString();
    }
}
