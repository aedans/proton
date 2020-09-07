package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.expr.SimpleName;
import io.github.proton.editor.*;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public record JavaSimpleName(Vector<Character> chars) implements Tree<JavaSimpleName> {
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

    @Override
    public boolean isEmpty() {
        return chars.isEmpty();
    }

    @Override
    public Projection<JavaSimpleName> project() {
        return TextProjection.text(new Text(chars), "").mapChar(c -> c.modify(
            character -> JavaSimpleName.isValid(character)
                ? c.insert(character).map(JavaSimpleName::new)
                : Option.none(),
            () -> c.delete().map(JavaSimpleName::new)));
    }

    @Override
    public String toString() {
        return new Text(chars).toString();
    }
}
