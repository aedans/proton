package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public class VerticalComponents implements Component {
    private final Observable<Component> before;
    private final Observable<Component> after;
    private final Component focus;
    private final int length;
    private final int selected;

    public VerticalComponents(Observable<Component> components) {
        this(Observable.empty(), components.skip(1), components.blockingFirst(), components.count().blockingGet().intValue(), 0);
    }

    private VerticalComponents(Observable<Component> before, Observable<Component> after, Component focus, int length, int selected) {
        this.before = before;
        this.after = after;
        this.focus = focus;
        this.length = length;
        if (selected < 0) {
            this.selected = 0;
        } else if (selected >= length) {
            this.selected = length - 1;
        } else {
            this.selected = selected;
        }
    }

    public Observable<Component> components() {
        return Observable.concat(before, Observable.just(focus), after);
    }

    @Override
    public Component update(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowDown)
            return new VerticalComponents(before, after, focus, length, selected + 1);
        if (keyStroke.getKeyType() == KeyType.ArrowUp)
            return new VerticalComponents(before, after, focus, length, selected - 1);
        return new VerticalComponents(before, after, focus.update(keyStroke), length, selected);
    }

    @Override
    public Render render(TerminalPosition position) {
        Observable<TerminalPosition> positions = Observable.range(0, length).map(position::withRelativeRow);
        Observable<Render> renders = components().zipWith(positions, Component::render);
        Observable<Observable<TextCharacter>> screen = renders.flatMap(x -> x.  screen);
        Maybe<TerminalPosition> cursor = renders.elementAt(selected).flatMap(x -> x.cursor);
        return new Render(screen, cursor);
    }
}
