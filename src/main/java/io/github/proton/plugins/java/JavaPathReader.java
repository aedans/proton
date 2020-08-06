package io.github.proton.plugins.java;

import com.sun.source.util.JavacTask;
import io.github.proton.io.PathReader;
import io.github.proton.plugins.java.tree.JavaCompilationUnit;
import io.vavr.control.Option;
import org.pf4j.Extension;

import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

@Extension
public final class JavaPathReader implements PathReader<JavaCompilationUnit> {
    @Override
    public Option<JavaCompilationUnit> read(Path path) throws IOException {
        if (path.toString().endsWith(".java")) {
            var compiler = ToolProvider.getSystemJavaCompiler();
            var fileManager = compiler.getStandardFileManager(null, null, null);
            var compilationUnits = fileManager.getJavaFileObjectsFromPaths(Collections.singletonList(path));
            var task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
            var trees = ((JavacTask) task).parse();
            var tree  = trees.iterator().next();
            return Option.some(JavaCompilationUnit.from(tree));
        } else {
            return Option.none();
        }
    }
}
