/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yang.concurrentcollection.threadfactory;

import java.util.concurrent.*;

/*
ThreadFactory
ThreadFactory is an interface that is meant for creating threads instead of explicitly creating threads by calling new
Thread(). For example, assume that you often create high-priority threads. You can create a MaxPriorityThreadFactory
to set the default priority of threads created by that factory to maximum priority


*/

// A ThreadFactory implementation that sets the thread priority to max
// for all the threads it creates

class MaxPriorityThreadFactory implements ThreadFactory {

    private static long count = 0;

    public Thread newThread(Runnable r) {
        Thread temp = new Thread(r);
        temp.setName("prioritythread" + count++);
        temp.setPriority(Thread.MAX_PRIORITY);
        return temp;
    }
}

class ARunnable implements Runnable {

    public void run() {
        System.out.println("Running the created thread ");
    }
}

class TestThreadFactory {

    public static void main(String[] args) {
        ThreadFactory threadFactory = new MaxPriorityThreadFactory();
        Thread t1 = threadFactory.newThread(new ARunnable());
        System.out.println("The name of the thread is " + t1.getName());
        System.out.println("The priority of the thread is " + t1.getPriority());
        t1.start();
    }
}
