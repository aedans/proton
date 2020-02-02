package io.github.proton.plugin.list;

import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.vavr.collection.Stream;
import io.vavr.collection.Vector;

public interface ListComponent<T extends Component> extends Component {
    Vector<T> getComponents();
    int getIndex();

    ListComponent<T> setComponents(Vector<T> components);
    ListComponent<T> setIndex(int index);

    @Override
    default Screen render(Style style, boolean selected) {
        return getComponents()
                .zipWith(Stream.continually(false).update(getIndex(), true),
                        (component, s) -> component.render(style, s))
                .foldRight(Screen.empty, Screen::horizontalPlus);
    }
}
