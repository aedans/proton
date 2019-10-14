package io.github.proton.plugins.file;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;

public final class FileLinkRenderer<T> implements Renderer<FileLink<T>> {
    @Override
    public Screen render(FileLink<T> link, boolean selected) {
        Screen name = Screen.from(link.file.getName(), x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen preview = link.closed
                ? Screen.empty
                : Renderer.renderer.render(link.preview.blockingGet(), selected && !link.focused).indent(2);
        if (selected) {
            if (link.focused)
                name = name.inverse();
        }
        return name.verticalPlus(preview);
    }
}
