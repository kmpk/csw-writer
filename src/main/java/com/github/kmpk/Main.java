package com.github.kmpk;

import com.github.kmpk.csvwriter.CsvHint;
import com.github.kmpk.csvwriter.CsvWriter;
import com.github.kmpk.csvwriter.CsvWriterBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException, IllegalAccessException {
        CsvWriter writer = new CsvWriterBuilder()
                .delimiter(',')
                .newLine("\r\n")
                .includeHeader(true)
                .ignoreNullElements(true)
                .build();
        File file = Files.createTempFile("", ".csv").toFile();
        file.deleteOnExit();
        Collection<Temp> objects = IntStream
                .range(1, 101)
                .mapToObj(i -> new Temp("a".repeat(i), i))
                .toList();
        writer.writeToFile(objects, Temp.class, file);
        System.out.printf("Csv file written to %s, press enter to close program and delete file%n", file.getAbsolutePath());
        System.in.read();
        System.exit(0);
    }

    private record Temp(@CsvHint(order = 2, name = "String field") String field1,
                        @CsvHint(order = 1, name = "int field") int field2) {
    }
}
