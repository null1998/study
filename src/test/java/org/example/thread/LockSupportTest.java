package org.example.thread;

import org.example.util.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {
    /**
     * 测试通过unpark方法唤醒park线程
     */
    @Test
    public void testStopParkByUnPark() {
        Thread threadA = new Thread(LockSupport::park);
        threadA.start();
        ThreadUtil.sleep(100);
        assert threadA.getState() == Thread.State.WAITING;
        LockSupport.unpark(threadA);
        ThreadUtil.sleep(100);
        assert threadA.getState() == Thread.State.TERMINATED;
    }

    /**
     * 测试通过interrupt方法唤醒park线程
     */
    @Test
    public void testStopParkByInterrupt() {
        Thread threadA = new Thread(LockSupport::park);
        threadA.start();
        ThreadUtil.sleep(100);
        assert threadA.getState() == Thread.State.WAITING;
        threadA.interrupt();
        ThreadUtil.sleep(100);
        assert threadA.getState() == Thread.State.TERMINATED;
    }
}
