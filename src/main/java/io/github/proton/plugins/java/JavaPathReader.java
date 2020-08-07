package io.github.proton.plugins.java;

import com.github.javaparser.StaticJavaParser;
import io.github.proton.io.PathReader;
import io.github.proton.plugins.java.tree.JavaCompilationUnit;
import io.vavr.control.Option;
import org.pf4j.Extension;

import java.io.IOException;
import java.nio.file.Path;

@Extension
public final class JavaPathReader implements PathReader<JavaCompilationUnit> {
    @Override
    public Option<JavaCompilationUnit> read(Path path) throws IOException {
        if (path.toString().endsWith(".java")) {
            var parse = StaticJavaParser.parse(path);
            return Option.some(JavaCompilationUnit.from(parse));
        } else {
            return Option.none();
        }
    }
}
