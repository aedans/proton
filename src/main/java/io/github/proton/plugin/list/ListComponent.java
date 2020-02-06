package io.github.proton.plugin.list;

import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.vavr.collection.List;
import io.vavr.collection.Vector;

public interface ListComponent<T extends Component> extends Component {
    Vector<T> getComponents();

    @Override
    default Screen render(Style style, boolean selected) {
        return render(getComponents(), style, selected);
    }

    static <T extends Component> Screen render(Vector<T> components, Style style, boolean selected) {
        return components
                .map((c) -> c.render(style, selected))
                .foldRight(Screen.empty, Screen::horizontalPlus);
    }
}
