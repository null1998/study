package org.example.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author huang
 */
public class StringReader {
    // 使用Logger记录错误信息
    final static Logger logger = LoggerFactory.getLogger(StringReader.class);

    /**
     * 从资源文件中读取内容。
     *
     * @param path 资源文件的路径，不能为空或只包含空白字符。
     * @return 从指定资源路径读取到的字符串内容。
     * @throws RuntimeException 如果路径为空或只包含空白字符，或者读取过程中发生IO异常。
     */
    public static String readFromResources(String path) {
        // 检查路径是否为空或只包含空白字符
        if (StringUtils.isBlank(path)) {
            logger.error("提供的路径为空或仅包含空白字符: {}", path);
            throw new RuntimeException("path is blank");
        }

        try {
            // 从资源路径读取内容，并以UTF-8编码转换为字符串
            InputStream inputStream = new ClassPathResource(path).getInputStream();
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // 记录IO异常的详细信息
            logger.error("读取资源文件时发生IO异常: {}", path, e);
            // 将IO异常转换为运行时异常抛出
            throw new RuntimeException("无法从资源路径 '" + path + "' 读取内容。", e);
        }
    }

    /**
     * 读取指定路径的文件内容。
     *
     * @param path 文件路径
     * @return 文件内容
     */
    public static String readFromPath(String path) {
        // 安全性检查：防止路径遍历
        if (path == null || path.contains("..")) {
            throw new IllegalArgumentException("非法路径输入，禁止使用相对路径跨越");
        }

        File paramFile = new File(path);

        // 检查文件是否存在且可读
        if (!paramFile.exists() || !paramFile.canRead()) {
            throw new IllegalArgumentException(paramFile.getAbsolutePath() + " 文件不存在或不可读");
        }

        try {
            return FileUtils.readFileToString(paramFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // 详细说明IO异常的原因
            throw new RuntimeException("读取文件 " + path + " 时发生错误", e);
        }
    }
}
