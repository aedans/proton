package io.github.proton.display;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import io.reactivex.rxjava3.core.Observable;

public interface Component {
    Component update(KeyStroke keyStroke);

    Observable<Observable<TextCharacter>> render();
}
