package io.github.proton.plugins.file;

import io.github.proton.display.Component;
import io.github.proton.util.Registry;
import io.reactivex.rxjava3.core.Single;

import java.io.File;

public interface FileOpener<T extends Component> {
    Registry<FileOpener> registry = new Registry<>("file opener");
    @SuppressWarnings("unchecked")
    FileOpener<Component> opener = file -> registry.getOrThrow(FileType.registry.get(file).getClass()).open(file);

    Single<T> open(File file);
}
