package io.github.proton.plugins;

public interface Combinable<T> extends Comparable<Combinable<T>> {
    T combine(T t);

    @Override
    default int compareTo(Combinable<T> o) {
        return Integer.compare(priority(), o.priority());
    }

    default int priority() {
        return 0;
    }
}
