package io.github.proton.plugins.java.tree;

public interface JavaExpression {
    static JavaExpression fromIdentifier(JavaIdentifier identifier) {
        try {
            return new Int(Integer.parseInt(identifier.toString()));
        } catch (NumberFormatException ignored) {
        }
        return new Identifier(identifier);
    }

    final record Int(int integer) implements JavaExpression {

    }

    final record Identifier(JavaIdentifier identifier) implements JavaExpression {
        public Identifier(String string) {
            this(new JavaIdentifier(string));
        }
    }
}
