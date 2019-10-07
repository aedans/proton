package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public interface Component {
    Component update(KeyStroke keyStroke);

    Render render(TerminalPosition position);

    class Render {
        public final Observable<Observable<TextCharacter>> screen;
        public final Maybe<TerminalPosition> cursor;

        public Render(Observable<Observable<TextCharacter>> screen, Maybe<TerminalPosition> cursor) {
            this.screen = screen;
            this.cursor = cursor;
        }
    }
}
