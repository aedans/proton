package io.github.proton.plugins.java.tree.expression;

import io.github.proton.plugins.java.tree.*;
import io.vavr.collection.Vector;

public record JavaFunctionExpression(JavaIdentifier name,
                                     Vector<JavaExpression> args) implements JavaExpression {
}
