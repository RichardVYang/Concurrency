/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
Executors class provide simple implementation of ExecutorService using ThreadPoolExecutor 
but ThreadPoolExecutor provides much more feature than that. We can specify the number of threads 
that will be alive when we create ThreadPoolExecutor instance and we can limit the size of thread pool 
and create our own RejectedExecutionHandler implementation to handle the jobs that can’t fit in the worker queue.
ThreadPoolExecutor provides several methods using which we can find out the current state of executor, pool size, 
active thread count and task count. So I have a monitor thread that will print the executor information at certain time interval.

Notice that while initializing the ThreadPoolExecutor, we are keeping initial pool size as 2, maximum pool size to 4 
and work queue size as 2. So if there are 4 running tasks and more tasks are submitted, the work queue will hold only 2 of them 
and rest of them will be handled by RejectedExecutionHandlerImpl.

Notice the change in active, completed and total completed task count of the executor. We can invoke shutdown() method to finish 
execution of all the submitted tasks and terminate the thread pool.
*/

package com.yang.concurrentcollection.threadpoolexecutor2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Richard Yang
 */
public class WorkerPoolMain {

    public static void main(String args[]) throws InterruptedException {

        //RejectedExecutionHandler implementation 
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();

        //Get the ThreadFactory implementation to use 
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        //creating the ThreadPoolExecutor 
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), threadFactory, rejectionHandler);

        //start the monitoring thread 
        MyMonitorThread monitor = new MyMonitorThread(executorPool, 3);

        Thread monitorThread = new Thread(monitor);

        monitorThread.start();

        //submit work to the thread pool 
        for (int i = 0; i < 10; i++) {

            executorPool.execute(new WorkerThread("cmd" + i));

        }

        Thread.sleep(30000);

        //shut down the pool 
        executorPool.shutdown();

        //shut down the monitor thread 
        Thread.sleep(5000);
        monitor.shutdown();

    }

}
