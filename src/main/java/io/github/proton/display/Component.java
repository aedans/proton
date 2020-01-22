package io.github.proton.display;

import com.googlecode.lanterna.input.KeyStroke;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public interface Component {
    Maybe<Component> update(KeyStroke keyStroke);

    Single<Screen> render(boolean selected);
}
