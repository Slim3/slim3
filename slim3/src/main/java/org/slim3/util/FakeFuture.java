package org.slim3.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A face {@link Future}.
 * 
 * @author luke
 * @author higa
 * @since 1.0.6
 * @param <T>
 */
public class FakeFuture<T> implements Future<T> {

    /**
     * The value.
     */
    protected final T value;

    /**
     * Constructor.
     * 
     * @param value
     *            the value
     */
    public FakeFuture(T value) {
        this.value = value;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public T get() throws InterruptedException, ExecutionException {
        return value;
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        return value;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return true;
    }

}
