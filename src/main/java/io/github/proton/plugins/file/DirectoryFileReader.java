package io.github.proton.plugins.file;

import io.github.proton.file.FileReader;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import java.io.File;
import java.util.Objects;
import org.pf4j.Extension;

@Extension
public final class DirectoryFileReader implements FileReader<Directory> {
  @Override
  public Option<Directory> read(File file) {
    if (file.isDirectory()) {
      return Option.some(
          new Directory(Vector.of(Objects.requireNonNull(file.listFiles())).map(FileLink::new)));
    } else {
      return Option.none();
    }
  }
}
