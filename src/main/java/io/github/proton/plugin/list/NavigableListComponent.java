package io.github.proton.plugin.list;

import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import io.vavr.collection.Vector;

public interface NavigableListComponent<T extends Component> extends ListComponent<T> {
    NavigableListComponent<T> next();
    NavigableListComponent<T> prev();

    int getIndex();

    default T getFocus() {
        return getComponents().get(getIndex());
    }

    default Tuple2<Vector<T>, Vector<T>> split() {
        return getComponents().splitAt(getIndex());
    }

    @Override
    default Screen render(Style style, boolean selected) {
        return render(getComponents(), getOrientation(), getIndex(), style, selected);
    }

    static <T extends Component> Screen render(Vector<T> components, Orientation orientation, int index, Style style, boolean selected) {
        return ListComponent.render(
                components.zipWith(Stream.continually(false).update(index, selected), (c, s) -> c.render(style, s)),
                orientation
        );
    }

    static <T> boolean isBounded(int index, Vector<T> vector) {
        if (index < 0) {
            return false;
        } else if (index >= vector.size()) {
            return false;
        } else {
            return true;
        }
    }

    static <T> int bounded(int index, Vector<T> vector) {
        if (index < 0) {
            return 0;
        } else if (index >= vector.size()) {
            return vector.size() - 1;
        } else {
            return index;
        }
    }
}
