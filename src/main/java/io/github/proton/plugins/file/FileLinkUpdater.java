package io.github.proton.plugins.file;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public final class FileLinkUpdater<T> implements Updater.Same<FileLink<T>> {
    private final Updater<T, T> updater;

    public FileLinkUpdater(Updater<T, T> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<FileLink<T>> update(FileLink<T> link, KeyStroke keyStroke) {
        if (link.focused) {
            if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                return Maybe.just(new FileLink<>(link.file, link.preview, false, true));
            }
            if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                return Maybe.just(new FileLink<>(link.file, link.preview, true, true));
            }
            if (keyStroke.getKeyType() == KeyType.ArrowDown && !link.closed) {
                return Maybe.just(new FileLink<>(link.file, link.preview, false, false));
            }
        }
        if (!link.focused && !link.closed) {
            Maybe<FileLink<T>> maybe = link.preview
                    .flatMapMaybe(preview -> updater.update(preview, keyStroke))
                    .map(preview -> new FileLink<>(link.file, Single.just(preview), false, false));
            if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                maybe = maybe.defaultIfEmpty(new FileLink<>(link.file, link.preview, false, true)).toMaybe();
            }
            return maybe;
        }

        return Maybe.empty();
    }
}
