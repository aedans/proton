package io.github.proton.plugins.java.tree.expression;

import io.github.proton.plugins.java.tree.JavaExpression;

public record JavaBinaryExpression(JavaExpression left,
                                   JavaExpression right,
                                   Operator op) implements JavaExpression {
    public enum Operator {
        ADD("+", 4), SUB("-", 4), MUL("*", 3), DIV("/", 3), MOD("%", 3),
        EQ("==", 7), NEQ("!=", 7), LT("<", 6), GT(">", 6), LEQ("<=", 6), GEQ(">=", 6),
        AND("&&", 11), OR("||", 12),
        BAND("&", 8), BOR("|", 10), BXOR("^", 9),
        SHL("<<", 5), SHR(">>", 5), USHR(">>>", 5),
        ASEQ("=", 14), ASADD("+=", 14), ASSUB("-=", 14), ASMUL("*=", 14), ASDIV("/=", 14), ASMOD("%=", 14),
        ASBAND("&=", 14), ASBOR("|=", 14), ASBXOR("^=", 14),
        ASSHL("<<=", 14), ASSHR(">>=", 14), ASUSHR(">>>=", 14);

        public final String string;
        public final int precedence;

        Operator(String s, int precedence) {
            this.string = s;
            this.precedence = precedence;
        }
    }
}
