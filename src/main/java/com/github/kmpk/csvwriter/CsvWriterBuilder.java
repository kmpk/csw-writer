package com.github.kmpk.csvwriter;

/**
 * CsvWriterBuilder provides an interface for building {@link CsvWriter} instances with customizable settings.
 */
public class CsvWriterBuilder {
    private char delimiter = ',';
    private String newLine = "\r\n";
    private boolean includeHeader = true;
    private boolean ignoreNullElements = true;

    /**
     * Sets the delimiter character used in the CSV.
     *
     * @param delimiter The delimiter character.
     * @return This instance.
     */
    public CsvWriterBuilder delimiter(char delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    /**
     * Sets the newline character sequence used in the CSV.
     *
     * @param newLine The newline character sequence.
     * @return This instance.
     */
    public CsvWriterBuilder newLine(String newLine) {
        this.newLine = newLine;
        return this;
    }

    /**
     * Sets whether to include a header row in the CSV.
     *
     * @param includeHeader {@code true} to include a header row, {@code false} otherwise.
     * @return This instance.
     */
    public CsvWriterBuilder includeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
        return this;
    }

    /**
     * Sets whether to ignore null elements when writing to the CSV.
     *
     * @param ignoreNullElements {@code true} to ignore null elements, {@code false} otherwise.
     * @return This instance.
     */
    public CsvWriterBuilder ignoreNullElements(boolean ignoreNullElements) {
        this.ignoreNullElements = ignoreNullElements;
        return this;
    }

    /**
     * Constructs a {@link CsvWriter} instance with the specified settings.
     *
     * @return A {@link CsvWriter} instance configured with the specified settings.
     * @throws IllegalArgumentException If the settings are invalid.
     */
    public CsvWriter build() {
        if (delimiter == '"') {
            throw new IllegalArgumentException("Can't use double quotes as delimiter");
        }
        if (newLine == null) {
            throw new IllegalArgumentException("Newline char sequence must be specified");
        }
        if (newLine.indexOf(delimiter) != -1 || newLine.indexOf('"') != -1) {
            throw new IllegalArgumentException("Newline char sequence must not contain delimiter or double quotes");
        }
        return new CsvWriter(delimiter, newLine, includeHeader, ignoreNullElements);
    }
}
