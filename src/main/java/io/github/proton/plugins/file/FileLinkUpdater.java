package io.github.proton.plugins.file;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Maybe;

public final class FileLinkUpdater<T> implements Updater<FileLink<T>, Object> {
    @Override
    public Object update(FileLink<T> link, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            @SuppressWarnings("unchecked")
            Maybe<T> preview = Maybe.fromCallable(() -> (T) FileOpener.opener.open(link.file));
            return new FileLink<>(link.file, preview, link.foreground, link.background);
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return new FileLink<>(link.file, link.foreground, link.background);
        } else {
            return link;
        }
    }
}
