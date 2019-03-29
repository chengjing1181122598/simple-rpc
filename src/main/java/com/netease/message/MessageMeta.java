package com.netease.message;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.netease.annotation.Tag;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageMeta<T> {

    private static final ConcurrentHashMap<Class<?>, MessageMeta<?>> MESSAGE_METAS = new ConcurrentHashMap<>();
    private static final Set<?> IGNORE_VALUE = Set.of((byte) 0, (short) 0, 0, (long) 0, (float) 0, 0.0, false, (char) 0, "");
    private final FieldMeta[] fieldMetas;
    private final FieldTypeEnum basicFieldTypeEnum;

    private MessageMeta(Class<T> clazz) {
        if ((basicFieldTypeEnum = FieldMeta.BASE_CLASS_MAPPING_FIELD_TYPE_ENUM.get(clazz)) != null) {
            fieldMetas = null;
            return;
        }

        forbidExtends(clazz);
        LinkedHashSet<FieldMeta> fieldMetas = new LinkedHashSet<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        int number = 1;
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())
                    || Modifier.isFinal(field.getModifiers())
                    || Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            Tag tag = field.getAnnotation(Tag.class);
            if (tag != null && tag.number() > 0) {
                number = tag.number();
            }
            if (!fieldMetas.add(new FieldMeta(field, number))) {
                throw new IllegalArgumentException(String.format("duplicate number[%d] on class[%s]", number, clazz.getName()));
            }
            number++;
        }
        this.fieldMetas = fieldMetas.toArray(new FieldMeta[fieldMetas.size()]);
    }

    static <T> MessageMeta<T> getMessageMeta(T message) {
        Class<T> clazz = (Class<T>) message.getClass();
        return getMessageMeta(clazz);
    }

    static <T> MessageMeta<T> getMessageMeta(Class<T> clazz) {
        MessageMeta<?> messageMeta = MESSAGE_METAS.get(clazz);
        if (messageMeta == null) {
            messageMeta = MESSAGE_METAS.computeIfAbsent(clazz, MessageMeta::new);
        }
        return (MessageMeta<T>) messageMeta;
    }

    static <T> void forbidExtends(Class<T> clazz) {
        if (!Objects.equals(Object.class, clazz.getSuperclass())) {
            throw new IllegalArgumentException(String.format("forbid extends:class[%s] has superclass[%s]", clazz.getName(), clazz.getSuperclass().getName()));
        }
    }

    static <T> T read(CodedInputStream input, Class<T> clazz) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        MessageMeta<T> messageMeta = getMessageMeta(clazz);
        if (messageMeta.basicFieldTypeEnum != null) {
            if (input.isAtEnd()) {
                return (T) FieldMeta.DEFAULT_VALUE.get(clazz);
            }
            input.readTag();
            return (T) messageMeta.basicFieldTypeEnum.read(input);
        }
        FieldMeta[] fieldMetas = messageMeta.fieldMetas;
        T instance = clazz.getConstructor().newInstance();
        if (fieldMetas.length <= 0) {
            return instance;
        }
        int tag, nowFieldIndex = 0;
        while (true) {
            tag = input.readTag();
            if (tag == 0) {
                break;
            }
            nowFieldIndex = untilEqualTag(nowFieldIndex, tag, fieldMetas);
            if (nowFieldIndex >= fieldMetas.length) {
                break;
            }
            FieldMeta fieldMeta = fieldMetas[nowFieldIndex];
            fillInstance(input, fieldMeta, instance);
        }
        return instance;
    }

    private static <T> void fillInstance(CodedInputStream input, FieldMeta fieldMeta, T instance) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {
        if (fieldMeta.fieldTypeEnum == FieldTypeEnum.Object) {
            fieldMeta.field.set(instance, readObject(input, fieldMeta.field.getType()));
            return;
        }
        if (fieldMeta.fieldTypeEnum == FieldTypeEnum.Collection) {
            Collection<Object> collection = (Collection<Object>) fieldMeta.field.get(instance);
            if (collection == null) {
                collection = (Collection<Object>) fieldMeta.field.getType().getConstructor().newInstance();
                fieldMeta.field.set(instance, collection);
            }
            FieldTypeEnum genericTypeEnum = FieldMeta.BASE_CLASS_MAPPING_FIELD_TYPE_ENUM.getOrDefault(fieldMeta.genericClass, FieldTypeEnum.Object);
            if (genericTypeEnum == FieldTypeEnum.String) {
                collection.add(genericTypeEnum.read(input));
                return;
            }
            if (genericTypeEnum == FieldTypeEnum.Object) {
                collection.add(readObject(input, fieldMeta.genericClass));
                return;
            }
            int length = input.readRawVarint32();
            int limit = input.pushLimit(length);
            while (input.getBytesUntilLimit() > 0) {
                collection.add(genericTypeEnum.read(input));
            }
            input.popLimit(limit);
            return;
        }
        fieldMeta.field.set(instance, fieldMeta.fieldTypeEnum.read(input));
    }

    private static <T> T readObject(CodedInputStream input, Class<T> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        int length = input.readRawVarint32();
        final int oldLimit = input.pushLimit(length);
        T read = read(input, clazz);
        input.popLimit(oldLimit);
        return read;
    }

    private static int untilEqualTag(int nowFieldIndex, int tag, FieldMeta[] fieldMetas) {
        FieldMeta fieldMeta = fieldMetas[nowFieldIndex];
        while (tag != fieldMeta.tag) {
            nowFieldIndex++;
            if (nowFieldIndex >= fieldMetas.length) {
                return nowFieldIndex;
            }
            fieldMeta = fieldMetas[nowFieldIndex];
        }
        return nowFieldIndex;
    }

    void write(CodedOutputStream output, Object message, Context<?> context) throws IOException {
        if (this.basicFieldTypeEnum != null) {
            if (message != null && !IGNORE_VALUE.contains(message)) {
                this.basicFieldTypeEnum.write(output, message, new FieldMeta(this.basicFieldTypeEnum.wireType), context);
            }
            return;
        }
        for (FieldMeta fieldMeta : fieldMetas) {
            Field field = fieldMeta.field;
            try {
                Object value = field.get(message);
                if (value == null || IGNORE_VALUE.contains(value)) {
                    continue;
                }
                fieldMeta.fieldTypeEnum.write(output, value, fieldMeta, context);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);//should not
            } catch (NullPointerException e) {//ignore
            }
        }
    }

    int getSerializedSize(Object message, Context<?> context) {
        int size = 0;
        for (FieldMeta fieldMeta : fieldMetas) {
            Field field = fieldMeta.field;
            try {
                Object value = field.get(message);
                if (value == null || IGNORE_VALUE.contains(value)) {
                    continue;
                }
                if (fieldMeta.fieldTypeEnum == FieldTypeEnum.Collection) {
                    Iterator<?> iterator = ((java.util.Collection<?>) value).iterator();
                    while (iterator.hasNext()) {
                        java.lang.Object next = iterator.next();
                        size += context.cacheSerializedSize.computeIfAbsent(next, key -> fieldMeta.genericFieldTypeEnum.computeSize(next, fieldMeta, context));
                    }
                    continue;
                }
                size += context.cacheSerializedSize.computeIfAbsent(value, key -> fieldMeta.fieldTypeEnum.computeSize(value, fieldMeta, context));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);//should not
            } catch (NullPointerException e) {//ignore
            }
        }
        return size;
    }
}
