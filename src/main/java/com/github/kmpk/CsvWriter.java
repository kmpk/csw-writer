package com.github.kmpk;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * CsvWriter provides functionality to write Java objects to CSV files.
 */
public class CsvWriter {
    private final char delimiter;
    private final String newLine;
    private final boolean includeHeader;
    private final boolean ignoreNullElements;
    CsvWriter(char delimiter, String newLine, boolean includeHeader, boolean ignoreNullElements) {
        this.delimiter = delimiter;
        this.newLine = newLine;
        this.includeHeader = includeHeader;
        this.ignoreNullElements = ignoreNullElements;
    }

    /**
     * Writes the elements of the specified collection to the CSV file. If the file does not exist, it will be created.
     * If the file already exists, it will be overwritten.
     *
     * @param collection The collection of objects to write to the CSV.
     * @param clazz      The class type of the objects in the collection.
     * @param file       The file to which the CSV will be written.
     * @param <T>        The type of elements in the collection.
     * @throws IOException            If an I/O error occurs while writing to the file.
     * @throws IllegalAccessException If access to the objects' fields is denied.
     * @throws NullPointerException   If collection, clazz or file is null.
     */
    public <T> void writeToFile(Collection<T> collection, Class<T> clazz, File file) throws IOException, IllegalAccessException {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(file);
        FieldAccessor<T> fieldAccessor = new FieldAccessor<>(clazz);
        Iterator<T> iterator = collection.iterator();
        writeToFile(iterator, fieldAccessor, file);
    }

    /**
     * Writes the elements of the specified collection to the CSV file. If the file does not exist, it will be created.
     * If the file already exists, it will be overwritten.
     *
     * @param array The array of objects to write to the CSV.
     * @param clazz   The class type of the objects in the array.
     * @param file    The file to which the CSV will be written.
     * @param <T>     The type of elements in the array.
     * @throws IOException            If an I/O error occurs while writing to the file.
     * @throws IllegalAccessException If access to the objects' fields is denied.
     * @throws NullPointerException   If array, clazz or file is null.
     */
    public <T> void writeToFile(T[] array, Class<T> clazz, File file) throws IOException, IllegalAccessException {
        Objects.requireNonNull(array);
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(file);
        FieldAccessor<T> fieldAccessor = new FieldAccessor<>(clazz);
        Iterator<T> iterator = Arrays.stream(array).iterator();
        writeToFile(iterator, fieldAccessor, file);
    }


    /**
     * Writes the specified object to the CSV file.If the file does not exist, it will be created.
     * If the file already exists, it will be overwritten.
     *
     * @param object The object to write to the CSV.
     * @param file   The file to which the CSV will be written.
     * @param <T>    The type of the object.
     * @throws IOException            If an I/O error occurs while writing to the file.
     * @throws IllegalAccessException If access to the object's fields is denied.
     * @throws NullPointerException   If object or file is null.
     */
    @SuppressWarnings("unchecked")
    public <T> void writeToFile(T object, File file) throws IOException, IllegalAccessException {
        Objects.requireNonNull(object);
        Objects.requireNonNull(file);
        FieldAccessor<T> fieldAccessor = new FieldAccessor<>((Class<T>) object.getClass());
        Iterator<T> iterator = Collections.singleton(object).iterator();
        writeToFile(iterator, fieldAccessor, file);
    }

    private <T> void writeToFile(Iterator<T> iterator, FieldAccessor<T> accessor, File file) throws IOException, IllegalAccessException {
        try (Writer writer = new Writer(file, delimiter, newLine)) {
            if (includeHeader) {
                writer.writeLine(accessor.getFieldNames());
            }
            int columns = accessor.getFieldNames().length;
            while (iterator.hasNext()) {
                String[] values = null;

                T next = iterator.next();

                if (next != null) {
                    values = accessor.getFieldValues(next);
                } else if (!ignoreNullElements) {
                    values = new String[columns];
                }

                if (values != null) {
                    changeNullToEmptyString(values);
                    writer.writeLine(values);
                }
            }
        }
    }

    private void changeNullToEmptyString(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                array[i] = "";
            }
        }
    }
}
