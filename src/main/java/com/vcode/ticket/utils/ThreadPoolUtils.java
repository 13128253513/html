package com.vcode.ticket.utils;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;

import java.util.List;
import java.util.concurrent.*;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: ThreadPoolUtils
 * Author:   wangpeng.sy
 * Date:     2018/12/25 9:10
 * Description: ${DESCRIPTION}
 * History:
 */
public class ThreadPoolUtils {

    /**
     * 根据cpu的数量动态的配置核心线程数和最大线程数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数 = CPU核心数 + 1
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    /**
     * 线程池最大线程数 = CPU核心数 * 2 + 1
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 非核心线程闲置时超时1s
     */
    private static final int KEEP_ALIVE = 1;
    /**
     * 线程池的对象
     */
    private static ThreadPoolExecutor executor;

    /**
     * 要确保该类只有一个实例对象，避免产生过多对象消费资源，所以采用单例模式
     */
    private ThreadPoolUtils() {
    }

    private static ThreadPoolUtils sInstance;

    public synchronized static ThreadPoolUtils getsInstance() {
        if (sInstance == null) {
            sInstance = new ThreadPoolUtils();
            /**
             * corePoolSize:核心线程数
             * maximumPoolSize：线程池所容纳最大线程数(workQueue队列满了之后才开启)
             * keepAliveTime：非核心线程闲置时间超时时长
             * unit：keepAliveTime的单位
             * workQueue：等待队列，存储还未执行的任务
             * threadFactory：线程创建的工厂
             * handler：异常处理机制
             *
             */
            executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20),
                    Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        }
        return sInstance;
    }

    /**
     * 开启一个无返回结果的线程
     *
     * @param r
     */
    public void execute(Runnable r) {
        executor.execute(r);
    }

    /**
     * 开启一个有返回结果的线程
     *
     * @param r
     * @return
     */
    public <T> Future<T> submit(Callable<T> r) {
        // 把一个任务丢到了线程池中
        return executor.submit(r);
    }

    /**
     * 开启一个有返回结果的线程
     *
     * @param r
     * @return
     */
    public Future<?> submit(Runnable r) {
        // 把一个任务丢到了线程池中
        return submit(r,null);
    }

    /**
     * 开启一个有返回结果的线程
     * @param task
     * @param result
     * @return
     */
    public <T> Future<T> submit(Runnable task, T result) {
        // 把一个任务丢到了线程池中
        return executor.submit(task,result);
    }

    /**
     * 把任务移除等待队列
     *
     * @param r
     */
    public void cancel(Runnable r) {
        if (r != null) {
            executor.getQueue().remove(r);
        }
    }


    /**
     * 在未来某个时间执行给定的命令链表
     * <p>该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。</p>
     *
     * @param commands 命令链表
     */
    public void execute(final List<Runnable> commands) {
        for (Runnable command : commands) {
            execute(command);
        }
    }

    /**
     * 待以前提交的任务执行完毕后关闭线程池
     * <p>启动一次顺序关闭，执行以前提交的任务，但不接受新任务。
     * 如果已经关闭，则调用没有作用。</p>
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * 试图停止所有正在执行的活动任务
     * <p>试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。</p>
     * <p>无法保证能够停止正在处理的活动执行任务，但是会尽力尝试。</p>
     *
     * @return 等待执行的任务的列表
     */
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    /**
     * 判断线程池是否已关闭
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    /**
     * 关闭线程池后判断所有任务是否都已完成
     * <p>注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isTerminated() {
        return executor.isTerminated();
    }
}
