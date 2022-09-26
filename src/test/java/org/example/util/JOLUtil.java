package org.example.util;

import org.openjdk.jol.info.ClassLayout;

public class JOLUtil {
    public static void printJavaObjectLayout(Object lock) {
        System.out.println(Thread.currentThread().getName() + "\n" + ClassLayout.parseInstance(lock).toPrintable());
    }
}
