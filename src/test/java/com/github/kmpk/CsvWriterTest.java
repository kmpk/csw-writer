package com.github.kmpk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvWriterTest {
    private static final TestClass[] TEST_VALUES = {new TestClass("s1", 1), new TestClass(null, 2), null};
    private static final String TEST_RESULT = """
            int,String\r
            1,s1\r
            2,\r
            ,\r
            """;
    private static final String TEST_RESULT_IGNORE_NULL = """
            int,String\r
            1,s1\r
            2,\r
            """;
    private static final String TEST_RESULT_NO_HEADER = """
            1,s1\r
            2,\r
            ,\r
            """;
    private Path testFile;

    @BeforeEach
    void beforeEach() throws IOException {
        testFile = Files.createTempFile("test", ".csv");
        testFile.toFile().deleteOnExit();
    }

    @Test
    void writeSingle() throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriter(',', "\r\n", true, false);

        writer.writeToFile(new TestClass("s1", 1), testFile.toFile());

        String expected = """
                int,String\r
                1,s1\r
                """;
        assertEquals(expected, readFile(testFile));
    }

    @Test
    void writeSingleNoHeader() throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriter(',', "\r\n", false, false);

        writer.writeToFile(new TestClass("s1", 1), testFile.toFile());

        String expected = """
                1,s1\r
                """;
        assertEquals(expected, readFile(testFile));
    }

    @Test
    void writeArray() throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriter(',', "\r\n", true, false);

        writer.writeToFile(TEST_VALUES, TestClass.class, testFile.toFile());

        assertEquals(TEST_RESULT, readFile(testFile));
    }

    @Test
    void writeArrayIgnoreNull() throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriter(',', "\r\n", true, true);

        writer.writeToFile(TEST_VALUES, TestClass.class, testFile.toFile());
        assertEquals(TEST_RESULT_IGNORE_NULL, readFile(testFile));
    }

    @Test
    void writeArrayNoHeader() throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriter(',', "\r\n", false, false);

        writer.writeToFile(TEST_VALUES, TestClass.class, testFile.toFile());
        assertEquals(TEST_RESULT_NO_HEADER, readFile(testFile));
    }

    @Test
    void writeCollection() throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriter(',', "\r\n", true, false);

        writer.writeToFile(Arrays.asList(TEST_VALUES), TestClass.class, testFile.toFile());
        assertEquals(TEST_RESULT, readFile(testFile));
    }

    @Test
    void writeCollectionIgnoreNull() throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriter(',', "\r\n", true, true);

        writer.writeToFile(Arrays.asList(TEST_VALUES), TestClass.class, testFile.toFile());
        assertEquals(TEST_RESULT_IGNORE_NULL, readFile(testFile));
    }

    @Test
    void writeCollectionNoHeader() throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriter(',', "\r\n", false, false);

        writer.writeToFile(Arrays.asList(TEST_VALUES), TestClass.class, testFile.toFile());
        assertEquals(TEST_RESULT_NO_HEADER, readFile(testFile));
    }

    @Test
    void writeCollectionEscaping() throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriter(',', "\r\n", true, false);

        List<TestClass> objects = Arrays.asList(new TestClass("s1\r\n", 1), new  TestClass("a,b,c,d", 2), new TestClass("\"asd\"", 3));

        writer.writeToFile(objects, TestClass.class, testFile.toFile());

        String expected = """
                int,String\r
                1,"s1\r\n"\r
                2,"a,b,c,d"\r
                3,\"""asd""\"\r
                """;
        assertEquals(expected, readFile(testFile));
    }

    private static class TestClass {
        @CsvHint(order = 2, name = "String")
        private final String field1;
        @CsvHint(order = 1, name = "int")
        private final int field2;

        private TestClass(String field1, int field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }

    private String readFile(Path file) throws IOException {
        return Files.readString(file, StandardCharsets.UTF_8);
    }
}