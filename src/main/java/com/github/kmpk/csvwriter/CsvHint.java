package com.github.kmpk.csvwriter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @CsvHint} is used to customize the order and names of fields in CSV output.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvHint {
    String CUSTOM_NAME_DEFAULT = "";
    /**
     * Specifies the order in which fields should appear in the CSV output.
     * Fields with lower order values will appear first. There is no guarantee on the order of fields with the same
     * order value.
     */
    int order() default Integer.MAX_VALUE;
    /**
     * Specifies a custom name for the field in the CSV output.
     * If not provided, the class field's name will be used.
     */
    String name() default CUSTOM_NAME_DEFAULT;
}
