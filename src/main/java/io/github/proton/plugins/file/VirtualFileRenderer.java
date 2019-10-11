package io.github.proton.plugins.file;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;

public final class VirtualFileRenderer implements Renderer<VirtualFile> {
    @Override
    public Screen render(VirtualFile file) {
        Observable<Character> name = ObservableUtil.fromString(file.file.getName());
        Observable<TextCharacter> coloredName = name.map(file.file.isDirectory()
                ? x -> new TextCharacter(x, TextColor.ANSI.CYAN, TextColor.ANSI.BLACK)
                : x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Observable<Observable<TextCharacter>> chars = Observable.just(coloredName);
        return new Screen(chars);
    }
}
