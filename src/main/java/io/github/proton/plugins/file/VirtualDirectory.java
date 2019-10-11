package io.github.proton.plugins.file;

import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;
import io.github.proton.plugins.list.FocusableObservable;
import io.github.proton.plugins.list.FocusableObservableVerticalRenderer;
import io.github.proton.plugins.list.FocusableObservableVerticalUpdater;
import io.reactivex.rxjava3.core.Observable;

import java.io.File;
import java.util.Objects;

public final class VirtualDirectory {
    static {
        Updater.registry.put(VirtualDirectory.class, new VirtualDirectoryUpdater(new FocusableObservableVerticalUpdater<>(new VirtualFileUpdater())));
        Renderer.registry.put(VirtualDirectory.class, new VirtualDirectoryRenderer(new FocusableObservableVerticalRenderer<>(new VirtualFileRenderer())));
    }

    public final FocusableObservable<VirtualFile> files;

    public VirtualDirectory(FocusableObservable<VirtualFile> files) {
        this.files = files;
    }

    public VirtualDirectory(File file) {
        this(new FocusableObservable<>(Observable.fromArray(Objects.requireNonNull(file.listFiles())).map(VirtualFile::new)));
    }
}
