package org.example.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NIOUtil {
    public static void writeByFileChannel(String destPath, String content) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(destPath)) {
            try (FileChannel fileChannel = fileOutputStream.getChannel()) {
                fileChannel.write(ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8)));
            }
        }
    }

    public static void copyByFileChannel(String srcPath, String destPath) {
        try (FileInputStream fileInputStream = new FileInputStream(srcPath);
             FileOutputStream fileOutputStream = new FileOutputStream(destPath)) {
            FileChannel fileInputStreamChannel = fileInputStream.getChannel();
            FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
            fileInputStreamChannel.transferTo(0, fileInputStreamChannel.size(), fileOutputStreamChannel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
