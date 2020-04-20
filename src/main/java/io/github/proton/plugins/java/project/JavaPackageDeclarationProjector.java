package io.github.proton.plugins.java.project;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.plugins.java.tree.JavaPackageDeclaration;
import io.github.proton.plugins.text.LabelProjection;
import io.github.proton.plugins.text.Line;
import org.pf4j.Extension;

@Extension
public final class JavaPackageDeclarationProjector implements Projector<JavaPackageDeclaration> {
  @Override
  public Class<JavaPackageDeclaration> clazz() {
    return JavaPackageDeclaration.class;
  }

  @Override
  public Projection<JavaPackageDeclaration> project(JavaPackageDeclaration packageDeclaration) {
    Projection<JavaPackageDeclaration> label =
        new LabelProjection("package ", "keyword").map(x -> packageDeclaration);
    Projection<JavaPackageDeclaration> projection =
        Projector.get(Line.class).project(packageDeclaration.name).map(JavaPackageDeclaration::new);
    return label.combineHorizontal(projection);
  }
}
