package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Observable;

public interface Component {
    Component update(KeyStroke keyStroke);

    Render render();

    class Render {
        public final Observable<Observable<TextCharacter>> screen;
        @Nullable public final TerminalPosition cursor;

        public Render(Observable<Observable<TextCharacter>> screen, @Nullable TerminalPosition cursor) {
            this.screen = screen;
            this.cursor = cursor;
        }
    }
}
