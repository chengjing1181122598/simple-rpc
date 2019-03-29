package com.netease.message;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class FieldMeta {

    static final Map<Class<?>, Object> DEFAULT_VALUE = new HashMap<>() {
        {
            put(Byte.TYPE, (byte) 0);
            put(Short.TYPE, (short) 0);
            put(Integer.TYPE, 0);
            put(Long.TYPE, (long) 0);
            put(Boolean.TYPE, false);
            put(Float.TYPE, (float) 0.0);
            put(Double.TYPE, 0.0);
            put(String.class, "");
        }
    };

    static final Map<Class<?>, FieldTypeEnum> BASE_CLASS_MAPPING_FIELD_TYPE_ENUM = new HashMap<>() {
        {
            put(Byte.TYPE, FieldTypeEnum.Byte);
            put(Byte.class, FieldTypeEnum.Byte);
            put(Short.TYPE, FieldTypeEnum.Short);
            put(Short.class, FieldTypeEnum.Short);
            put(Integer.TYPE, FieldTypeEnum.Int);
            put(Integer.class, FieldTypeEnum.Int);
            put(Long.TYPE, FieldTypeEnum.Long);
            put(Long.class, FieldTypeEnum.Long);
            put(Boolean.TYPE, FieldTypeEnum.Boolean);
            put(Boolean.class, FieldTypeEnum.Boolean);
            put(Float.TYPE, FieldTypeEnum.Float);
            put(Float.class, FieldTypeEnum.Float);
            put(Double.TYPE, FieldTypeEnum.Double);
            put(Double.class, FieldTypeEnum.Double);
            put(String.class, FieldTypeEnum.String);
        }
    };
    private static final int TAG_TYPE_BITS = 3;

    final Field field;
    final int number;
    final int tag;
    final FieldTypeEnum fieldTypeEnum;
    final Class<?> genericClass;
    final FieldTypeEnum genericFieldTypeEnum;

    FieldMeta(int wireType) {
        field = null;
        number = 1;
        tag = makeTag(number, wireType);
        fieldTypeEnum = null;
        genericClass = null;
        genericFieldTypeEnum = null;
    }

    FieldMeta(Field field, int number) {
        FieldTypeEnum fieldTypeEnum = detectFieldTypeEnum(field.getType());
        this.field = field;
        this.number = number;
        this.tag = makeTag(number, fieldTypeEnum.wireType);
        this.fieldTypeEnum = fieldTypeEnum;
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            if (parameterizedType.getActualTypeArguments().length > 0) {
                this.genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                this.genericFieldTypeEnum = detectFieldTypeEnum(genericClass);
                if (this.genericFieldTypeEnum == FieldTypeEnum.Collection) {
                    throw new IllegalArgumentException(String.format("forbid embedded repeated,field:[%s],generic class[%s]", field, genericClass.getName()));
                }
                if (this.genericFieldTypeEnum == FieldTypeEnum.Object) {
                    MessageMeta.forbidExtends(this.genericClass);
                }
                return;
            }
        }
        this.genericClass = Object.class;
        this.genericFieldTypeEnum = FieldTypeEnum.Object;
    }

    //copy protobuf
    public static int makeTag(final int fieldNumber, final int wireType) {
        return (fieldNumber << TAG_TYPE_BITS) | wireType;
    }

    public static FieldTypeEnum detectFieldTypeEnum(Class<?> fieldType) {
        FieldTypeEnum fieldTypeEnum = BASE_CLASS_MAPPING_FIELD_TYPE_ENUM.get(fieldType);
        if (fieldTypeEnum != null) {
            return fieldTypeEnum;
        }
        if (Collection.class.isAssignableFrom(fieldType)) {
            return FieldTypeEnum.Collection;
        }
        return FieldTypeEnum.Object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldMeta)) return false;
        FieldMeta fieldMeta = (FieldMeta) o;
        return number == fieldMeta.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}

