package org.example.spi;

import org.junit.Test;

import java.util.ServiceLoader;

public class ServiceProviderInterfaceTest {
    @Test
    public void testServiceLoader() {
        ServiceLoader<HelloServiceProvider> serviceProviders = ServiceLoader.load(HelloServiceProvider.class);
        // 延迟加载，在迭代器中一个一个加载实现类
        for (HelloServiceProvider serviceProvider : serviceProviders) {
            assert serviceProvider instanceof HelloEnglishServiceProviderImpl || serviceProvider instanceof HelloChineseServiceProviderImpl;
        }
    }
}
