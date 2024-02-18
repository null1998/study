package org.example;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.example.config.DefaultAutoExpireCacheManager;
import org.example.dao.RegionMapper;
import org.example.dao.StudentDao;
import org.example.entity.Region;
import org.example.eventbus.MyEventBusListener;
import org.example.service.DataBaseService;
import org.example.service.parser.ParserFactory;
import org.example.util.TimeUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

@SpringBootApplication(exclude = {SpringBootConfiguration.class})
@EnableCaching
@RestController
public class Application {
    private final Semaphore semaphore = new Semaphore(1);
    @Resource
    private DataBaseService dataBaseService;
    @Resource
    private StudentDao studentDao;
    @Resource
    private RegionMapper regionMapper;

    @GetMapping("/test")
    public Region test() {
        return regionMapper.selectByPrimaryKey(1);
    }

    @GetMapping("/testList")
    public List<Region> testList() {
        return regionMapper.selectByIds(Lists.newArrayList(1,2,3));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();
        Random random = new Random();
        // 设定缓存在凌晨3-5点范围内的随机时间点过期
        long ttl = ChronoUnit.MILLIS.between(LocalDateTime.now(), TimeUtil.getNextAssignTime(3 + random.nextInt(2), random.nextInt(60), random.nextInt(60)));
        // TODO 设置分级缓存
        config.put("cache_l1", new CacheConfig(ttl, ttl));
        config.put("cache_l2", new CacheConfig(ttl, ttl));
        return new DefaultAutoExpireCacheManager(redissonClient, config);
    }

    @Bean
    PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    EventBus eventBus(List<MyEventBusListener> eventBusListenerList) {
        EventBus eventBus = new EventBus();
        for (MyEventBusListener eventBusListener : eventBusListenerList) {
            eventBus.register(eventBusListener);
        }
        return eventBus;
    }

    @Bean("parserFactory")
    ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean serviceLocatorFactoryBean = new ServiceLocatorFactoryBean();
        serviceLocatorFactoryBean.setServiceLocatorInterface(ParserFactory.class);
        return serviceLocatorFactoryBean;
    }

    @Bean("defaultMqProducer")
    DefaultMQProducer defaultMqProducerBean() {
        DefaultMQProducer producer = new DefaultMQProducer("defaultMqProducer");
        producer.setNamesrvAddr("1.13.176.168");
        try {
            producer.start();
        } catch (MQClientException e) {
            throw new RuntimeException("rocketMq生产者启动失败", e);
        }
        return producer;
    }

    @GetMapping("/limit/{id}")
    public String getById(@PathVariable String id) throws InterruptedException {
        semaphore.acquire();
        String name = studentDao.getById(id);
        semaphore.release();
        return name;
    }

    @GetMapping("/cache/{id}")
    public String getCacheById(@PathVariable String id) {
        return dataBaseService.getById(id);
    }

    @GetMapping("/compression/{str}")
    public String testCompression(@PathVariable String str) {
        return str + new String(new byte[2 * 1024]);
    }

    @GetMapping("/query/region")
    public Region queryRegionById(@RequestParam Integer id) {
        Region region = new Region();
        region.setId(1);
        region.setName("福建");
        if (region.getId().equals(id)) {
            return region;
        }
        return null;
    }

    @PostMapping("/add/region")
    public Region addRegion(@RequestBody Region region) {
        return region;
    }

    /**
     * 文件上传
     *
     * @param files 多个文件情况
     * @return {@link String}
     * @throws IOException io异常
     */
    @GetMapping("/upload")
    public String upload(@RequestParam MultipartFile[] files) throws IOException {
        class UploadFileException extends RuntimeException {
            public UploadFileException(String message) {
                super(message);
            }
        }
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            File dest = new File("I:\\storage\\" + originalFilename);
            if (!dest.getParentFile().exists()) {
                boolean mkdirs = dest.getParentFile().mkdirs();
                if (!mkdirs) {
                    throw new UploadFileException("上传文件失败，服务器创建目录错误");
                }
            }
            file.transferTo(dest);
        }
        JSONObject result = new JSONObject();
        result.put("code","200");
        result.put("message", "上传文件成功");
        return result.toString();
    }

    /**
     * 文件下载，不建议用接口做，因为文件下载一般都是下载一些静态文件，可以先将文件处理好，
     * 然后通过nginx服务下载静态文件，速度会快很多
     *
     * @param response http响应
     * @param fileName 下载文件名
     * @return {@link String}
     */
    @GetMapping("/download")
    public String fileDownLoad(HttpServletResponse response, @RequestParam("fileName") String fileName){
        class DownLoadException extends RuntimeException {
            public DownLoadException(String message) {
                super(message);
            }
        }
        File file = new File("I:\\storage\\"+ fileName);
        if(!file.exists()){
            throw new DownLoadException("下载文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

        try(BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath()));) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            throw new DownLoadException("下载文件不存在");
        }
        JSONObject result = new JSONObject();
        result.put("code","200");
        result.put("message", "下载文件成功");
        return result.toString();
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello " + LocalDateTime.now();
    }
}
