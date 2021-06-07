package com.zx.summary.learning.线程池;

import cc.ewell.cvs.inhospital.service.business.ExtFieldHelperService;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ScheduledExecutorService代码实现定时任务 implements ApplicationListener {

    @Autowired
    ExtFieldHelperService extFieldHelperService;

    long     initialDelay = 5L;
    long     delay        = 120L;
    TimeUnit unit         = TimeUnit.SECONDS;

    //扩展字段重试线程池
    ScheduledExecutorService extInfoReTryExecutor = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("extInfoReTry", true, 5));

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            extInfoReTryExecutor.scheduleWithFixedDelay(new ExtFieldsReTry(extFieldHelperService),
                    initialDelay,
                    delay,
                    unit);
            log.info("平台日志分发失败重试任务启动....");
        } else if (event instanceof ContextClosedEvent) {
            shutdownAndAwaitTermination(extInfoReTryExecutor);
            log.info("平台日志分发服务正常停止了~");
        }
    }

    /**
     * 消息重发服务
     */
    final class ExtFieldsReTry implements Runnable {
        public ExtFieldsReTry(ExtFieldHelperService extFieldHelperService) {
            this.extFieldHelperService = extFieldHelperService;
        }

        ExtFieldHelperService extFieldHelperService;
        int                   maxCount = 100;

        @Override
        public void run() {
            try {
                int count = extFieldHelperService.reFillExtFields(maxCount);
                if (count > 0) {
                    log.error("---期望更新【{}】条带EXT扩展字段的危急值信息,实际更新【{}】条！", maxCount, count);
                } else {
                    log.info("---good,暂时没有需要更新的EXT扩展字段信息...");
                }
            } catch (Exception e) {
                log.error("---更新EXT扩展字段信息失败！", e);
            }
        }
    }

    /**
     * 可靠关闭
     *
     * @param pool
     */
    void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
