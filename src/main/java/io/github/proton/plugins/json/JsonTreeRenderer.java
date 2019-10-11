package io.github.proton.plugins.json;

import com.eclipsesource.json.WriterConfig;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;

public final class JsonTreeRenderer implements Renderer<JsonTree> {
    @Override
    public Screen render(JsonTree json, boolean selected) {
        Observable<Character> observable = ObservableUtil.fromString(json.object.toString(WriterConfig.PRETTY_PRINT));
        Observable<Observable<Character>> chars = ObservableUtil.split(observable, x -> x == '\n');
        Observable<Observable<TextCharacter>> textChars = chars
                .map(x -> x.map(c -> new TextCharacter(c, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK)));
        return new Screen(textChars);
    }
}
