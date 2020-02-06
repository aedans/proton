package io.github.proton.display;

import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.pf4j.ExtensionPoint;

@SuppressWarnings("unchecked")
public interface Projection extends ExtensionPoint {
    <T extends Component> Vector<T> projectGeneric(Object tree);

    int precedence();

    default Projection combine(Projection projection) {
        return new Projection() {
            @Override
            public <T extends Component> Vector<T> projectGeneric(Object tree) {
                return (Vector<T>) Projection.this.projectGeneric(tree).appendAll(projection.projectGeneric(tree));
            }

            @Override
            public int precedence() {
                return Math.min(Projection.this.precedence(), projection.precedence());
            }
        };
    }

    Projection unit = new Projection() {
        @Override
        public <T extends Component> Vector<T> projectGeneric(Object tree) {
            return Vector.empty();
        }

        @Override
        public int precedence() {
            return 0;
        }
    };

    abstract class Of<T> implements Projection {
        public final Class<T> clazz;

        public Of(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public int precedence() {
            return precedence(clazz);
        }

        public static <T> int precedence(Class<T> clazz) {
            return Controller.Of.precedence(clazz);
        }

        protected abstract Vector<Component> project(T tree);

        @Override
        public <A extends Component> Vector<A> projectGeneric(Object tree) {
            if (clazz.isInstance(tree)) {
                return (Vector<A>) project(clazz.cast(tree));
            } else {
                return Vector.empty();
            }
        }
    }
}
