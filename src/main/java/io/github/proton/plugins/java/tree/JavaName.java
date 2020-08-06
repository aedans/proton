package io.github.proton.plugins.java.tree;

import com.sun.source.tree.*;
import com.sun.source.util.SimpleTreeVisitor;
import io.github.proton.editor.Text;
import io.vavr.collection.Vector;

import javax.lang.model.element.Name;

public record JavaName(Vector<Character> chars) {
    public JavaName(String name) {
        this(new Text(name));
    }

    public JavaName(Text text) {
        this(text.chars());
    }

    public JavaName(Name name) {
        this(name.toString());
    }

    public static JavaName from(Tree tree) {
        return new SimpleTreeVisitor<JavaName, Object>() {
            @Override
            public JavaName visitIdentifier(IdentifierTree node, Object o) {
                return new JavaName(node.getName());
            }
        }.visit(tree, null);
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
