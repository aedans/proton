package io.github.proton.plugins.file;

import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;

import java.io.File;

public final class VirtualFile {
    static {
        Updater.registry.put(VirtualFile.class, new VirtualFileUpdater());
        Renderer.registry.put(VirtualFile.class, new VirtualFileRenderer());
    }

    public final File file;

    public VirtualFile(File file) {
        this.file = file;
    }
}
