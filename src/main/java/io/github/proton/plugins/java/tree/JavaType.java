/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.tree;

import io.vavr.collection.Vector;

import java.util.Objects;
import java.util.function.Function;

public interface JavaType {
    <A> A match(Function<Primitive, A> primitive, Function<ClassOrInterface, A> classOrInterface);

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

        @Override
        public <A> A match(Function<Primitive, A> primitive, Function<ClassOrInterface, A> classOrInterface) {
            return primitive.apply(this);
        }
    }

    final class ClassOrInterface implements JavaType {
        public final JavaIdentifier name;

        public ClassOrInterface(JavaIdentifier name) {
            this.name = name;
        }

        @Override
        public <A> A match(Function<Primitive, A> primitive, Function<ClassOrInterface, A> classOrInterface) {
            return classOrInterface.apply(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClassOrInterface that = (ClassOrInterface) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
