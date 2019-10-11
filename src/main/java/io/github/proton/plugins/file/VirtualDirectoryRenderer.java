package io.github.proton.plugins.file;

import com.googlecode.lanterna.TerminalPosition;
import io.github.proton.display.Renderer;
import io.github.proton.plugins.list.FocusableObservable;

public final class VirtualDirectoryRenderer implements Renderer<VirtualDirectory> {
    private final Renderer<FocusableObservable<VirtualFile>> renderer;

    public VirtualDirectoryRenderer(Renderer<FocusableObservable<VirtualFile>> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Render render(VirtualDirectory directory, TerminalPosition position) {
        return renderer.render(directory.files, position);
    }
}
