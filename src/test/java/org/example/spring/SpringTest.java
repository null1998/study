package org.example.spring;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SpringTest {
    @Test
    public void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(UserConfig.class);
        PersonService personService = annotationConfigApplicationContext.getBean(PersonService.class);
        personService.print();
        User user = annotationConfigApplicationContext.getBean(User.class);
        assert "001".equals(user.getId());
        Company company = annotationConfigApplicationContext.getBean(Company.class);
        assert "002".equals(company.getId());
        annotationConfigApplicationContext.close();
    }

    interface PersonService {
        void print();
    }

    @Component
    static class BusinessPersonService implements PersonService,
            // 若开启懒加载，在真正需要用到bean之前，construct和下面接口定义的方法不会被调用
            BeanNameAware,
            BeanFactoryAware,
            ApplicationContextAware,
            InitializingBean,
            DisposableBean {

        private User user;

        private Company company;

        @Autowired
        public BusinessPersonService(User user, Company company) {
            System.out.println("construct-param");
            this.user = user;
            this.company = company;
        }

        public BusinessPersonService() {
            System.out.println("construct");
        }

        @Override
        public void print() {

        }

        @Override
        public void setBeanName(String s) {
            System.out.println("bean感知到beanName是" + s);
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            System.out.println("bean感知到beanFactory是" + beanFactory.getClass());
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            System.out.println("bean感知到applicationContext是" + applicationContext.getClass());
        }

        // postProcessBeforeInitialization执行顺序

        @PostConstruct
        public void postConstruct() {
            System.out.println("postConstruct");
        }

        /**
         * 作用和init-method相似，两者可同时存在
         */
        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("afterPropertiesSet");
        }
        // init-method执行顺序

        // postProcessAfterInitialization执行顺序

        // annotationConfigApplicationContext.close()执行后

        @PreDestroy
        public void preDestroy() {
            System.out.println("preDestroy");
        }

        /**
         * 作用和destroy-method相似，两者可同时存在
         */
        @Override
        public void destroy() throws Exception {
            System.out.println("destroy");
        }

        // destroy-method执行顺序
    }

    @Configuration
    // 延迟到使用bean时才将bean加载进容器，使用前仅获取bean的定义
    @ComponentScan(lazyInit = true)
    static
    class UserConfig {
        @Bean(name = "user", initMethod = "initMethod", destroyMethod = "destroyMethod")
        public User user() {
            User user = new User();
            user.setId("001");
            return user;
        }
    }

    static class User implements
            InitializingBean,
            DisposableBean {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void initMethod() {
            System.out.println(this.getClass() + "-initMethod");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println(this.getClass() + "-afterPropertiesSet");
        }

        public void destroyMethod() {
            System.out.println(this.getClass() + "-destroyMethod");
        }

        @Override
        public void destroy() throws Exception {
            System.out.println(this.getClass() + "-destroy");
        }
    }

    @Component
    static class Company {
        @Value("002")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    /**
     * 所有bean公用的方法，在bean加载到容器的过程中调用
     */
    @Component
    static class MyBeanPostProcess implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            System.out.println(beanName + "-postProcessBeforeInitialization");
            return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            System.out.println(beanName + "-postProcessAfterInitialization");
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }
    }
}

