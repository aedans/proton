package io.github.proton.plugins.file;

import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;

public final class FileLinkRenderer<T> implements Renderer<FileLink<T>> {
    @Override
    public Screen render(FileLink<T> link, boolean selected) {
        Screen screen = Screen.from(link.file.getName(), x -> new TextCharacter(x, link.foreground, link.background));
        Screen preview = link.preview == null ? Screen.empty : Renderer.renderer.render(link.preview, selected).indent(2);
        return screen.verticalPlus(selected ? preview.inverse() : preview);
    }
}
