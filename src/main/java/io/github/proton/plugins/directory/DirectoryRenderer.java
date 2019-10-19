package io.github.proton.plugins.directory;

import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.plugins.list.FocusableObservable;

public final class DirectoryRenderer implements Renderer<Directory> {
    private final Renderer<FocusableObservable<Object>> renderer;

    public DirectoryRenderer(Renderer<FocusableObservable<Object>> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Screen render(Directory directory, boolean selected) {
        return directory.files.map(x -> renderer.render(x, selected)).orElse(Screen.empty);
    }
}
