package io.github.proton.plugin.project;

import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.vavr.collection.Vector;

public interface SwitchProjectionComponent extends Component {
    Vector<Component> getProjections();
    int getIndex();

    SwitchProjectionComponent setProjections(Vector<Component> projections);
    SwitchProjectionComponent setIndex(int index);

    static SwitchProjectionComponent of(Vector<Component> projections, int index) {
        return new SwitchProjectionComponent() {
            @Override
            public Vector<Component> getProjections() {
                return projections;
            }

            @Override
            public int getIndex() {
                return index;
            }

            @Override
            public SwitchProjectionComponent setProjections(Vector<Component> projections) {
                return of(projections, index);
            }

            @Override
            public SwitchProjectionComponent setIndex(int index) {
                return of(projections, index);
            }
        };
    }

    @Override
    default Screen render(Style style, boolean selected) {
        return getProjections().get(getIndex()).render(style, selected);
    }
}
