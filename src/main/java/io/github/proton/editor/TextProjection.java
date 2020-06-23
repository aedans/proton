package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

public record TextProjection(Text text, String scope, boolean decorative) implements Projection<Text> {
    public static Projection<Text> space = label(" ", "punctuation.space");
    public static Projection<Text> openParen = label("(", "punctuation.paren");
    public static Projection<Text> closeParen = label(")", "punctuation.paren");
    public static Projection<Text> openBracket = label("{", "punctuation.bracket");
    public static Projection<Text> closeBracket = label("}", "punctuation.bracket");

    public static Projection<Text> text(String text, String scope) {
        return text(new Text(text), scope);
    }

    public static Projection<Text> text(Text text, String scope) {
        return new TextProjection(text, scope, false);
    }

    public static Projection<Text> label(String text, String scope) {
        return label(new Text(text), scope);
    }

    public static Projection<Text> label(Text text, String scope) {
        return new TextProjection(text, scope, true);
    }

    @Override
    public Result<Text> project(int width, boolean fit, int space, int position, int indent) {
        var length = text.chars().length();
        var trail = new Char<Text>() {
            @Override
            public boolean decorative() {
                return decorative;
            }

            @Override
            public boolean mergeable() {
                return true;
            }

            @Override
            public StyledCharacter character(Style style) {
                return style.base(' ');
            }

            @Override
            public Option<Text> insert(char character) {
                return Option.some(new Text(text.chars().append(character)));
            }

            @Override
            public Option<Text> delete() {
                return Option.none();
            }
        };
        Vector<Char<Text>> chars = text.chars().zipWithIndex((c, i) -> new Char<Text>() {
            @Override
            public boolean decorative() {
                return decorative;
            }

            @Override
            public boolean mergeable() {
                return false;
            }

            @Override
            public StyledCharacter character(Style style) {
                return style.style(scope, text.chars().get(i));
            }

            @Override
            public Option<Text> insert(char character) {
                return Option.some(new Text(text.chars().insert(i, character)));
            }

            @Override
            public Option<Text> delete() {
                return Option.some(new Text(text.chars().removeAt(i)));
            }
        });
        return new Result<>(space - length, position + length, chars.append(trail));
    }
}
