package io.github.proton.plugins.java.tree.expression;

import io.github.proton.plugins.java.tree.JavaExpression;
import io.vavr.collection.Vector;

public record JavaMethodExpression(JavaFieldExpression field,
                                   Vector<JavaExpression>args) implements JavaExpression {
}
