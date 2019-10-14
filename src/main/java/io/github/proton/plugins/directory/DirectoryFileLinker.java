package io.github.proton.plugins.directory;

import io.github.proton.plugins.file.FileLink;
import io.github.proton.plugins.file.FileLinker;
import io.reactivex.rxjava3.core.Single;

import java.io.File;

public final class DirectoryFileLinker implements FileLinker<FileLink<Directory>> {
    @Override
    public FileLink<Directory> link(File file) {
        return new FileLink<>(file, Single.just(new Directory(file)));
    }
}
