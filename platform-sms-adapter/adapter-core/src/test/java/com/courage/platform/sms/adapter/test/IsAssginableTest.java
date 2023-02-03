package com.courage.platform.sms.adapter.test;

/**
 * Created by zhangyong on 2023/2/3.
 */
public class IsAssginableTest {

    public static void main(String[] args) throws Exception {
        ClassLoader cl1 = new SimpleClassLoader();
        ClassLoader cl2 = new SimpleClassLoader();

        Class<?> clazz1 = cl1.loadClass("com.courage.platform.sms.adapter.test.JustAClass");
        Class<?> clazz2 = cl2.loadClass("com.courage.platform.sms.adapter.test.JustAClass");

        System.out.println("cl1 is " + clazz1.getClassLoader().toString());
        System.out.println("cl2 is " + clazz2.getClassLoader().toString());
        System.out.println(
                "clazz1 " +
                        (clazz1.isAssignableFrom(clazz2) ? "is" : "is NOT") +
                        " assignable from clazz2");
        System.out.println(
                "clazz2 " +
                        (clazz2.isAssignableFrom(clazz1) ? "is" : "is NOT") +
                        " assignable from clazz1");

        Object i1 = clazz1.newInstance();
        Object i2 = clazz2.newInstance();

        System.out.println(
                "i1 " +
                        (clazz1.isInstance(i1) ? "is" : "is NOT") +
                        " instance of clazz1");
        System.out.println(
                "i2 " +
                        (clazz2.isInstance(i2) ? "is" : "is NOT") +
                        " instance of clazz2");
        System.out.println(
                "i1 " +
                        (clazz2.isInstance(i1) ? "is" : "is NOT") +
                        " instance of clazz2");
        System.out.println(
                "i2 " +
                        (clazz1.isInstance(i2) ? "is" : "is NOT") +
                        " instance of clazz1");

        Class type = JustAInterface.class;
        System.out.println(type.getClassLoader());
        System.out.println(clazz1.getClassLoader());
        System.out.println(type.isAssignableFrom(clazz1));
        System.out.println(type.isAssignableFrom(clazz2));
        System.out.println(clazz2.getClassLoader());

    }

}
