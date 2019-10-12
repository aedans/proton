package io.github.proton.plugins.file;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public final class FileLinkUpdater<T> implements Updater<FileLink<T>, Object> {
    @Override
    public Single<Object> update(FileLink<T> link, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            @SuppressWarnings("unchecked")
            Maybe<T> preview = Maybe.fromCallable(() -> (T) FileOpener.opener.open(link.file));
            return Single.just(new FileLink<>(link.file, preview, link.foreground, link.background));
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return Single.just(new FileLink<>(link.file, link.foreground, link.background));
        } else {
            return Single.just(link);
        }
    }
}
