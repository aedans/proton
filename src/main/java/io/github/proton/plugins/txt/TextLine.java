package io.github.proton.plugins.txt;

import io.github.proton.plugins.list.FocusableObservable;

public final class TextLine {
    public final FocusableObservable<Character> chars;

    public TextLine(FocusableObservable<Character> chars) {
        this.chars = chars;
    }
}
