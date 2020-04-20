package io.github.proton.display;

import io.github.proton.plugins.Plugins;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.pf4j.ExtensionPoint;

public interface Projector<T> extends ExtensionPoint {
  @SuppressWarnings("rawtypes")
  Map<Class, Projector> instances =
      Plugins.getExtensions(Projector.class)
          .toMap(projector -> new Tuple2<>(projector.clazz(), projector));

  static <T> Projector<T> get(Class<T> clazz) {
    return getOption(clazz)
        .getOrElseThrow(() -> new RuntimeException("Could not find projector for " + clazz));
  }

  @SuppressWarnings("unchecked")
  static <T> Option<Projector<T>> getOption(Class<T> clazz) {
    return instances.get(clazz).map(projector -> (Projector<T>) projector);
  }

  Class<T> clazz();

  Projection<T> project(T t);
}
