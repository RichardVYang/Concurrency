/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yang.concurrentcollection.threadpoolexecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 *
 * @author Richard Yang
 * 
 * A thread pool manages the pool of worker threads, it contains a queue that keeps tasks waiting to get executed. 
 * A thread pool manages the collection of Runnable threads and worker threads execute Runnable from the queue. 
 * java.util.concurrent.Executors provide implementation of java.util.concurrent.Executor interface to create the thread pool in java
 * 
 * we are creating fixed size thread pool of 5 worker threads. Then we are submitting 10 jobs to this pool, 
 * since the pool size is 5, it will start working on 5 jobs and other jobs will be in wait state, 
 * as soon as one of the job is finished, another job from the wait queue will be picked up by worker thread and get’s executed
 */
public class SimpleThreadPoolMain {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {

            Runnable worker = new WorkerThread(" " + i); 
            executor.execute(worker);
        }

        executor.shutdown();

        while (!executor.isTerminated()) {

        }

        System.out.println("Finished all threads"); 

    } 

    
}
