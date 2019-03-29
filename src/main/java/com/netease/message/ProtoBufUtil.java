package com.netease.message;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public abstract class ProtoBufUtil {

    public static <T> void writeTo(CodedOutputStream output, T message) throws IOException {
        Objects.requireNonNull(output);
        Objects.requireNonNull(message);
        Context<T> context = new Context<>(message);
        MessageMeta.getMessageMeta(message).write(output, message, context);
    }

    public static <T> T read(CodedInputStream input, Class<T> clazz) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Objects.requireNonNull(input);
        Objects.requireNonNull(clazz);
        return MessageMeta.read(input, clazz);
    }

    public static <T> byte[] toByteArray(T message) {
        Objects.requireNonNull(message);
        Context<T> context = new Context<>(message);
        int serializedSize = MessageMeta.getMessageMeta(message).getSerializedSize(message, context);
        final byte[] result = new byte[serializedSize];
        final CodedOutputStream output = CodedOutputStream.newInstance(result);
        try {
            writeTo(output, message);
        } catch (IOException e) {
            throw new RuntimeException(e);//should not
        }
        output.checkNoSpaceLeft();
        return result;
    }
}