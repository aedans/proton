package io.github.proton.plugins.file;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Component;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

import java.io.File;

public final class FileComponent implements Component {
    private final File file;

    public FileComponent(File file) {
        this.file = file;
    }

    @Override
    public Component update(KeyStroke keyStroke) {
        return this;
    }

    @Override
    public Render render(TerminalPosition position) {
        Observable<Character> name = ObservableUtil.fromString(file.getName());
        Observable<TextCharacter> coloredName = name.map(file.isDirectory()
                ? x -> new TextCharacter(x, TextColor.ANSI.CYAN, TextColor.ANSI.DEFAULT)
                : TextCharacter::new);
        Observable<Observable<TextCharacter>> screen = Observable.just(coloredName);
        return new Render(screen, Maybe.just(position));
    }
}
