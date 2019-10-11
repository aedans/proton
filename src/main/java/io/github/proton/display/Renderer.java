package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.github.proton.util.Registry;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public interface Renderer<T> {
    Render render(T t, TerminalPosition position);

    Registry<Renderer> registry = new Registry<>("renderer");

    @SuppressWarnings("unchecked")
    Renderer<Object> renderer = (o, position) -> registry.get(o.getClass()).render(o, position);

    class Render {
        public final Observable<Observable<TextCharacter>> screen;
        public final Maybe<TerminalPosition> cursor;

        public Render(Observable<Observable<TextCharacter>> screen, Maybe<TerminalPosition> cursor) {
            this.screen = screen;
            this.cursor = cursor;
        }
    }
}
