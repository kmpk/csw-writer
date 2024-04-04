Library that provides functionality to write Java objects to CSV files.

## Features
- Include or exclude header row in CSV output.
- Specify delimiter character and newline sequence.
- Include or ignore null elements.
- Ignore fields by using @CsvIgnore annotation.
- Use @CsvHint annotation to customize the order and names of fields in the CSV output.
- Include content of inner collections and arrays.
- Inner objects of other types are written using their toString() method.

## Usage

Maven (Note: The library must be in your local repository as it is not available on Maven Central)
```xml
<dependency>
  <groupId>com.github.kmpk</groupId>
  <artifactId>csw-writer</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Usage:
```java
CsvWriter csvWriter = new CsvWriterBuilder()
        .delimiter(',')
        .newLine("\r\n")
        .includeHeader(true)
        .ignoreNullElements(true)
        .build();

List<Person> persons = // Populate the list of Person objects
File outputFile = new File("output.csv");

csvWriter.writeToFile(persons, Person.class, outputFile);
```
