package io.github.proton.txt;

import io.github.proton.display.views.ListView;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;

public final class Line {
    public final Observable<Character> chars;

    private Line(Observable<Character> chars) {
        this.chars = chars;
    }

    public static Line of(String string) {
        return of(ObservableUtil.fromString(string));
    }

    public static Line of(Observable<Character> chars) {
        return new Line(Observable.concat(chars, Observable.just(' ')));
    }

    public static final ListView<Line> listView = new ListView<Line>() {
        @Override
        public Line removeAt(Line list, long index) {
            return new Line(ObservableUtil.removeAt(list.chars, index));
        }
    };
}
