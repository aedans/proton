package io.github.proton.display;

public interface Component {
    Screen render(Style style, boolean selected);

    Component empty = (style, selected) -> Screen.empty;
}
