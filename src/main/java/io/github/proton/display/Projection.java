package io.github.proton.display;

import io.vavr.collection.Vector;
import org.pf4j.ExtensionPoint;

@SuppressWarnings("unchecked")
public interface Projection extends ExtensionPoint {
    <T extends Component> Vector<T> projectGeneric(Object tree);

    default Projection combine(Projection projection) {
        return new Projection() {
            @Override
            public <T extends Component> Vector<T> projectGeneric(Object tree) {
                return (Vector<T>) Projection.this.projectGeneric(tree).appendAll(projection.projectGeneric(tree));
            }
        };
    }

    Projection unit = new Projection() {
        @Override
        public <T extends Component> Vector<T> projectGeneric(Object tree) {
            return Vector.empty();
        }
    };

    abstract class Of<T> implements Projection {
        public final Class<T> clazz;

        public Of(Class<T> clazz) {
            this.clazz = clazz;
        }

        protected abstract Vector<Component> project(T tree);

        @Override
        public  <A extends Component> Vector<A> projectGeneric(Object tree) {
            if (clazz.isInstance(tree)) {
                return (Vector<A>) project(clazz.cast(tree));
            } else {
                return Vector.empty();
            }
        }
    }
}
