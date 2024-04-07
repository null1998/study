package org.example.spring;

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

/**
 * @author huang
 */
@SpringBootTest
@AutoConfigureMockMvc
public class JsonDeserializeTest {
    @Resource
    private MockMvc mvc;
    @Test
    public void test() throws Exception {
        // 业务接口
        String uri = "/jsonDeserialize";
        // 请求业务接口
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startDate\": \"2024/04/07\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assertions.assertEquals(200, status);

    }
}
