package io.github.proton.plugins.file;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.plugins.text.Line;
import io.github.proton.plugins.text.LineProjector;
import org.pf4j.Extension;

@Extension
public final class FileLinkProjector implements Projector<FileLink> {
  @Override
  public Class<FileLink> clazz() {
    return FileLink.class;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Projection<FileLink> project(FileLink fileLink) {
    Projection<FileLink> name =
        () ->
            new LineProjector()
                .project(new Line(fileLink.file.getName()))
                .characters()
                .mapValues(
                    c ->
                        c.map(x -> fileLink)
                            .onSubmit(new FileLink(fileLink.tree, fileLink.file, !fileLink.open)));
    if (fileLink.open) {
      Projector<Object> projector = Projector.get((Class) fileLink.tree.getClass());
      Projection<FileLink> content =
          () ->
              projector
                  .project(fileLink.tree)
                  .characters()
                  .mapValues(c -> c.map(object -> new FileLink(object, fileLink.file, true)));
      return name.combineVertical(content.indent(2));
    } else {
      return name;
    }
  }
}
