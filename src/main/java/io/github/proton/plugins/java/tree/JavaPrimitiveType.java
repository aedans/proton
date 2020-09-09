package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.type.PrimitiveType;
import io.github.proton.editor.*;

public enum JavaPrimitiveType implements JavaType {
    BOOLEAN, CHAR, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE;

    public static JavaPrimitiveType from(PrimitiveType type) {
        return switch (type.getType()) {
            case BOOLEAN -> BOOLEAN;
            case CHAR -> CHAR;
            case BYTE -> BYTE;
            case SHORT -> SHORT;
            case INT -> INT;
            case LONG -> LONG;
            case FLOAT -> FLOAT;
            case DOUBLE -> DOUBLE;
        };
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Projection<JavaType> project() {
        return new JavaSimpleName(name().toLowerCase()).project()
            .mapChar(c -> c.withStyle("keyword"))
            .map(JavaType::from);
    }
}
