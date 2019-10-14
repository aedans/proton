package io.github.proton.plugins.txt;

import io.github.proton.plugins.file.FileLink;
import io.github.proton.plugins.file.FileLinker;
import io.reactivex.rxjava3.core.Single;

import java.io.File;

public final class TextFileLinker implements FileLinker<FileLink> {
    @Override
    public FileLink link(File file) {
        return new FileLink<>(file, Single.error(new RuntimeException("TODO")));
    }
}
