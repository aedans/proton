package io.github.proton.display;

import com.googlecode.lanterna.input.KeyStroke;
import io.reactivex.rxjava3.core.Maybe;

public interface Component {
    Maybe<? extends Component> update(KeyStroke keyStroke);

    Screen render(boolean selected);
}
