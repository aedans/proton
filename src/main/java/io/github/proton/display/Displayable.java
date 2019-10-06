package io.github.proton.display;

import com.googlecode.lanterna.TextCharacter;
import io.reactivex.rxjava3.core.Observable;

public interface Displayable {
    Observable<Observable<TextCharacter>> render();
}
