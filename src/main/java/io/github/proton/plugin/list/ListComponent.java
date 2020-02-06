package io.github.proton.plugin.list;

import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.vavr.collection.Vector;

import java.util.function.BiFunction;

public interface ListComponent<T extends Component> extends Component {
    Vector<T> getComponents();
    Orientation getOrientation();

    @Override
    default Screen render(Style style, boolean selected) {
        return render(getComponents(), getOrientation(), style, selected);
    }

    static <T extends Component> Screen render(Vector<T> components, Orientation orientation, Style style, boolean selected) {
        return render(components.map((c) -> c.render(style, selected)), orientation);
    }

    static Screen render(Vector<Screen> screens, Orientation orientation) {
        BiFunction<Screen, Screen, Screen> combine = null;
        switch (orientation) {
            case HORIZONTAL:
                combine = Screen::horizontalPlus;
                break;
            case VERTICAL:
                combine = Screen::verticalPlus;
                break;
        }
        return screens.foldRight(Screen.empty, combine);
    }

    enum Orientation {
        VERTICAL, HORIZONTAL
    }
}
