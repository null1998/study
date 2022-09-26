package org.example.io;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

public class NIOTest {
    public static final int _10MB = 10 * 1024 * 1024;
    public static final int _200MB = 200 * 1024 * 1024;
    public static final int _400MB = 400 * 1024 * 1024;

    public static final int _1000MB = 1000 * 1024 * 1024;

    public static void main(String[] args) throws IOException {

    }

    /**
     * 测试allocateDirect
     * 直接在物理机内存上分配空间，不占用jvm
     * 没有可访问的底层实现数组
     * -XX:+PrintGCDetails
     */
    @Test
    public void testBufferAllocateDirect() {
        // 分配在jvm外的物理机内存
        ByteBuffer byteBufferDirect = ByteBuffer.allocateDirect(_1000MB);
        // 没有可访问的底层实现数组
        assert !byteBufferDirect.hasArray();
        System.gc();

        // 分配在堆内老年代
        ByteBuffer byteBuffer = ByteBuffer.allocate(_1000MB);
        // 具有可访问的底层实现数组
        assert byteBuffer.hasArray();
        System.gc();
    }

    /**
     * 测试Buffer的clear
     */
    @Test
    public void testBufferClear() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0, 1, 2});
        // 没有将缓冲区内容清空，只是将Buffer的状态还原
        byteBuffer.clear();
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            assert i == byteBuffer.get(i);
        }
    }
}
