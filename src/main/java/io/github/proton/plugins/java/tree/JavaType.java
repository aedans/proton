package io.github.proton.plugins.java.tree;

import io.vavr.collection.Vector;

public interface JavaType {
    static JavaType fromIdentifier(JavaIdentifier identifier) {
        return Vector.of(Primitive.values())
                .find(x -> x.name().toLowerCase().equals(identifier.toString()))
                .map(x -> (JavaType) x)
                .getOrElse(new ClassOrInterface(identifier));
    }

    enum Primitive implements JavaType {
        BYTE,
        SHORT,
        CHAR,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        BOOLEAN;
    }

    final record ClassOrInterface(JavaIdentifier name) implements JavaType {
    }
}
