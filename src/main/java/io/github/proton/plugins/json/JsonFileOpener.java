package io.github.proton.plugins.json;

import com.eclipsesource.json.Json;
import io.github.proton.plugins.file.FileOpener;
import io.reactivex.rxjava3.core.Single;

import java.io.File;
import java.io.FileReader;

public final class JsonFileOpener implements FileOpener<JsonTree> {
    @Override
    public Single<JsonTree> open(File file) {
        return Single.fromCallable(() -> JsonTree.from(Json.parse(new FileReader(file))));
    }
}
