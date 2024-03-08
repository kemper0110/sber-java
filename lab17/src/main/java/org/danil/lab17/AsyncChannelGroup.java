package org.danil.lab17;

import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;

@Getter
@Component
public class AsyncChannelGroup implements DisposableBean {

    private final AsynchronousChannelGroup group;

    public AsyncChannelGroup(Executor executor) throws IOException {
        this.group = AsynchronousChannelGroup.withThreadPool(executor);
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy group");
    }
}
