/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yang.concurrentcollection.callable;

// Factorial implements Callable so that it can be passed to a ExecutorService
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
Callable, Executors, ExecutorService, ThreadPool, and Future
Callable is an interface that declares only one method: call(). Its full signature is V call() throws Exception. It
represents a task that needs to be completed by a thread. Once the task completes, it returns a value. For some reason,
if the call() method cannot execute or fails, it throws an Exception.
To execute a task using the Callable object, you first create a thread pool. A thread pool is a collection of threads
that can execute tasks. You create a thread pool using the Executors utility class. This class provides methods to get
instances of thread pools, thread factories, etc.

The ExecutorService interface implements the Executor interface and provides services such as termination of
threads and production of Future objects. Some tasks may take considerable execution time to complete. So, when
you submit a task to the executor service, you get a Future object.
Future represents objects that contain a value that is returned by a thread in the future (i.e., it returns the value
once the thread terminates in the “future”). You can use the isDone() method in the Future class to check if the task is
complete and then use the get() method to fetch the task result. If you call the get() method directly while the task is
not complete, the method blocks until it completes and returns the value once available.

In this program, you have a Factorial class that implements Callable. Since the task is to compute the
factorial of a number N, the task needs to return a result. You use Long type for the factorial value, so you implement
Callable<Long>. Inside the Factorial class, you define the call() method that actually performs the task (the task
here is to compute the factorial of the given number). If the given value N is negative or zero, you don’t perform the
task and throw an exception to the caller. Otherwise, you loop from 1 to N and find the factorial value.
In the CallableTest class, you first create an instance of the Factorial class. You then need to execute this task.
For the sake of simplicity, you get a singled-threaded executor by calling the newSingleThreadExecutor() method
in the Executors class. Note that you could use other methods such as newFixedThreadPool(nThreads) to create a
thread pool with multiple threads depending on the level of parallelism you need.
Once you get an ExecutorService, you submit the task for execution. ExecutorService abstracts details such
as when the task is executed, how the task is assigned to the threads, etc. You get a reference to Future<Long> when
you call the submit(task) method. From this future reference, you call the get() method to fetch the result after
completing the task. If the task is still executing when you call future.get(), this get() method will block until the
task execution completes. Once the execution is complete, you need to manually release the ExecutorService by
calling the shutdown() method.
*/


// and get executed as a task.
class Factorial implements Callable<Long> {

    long n;

    public Factorial(long n) {
        this.n = n;
    }

    public Long call() throws Exception {
        if (n <= 0) {
            throw new Exception("for finding factorial, N should be > 0");
        }
        long fact = 1;
        for (long longVal = 1; longVal <= n; longVal++) {
            fact *= longVal;
        }
        return fact;
    }
}

// Illustrates how Callable, Executors, ExecutorService, and Future are related;
// also shows how they work together to execute a task

class CallableTest {

    public static void main(String[] args) throws Exception {
// the value for which we want to find the factorial
        long N = 20;
// get a callable task to be submitted to the executor service
        Callable<Long> task = new Factorial(N);
// create an ExecutorService with a fixed thread pool consisting of one thread
        ExecutorService es = Executors.newSingleThreadExecutor();
// submit the task to the executor service and store the Future object
        Future<Long> future = es.submit(task);
// wait for the get() method that blocks until the computation is complete.
        System.out.printf("factorial of %d is %d", N, future.get());
// done. shutdown the executor service since we don't need it anymore
        es.shutdown();
    }
}
