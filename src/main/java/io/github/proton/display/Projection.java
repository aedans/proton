package io.github.proton.display;

import io.vavr.collection.Vector;
import org.pf4j.ExtensionPoint;

public interface Projection extends ExtensionPoint {
    Vector<Component> projectGeneric(Object tree);

    default Projection combine(Projection projection) {
        return tree -> Projection.this.projectGeneric(tree).appendAll(projection.projectGeneric(tree));
    }

    Projection unit = tree -> Vector.empty();

    interface Of<T> extends Projection {
        Class<T> clazz();

        Vector<Component> project(T tree);

        @Override
        default Vector<Component> projectGeneric(Object tree) {
            if (clazz().isInstance(tree)) {
                return project(clazz().cast(tree));
            } else {
                return Vector.empty();
            }
        }
    }
}
