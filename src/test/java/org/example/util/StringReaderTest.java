package org.example.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringReaderTest {

    @BeforeAll
    static void setup() {
        // 这里可以进行一些全局的设置，例如初始化工作
    }

    @Test
    void testReadFromResourcesWithValidPath() {
        String expectedContent = "This is a test content.";
        // 用一个实际存在的资源文件路径来测试
        String path = "test.txt";
        // 将预期结果写入到资源文件test.txt中，确保测试环境一致

        String actualContent = StringReader.readFromResources(path);
        assertEquals(expectedContent, actualContent, "The content read from resources should match the expected content.");
    }

    @Test
    void testReadFromResourcesWithInvalidPath() {
        String path = "non_existent.txt";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            StringReader.readFromResources(path);
        });
        assertTrue(exception.getMessage().contains("无法从资源路径"), "The exception message should indicate the failure to read the resource path.");
    }

    @Test
    void testReadFromResourcesWithBlankPath() {
        String path = " ";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            StringReader.readFromResources(path);
        });
        assertEquals("path is blank", exception.getMessage(), "The exception message should indicate that the path is blank.");
    }

}
