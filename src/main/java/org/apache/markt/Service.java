package org.apache.markt;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletResponse;

public class Service {

    private static final ServiceThread serviceThread = new ServiceThread();

    static {
        serviceThread.setName("Service Thread");
        serviceThread.start();
    }

    public static final void blocking() {
        if (serviceThread.running) {
            CountDownLatch l = new CountDownLatch(1);
            serviceThread.add(l);
            try {
                l.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static final void async(AsyncContext ac) {
        if (serviceThread.running) {
            serviceThread.add(ac);
        }
    }

    public static void stopService() {
        serviceThread.stopService();
    }


    private static final class ServiceThread extends Thread {

        private static final long DELAY_NS = 5_000_000_000L;
        //private static final long DELAY_NS = 0L;

        private static final LinkedBlockingDeque<ServiceCall> QUEUE = new LinkedBlockingDeque<>();

        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                ServiceCall serviceCall = null;
                try {
                    serviceCall = QUEUE.take();
                    long sleepms = (DELAY_NS - (System.nanoTime() - serviceCall.getStartTime())) / 1_000_000L;
                    if (sleepms > 0) {
                        Thread.sleep(sleepms);
                    }
                } catch (InterruptedException e) {
                    // Do nothing.
                } finally {
                    if (serviceCall != null) {
                        serviceCall.complete();
                    }
                }
            }
        }

        public void add(CountDownLatch latch) {
            ServiceCall serviceCall = new BlockingServiceCall(latch);
            QUEUE.add(serviceCall);
        }

        public void add(AsyncContext ac) {
            ServiceCall serviceCall = new AsyncServiceCall(ac);
            QUEUE.add(serviceCall);
        }

        public void stopService() {
            running = false;
            this.interrupt();
        }
    }


    private static interface ServiceCall {
        long getStartTime();
        void complete();
    }


    private static abstract class ServiceCallBase implements ServiceCall {
        private final long startTime = System.nanoTime();

        @Override
        public long getStartTime() {
            return startTime;
        }
    }


    private static final class BlockingServiceCall extends ServiceCallBase {

        private final CountDownLatch latch;

        public BlockingServiceCall(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void complete() {
            latch.countDown();
        }
    }


    private static final class AsyncServiceCall extends ServiceCallBase {

        private final AsyncContext ac;

        public AsyncServiceCall(AsyncContext ac) {
            this.ac = ac;
        }

        @Override
        public void complete() {
            ServletResponse resp = ac.getResponse();
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            try {
                resp.getOutputStream().println("OK");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            ac.complete();
        }
    }
}
