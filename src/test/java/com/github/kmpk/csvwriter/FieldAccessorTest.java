package com.github.kmpk.csvwriter;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldAccessorTest {
    @Test
    void testIgnored() throws IllegalAccessException {
        FieldAccessor<TestClassIgnored> accessor = new FieldAccessor<>(TestClassIgnored.class);
        assertArrayEquals(new String[]{"field"}, accessor.getFieldNames());
        assertArrayEquals(new String[]{"visible field"}, accessor.getFieldValues(new TestClassIgnored()));
    }

    static class TestClassIgnored {
        private static final String STATIC = "ignored static";

        private final String field = "visible field";
        @CsvIgnore
        private final String ignoredField = "ignoredField";
    }

    @Test
    void testOrder() throws IllegalAccessException {
        FieldAccessor<TestClassOrder> accessor = new FieldAccessor<>(TestClassOrder.class);
        assertArrayEquals(new String[]{"first", "second", "third"}, accessor.getFieldNames());
        assertArrayEquals(new String[]{"first", "second", "third"}, accessor.getFieldValues(new TestClassOrder()));
    }

    static class TestClassOrder {
        @CsvHint(order = 3)
        private final String third = "third";
        @CsvHint(order = 2)
        private final String second = "second";
        @CsvHint(order = 1)
        private final String first = "first";

    }

    @Test
    void testCustomName() {
        FieldAccessor<TestClassCustomName> accessor = new FieldAccessor<>(TestClassCustomName.class);
        assertEquals(Set.of("custom1", "custom2", "custom3"), Set.of(accessor.getFieldNames()));
    }

    static class TestClassCustomName {
        @CsvHint(name = "custom1")
        private final String field1 = "1";
        @CsvHint(name = "custom2")
        private final String field2 = "2";
        @CsvHint(name = "custom3")
        private final String field3 = "3";

    }

    @Test
    void testFormat() throws IllegalAccessException {
        FieldAccessor<TestClassFormat> accessor = new FieldAccessor<>(TestClassFormat.class);
        TestClassFormat testObject = new TestClassFormat();
        assertEquals(Set.of("array", "innerClassToString", "innerClass", "map", "list"), Set.of(accessor.getFieldNames()));
        assertEquals(Set.of("[array]", "[list]", "{map=1}", testObject.innerClass.toString(), "Inner class with toString()"), Set.of(accessor.getFieldValues(testObject)));
    }

    static class TestClassFormat {

        private final String[] array = new String[]{"array"};
        private final List<String> list = List.of("list");
        private final Map<String, Integer> map = Map.of("map", 1);
        private final InnerClass innerClass = new InnerClass();
        private final InnerClassToString innerClassToString = new InnerClassToString();

        private static class InnerClass {
            private final String field = "Inner class";
        }

        private static class InnerClassToString {
            @Override
            public String toString() {
                return "Inner class with toString()";
            }
        }

    }
}