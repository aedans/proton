package io.github.proton.display;

import com.googlecode.lanterna.input.KeyStroke;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

import java.util.function.Supplier;

public interface Editor<T> extends Supplier<T> {
    Maybe<? extends Editor<T>> update(KeyStroke keyStroke);

    Single<Screen> render(boolean selected);
}
