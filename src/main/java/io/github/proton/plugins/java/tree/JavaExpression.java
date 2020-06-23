package io.github.proton.plugins.java.tree;

public interface JavaExpression {
    static JavaExpression fromIdentifier(JavaIdentifier identifier) {
        try {
            return new Int(Integer.parseInt(identifier.toString()));
        } catch (NumberFormatException ignored) {
        }
        return new Identifier(identifier);
    }

    boolean isEmpty();

    final record Int(int integer) implements JavaExpression {
        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    final record Identifier(JavaIdentifier identifier) implements JavaExpression {
        public Identifier(String string) {
            this(new JavaIdentifier(string));
        }

        @Override
        public boolean isEmpty() {
            return identifier.isEmpty();
        }
    }
}
