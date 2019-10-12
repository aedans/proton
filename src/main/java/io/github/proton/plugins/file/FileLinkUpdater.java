package io.github.proton.plugins.file;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Maybe;

public final class FileLinkUpdater<T> implements Updater<FileLink<T>, Object> {
    private final Updater<T, T> updater;

    public FileLinkUpdater(Updater<T, T> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<Object> update(FileLink<T> link, KeyStroke keyStroke) {
        Maybe<FileLink<Object>> otherwise = Maybe.empty();
        if (link.preview == null && keyStroke.getKeyType() == KeyType.ArrowRight)
            otherwise = Maybe.fromCallable(() -> FileOpener.opener.open(link.file))
                    .map(preview -> new FileLink<>(link.file, preview, link.foreground, link.background));
        if (link.preview != null && keyStroke.getKeyType() == KeyType.ArrowLeft)
            otherwise = Maybe.just(new FileLink<>(link.file, link.foreground, link.background));

        Maybe<Object> preview = Maybe.empty();
        if (link.preview != null) {
            preview = updater.update(link.preview, keyStroke)
                    .map(newPreview -> new FileLink<>(link.file, newPreview, link.foreground, link.background));
        }

        return preview.switchIfEmpty(otherwise);
    }
}
