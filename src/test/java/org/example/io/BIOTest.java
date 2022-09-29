package org.example.io;

import org.example.util.ThreadUtil;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

public class BIOTest {
    @Test
    public void testServerSocket() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8088)) {
            ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor("bio-test-");
            for (; ; ) {
                Socket socket = serverSocket.accept();
                threadPoolExecutor.execute(() -> {
                    try (Socket autoCloseSocket = socket;
                         Reader reader = new InputStreamReader(autoCloseSocket.getInputStream())) {
                        StringBuilder sb = new StringBuilder();
                        char[] chars = new char[1];
                        while (reader.read(chars) != -1) {
                            sb.append(chars);
                        }
                        System.out.println(Thread.currentThread().getName() + "-" + sb);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    @Test
    public void testSocket() throws IOException {
        for (int i = 0; i < 10; i++) {
            try (Socket socket = new Socket("localhost", 8088);
                 Writer writer = new OutputStreamWriter(socket.getOutputStream())) {
                writer.write("你好，我是" + i);
            }
        }
    }
}
