package org.example.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

/**
 * @author huang
 */
@ControllerAdvice
public class AsyncRequestTimeoutHandler {
    private final Logger logger = LoggerFactory.getLogger(AsyncRequestTimeoutHandler.class);

    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    @ResponseBody
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public void asyncRequestTimeoutHandler(AsyncRequestTimeoutException e) {
        logger.info("异步请求超时");
    }
}
