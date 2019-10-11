package io.github.proton.plugins.file;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;

public final class VirtualFileUpdater implements Updater<VirtualFile> {
    @Override
    public VirtualFile update(VirtualFile file, KeyStroke keyStroke) {
        return file;
    }
}
