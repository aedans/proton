package io.github.proton.plugins.file;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public final class VirtualFileRenderer implements Renderer<VirtualFile> {
    @Override
    public Render render(VirtualFile file, TerminalPosition position) {
        Observable<Character> name = ObservableUtil.fromString(file.file.getName());
        Observable<TextCharacter> coloredName = name.map(file.file.isDirectory()
                ? x -> new TextCharacter(x, TextColor.ANSI.CYAN, TextColor.ANSI.DEFAULT)
                : TextCharacter::new);
        Observable<Observable<TextCharacter>> screen = Observable.just(coloredName);
        return new Render(screen, Maybe.just(position));
    }
}
