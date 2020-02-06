package io.github.proton.plugin.list;

import io.github.proton.display.Component;
import io.vavr.control.Option;

public interface EditableListComponent<T extends Component> extends NavigableListComponent<T> {
    @Override
    EditableListComponent<T> next();

    @Override
    EditableListComponent<T> prev();

    Option<? extends EditableListComponent<T>> insert(T component);
    Option<? extends EditableListComponent<T>> delete();
}
