package ru.akirakozov.sd.refactoring.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextUtils {
    public static String getResourceAsString(String path) throws IOException {
        Path javaPath = FileSystems.getDefault().getPath(path);
        try (final InputStream is = Files.newInputStream(javaPath)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
