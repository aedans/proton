package io.github.proton.plugins.file;

import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.annotations.Nullable;

import java.io.File;

public final class FileLink<T> {
    static {
        Updater.registry.put(FileLink.class, new FileLinkUpdater<>(Updater.updater));
        Renderer.registry.put(FileLink.class, new FileLinkRenderer());
    }

    @Nullable
    public final T preview;
    public final File file;
    public final TextColor foreground;
    public final TextColor background;

    public FileLink(File file, @Nullable T preview, TextColor foreground, TextColor background) {
        this.file = file;
        this.preview = preview;
        this.foreground = foreground;
        this.background = background;
    }

    public FileLink(File file, TextColor foreground, TextColor background) {
        this(file, null, foreground, background);
    }
}
