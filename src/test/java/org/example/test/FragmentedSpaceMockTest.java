package org.example.test;

import org.example.dao.FragmentedSpaceMapper;
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
import java.util.Random;

/**
 * @author huang
 */
@SpringBootTest
@AutoConfigureMockMvc
public class FragmentedSpaceMockTest {
    @Resource
    private MockMvc mvc;

    @Resource
    private FragmentedSpaceMapper fragmentedSpaceMapper;

    @Test
    void testMultiInsert() throws Exception {
        // 业务批量插入接口
        String businessMultiInsertUrl = "/businessMultiInsert";
        // 请求业务接口，批量插入记录
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessMultiInsertUrl)
                .param("startId", String.valueOf(1))
                .param("number", String.valueOf(5000000))
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
        for (int i = 2000000; i <= 2000001; i++) {
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
                .param("startId", String.valueOf(2000001))
                .param("number", String.valueOf(10))
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

    @Test
    void testLoopIncreaseNum() throws Exception {
        // 业务num增长接口
        String businessIncreaseNumUrl = "/businessIncreaseNum";
        Random random = new Random();
        for (int i = 1; i <= 1000; i++) {
            int randomId = random.nextInt(1000000) + 1;
            // 请求业务接口，num+1
            MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessIncreaseNumUrl)
                    .param("id", String.valueOf(randomId))
                    .accept(MediaType.APPLICATION_JSON))
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            int status = response.getStatus();
            Assertions.assertEquals(200, status);
        }
    }

    @Test
    void testLoopIncreaseNumPlural() throws Exception {
        // 业务num增长接口
        String businessIncreaseNumUrl = "/businessIncreaseNum";
        Random random = new Random();
        int i = 1;
        while (i <= 1000) {
            int randomId = random.nextInt(5000000) + 1;
            if (randomId % 2 == 0) {
                // 请求业务接口，num+1
                MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessIncreaseNumUrl)
                        .param("id", String.valueOf(randomId))
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();
                MockHttpServletResponse response = mvcResult.getResponse();
                int status = response.getStatus();
                Assertions.assertEquals(200, status);
                i++;
            }
        }
    }

    @Test
    void testLoopQueryPlural() throws Exception {
        // 业务查询接口
        String businessQueryUrl = "/businessQuery";
        Random random = new Random();
        int i = 1;
        while (i <= 1000) {
            int randomId = random.nextInt(5000000) + 1;
            if (randomId % 2 == 0) {
                // 请求业务查询接口
                MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessQueryUrl)
                        .param("id", String.valueOf(randomId))
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();
                MockHttpServletResponse response = mvcResult.getResponse();
                int status = response.getStatus();
                Assertions.assertEquals(200, status);
                i++;
            }
        }
    }

    @Test
    void testAlterTableEngine() {
        fragmentedSpaceMapper.alterTableEngine("REGION");
    }

    @Test
    void testShrink() {
        fragmentedSpaceMapper.enableRowMovement("REGION");
        fragmentedSpaceMapper.shrinkSpaceCascade("REGION");
        fragmentedSpaceMapper.disableRowMovement("REGION");
    }

    @Test
    void testEnableRowMovement() {
        fragmentedSpaceMapper.enableRowMovement("REGION");
    }

    @Test
    void testShrinkSpaceCascade() {
        fragmentedSpaceMapper.shrinkSpaceCascade("REGION");
    }

    @Test
    void testDisableRowMovement() {
        fragmentedSpaceMapper.disableRowMovement("REGION");
    }

    @Test
    void testVacuumFull() {
        fragmentedSpaceMapper.vacuumFull("REGION");
    }
}
