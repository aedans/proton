package io.github.proton.api.parse;

import java.util.function.Supplier;

public final class ParseError {
    private final Supplier<String> name;

    public ParseError(Supplier<String> name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.get();
    }
}
