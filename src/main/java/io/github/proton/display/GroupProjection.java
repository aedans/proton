package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;

public interface GroupProjection<T, E> extends Projection<T> {
    Projection<E> projectElem(E elem);

    Vector<E> getElems();

    T setElems(Vector<E> elems);

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
                            public T insert(char w) {
                                return setElems(getElems().update(i, c.insert(w)));
                            }

                            @Override
                            public T delete() {
                                return setElems(getElems().update(i, c.delete()));
                            }

                            @Override
                            public T submit() {
                                return setElems(getElems().update(i, c.submit()));
                            }
                        }))
                .reduceOption(Projection::combineVertical)
                .map(Projection::characters)
                .getOrElse(HashMap.empty());
    }
}
