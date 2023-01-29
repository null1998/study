package org.example.test;

import com.alibaba.fastjson.JSON;
import org.example.entity.Region;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * MockMvc使用
 *
 * @author huang
 */
@SpringBootTest
@AutoConfigureMockMvc
class MockMvcTest {
    @Resource
    private MockMvc mvc;

    /**
     * 测试模拟get
     *
     * @throws Exception 异常
     */
    @Test
    void testMockGet() throws Exception {
        String url = "/query/region";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)
                        .param("id","1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        String contentAsString = response.getContentAsString(Charset.defaultCharset());

        Assertions.assertEquals(200, status);
        System.out.println(contentAsString);
    }


    /**
     * 测试模拟post
     *
     * @throws Exception 异常
     */
    @Test
    void testMockPost() throws Exception {
        String uri = "/add/region";
        Region region = new Region();
        region.setId(2);
        region.setName("北京");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(region))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        String contentAsString = response.getContentAsString(Charset.defaultCharset());

        Assertions.assertEquals(200, status);
        System.out.println(contentAsString);
    }
}
