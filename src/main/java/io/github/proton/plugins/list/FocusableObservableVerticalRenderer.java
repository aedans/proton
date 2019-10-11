package io.github.proton.plugins.list;

import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;

public final class FocusableObservableVerticalRenderer<T> extends FocusableObservableRenderer<T> {
    public FocusableObservableVerticalRenderer(Renderer<T> renderer) {
        super(renderer);
    }

    @Override
    protected Screen combine(Screen a, Screen b) {
        return a.verticalPlus(b);
    }
}
