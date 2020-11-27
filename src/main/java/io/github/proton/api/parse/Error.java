package io.github.proton.api.parse;

import java.util.function.Supplier;

public final class Error {
    private final Supplier<String> name;

    public Error(Supplier<String> name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.get();
    }
}
