//package com.example.demo.mapper.univMapper;
//
//import com.example.demo.models.entities.Teacher;
//import com.example.demo.models.entities.User;
//import org.springframework.stereotype.Service;
//
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//
//@Service
//public class UniversalMapper {
//    public UniversalMapper() {
//    }
//
//    public static  <S, T> T generalMapper(S source, Class<T> targetClass){
//        if(source==null){
//            return null;
//        }
//        try{
//
//            T target = targetClass.getDeclaredConstructor().newInstance();
//
//            Field[] sourceFields = source.getClass().getDeclaredFields();
//            Field[] targetFields = targetClass.getDeclaredFields();
//
//            for(Field s : sourceFields){
//                s.setAccessible(true);
//                for(Field t : targetFields){
//                    if(s.getName().equals(t.getName())){
//                        if(s.getType().equals(t.getType())){
//                            t.setAccessible(true);
//                            t.set(target, s.get(source));
//                        }else{
//                            t.setAccessible(true);
//                            if(s.getType().equals(LocalDateTime.class)){
//                                t.set(target, ((LocalDateTime)s.get(source)).toString());
//                            }
//                            else{
//                                t.set(target, generalMapper(s.get(source), t.getType()));
//                            }
//
////                            t.set(target, generalMapper(s.getType().cast(s), t.getType()));
//                         }
//
//                    }
//                }
//            }
//            return target;
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }
//}
package com.example.demo.mapper.univMapper;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UniversalMapper {

//    public static <S, T> T generalMapper(S source, Class<T> targetClass) {
//
//        if (source == null) return null;
//
//        try {
//            T target = targetClass.getDeclaredConstructor().newInstance();
//
//            List<Field> sourceFields = getAllFields(source.getClass());
//            List<Field> targetFields = getAllFields(targetClass);
//
//            for (Field s : sourceFields) {
//                s.setAccessible(true);
//
//                for (Field t : targetFields) {
//
//                    if (s.getName().equals(t.getName())) {
//
//                        t.setAccessible(true);
//
//                        Object value = s.get(source);
//
//                        if (value == null) continue;
//
//                        if (t.getType().isAssignableFrom(s.getType())) {
//                            t.set(target, value);
//                        }
//                        else if (s.getType().equals(LocalDateTime.class)
//                                && t.getType().equals(String.class)) {
//
//                            t.set(target, value.toString());
//                        }
//                        else if (isSimpleType(t.getType())) {
//                            t.set(target, value);
//                        }
//                        else {
//                            t.set(target, generalMapper(value, t.getType()));
//                        }
//                    }
//                }
//            }
//
//            return target;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private static List<Field> getAllFields(Class<?> clazz) {
//
//        List<Field> fields = new ArrayList<>();
//
//        while (clazz != null && clazz != Object.class) {
//
//            for (Field f : clazz.getDeclaredFields()) {
//                fields.add(f);
//            }
//
//            clazz = clazz.getSuperclass();
//        }
//
//        return fields;
//    }
//    private static boolean isSimpleType(Class<?> type) {
//        return type.isPrimitive()
//                || type.equals(String.class)
//                || Number.class.isAssignableFrom(type)
//                || type.equals(Boolean.class)
//                || type.equals(Character.class)
//                || type.equals(java.time.LocalDate.class)
//                || type.equals(java.time.LocalDateTime.class);
//    }
public UniversalMapper() {
}

    public static  <S, T> T generalMapper(S source, Class<T> targetClass){
        if(source==null){
            return null;
        }
        try{
            T target = targetClass.getDeclaredConstructor().newInstance();

            Field[] sourceFields = source.getClass().getDeclaredFields();
            Field[] targetFields = targetClass.getDeclaredFields();

            for(Field s : sourceFields){
                s.setAccessible(true);
                for(Field t : targetFields){
                    if(s.getName().equals(t.getName())){
                        if(s.getType().equals(t.getType())){
                            t.setAccessible(true);
                            t.set(target, s.get(source));
                        }else{
                            t.setAccessible(true);
                            if(s.getType().equals(LocalDateTime.class)){
                                t.set(target, ((LocalDateTime)s.get(source)).toString());
                            }
                            else{
                                t.set(target, generalMapper(s.get(source), t.getType()));
                            }


                        }

                    }
                }
            }
            return target;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

