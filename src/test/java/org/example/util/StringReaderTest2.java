package org.example.util;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author huang
 */
public class StringReaderTest2 {
    private File testFile;
    private String testFilePath;

    @BeforeEach
    public void setUp(@TempDir Path tempDir) throws IOException {
        // 创建一个临时文件用于测试
        testFile = tempDir.resolve("testFile.txt").toFile();
        testFilePath = testFile.getAbsolutePath();
        assertTrue(testFile.createNewFile());

        // 写入一些内容到测试文件
        FileUtils.writeStringToFile(testFile, "This is a test file.", StandardCharsets.UTF_8);
    }

    @AfterEach
    public void tearDown() {
        // 删除测试文件
        testFile.delete();
    }

    @Test
    public void testReadFromExistingPath() {
        // 正常读取文件内容的测试
        String content = StringReader.readFromPath(testFilePath);
        assertEquals("This is a test file.", content);
    }

    @Test
    public void testReadFromNonExistingPath() {
        // 测试一个不存在的文件路径
        String nonExistingPath = testFilePath + "nonExisting";
        assertThrows(IllegalArgumentException.class, () -> {
            StringReader.readFromPath(nonExistingPath);
        });
    }

    @Test
    public void testReadFromPathWithDotDot() {
        // 测试包含".."的路径，应该抛出异常
        String invalidPath = testFilePath + "../invalid";
        assertThrows(IllegalArgumentException.class, () -> {
            StringReader.readFromPath(invalidPath);
        });
    }
}
