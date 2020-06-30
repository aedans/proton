package io.github.proton.plugins.java.tree.expression;

import io.github.proton.plugins.java.tree.*;

public record JavaFieldExpression(JavaExpression expr,
                                  JavaIdentifier field) implements JavaExpression {
}
