package io.github.proton.plugins.directory;

import io.github.proton.plugins.file.FileOpener;
import io.reactivex.rxjava3.core.Single;

import java.io.File;

public final class DirectoryFileOpener implements FileOpener<Directory> {
    @Override
    public Single<Directory> open(File file) {
        return Single.just(new Directory(file));
    }
}
