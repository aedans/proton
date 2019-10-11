package io.github.proton.plugins.file;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.github.proton.plugins.list.FocusableObservable;

public final class VirtualDirectoryUpdater implements Updater<VirtualDirectory> {
    private final Updater<FocusableObservable<VirtualFile>> updater;

    public VirtualDirectoryUpdater(Updater<FocusableObservable<VirtualFile>> updater) {
        this.updater = updater;
    }

    @Override
    public VirtualDirectory update(VirtualDirectory directory, KeyStroke keyStroke) {
        return new VirtualDirectory(updater.update(directory.files, keyStroke));
    }
}
