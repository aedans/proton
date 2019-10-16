package io.github.proton.plugins.txt;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.github.proton.plugins.list.FocusableObservableUpdater;
import io.reactivex.rxjava3.core.Maybe;

public final class TextLineUpdater implements Updater.Same<TextLine> {
    public final FocusableObservableUpdater<Character> updater;

    public TextLineUpdater(FocusableObservableUpdater<Character> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<TextLine> update(TextLine textLine, KeyStroke keyStroke) {
        return updater.update(textLine.chars, keyStroke).map(TextLine::new);
    }
}
