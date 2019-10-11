package io.github.proton.plugins.json;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.Renderer;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public final class JsonTreeRenderer implements Renderer<JsonTree> {
    @Override
    public Render render(JsonTree json, TerminalPosition position) {
        Observable<Character> observable = Observable.create(emitter -> {
            json.gson.toJson(json.object, new Appendable() {
                @Override
                public Appendable append(CharSequence csq) {
                    for (int i = 0; i < csq.length(); i++) {
                        append(csq.charAt(i));
                    }
                    return this;
                }

                @Override
                public Appendable append(CharSequence csq, int start, int end) {
                    return append(csq.subSequence(start, end));
                }

                @Override
                public Appendable append(char c) {
                    emitter.onNext(c);
                    return this;
                }
            });
            emitter.onComplete();
        });
        Observable<Observable<TextCharacter>> screen = ObservableUtil.split(observable, x -> x == '\n')
                .map(x -> x.map(TextCharacter::new));
        return new Render(screen, Maybe.empty());
    }
}
