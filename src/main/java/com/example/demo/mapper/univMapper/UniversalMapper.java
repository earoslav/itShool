package com.example.demo.mapper.univMapper;

import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * UniversalMapper copies fields with matching names between entities and DTOs using reflection.
 * It handles nested objects and collections recursively with safety checks for system classes and Hibernate lazy loading.
 */
@Service
public class UniversalMapper {

    public UniversalMapper() {}

    public static <S, T> T generalMapper(S source, Class<T> targetClass) {
        if (source == null) return null;

        // If types match exactly or it's a simple type (String, Integer, etc.), return as is
        if (source.getClass().equals(targetClass) || isSimpleType(targetClass)) {
            return (T) source;
        }

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            Field[] sourceFields = getAllFields(source.getClass());
            Field[] targetFields = getAllFields(targetClass);

            for (Field s : sourceFields) {
                s.setAccessible(true);
                for (Field t : targetFields) {
                    if (s.getName().equals(t.getName())) {
                        t.setAccessible(true);
                        try {
                            Object value = s.get(source);
                            if (value == null) continue;

                            // 1. Same types and not a collection - direct copy
                            if (s.getType().equals(t.getType()) && !Collection.class.isAssignableFrom(s.getType())) {
                                t.set(target, value);
                            }
                            // 2. Collections (Lists) - map elements recursively
                            else if (Collection.class.isAssignableFrom(s.getType()) && Collection.class.isAssignableFrom(t.getType())) {
                                if (value instanceof List) {
                                    mapList((List<?>) value, t, target);
                                }
                            }
                            // 3. Different types - handle dates or nested objects
                            else {
                                if (s.getType().equals(LocalDateTime.class) && t.getType().equals(String.class)) {
                                    t.set(target, value.toString());
                                } else if (!isSimpleType(t.getType()) && !t.getType().getName().startsWith("java.")) {
                                    t.set(target, generalMapper(value, t.getType()));
                                }
                            }
                        } catch (Exception e) {
                            // Silently skip fields that cause LazyInitializationException or access errors
                        }
                    }
                }
            }
            return target;
        } catch (Exception e) {
            return null;
        }
    }

    private static void mapList(List<?> sourceList, Field targetField, Object targetObj) {
        try {
            Type genericType = targetField.getGenericType();
            if (genericType instanceof ParameterizedType pt) {
                Class<?> itemClass = (Class<?>) pt.getActualTypeArguments()[0];
                List<Object> newList = new ArrayList<>();
                for (Object item : sourceList) {
                    Object mappedItem = generalMapper(item, itemClass);
                    if (mappedItem != null) {
                        newList.add(mappedItem);
                    }
                }
                targetField.set(targetObj, newList);
            }
        } catch (Exception e) {
            // If mapping the list fails (e.g. lazy loading without session), field remains null/empty
        }
    }

    private static boolean isSimpleType(Class<?> type) {
        return type.isPrimitive()
                || type.getName().startsWith("java.lang")
                || type.getName().startsWith("java.time")
                || Number.class.isAssignableFrom(type)
                || type.equals(String.class)
                || type.equals(Boolean.class);
    }

    private static Field[] getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && !clazz.getName().startsWith("java.")) {
            for (Field f : clazz.getDeclaredFields()) {
                fields.add(f);
            }
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }
}
