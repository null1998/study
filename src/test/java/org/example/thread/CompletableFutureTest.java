package org.example.thread;

import org.example.util.ExceptionUtils;
import org.example.util.ThreadUtil;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * run与supply、apply的区别在于是否有参数在任务间传递
 * 带async的始终使用线程池中的线程执行任务，不带async的可能使用线程池线程，也可能使用main线程
 * @author huang
 */
public class CompletableFutureTest {

    /**
     * 链式任务处理，处理结果在任务间传递
     */
    @Test
    public void testThenApply() throws ExecutionException, InterruptedException {
        // 使用自定义线程池创建线程执行任务
        ThreadPoolExecutor threadPoolExecutor = ThreadUtil.getThreadPoolExecutor("completableFuture");
        // 测试一：任务处理结果依次传递
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> 1, threadPoolExecutor).thenApply(res -> ++res).thenApply(res -> res * res).whenComplete((res, ex) -> System.out.printf("测试一：任务执行完毕，执行结果为%s，异常为%s\n", res, ex));
        Assertions.assertEquals(4, completableFuture.get());

        // 测试二：注册thenApply时，若被依赖的supplyAsync已经完成，则使用main线程执行thenApply
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("测试二：" + Thread.currentThread().getName());
            return 1;
        }).thenApply(res -> {
            System.out.println("测试二：" + Thread.currentThread().getName());
            return ++res;
        });
        Assertions.assertEquals(2, completableFuture1.get());

        // 测试三：注册thenApply时，若被依赖的supplyAsync未完成，则使用supplyAsync线程执行thenApply
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(100);
            System.out.println("测试三：" + Thread.currentThread().getName());
            return 1;
        }).thenApply(res -> {
            System.out.println("测试三：" + Thread.currentThread().getName());
            return ++res;
        });
        Assertions.assertEquals(2, completableFuture2.get());

        // 测试四：使用main线程执行thenApply时，若thenApply执行太慢会阻塞main线程
        CompletableFuture<Integer> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("测试四：" + Thread.currentThread().getName());
            return 1;
        }).thenApply(res -> {
            ThreadUtil.sleep(1000);
            System.out.println("测试四：" + Thread.currentThread().getName());
            return ++res;
        });
        System.out.println("测试四：" + Thread.currentThread().getName());
        Assertions.assertEquals(2, completableFuture3.get());
    }

    /**
     * 带async的任务始终使用线程池线程
     */
    @Test
    public void testThenApplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync：" + Thread.currentThread().getName());
            return 1;
        }).thenApplyAsync(res -> {
            System.out.println("thenApplyAsync：" + Thread.currentThread().getName());
            return ++res;
        });
        Assertions.assertEquals(2, completableFuture.get());
    }

    /**
     * 由于异步执行的任务在其他线程上执行，而异常信息存储在线程栈中，因此当前线程
     * 除非阻塞等待返回结果，否则无法通过 try\catch 捕获异常。CompletableFuture
     * 提供了异常捕获回调 exceptionally，相当于同步调用中的 try\catch
     */
    @Test
    public void testExceptionally() {
        CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("测试exceptionally异常");
        }).exceptionally(err -> {
            ExceptionUtils.extractRealException(err).printStackTrace();
            return 0;
        });
    }

    /**
     * 执行两组任务，将执行结果合并后返回
     */
    @Test
    public void testThenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> 1)
                .thenCombine(CompletableFuture.supplyAsync(() -> 2), (res1, res2) -> {
                    System.out.printf("两组任务执行结果分别为%s和%s，最终执行结果为%s", res1, res2, res1 + res2);
                    return res1 + res2;
                });
        Assertions.assertEquals(3, completableFuture.get());
    }

    /**
     * 执行多组任务，阻塞等待所有任务执行完毕
     */
    @Test
    public void testAllOf() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> System.out.println("任务完成")),
                CompletableFuture.runAsync(() -> System.out.println("任务完成")),
                CompletableFuture.runAsync(() -> System.out.println("任务完成"))
        );
        completableFuture.get();
    }

    /**
     * 执行多组任务，返回最快那组任务的执行结果
     */
    @Test
    public void testAnyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<Object> completableFuture = CompletableFuture.anyOf(CompletableFuture.supplyAsync(() -> {
            // 第一个任务执行速度慢
            ThreadUtil.sleep(1000);
            return 1;
            // 第二个任务执行速度快
        }), CompletableFuture.supplyAsync(() -> 2));
        Assertions.assertEquals(2, completableFuture.get());
    }
}
