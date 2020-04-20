package io.github.proton.plugins.text;

import io.github.proton.file.FileReader;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.pf4j.Extension;

@Extension
public final class TextFileReader implements FileReader<Text> {
  @Override
  public Option<Text> read(File file) {
    if (file.getName().endsWith(".txt")) {
      try {
        return Option.of(
            new Text(
                Files.readAllLines(file.toPath())
                    .stream()
                    .map(Line::new)
                    .collect(Vector.collector())));
      } catch (IOException e) {
        return Option.none();
      }
    } else {
      return Option.none();
    }
  }
}
