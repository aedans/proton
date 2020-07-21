package io.github.proton.editor;

import org.pf4j.ExtensionPoint;

import java.awt.*;

public interface Theme extends ExtensionPoint {
    Color background();

    Color color(String scope);
}
