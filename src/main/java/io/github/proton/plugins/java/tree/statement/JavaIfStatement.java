package io.github.proton.plugins.java.tree.statement;

import io.github.proton.plugins.java.tree.*;
import io.vavr.collection.Vector;

public record JavaIfStatement(JavaExpression condition,
                              Vector<JavaStatement> trueBlock,
                              Vector<JavaStatement> falseBlock) implements JavaStatement {
}
