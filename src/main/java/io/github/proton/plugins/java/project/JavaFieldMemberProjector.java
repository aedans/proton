package io.github.proton.plugins.java.project;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.plugins.java.tree.JavaFieldMember;
import io.github.proton.plugins.text.LabelProjection;
import io.github.proton.plugins.text.Line;
import org.pf4j.Extension;

@Extension
public final class JavaFieldMemberProjector implements Projector<JavaFieldMember> {
  @Override
  public Class<JavaFieldMember> clazz() {
    return JavaFieldMember.class;
  }

  @Override
  public Projection<JavaFieldMember> project(JavaFieldMember fieldMember) {
    Projection<JavaFieldMember> label =
        new LabelProjection("val ", "keyword").map(x -> fieldMember);
    Projection<JavaFieldMember> projection =
        Projector.get(Line.class).project(fieldMember.name).map(JavaFieldMember::new);
    return label.combineHorizontal(projection);
  }
}
