package io.github.proton.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.Displayable;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;

public final class JsonComponent implements Displayable {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Object object;

    public JsonComponent(Object object) {
        this.object = object;
    }

    @Override
    public Observable<Observable<TextCharacter>> render() {
        Observable<Character> observable = Observable.create(emitter -> {
            gson.toJson(object, new Appendable() {
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
        return ObservableUtil.split(observable, x -> x == '\n').map(x -> x.map(TextCharacter::new));
    }
}
