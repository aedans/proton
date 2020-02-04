package io.github.proton;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.*;
import io.github.proton.plugin.project.CycleProjectionComponent;
import io.github.proton.plugin.text.Line;

import java.io.File;
import java.io.IOException;

public final class Main {
    public static void main(String[] args) throws IOException {
        File home = new File(args.length == 0 ? "." : args[0]);

        Plugins.start();
        Projection projection = Plugins.projection();
        Controller controller = Plugins.controller();

        Object tree = '!';

        Component component = new CycleProjectionComponent.Impl<>(projection.projectGeneric(tree));

        Style style = Plugins.getExtensions(Style.class).get(0);

        try (TerminalDisplay display = new TerminalDisplay()) {
            while (true) {
                display.clear();
                display.resizeIfNecessary();
                display.background(style.base(' '));
                Screen screen = component.render(style, true);
                screen.characters.forEach(t -> display.write(t._1, t._2));
                display.refresh();
                KeyStroke keyStroke = display.read();
                if (keyStroke.getKeyType() == KeyType.EOF) break;
                component = controller.updateGeneric(component, keyStroke).getOrElse(component);
            }
        }
    }
}
