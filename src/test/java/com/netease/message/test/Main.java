package com.netease.message.test;

import com.google.protobuf.CodedOutputStream;
import com.netease.message.ProtoBufUtil;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    @ToString
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class User {
        public int uid;
        public String name;
        public Account account;
        public List<Integer> score = new ArrayList<>();
        public List<String> special = new ArrayList<>();
        public List<Address> address = new ArrayList<>();

        public User(int uid, String name, Account account) {
            this.uid = uid;
            this.name = name;
            this.account = account;
        }
    }

    @ToString
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class Account {
        public long registerTime;
        public String headUrl;
        public Address address;
        public List<Address> addresses = new ArrayList<>();

        public Account(int registerTime, String headUrl, Address address) {
            this.registerTime = registerTime;
            this.headUrl = headUrl;
            this.address = address;
        }
    }

    @ToString
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class Address {
        public String province;
        public String city;
        public boolean isDefault;

        public Address(String province, String city, boolean isDefault) {
            this.province = province;
            this.city = city;
            this.isDefault = isDefault;
        }
    }

    private static byte[] official() {
        TestProtoMessage.Address address1 = TestProtoMessage.Address.newBuilder().setProvince("广东省").setIsDefault(false).build();
        TestProtoMessage.Address address2 = TestProtoMessage.Address.newBuilder().setProvince("").setCity("茂名市").setIsDefault(true).build();
        TestProtoMessage.User raw = TestProtoMessage.User.newBuilder()
                .setUid(100)
                .setName("程景")
                .setAccount(
                        TestProtoMessage.Account.newBuilder()
                                .setRegisterTime(0)
                                .setHeadUrl("")
                                .setAddress(TestProtoMessage.Address.newBuilder().setIsDefault(false).setCity("广州").build())
                                .addAddresses(address1)
                                .addAddresses(address2)
                                .build()
                )
                .addAddress(address1)
                .addAddress(address2)
                .addScore(1).addScore(10)
                .addSpecial("哈哈").addSpecial("呵呵")
                .build();
        byte[] bytes = raw.toByteArray();
        return bytes;
    }

    private static byte[] my() {
        Address address1 = new Address("广东省", null, false);
        Address address2 = new Address("", "茂名市", true);
        User user = new User(100, "程景", new Account(0, "", new Address(null, "广州", false)));
        user.account.addresses.add(address1);
        user.account.addresses.add(address2);
        user.address.add(address1);
        user.address.add(address2);
        user.score.add(1);
        user.score.add(10);
        user.special.add("哈哈");
        user.special.add("呵呵");
        byte[] bytes = ProtoBufUtil.toByteArray(user);
        return bytes;
    }

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        System.out.println(Arrays.equals(official(), my()));
        Address address1 = new Address("广东省", null, false);
        Address address2 = new Address("", "茂名市", true);
        User user = new User(100, "程景", new Account(0, "", new Address(null, "广州", false)));
        user.account.addresses.add(address1);
        user.account.addresses.add(address2);
        user.address.add(address1);
        user.address.add(address2);
        user.score.add(1);
        user.score.add(10);
        user.special.add("哈哈");
        user.special.add("呵呵");
        CodedOutputStream codedOutputStream = CodedOutputStream.newInstance(new byte[1 << 10]);
        ProtoBufUtil.writeTo(codedOutputStream, user);
        System.out.println(user);
        System.out.println(codedOutputStream.getTotalBytesWritten());
    }

}
