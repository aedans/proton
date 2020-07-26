package io.github.proton.plugins.style;

import io.github.proton.editor.Theme;
import org.pf4j.Extension;

import java.awt.*;

@Extension
public final class SolarizedTheme implements Theme {
    Color base03 = new Color(0x002b36);
    Color base01 = new Color(0x586e75);
    Color base0 = new Color(0x839496);
    Color green = new Color(0x859900);
    Color cyan = new Color(0x2aa198);

    @Override
    public Color background() {
        return base03;
    }

    @Override
    public Color color(String style) {
        if (style.startsWith("keyword")) {
            return green;
        }
        if (style.startsWith("comment")) {
            return base01;
        }
        if (style.startsWith("constant.numeric")) {
            return cyan;
        }
        return base0;
    }
}
