package io.github.proton.plugin.list;

import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.vavr.collection.Vector;

public interface ListComponent<T extends Component> extends Component {
    Vector<T> getStart();
    Vector<T> getEnd();
    T getFocus();

    ListComponent<T> setStart(Vector<T> start);
    ListComponent<T> setEnd(Vector<T> end);
    ListComponent<T> setFocus(T focus);

    default ListComponent<T> insert(T component) {
        return this.setStart(getStart().append(component));
    }

    @Override
    default Screen render(Style style, boolean selected) {
        Vector<Screen> start = getStart().map(c -> c.render(style, false));
        Vector<Screen> end = getEnd().map(c -> c.render(style, false));
        Screen focus = getFocus().render(style, selected);
        return start.append(focus).appendAll(end)
                .foldRight(Screen.empty, Screen::horizontalPlus);
    }
}
