package org.danil.lab17;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class Executor extends ThreadPoolExecutor implements DisposableBean {

    public Executor() {
        super(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory());
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("Destroying executor");
        this.shutdown();
    }
}
