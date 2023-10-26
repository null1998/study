package org.example.test;


import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author huang
 */
public class UtilTest {
    @Test
    void testAnnotationUtil() throws NoSuchMethodException {
        Test test = AnnotationUtils.findAnnotation(UtilTest.class.getDeclaredMethod("testAnnotationUtil"), Test.class);
        System.out.println(test);
    }
}
