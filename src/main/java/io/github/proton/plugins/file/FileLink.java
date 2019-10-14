package io.github.proton.plugins.file;

import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Single;

import java.io.File;

public final class FileLink<T> {
    static {
        Updater.registry.put(FileLink.class, new FileLinkUpdater<>(Updater.updater));
        Renderer.registry.put(FileLink.class, new FileLinkRenderer());
    }

    public final Single<T> preview;
    public final File file;
    public final boolean closed;
    public final boolean focused;

    public FileLink(File file, Single<T> preview, boolean closed, boolean focused) {
        this.file = file;
        this.preview = preview;
        this.closed = closed;
        this.focused = focused;
    }

    public FileLink(File file, Single<T> preview) {
        this(file, preview, true, true);
    }
}
