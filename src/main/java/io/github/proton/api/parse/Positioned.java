package io.github.proton.api.parse;

public final class Positioned<T> {
    public final T t;
    public final int start;
    public final int end;

    public Positioned(T t, int start, int end) {
        this.t = t;
        this.start = start;
        this.end = end;
    }
}
