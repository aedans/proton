package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

public record TextProjection(Text text, String style, boolean edit) implements Projection.Delegate<Text> {
    public static Projection<Text> space = label(" ", "punctuation.space");
    public static Projection<Text> openParen = label("(", "punctuation.paren");
    public static Projection<Text> closeParen = label(")", "punctuation.paren");
    public static Projection<Text> openBracket = label("{", "punctuation.bracket");
    public static Projection<Text> closeBracket = label("}", "punctuation.bracket");
    public static Projection<Text> dot = label(".", "punctuation.dot");
    public static Projection<Text> comma = label(",", "punctuation.comma");

    public static Projection<Text> text(String text, String scope) {
        return text(new Text(text), scope);
    }

    public static Projection<Text> text(Text text, String scope) {
        return new TextProjection(text, scope, true);
    }

    public static Projection<Text> label(String text, String scope) {
        return label(new Text(text), scope);
    }

    public static Projection<Text> label(Text text, String scope) {
        return new TextProjection(text, scope, false);
    }

    @Override
    public Projection<Text> delegate() {
        var trail = Char.<Text>trailing()
            .withEdit(edit)
            .withInsert(character -> Option.some(new Text(text.chars().append(character))));
        Vector<Char<Text>> chars = text.chars().zipWithIndex((c, i) -> Char.<Text>empty(text.chars().get(i))
            .withEdit(edit)
            .withInsert(character -> Option.some(new Text(text.chars().insert(i, character))))
            .withDelete(() -> Option.some(new Text(text.chars().removeAt(i))))
            .withStyle(style));
        return Projection.chars(chars.append(trail));
    }
}
