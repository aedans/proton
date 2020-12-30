package io.github.proton.plugin.java;

import io.github.proton.api.PatternHighlighter;

import java.util.*;

public final class JavaPatternHighlighter extends PatternHighlighter {
    public static final String[] KEYWORDS = new String[]{
        "abstract", "assert", "boolean", "break", "byte",
        "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else",
        "enum", "extends", "final", "finally", "float",
        "for", "goto", "if", "implements", "import",
        "instanceof", "int", "interface", "long", "native",
        "new", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super",
        "switch", "synchronized", "this", "throw", "throws",
        "transient", "try", "void", "volatile", "while",
        "var", "record", "sealed", "permits", "null", "true",
        "false"
    };

    private static final Map<String, String> patterns = new LinkedHashMap<>();

    static {
        String HEX_PATTERN = "(?x)\n\\b(?<!\\$)\n0(x|X)\n(\n  (?<!\\.)[0-9a-fA-F]([0-9a-fA-F_]*[0-9a-fA-F])?[Ll]?(?!\\.)\n  |\n  (\n    [0-9a-fA-F]([0-9a-fA-F_]*[0-9a-fA-F])?\\.?\n    |\n    ([0-9a-fA-F]([0-9a-fA-F_]*[0-9a-fA-F])?)?\\.[0-9a-fA-F]([0-9a-fA-F_]*[0-9a-fA-F])?\n  )\n  [Pp][+-]?[0-9]([0-9_]*[0-9])?[FfDd]?\n)\n\\b(?!\\$)";
        String BINARY_PATTERN = "\\b(?<!\\$)0(b|B)[01]([01_]*[01])?[Ll]?\\b(?!\\$)";
        String OCTAL_PATTERN = "\\b(?<!\\$)0[0-7]([0-7_]*[0-7])?[Ll]?\\b(?!\\$)";
        String DECIMAL_PATTERN = "(?x)\n(?<!\\$)\n(\n  \\b[0-9]([0-9_]*[0-9])?\\.\\B(?!\\.)\n  |\n  \\b[0-9]([0-9_]*[0-9])?\\.([Ee][+-]?[0-9]([0-9_]*[0-9])?)[FfDd]?\\b\n  |\n  \\b[0-9]([0-9_]*[0-9])?\\.([Ee][+-]?[0-9]([0-9_]*[0-9])?)?[FfDd]\\b\n  |\n  \\b[0-9]([0-9_]*[0-9])?\\.([0-9]([0-9_]*[0-9])?)([Ee][+-]?[0-9]([0-9_]*[0-9])?)?[FfDd]?\\b\n  |\n  (?<!\\.)\\B\\.[0-9]([0-9_]*[0-9])?([Ee][+-]?[0-9]([0-9_]*[0-9])?)?[FfDd]?\\b\n  |\n  \\b[0-9]([0-9_]*[0-9])?([Ee][+-]?[0-9]([0-9_]*[0-9])?)[FfDd]?\\b\n  |\n  \\b[0-9]([0-9_]*[0-9])?([Ee][+-]?[0-9]([0-9_]*[0-9])?)?[FfDd]\\b\n  |\n  \\b(0|[1-9]([0-9_]*[0-9])?)(?!\\.)[Ll]?\\b\n)\n(?!\\$)";

        patterns.put("keyword", "\\b(" + String.join("|", KEYWORDS) + ")\\b");
        patterns.put("call", "([A-Za-z_$][\\w$]*)(?=\\s*(\\())");
        patterns.put("identifier", "[a-zA-Z$_][a-zA-Z$_0-9]*");
        patterns.put("string", "\"([^\"\\\\]|\\\\.)*\"");
        patterns.put("char", "'([^\"\\\\]|\\\\.)*'");
        patterns.put("number", HEX_PATTERN + "|" + BINARY_PATTERN + "|" + OCTAL_PATTERN + "|" + DECIMAL_PATTERN);
        patterns.put("comment", "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/" + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)");
        patterns.put("annotation", "((@)\\s*([^\\s(]+))");
    }

    public JavaPatternHighlighter() {
        super(patterns);
    }
}
