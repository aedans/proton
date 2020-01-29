package io.github.proton.plugin.project;

import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.vavr.collection.Vector;

public final class SwitchProjectionComponent implements Component {
    public final Vector<Component> projections;
    public final int current;

    public SwitchProjectionComponent(Vector<Component> projections, int current) {
        this.projections = projections;
        this.current = current;
    }

    @Override
    public Screen render(Style style, boolean selected) {
        return projections.get(current).render(style, selected);
    }
}
