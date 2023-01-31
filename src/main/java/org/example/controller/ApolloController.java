package org.example.controller;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * apollo长轮询服务端消息推送
 *
 * @author huang
 */
@RestController
@RequestMapping("/apollo")
public class ApolloController {

    private static final Long TIME_OUT = 3 * 1000L;

    private static final Multimap<String, DeferredResult<String>> watchRequests = Multimaps.synchronizedMultimap(HashMultimap.create());

    /**
     * 长轮询查询数据
     */
    @GetMapping(path = "watch/{id}")
    public DeferredResult<String> watch(@PathVariable String id) {
        // DeferredResult字面意思是"延迟结果"，它允许Spring MVC收到请求后，立即释放(归还)容器线程，以便容器可以接收更多的外部请求，提升吞吐量，
        // 与此同时，DeferredResult将陷入阻塞，直到我们主动将结果set到DeferredResult，最后，DeferredResult会重新申请容器线程，并将本次请求返回给客户端。
        DeferredResult<String> deferredResult = new DeferredResult<>(TIME_OUT);
        // 代码任意位置调用了同一个DeferredResult的setResult()后，则会被DeferredResult的onCompletion()监听器捕获到
        deferredResult.onCompletion(() -> watchRequests.remove(id, deferredResult));
        watchRequests.put(id, deferredResult);
        return deferredResult;
    }

    /**
     * 变更数据
     */
    @GetMapping(path = "publish/{id}")
    public String publish(@PathVariable String id) {
        // 数据变更 取出监听ID的所有长轮询请求，并一一响应处理
        if (watchRequests.containsKey(id)) {
            Collection<DeferredResult<String>> deferredResults = watchRequests.get(id);
            for (DeferredResult<String> deferredResult : deferredResults) {
                deferredResult.setResult("changed " + LocalDateTime.now());
            }
        }
        return "success";
    }
}
