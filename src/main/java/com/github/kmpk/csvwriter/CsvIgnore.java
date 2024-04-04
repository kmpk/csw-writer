package com.github.kmpk.csvwriter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @CsvIgnore} is used to mark fields that should be ignored by CsvWriter when generating CSV output.
 * Fields annotated with {@code @CsvIgnore} will not be included in the generated CSV.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvIgnore {
}
