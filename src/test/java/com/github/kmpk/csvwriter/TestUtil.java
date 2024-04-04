package com.github.kmpk.csvwriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtil {
    private TestUtil() {
    }

    public static String readFile(Path file) throws IOException {
        return Files.readString(file, StandardCharsets.UTF_8);
    }
}
