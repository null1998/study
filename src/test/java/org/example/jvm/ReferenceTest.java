package org.example.jvm;

import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 被不同类型引用描述的对象回收测试，需要增加gc日志分析参数-XX:+PrintGCDetails
 */
public class ReferenceTest {
    /**
     * -Xmx400m，400MB无法同时存放200MB和100MB的被软引用描述对象，因为需要预留一部分空间
     * 在为100MB的被软引用描述的对象分配内存时，200MB的被软引用描述的对象将被回收
     */
    @Test
    public void testSoftReference() {
        SoftReference<byte[]> _200MB = new SoftReference<>(new byte[200 * 1024 * 1024]);
        System.gc();
        SoftReference<byte[]> _100MB = new SoftReference<>(new byte[100 * 1024 * 1024]);
        System.gc();
    }

    /**
     * -Xmx400m，被弱引用描述的对象在gc后一定会被回收
     */
    @Test
    public void testWeakReference() {
        WeakReference<byte[]> _100MB = new WeakReference<>(new byte[100 * 1024 * 1024]);
        System.gc();
    }

    /**
     * -Xmx400m，虚引用不能用来获取对象实例，被虚引用描述的对象生命周期完全不受虚引用影响，随时可能被回收，也可能不被回收
     */
    @Test
    public void testPhantomReference() {
        PhantomReference<byte[]> _100MB = new PhantomReference<>(new byte[100 * 1024 * 1024], new ReferenceQueue<>());
        assert _100MB.get() == null;
        System.gc();
    }
}
