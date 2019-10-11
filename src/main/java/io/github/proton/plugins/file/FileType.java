package io.github.proton.plugins.file;

import io.github.proton.plugins.txt.TextFileType;
import io.reactivex.rxjava3.functions.Predicate;

import java.io.File;
import java.util.*;

public interface FileType extends Predicate<File> {
    Registry registry = new Registry();

    String[] extensions();

    @Override
    default boolean test(File file) {
        return Arrays.stream(extensions()).anyMatch(x -> file.getName().endsWith(x));
    }

    final class Registry {
        private final Map<String, List<FileType>> fileTypes = new HashMap<>();

        public FileType get(File file) {
            String name = file.getName();
            int index = name.lastIndexOf('.');
            String extension = index == -1 ? "" : name.substring(index + 1);
            return fileTypes.getOrDefault(extension, Collections.emptyList())
                    .stream()
                    .filter(x -> x.test(file))
                    .findFirst()
                    .orElse(new TextFileType());
        }

        public void put(FileType type) {
            Arrays.stream(type.extensions()).forEach(extension -> {
                List<FileType> types = this.fileTypes.get(extension);
                if (types == null) {
                    fileTypes.put(extension, Collections.singletonList(type));
                } else {
                    types.add(0, type);
                }
            });
        }
    }
}
