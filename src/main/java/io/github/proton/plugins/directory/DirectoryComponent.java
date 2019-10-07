package io.github.proton.plugins.directory;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Component;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;

import java.io.File;
import java.util.Objects;

public class DirectoryComponent implements Component {
    private final File file;

    public DirectoryComponent(File file) {
        this.file = file;
    }

    @Override
    public Component update(KeyStroke keyStroke) {
        return null;
    }

    @Override
    public Observable<Observable<TextCharacter>> render() {
        Observable<File> listing = Observable.fromArray(Objects.requireNonNull(file.listFiles()));
        Observable<File> directories = listing.filter(File::isDirectory);
        Observable<File> files = listing.filter(File::isFile);
        Observable<Observable<TextCharacter>> directoriesTS = directories.map(x ->
                ObservableUtil.fromString("> " + x.getName())
                        .map(c -> new TextCharacter(c, TextColor.ANSI.CYAN, TextColor.ANSI.DEFAULT)));
        Observable<Observable<TextCharacter>> filesTS = files.map(x ->
                ObservableUtil.fromString(x.getName())
                        .map(TextCharacter::new));
        return Observable.concat(directoriesTS, filesTS);
    }
}
