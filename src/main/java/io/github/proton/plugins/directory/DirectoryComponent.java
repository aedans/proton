package io.github.proton.plugins.directory;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;

import java.io.File;
import java.util.Objects;

public final class DirectoryComponent implements Component {
    private final File file;
    private final int selected;

    public DirectoryComponent(File file, int selected) {
        this.file = file;
        this.selected = selected;
    }

    @Override
    public Component update(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowDown)
            return new DirectoryComponent(file, selected + 1);
        if (keyStroke.getKeyType() == KeyType.ArrowUp)
            return new DirectoryComponent(file, selected - 1);
        return this;
    }

    @Override
    public Render render() {
        Observable<File> listing = Observable.fromArray(Objects.requireNonNull(file.listFiles()));
        Observable<File> directories = listing.filter(File::isDirectory);
        Observable<File> files = listing.filter(File::isFile);
        Observable<Observable<TextCharacter>> directoriesTS = directories.map(x ->
                ObservableUtil.fromString("> " + x.getName())
                        .map(c -> new TextCharacter(c, TextColor.ANSI.CYAN, TextColor.ANSI.DEFAULT)));
        Observable<Observable<TextCharacter>> filesTS = files.map(x ->
                ObservableUtil.fromString(x.getName())
                        .map(TextCharacter::new));
        return new Render(Observable.concat(directoriesTS, filesTS), new TerminalPosition(0, selected));
    }
}
