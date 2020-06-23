package io.github.proton.plugins.java.tree;

import io.vavr.control.Option;

public record JavaFieldMember(JavaType type, JavaIdentifier name, Option<JavaExpression>expression) {
}
