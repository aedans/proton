package io.github.proton.plugins.file;

import io.github.proton.display.VerticalComponents;
import io.reactivex.rxjava3.core.Observable;

import java.io.File;
import java.util.Objects;

public final class DirectoryComponent extends VerticalComponents {
    public DirectoryComponent(File file) {
        super(Observable.fromArray(Objects.requireNonNull(file.listFiles())).map(FileComponent::new));
    }
}
