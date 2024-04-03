package com.github.kmpk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WriterTest {
    private Path testFile;

    @BeforeEach
    void beforeEach() throws IOException {
        testFile = Files.createTempFile("test", ".csv");
        testFile.toFile().deleteOnExit();
    }

    @Test
    void writeFile() throws IOException {
        String[] testString1 = new String[]{"header1", "header2", "header3"};
        String[] testString2 = new String[]{"1", "2", "3"};
        String[] testString3 = new String[]{"11", "22", "33"};

        try (Writer writer = new Writer(testFile.toFile(), ',', "\r\n")) {
            writer.writeLine(testString1);
            writer.writeLine(testString2);
            writer.writeLine(testString3);
        }

        String expected = """
                header1,header2,header3\r
                1,2,3\r
                11,22,33\r
                """;

        assertEquals(expected, readFile(testFile));
    }

    @Test
    void writeFileEscaped() throws IOException {
        String[] testString1 = new String[]{"header1,", "header2", "header3"};
        String[] testString2 = new String[]{"1", "2,", "3\r\n"};
        String[] testString3 = new String[]{"11\"", "22", "33"};

        try (Writer writer = new Writer(testFile.toFile(), ',', "\r\n")) {
            writer.writeLine(testString1);
            writer.writeLine(testString2);
            writer.writeLine(testString3);
        }

        String expected = """
                "header1,",header2,header3\r
                1,"2,","3\r\n"\r
                "11\"\"",22,33\r
                """;

        assertEquals(expected, readFile(testFile));
    }

    @Test
    void writeFileEmpty() throws IOException {

        try (Writer writer = new Writer(testFile.toFile(), ',', "\r\n")) {
            writer.writeLine();
        }

        String expected = "";

        assertEquals(expected, readFile(testFile));
    }

    private String readFile(Path file) throws IOException {
        return Files.readString(file, StandardCharsets.UTF_8);
    }
}