package org.example.test;

import org.example.util.ThreadUtil;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * @author huang
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BusinessMockTest {
    @Resource
    private MockMvc mvc;

    @Test
    void testMockBusiness() throws Exception {
        // 业务接口
        String businessUrl = "/business";
        // 请求业务接口
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessUrl)
                .param("param", "abc")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assertions.assertEquals(200, status);
    }

    @Test
    void testMockChangeTableName() throws Exception {
        // 切换表名接口
        String changeTableNameUrl = "/changeTableName";
        mvc.perform(MockMvcRequestBuilders.get(changeTableNameUrl)
                .param("busType", "type_A")
                .param("tableName", "region_3")
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    void testMockBusinessInsert() throws Exception {
        new Thread(this::changeTableName).start();
        for (int i = 1; i < 1000; i++) {
            businessInsert(i);
            // 间隔0.1秒
            ThreadUtil.sleep(100);
        }
    }

    @Test
    void testMockBusinessQueryAndDelete() throws Exception {
        new Thread(this::changeTableName).start();
        for (int i = 1; i < 1000; i++) {
            businessQueryAndDelete(i);
            // 间隔0.1秒
            ThreadUtil.sleep(100);
        }
    }

    @Test
    void testMockBusinessQueryAllAndDelete() throws Exception {
        new Thread(this::changeTableName).start();
        for (int i = 1; i < 1000; i++) {
            businessQueryAllAndDelete();
            // 间隔0.1秒
            ThreadUtil.sleep(100);
        }
    }

    @Test
    void testMockBusinessAsync() throws Exception {
        new Thread(this::changeTableName).start();
        for (int i = 1; i < 1000; i++) {
            businessAsync(i);
            // 间隔0.1秒
            ThreadUtil.sleep(100);
        }
    }

    @Test
    void testMockBusinessMulti() throws Exception {
        new Thread(this::changeTableName).start();
        Random random = new Random();
        for (int i = 1; i < 1000; i++) {
            int r = random.nextInt(5);
            if (r == 0) {
                businessQueryAllAndDelete();
            } else {
                businessInsert(i);
            }
            // 间隔0.1秒
            ThreadUtil.sleep(100);
        }
    }

    private void changeTableName() {
        LocalDateTime time = LocalDateTime.now();
        // 切换表名接口
        String changeTableNameUrl = "/changeTableName";
        // 第一次切换表名的时间
        int t1 = new Random().nextInt(5);
        // 第二次切换表名的时间
        int t2 = t1 + new Random().nextInt(5);
        for (; ; ) {
            try {
                // 第一次切换表名
                if (ChronoUnit.SECONDS.between(time, LocalDateTime.now()) > t1) {
                    mvc.perform(MockMvcRequestBuilders.get(changeTableNameUrl)
                            .param("busType", "type_A")
                            .param("tableName", "region_1")
                            .accept(MediaType.APPLICATION_JSON));
                }
                // 第二次切换表名
                if (ChronoUnit.SECONDS.between(time, LocalDateTime.now()) > t2) {
                    mvc.perform(MockMvcRequestBuilders.get(changeTableNameUrl)
                            .param("busType", "type_A")
                            .param("tableName", "region_2")
                            .accept(MediaType.APPLICATION_JSON));
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void businessInsert(int i) throws Exception {
        // 业务插入接口
        String businessInsertUrl = "/businessInsert";
        // 请求业务接口，插入一条记录
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessInsertUrl)
                .param("id", String.valueOf(i))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assertions.assertEquals(200, status);
    }

    private void businessQueryAndDelete(int i) throws Exception {
        // 查询并删除业务接口
        String businessQueryAndDeleteUrl = "/businessQueryAndDelete";
        // 请求业务接口，查询并删除一条记录
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessQueryAndDeleteUrl)
                .param("id", String.valueOf(i))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assertions.assertEquals(200, status);
    }

    private void businessQueryAllAndDelete() throws Exception {
        // 查询全部并删除业务接口
        String businessQueryAllAndDeleteUrl = "/businessQueryAllAndDelete";
        // 请求业务接口，查询全部记录并删除
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessQueryAllAndDeleteUrl)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assertions.assertEquals(200, status);
    }

    private void businessAsync(int i) throws Exception {
        // 异步业务接口
        String businessAsyncUrl = "/businessAsync";
        // 请求异步业务接口，插入一条记录并异步删除
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(businessAsyncUrl)
                .param("id", String.valueOf(i))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assertions.assertEquals(200, status);
    }
}
