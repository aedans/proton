package io.github.proton.plugins.directory;

import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;
import io.github.proton.plugins.file.FileLink;
import io.github.proton.plugins.file.FileOpener;
import io.github.proton.plugins.file.FileType;
import io.github.proton.plugins.list.FocusableObservable;
import io.github.proton.plugins.list.FocusableObservableVerticalRenderer;
import io.github.proton.plugins.list.FocusableObservableVerticalUpdater;
import io.reactivex.rxjava3.core.Observable;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public final class Directory {
    static {
        Updater.registry.put(Directory.class, new DirectoryUpdater(new FocusableObservableVerticalUpdater<>(Updater.updater)));
        Renderer.registry.put(Directory.class, new DirectoryRenderer(new FocusableObservableVerticalRenderer<>(Renderer.renderer)));
        FileType.registry.put(new DirectoryFileType());
    }

    public final Optional<FocusableObservable<Object>> files;

    public Directory(Optional<FocusableObservable<Object>> files) {
        this.files = files;
    }

    public Directory(File file) {
        this(FocusableObservable.from(Observable.fromArray(Objects.requireNonNull(file.listFiles()))
                .map(x -> new FileLink<>(x, FileOpener.opener.open(x)))));
    }
}
