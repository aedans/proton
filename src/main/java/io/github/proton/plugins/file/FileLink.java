package io.github.proton.plugins.file;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

import java.io.File;

public final class FileLink implements Component {
    public final Single<Component> preview;
    public final File file;
    public final boolean closed;
    public final boolean focused;

    public FileLink(File file, Single<Component> preview, boolean closed, boolean focused) {
        this.file = file;
        this.preview = preview;
        this.closed = closed;
        this.focused = focused;
    }

    public FileLink(File file, Single<Component> preview) {
        this(file, preview, true, true);
    }

    @Override
    public Maybe<Component> update(KeyStroke keyStroke) {
        if (focused) {
            if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                return Maybe.just(new FileLink(file, preview, false, true));
            }
            if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                return Maybe.just(new FileLink(file, preview, true, true));
            }
            if (keyStroke.getKeyType() == KeyType.ArrowDown && !closed) {
                return Maybe.just(new FileLink(file, preview, false, false));
            }
        }
        if (!focused && !closed) {
            Maybe<Component> maybe = preview
                    .flatMapMaybe(preview -> preview.update(keyStroke))
                    .map(preview -> new FileLink(file, Single.just(preview), false, false));
            if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                maybe = maybe.defaultIfEmpty(new FileLink(file, preview, false, true)).toMaybe();
            }
            return maybe;
        }

        return Maybe.empty();
    }

    @Override
    public Screen render(boolean selected) {
        Screen name = Screen.from(file.getName(), x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen screen = closed
                ? Screen.empty
                : preview.blockingGet().render(selected && !focused).indent(2);
        if (selected) {
            if (focused)
                name = name.inverse();
        }
        return name.verticalPlus(screen);
    }
}
