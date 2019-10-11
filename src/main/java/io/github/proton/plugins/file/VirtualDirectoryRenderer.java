package io.github.proton.plugins.file;

import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.plugins.list.FocusableObservable;

public final class VirtualDirectoryRenderer implements Renderer<VirtualDirectory> {
    private final Renderer<FocusableObservable<VirtualFile>> renderer;

    public VirtualDirectoryRenderer(Renderer<FocusableObservable<VirtualFile>> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Screen render(VirtualDirectory directory) {
        return renderer.render(directory.files);
    }
}
