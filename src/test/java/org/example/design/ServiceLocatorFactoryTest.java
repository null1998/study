package org.example.design;

import org.example.common.ParserType;
import org.example.service.parser.ParserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author huang
 */
@SpringBootTest
public class ServiceLocatorFactoryTest {
    @Resource
    private ParserFactory parserFactory;

    /**
     * 使用ServiceLocatorFactoryBean实现根据传入的类型，调用接口的不同实现类
     */
    @Test
    public void testServiceLocatorFactory() {
        Assertions.assertEquals("json", parserFactory.getParser(ParserType.JSON).parse("data"));
        Assertions.assertEquals("xml", parserFactory.getParser(ParserType.XML).parse("data"));
    }
}
