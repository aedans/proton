package io.github.proton.ui;

import io.github.proton.editor.Theme;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;

import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;

public final class ProtonLookAndFeel extends MaterialLookAndFeel {
    public ProtonLookAndFeel(Theme theme) {
        super(new ProtonMaterialTheme(theme));
    }

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
    }

    @Override
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        table.put("ScrollBar.width", 5);
        table.put("SplitPane.continuousLayout", true);
        table.put("TabbedPane.textIconGap", 100);
        table.put("TextPane.font", theme.getFontRegular());
    }

    public static final class ProtonMaterialTheme extends JMarsDarkTheme {
        private final Theme theme;

        public ProtonMaterialTheme(Theme theme) {
            this.theme = theme;
        }

        @Override
        public void installTheme() {
            super.installTheme();
        }

        @Override
        protected void installFonts() {
            super.installFonts();

            var name = "Monospaced";
            var size = 12;

            setFontRegular(new FontUIResource(new Font(name, Font.PLAIN, size)));
            setFontMedium(new FontUIResource(new Font(name, Font.PLAIN, size)));
            setFontBold(new FontUIResource(new Font(name, Font.BOLD, size)));
            setFontItalic(new FontUIResource(new Font(name, Font.ITALIC, size)));
        }

        @Override
        protected void installColor() {
            super.installColor();

            var baseColor = new ColorUIResource(theme.color(""));
            var keywordColor = new ColorUIResource(theme.color("keyword"));
            var backgroundColor = new ColorUIResource(theme.background());

            backgroundPrimary = backgroundColor;
            highlightBackgroundPrimary = keywordColor;
            textColor = baseColor;
            disableTextColor = backgroundColor;

            focusColorLineTabbedPane = keywordColor;

            backgroundTextField = backgroundColor;

            colorDividierSplitPane = baseColor;
            colorDividierFocusSplitPane = baseColor;
        }

        @Override
        public String getName() {
            return theme.getClass().getSimpleName();
        }
    }
}
