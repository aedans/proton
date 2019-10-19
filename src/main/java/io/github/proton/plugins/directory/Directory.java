package io.github.proton.plugins.directory;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.plugins.file.FileLink;
import io.github.proton.plugins.file.FileOpener;
import io.github.proton.plugins.file.FileType;
import io.github.proton.plugins.list.OptionalFocusableObservable;
import io.github.proton.plugins.list.OptionalFocusableObservableComponent;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

import java.io.File;
import java.util.Objects;

public final class Directory implements Component {
    static {
        FileType.registry.put(new DirectoryFileType());
    }

    public final OptionalFocusableObservable<Component> files;

    public Directory(OptionalFocusableObservable<Component> files) {
        this.files = files;
    }

    public Directory(File file) {
        this(OptionalFocusableObservable.from(Observable.fromArray(Objects.requireNonNull(file.listFiles()))
                .map(x -> new FileLink(x, FileOpener.opener.open(x)))));
    }

    @Override
    public Maybe<Component> update(KeyStroke keyStroke) {
        return OptionalFocusableObservableComponent.vertical(files).update(keyStroke)
                .map(x -> new Directory(x.optional));
    }

    @Override
    public Screen render(boolean selected) {
        return OptionalFocusableObservableComponent.vertical(files).render(selected);
    }
}
