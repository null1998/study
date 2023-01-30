package org.example.thread;

import org.example.util.ThreadUtil;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
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
        // 任务处理结果依次传递
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> 1, threadPoolExecutor).thenApply(res -> ++res).thenApply(res -> res * res).whenComplete((res, ex) -> System.out.printf("任务执行完毕，执行结果为%s，异常为%s", res, ex));
        Assertions.assertEquals(4, completableFuture.get());
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
