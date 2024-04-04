package com.github.kmpk.csvwriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

class Writer implements AutoCloseable {
    private final FileWriter fileWriter;
    private final char delimiter;
    private final String newLine;

    public Writer(File file, char delimiter, String newLine) throws IOException {
        fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
        this.delimiter = delimiter;
        this.newLine = newLine;
    }

    public void writeLine(String... values) throws IOException {
        if (values.length == 0) {
            return;
        }
        StringBuilder builder = new StringBuilder();

        Iterator<String> iterator = Arrays.stream(values).iterator();
        builder.append(escapeChars(iterator.next()));
        while (iterator.hasNext()) {
            builder.append(delimiter).append(escapeChars(iterator.next()));
        }
        builder.append(newLine);

        fileWriter.write(builder.toString());
    }

    private String escapeChars(String s) {
        boolean toEscape = s.contains("\"") || s.indexOf(delimiter) != -1 || s.contains(newLine);

        if (toEscape) {
            s = "\"" + s.replaceAll("\"", "\"\"") + "\"";
        }

        return s;
    }

    @Override
    public void close() throws IOException {
        fileWriter.close();
    }
}
