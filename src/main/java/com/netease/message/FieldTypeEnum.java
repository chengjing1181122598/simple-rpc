package com.netease.message;

import com.google.protobuf.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

enum FieldTypeEnum {
    Byte(WireFormat.WIRETYPE_VARINT) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeUInt32NoTag(fieldMeta.tag);
            writeNoTag(output, value, fieldMeta, context);
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeInt32NoTag((Byte) value);
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeUInt32SizeNoTag(fieldMeta.tag) + computeSizeNoTag(value, fieldMeta, context);
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeInt32SizeNoTag((Byte) value);
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            return (byte) (input.readInt32());
        }
    },
    Short(WireFormat.WIRETYPE_VARINT) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeUInt32NoTag(fieldMeta.tag);
            writeNoTag(output, value, fieldMeta, context);
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeInt32NoTag((Short) value);
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeUInt32SizeNoTag(fieldMeta.tag) + computeSizeNoTag(value, fieldMeta, context);
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeInt32SizeNoTag((Short) value);
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            return (short) (input.readInt32());
        }
    },
    Int(WireFormat.WIRETYPE_VARINT) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeUInt32NoTag(fieldMeta.tag);
            writeNoTag(output, value, fieldMeta, context);
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeInt32NoTag((Integer) value);
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeUInt32SizeNoTag(fieldMeta.tag) + computeSizeNoTag(value, fieldMeta, context);
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeInt32SizeNoTag((Integer) value);
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            return input.readInt32();
        }
    },
    Long(WireFormat.WIRETYPE_VARINT) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeUInt32NoTag(fieldMeta.tag);
            writeNoTag(output, value, fieldMeta, context);
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeInt64NoTag((Long) value);
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeUInt32SizeNoTag(fieldMeta.tag) + computeSizeNoTag(value, fieldMeta, context);
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeInt64SizeNoTag((Long) value);
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            return input.readInt64();
        }
    },
    Boolean(WireFormat.WIRETYPE_VARINT) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeUInt32NoTag(fieldMeta.tag);
            writeNoTag(output, value, fieldMeta, context);
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.write((byte) ((Boolean) value ? 1 : 0));
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeUInt32SizeNoTag(fieldMeta.tag) + computeSizeNoTag(value, fieldMeta, context);
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeBoolSizeNoTag((Boolean) value);
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            return input.readBool();
        }
    },
    Float(WireFormat.WIRETYPE_FIXED32) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeUInt32NoTag(fieldMeta.tag);
            writeNoTag(output, value, fieldMeta, context);
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeFixed32NoTag(java.lang.Float.floatToRawIntBits((Float) value));
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeUInt32SizeNoTag(fieldMeta.tag) + computeSizeNoTag(value, fieldMeta, context);
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeFloatSizeNoTag((Float) value);
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            return input.readFloat();
        }
    },
    Double(WireFormat.WIRETYPE_FIXED64) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeUInt32NoTag(fieldMeta.tag);
            writeNoTag(output, value, fieldMeta, context);
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeFixed64NoTag(java.lang.Double.doubleToRawLongBits((Double) value));
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeUInt32SizeNoTag(fieldMeta.tag) + computeSizeNoTag(value, fieldMeta, context);
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeDoubleSizeNoTag((Double) value);
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            return input.readDouble();
        }
    },
    String(WireFormat.WIRETYPE_LENGTH_DELIMITED) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeUInt32NoTag(fieldMeta.tag);
            writeNoTag(output, value, fieldMeta, context);
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            ByteString byteString = (value instanceof ByteString) ? (ByteString) value : ByteString.copyFromUtf8((String) value);
            output.writeUInt32NoTag(byteString.size());
            try {
                Method writeTo = ByteString.class.getDeclaredMethod("writeTo", ByteOutput.class);
                writeTo.setAccessible(true);
                writeTo.invoke(byteString, output);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            ByteString byteString = (value instanceof ByteString) ? (ByteString) value : ByteString.copyFromUtf8((String) value);
            return CodedOutputStream.computeUInt32SizeNoTag(fieldMeta.tag) + computeSizeNoTag(byteString, fieldMeta, context);
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            ByteString byteString = (value instanceof ByteString) ? (ByteString) value : ByteString.copyFromUtf8((String) value);
            return CodedOutputStream.computeBytesSizeNoTag(byteString);
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            return input.readStringRequireUtf8();
        }
    },
    Object(WireFormat.WIRETYPE_LENGTH_DELIMITED) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            output.writeUInt32NoTag(fieldMeta.tag);
            writeNoTag(output, value, fieldMeta, context);
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            MessageMeta<?> messageMeta = MessageMeta.getMessageMeta(value);
            output.writeUInt32NoTag(messageMeta.getSerializedSize(value, context));
            messageMeta.write(output, value, context);
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            return CodedOutputStream.computeUInt32SizeNoTag(fieldMeta.tag) + computeSizeNoTag(value, fieldMeta, context);
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            int serializedSize = MessageMeta.getMessageMeta(value).getSerializedSize(value, context);
            return CodedOutputStream.computeUInt32SizeNoTag(serializedSize) + serializedSize;
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            throw new UnsupportedOperationException();
        }
    },
    Collection(WireFormat.WIRETYPE_LENGTH_DELIMITED) {
        @Override
        void write(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            Iterator<?> iterator = ((java.util.Collection<?>) value).iterator();
            int size = 0;
            boolean isBasicExceptString = (fieldMeta.genericFieldTypeEnum != String && fieldMeta.genericFieldTypeEnum != Object);
            if (isBasicExceptString) {
                while (iterator.hasNext()) {
                    java.lang.Object next = iterator.next();
                    size += fieldMeta.genericFieldTypeEnum.computeSizeNoTag(next, fieldMeta, context);
                }
            }
            if (isBasicExceptString && size > 0) {
                output.writeUInt32NoTag(fieldMeta.tag);
                output.writeUInt32NoTag(size);
            }
            iterator = ((java.util.Collection<?>) value).iterator();
            while (iterator.hasNext()) {
                java.lang.Object next = iterator.next();
                if (isBasicExceptString) {
                    fieldMeta.genericFieldTypeEnum.writeNoTag(output, next, fieldMeta, context);
                } else {
                    fieldMeta.genericFieldTypeEnum.write(output, next, fieldMeta, context);
                }
            }
        }

        @Override
        void writeNoTag(CodedOutputStream output, java.lang.Object value, FieldMeta fieldMeta, Context<?> context) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        int computeSize(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            throw new UnsupportedOperationException();
        }

        @Override
        int computeSizeNoTag(java.lang.Object value, FieldMeta fieldMeta, Context<?> context) {
            throw new UnsupportedOperationException();
        }

        @Override
        java.lang.Object read(CodedInputStream input) throws IOException {
            throw new UnsupportedOperationException();
        }
    };

    final int wireType;

    FieldTypeEnum(int wireType) {
        this.wireType = wireType;
    }

    abstract void write(CodedOutputStream output, Object value, FieldMeta fieldMeta, Context<?> context) throws IOException;

    abstract void writeNoTag(CodedOutputStream output, Object value, FieldMeta fieldMeta, Context<?> context) throws IOException;

    abstract int computeSize(Object value, FieldMeta fieldMeta, Context<?> context);

    abstract int computeSizeNoTag(Object value, FieldMeta fieldMeta, Context<?> context);

    abstract Object read(CodedInputStream input) throws IOException;

}
