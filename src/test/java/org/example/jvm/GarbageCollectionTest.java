package org.example.jvm;

import org.junit.Test;

/**
 * 垃圾回收测试，需要增加gc日志分析参数-XX:+PrintGCDetails
 */
public class GarbageCollectionTest {
    /**
     * jvm垃圾回收使用的是可达性分析技术，引用计数不能保证对象不会被回收
     */
    @Test
    public void testReferenceCount() {
        class ReferenceCountGC {
            private static final int _1MB = 1024 * 1024;
            private final byte[] payload = new byte[200 * _1MB];
            public ReferenceCountGC referenceCountGC;
        }
        ReferenceCountGC a = new ReferenceCountGC();
        ReferenceCountGC b = new ReferenceCountGC();
        a.referenceCountGC = b;
        b.referenceCountGC = a;
        a = null;
        b = null;
        System.gc();
    }

    /**
     * 测试可达性分析的几种情况，虚拟机栈中引用的对象不能回收
     */
    @Test
    public void testVmStackGC() {
        byte[] bytes = new byte[200 * 1024 * 1024];
        System.gc();
        bytes = null;
        System.gc();
    }

    /**
     * 类静态变量引用的对象不能回收
     */
    @Test
    public void testClassStaticFieldGC() {
        HaveStaticBytes haveStaticBytes = new HaveStaticBytes();
        haveStaticBytes = null;
        System.gc();
    }

    /**
     * 类静态常量引用的对象不能回收
     */
    @Test
    public void testClassStaticFinalFieldGC() {
        HaveStaticFinalBytes haveStaticFinalBytes = new HaveStaticFinalBytes();
        haveStaticFinalBytes = null;
        System.gc();
    }

    /**
     * -Xmx300m，测试老年代分配担保
     * eden 80MB， from 10MB， to 10MB， old 200MB
     * 50MB对象分配在eden，gc后复制到to失败，触发老年代分配担保，分配到old
     */
    @Test
    public void testOldGenHandlePromotionGC() {
        class PayLoad {
            private byte[] bytes = new byte[50 * 1024 * 1024];
        }
        PayLoad _50MB = new PayLoad();
        System.gc();
    }
}

class HaveStaticBytes {
    private static byte[] bytes = new byte[200 * 1024 * 1024];
}

class HaveStaticFinalBytes {
    private static final byte[] bytes = new byte[200 * 1024 * 1024];
}
