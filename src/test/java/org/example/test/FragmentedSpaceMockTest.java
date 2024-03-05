package org.example.test;

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
public class FragmentedSpaceMockTest {
    @Resource
    private MockMvc mvc;

    @Test
    void testMultiInsert() throws Exception {
        // 业务批量插入接口
        String businessMultiInsertUrl = "/businessMultiInsert";
        // 请求业务接口，批量插入记录
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessMultiInsertUrl)
                .param("startId", String.valueOf(500001))
                .param("number", String.valueOf(100000))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    void testLoopInsert() throws Exception {
        // 业务插入接口
        String businessInsertUrl = "/businessInsert";
        for (int i = 600001; i <= 610000; i++) {
            // 请求业务接口，插入一条记录
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessInsertUrl)
                    .param("id", String.valueOf(i))
                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            int status = response.getStatus();
            Assertions.assertEquals(200, status);
        }
    }

    @Test
    void testMultiDelete() throws Exception {
        // 业务批量插入接口
        String businessMultiDeleteUrl = "/businessMultiDelete";
        // 请求业务接口，批量插入记录
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessMultiDeleteUrl)
                .param("startId", String.valueOf(500001))
                .param("number", String.valueOf(100000))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    void testLoopDelete() throws Exception {
        // 业务删除接口
        String businessDeleteUrl = "/businessDelete";
        for (int i = 1; i <= 610000; i++) {
            if (i % 2 == 0) {
                continue;
            }
            // 请求业务接口，删除一条记录
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessDeleteUrl)
                    .param("id", String.valueOf(i))
                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            int status = response.getStatus();
            Assertions.assertEquals(200, status);
        }
    }
}
