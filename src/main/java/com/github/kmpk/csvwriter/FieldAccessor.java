package com.github.kmpk.csvwriter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.PriorityQueue;

class FieldAccessor<T> {
    private final Class<T> clazz;
    private final HintedField[] fields;

    public FieldAccessor(Class<T> clazz) {
        this.clazz = clazz;
        fields = populateFields();
    }

    public String[] getFieldNames() {
        return Arrays.stream(fields)
                .map(of -> of.name().isEmpty() ? of.field().getName() : of.name())
                .toArray(String[]::new);
    }

    public String[] getFieldValues(T o) throws IllegalAccessException {
        String[] values = new String[fields.length];
        for (int i = 0; i < values.length; i++) {
            Field field = fields[i].field();

            if (field.getType().isArray()) {
                Object[] value = (Object[]) field.get(o);
                values[i] = value == null ? null : Arrays.toString(value);
            } else {
                Object value = field.get(o);
                values[i] = value == null ? null : value.toString();
            }
        }
        return values;
    }

    private HintedField[] populateFields() {
        PriorityQueue<HintedField> queue = new PriorityQueue<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) || field.getAnnotation(CsvIgnore.class) != null) {
                continue;
            }

            field.setAccessible(true);
            CsvHint hint = field.getAnnotation(CsvHint.class);
            if (hint != null) {
                queue.add(new HintedField(hint.order(), hint.name(), field));
            } else {
                queue.add(new HintedField(Integer.MAX_VALUE, CsvHint.CUSTOM_NAME_DEFAULT, field));
            }
        }

        HintedField[] result = new HintedField[queue.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = queue.poll();
        }
        return result;
    }

    private record HintedField(int order, String name, Field field) implements Comparable<HintedField> {
        @Override
        public int compareTo(HintedField o) {
            return Integer.compare(this.order, o.order);
        }
    }
}
