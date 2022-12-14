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

    @Test
    public void testAutowiredConstructor() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(MyConfig.class);
        annotationConfigApplicationContext.getBean(StoreA.class);
        annotationConfigApplicationContext.getBean(StoreB.class);
    }

    interface PersonService {
        void print();
    }

    @Configuration
    @ComponentScan(basePackageClasses = {StoreA.class, StoreB.class})
    static class MyConfig {
        @Bean
        public Book book() {
            Book book = new Book();
            book.setTitle("title");
            return book;
        }
    }

    static class Book {
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "Book{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Component
    static class StoreA {
        @Autowired
        private Book book;

        public StoreA() {
            System.out.println("setter??????" + this.book);
        }
    }

    @Component
    static class StoreB {
        private Book book;

        @Autowired
        public StoreB(Book book) {
            this.book = book;
            System.out.println("constructor??????" + this.book);
        }
    }

    @Component
    static class BusinessPersonService implements PersonService,
            // ??????????????????????????????????????????bean?????????construct?????????????????????????????????????????????
            BeanNameAware,
            BeanFactoryAware,
            ApplicationContextAware,
            InitializingBean,
            DisposableBean {

        private User user;

        private Company company;

        @Autowired
        public BusinessPersonService(User user, Company company) {
            System.out.println(this.getClass() + "-construct-param");
            this.user = user;
            this.company = company;
        }

        public BusinessPersonService() {
            System.out.println(this.getClass() + "-construct");
        }

        @Override
        public void print() {

        }

        @Override
        public void setBeanName(String s) {
            System.out.println("bean?????????beanName???" + s);
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            System.out.println("bean?????????beanFactory???" + beanFactory.getClass());
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            System.out.println("bean?????????applicationContext???" + applicationContext.getClass());
        }

        // postProcessBeforeInitialization????????????

        @PostConstruct
        public void postConstruct() {
            System.out.println(this.getClass() + "-postConstruct");
        }

        /**
         * ?????????init-method??????????????????????????????
         */
        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println(this.getClass() + "-afterPropertiesSet");
        }
        // init-method????????????

        // postProcessAfterInitialization????????????

        // annotationConfigApplicationContext.close()?????????

        @PreDestroy
        public void preDestroy() {
            System.out.println(this.getClass() + "-preDestroy");
        }

        /**
         * ?????????destroy-method??????????????????????????????
         */
        @Override
        public void destroy() throws Exception {
            System.out.println(this.getClass() + "-destroy");
        }

        // destroy-method????????????
    }

    @Configuration
    // ???????????????bean?????????bean????????????????????????????????????bean?????????
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
     * ??????bean?????????????????????bean?????????????????????????????????
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

