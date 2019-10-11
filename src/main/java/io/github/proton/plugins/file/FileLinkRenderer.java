package io.github.proton.plugins.file;

import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;

public final class FileLinkRenderer<T> implements Renderer<FileLink<T>> {
    @Override
    public Screen render(FileLink<T> link, boolean selected) {
        Observable<Character> name = ObservableUtil.fromString(link.file.getName());
        Observable<TextCharacter> coloredName = name.map(x -> new TextCharacter(x, link.foreground, link.background));
        Screen screen = new Screen(Observable.just(coloredName));
        Screen preview = link.preview
                .map(p -> Renderer.renderer.render(p, false).indent(2))
                .defaultIfEmpty(Screen.empty)
                .blockingGet();
        return screen.verticalPlus(selected ? preview.inverse() : preview);
    }
}
