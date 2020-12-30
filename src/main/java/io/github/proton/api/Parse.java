package io.github.proton.api;

import java.util.function.Predicate;

public final class Parse {
    public final CharSequence input;
    public int pos = 0;

    public Parse(CharSequence input) {
        this.input = input;
    }

    public char consume(char c) {
        return consume(x -> x == c);
    }

    public char consume(Predicate<Character> p) {
        if (input.length() > pos && p.test(input.charAt(pos))) {
            pos++;
            return input.charAt(pos - 1);
        }
        return 0;
    }
}
