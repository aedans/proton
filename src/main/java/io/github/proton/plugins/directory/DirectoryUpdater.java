package io.github.proton.plugins.directory;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.github.proton.plugins.list.FocusableObservable;
import io.reactivex.rxjava3.core.Maybe;

import java.util.Optional;

public final class DirectoryUpdater implements Updater.Same<Directory> {
    private final Updater.Same<FocusableObservable<Object>> updater;

    public DirectoryUpdater(Updater.Same<FocusableObservable<Object>> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<Directory> update(Directory directory, KeyStroke keyStroke) {
        return directory.files.map(x -> updater.update(x, keyStroke)).orElse(Maybe.empty())
                .map(x -> new Directory(Optional.of(x)));
    }
}
