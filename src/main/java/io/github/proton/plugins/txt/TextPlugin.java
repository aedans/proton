package io.github.proton.plugins.txt;

import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;
import io.github.proton.plugins.list.FocusableObservableVerticalUpdater;

public final class TextPlugin {
    public void init() {
        Renderer.registry.put(TextLine.class, new TextLineRenderer());
        Updater.registry.put(TextLine.class, new TextLineUpdater(new FocusableObservableVerticalUpdater<>(new Updater.Const<>())));
    }
}
