package org.example.util;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

/**
 * @author huang
 */
public class ExceptionUtils {
    /**
     * 获取包装在CompletionException内的真正异常
     *
     * @param throwable 异常
     * @return {@link Throwable}
     */
    public static Throwable extractRealException(Throwable throwable) {
        if (throwable instanceof CompletionException || throwable instanceof ExecutionException) {
            if (throwable.getCause() != null) {
                return throwable.getCause();
            }
        }
        return throwable;
    }
}
