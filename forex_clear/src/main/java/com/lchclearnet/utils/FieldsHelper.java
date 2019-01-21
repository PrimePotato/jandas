package com.lchclearnet.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;

public class FieldsHelper {

    private static Logger logger = LogManager.getLogger();

    private FieldsHelper() {
    }

    /**
     * Get the specified field on the class. If the field is not found on the class itself will recursively check
     * the superclass.
     *
     * @param clazz     the class definition containing the field
     * @param fieldName the field to locate
     * @return the field if found, otherwise an exception is thrown
     */
    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException nsf) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), fieldName);
            }

            throw new IllegalStateException("Could not locate field '" + fieldName + "' on class " + clazz);
        }
    }

    /**
     * Returns the value of a (nested) field on a bean.
     *
     * @param bean         the object
     * @param nestedFields the fields name representing nested properties
     * @return the value
     */
    public static <T> T get(Object bean, String... nestedFields) {
        Object value = bean;
        try {
            Class<?> componentClass = bean.getClass();
            for (String nestedField : nestedFields) {
                if (value instanceof Map) {
                    value = ((Map) value).get(nestedField);
                } else {
                    Field field = getField(componentClass, nestedField);
                    field.setAccessible(true);
                    value = field.get(value);
                }
                if (value != null) {
                    componentClass = value.getClass();
                }
            }
        } catch (IllegalAccessException iae) {
            throw new IllegalStateException(iae);
        }

        return (T) value;

    }

    /**
     * Returns the value of a (nested) field on a bean.
     *
     * @param bean         the object
     * @param defaultValue the value to return in case the property is not found
     * @param nestedFields the fields name representing nested properties
     * @return the value
     */
    public static <T> T getOrDefault(T defaultValue, Object bean, String... nestedFields) {
        T value;
        try {
            value = get(bean, nestedFields);
        } catch (Exception iae) {
            value = null;
        }

        return value != null ? value : defaultValue;
    }

    /**
     * Sets the value of a (nested) field on a bean.
     *
     * @param newValue     the value to set
     * @param bean         the object
     * @param nestedFields the fields name representing nested properties
     */
    public static void set(Object newValue, Object bean, String... nestedFields) {
        Class<?> componentClass = bean.getClass();
        Object value = bean;
        Field field = null;

        try {
            for (int i = 0; i < nestedFields.length; i++) {
                String nestedField = nestedFields[i];
                field = getField(componentClass, nestedField);
                field.setAccessible(true);

                if (i == nestedFields.length - 1) {
                    break;
                }

                value = field.get(value);
                if (value != null) {
                    componentClass = value.getClass();
                }
            }

            field.set(value, newValue);
        } catch (IllegalAccessException iae) {
            throw new IllegalStateException(iae);
        }
    }
}
