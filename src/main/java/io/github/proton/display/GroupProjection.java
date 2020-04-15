package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public interface GroupProjection<T, E> extends Projection<T> {
    Projection<E> projectElem(E elem);

    Vector<E> getElems();

    T setElems(Vector<E> elems);

    default Option<E> newElem() {
        return Option.none();
    }

    ;

    @Override
    default Map<TerminalPosition, Char<T>> characters() {
        return getElems()
                .<Projection<T>>zipWithIndex((elem, i) -> () -> projectElem(elem).characters()
                        .mapValues(c -> new Char<T>() {
                            @Override
                            public TextCharacter character(Style style) {
                                return c.character(style);
                            }

                            @Override
                            public Option<T> insert(char w) {
                                return c.insert(w).map(t -> setElems(getElems().update(i, t)));
                            }

                            @Override
                            public Option<T> delete() {
                                return c.delete().map(t -> setElems(getElems().update(i, t)))
                                        .orElse(() -> Option.of(setElems(getElems().removeAt(i))));
                            }

                            @Override
                            public Option<T> submit() {
                                return c.submit().map(t -> setElems(getElems().update(i, t)))
                                        .orElse(() -> newElem().map(e -> setElems(getElems().insert(i + 1, e))));
                            }
                        }))
                .reduceOption(Projection::combineVertical)
                .map(Projection::characters)
                .getOrElse(HashMap.empty());
    }
}
