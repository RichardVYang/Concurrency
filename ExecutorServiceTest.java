/*

 ExecutorService is an interface that extends Executor class and represents an asynchronous execution. 
It provides us mechanisms to manage the end and detect progress of the asynchronous tasks.

In this example we are going to see some basic functionalities of ExecutorService, 
as well as handle the Future object, the result of asynchronous computation.

We are going to create a Runnable that is intended to be executed by the ExecutorService. 
Create a java class named myThread and paste the following code.

The functionality of the Runnable is very simple. It computes a sum from the giving argument and it sleeps for a specified time.

In this example we will use a factor method of ExecutorService that creates a thread pool of fixed number of threads. 
For this reason, newFixedThreadPool() method is used where we specify the number of threads in the pool. To execute the thread, 
we can use either execute() method or submit(), where both of them take Runnable as a parameter. 
execute() method is depending on the implementation of the Executor class and may perform the Runnable in a new thread, in a pooled thread, 
or in the calling thread. submit() method extends execute(), by returning a Future that represents the submitting task.

The Future can be used to indicate the termination of execution of the thread. For instance, get() method waits for the completion of the computation. 
If the returning value is null, the task has finished correctly. Otherwise, cancel() method can be called in order to end the execution of this task. 
It is worth to mention that for bulk or a collection of thread execution, invokeAll() and invokeAny() are used respectively, although there are not used in this example.

To close down the ExecutorService, there are many methods that can be used. In our example we use shutdown() method, in which the submitted 
tasks are executed before the shutting down but new tasks can not be accepted. Another approach is shutdownNow() method, which stops the executing tasks, 
pause the waiting ones and returns the list of the awaiting ones. Moreover, awaitTermination() can be used in order to wait until all threads are terminated.


 */
package com.yang.concurrentcollection.executorservice;

import java.util.concurrent.ExecutionException; 
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors; 
import java.util.concurrent.Future;  
import java.util.concurrent.TimeUnit; 

/**
 *
 * @author Richard Yang
 */
public class ExecutorServiceTest {

    private static Future taskTwo = null;
    private static Future taskThree = null;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // execute the Runnable 
        Runnable taskOne = new MyThread("TaskOne", 2, 100);

        executor.execute(taskOne);

        for (int i = 0; i < 2; i++) {

            // if this task is not created or is canceled or is completed 
            if ((taskTwo == null) || (taskTwo.isDone()) || (taskTwo.isCancelled())) {

                // submit a task and return a Future 
                taskTwo = executor.submit(new MyThread("TaskTwo", 4, 200));

            }

            if ((taskThree == null) || (taskThree.isDone()) || (taskThree.isCancelled())) {
                taskThree = executor.submit(new MyThread("TaskThree", 5, 100));
            }

            // if null the task has finished 
            if (taskTwo.get() == null) {
                System.out.println(i + 1 + ") TaskTwo terminated successfully");
            } else {

                // if it doesn't finished, cancel it 
                taskTwo.cancel(true);
            }

            if (taskThree.get() == null) {
                System.out.println(i + 1 + ") TaskThree terminated successfully");
            } else {
                taskThree.cancel(true);

            }

        }

        executor.shutdown();

        System.out.println("-----------------------");

        // wait until all tasks are finished 
        executor.awaitTermination(1, TimeUnit.SECONDS);

        System.out.println("All tasks are finished!");

    }

}
