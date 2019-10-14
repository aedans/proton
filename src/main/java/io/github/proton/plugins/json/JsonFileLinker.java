package io.github.proton.plugins.json;

import io.github.proton.plugins.file.FileLink;
import io.github.proton.plugins.file.FileLinker;
import io.reactivex.rxjava3.core.Single;

import java.io.File;

public final class JsonFileLinker implements FileLinker<FileLink<JsonTree>> {
    @Override
    public FileLink<JsonTree> link(File file) {
        return new FileLink<>(file, Single.fromCallable(() -> new JsonFileOpener().open(file)));
    }
}
