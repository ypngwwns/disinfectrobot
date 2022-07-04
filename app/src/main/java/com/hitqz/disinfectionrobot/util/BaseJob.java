package com.hitqz.disinfectionrobot.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BaseJob {
    private ScheduledExecutorService executorService = null;
    private ScheduledFuture future;

    public void start(Runnable job, int initDelay, int delay) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(job, initDelay, delay, TimeUnit.MILLISECONDS);
    }

    public void stop() {
//        if (future != null) {
//            future.cancel(true);
//        }

        if (executorService != null) {
            try {
                executorService.shutdown();

                if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
    }

    public boolean isRunning() {
        if (executorService == null) {
            return false;
        }
        return !executorService.isShutdown();
    }
}
