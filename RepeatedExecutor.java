/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yang.concurrentcollection.executor;

import java.util.concurrent.*;
// This Task class implements Runnable, so its a Thread object

class Task implements Runnable {

    public void run() {
        System.out.println("Calling Task.run() ");
    }
}
// This class implements Executor interface and should override execute(Runnable) method.
// We provide an overloaded execute method with an additional argument 'times' to create and
// run the threads for given number of times

class RepeatedExecutor implements Executor {

    public void execute(Runnable runnable) {
        new Thread(runnable).start();
    }

    public void execute(Runnable runnable, int times) {
        System.out.printf("Calling Task.run() thro' Executor.execute() for %d times %n", times);
        for (int i = 0; i < times; i++) {
            execute(runnable);
        }
    }
}

/*
Use Executors and ThreadPools
You can directly create and manage threads in the application by creating Thread objects. However, if you want to
abstract away the low-level details of multi-threaded programming, you can make use of the Executor interface.

Executor
Executor is an interface that declares only one method: void execute(Runnable). This may not look like a big
interface by itself, but its derived classes (or interfaces), such as ExecutorService, ThreadPoolExecutor, and
ForkJoinPool, support useful functionality. We will discuss the derived classes of Executor in more detail in the rest
of this section. For now, for a simple example of the Executor interface to understand how to
implement this interface and use it in practice.

In this program, you have a Task class that implements Runnable by providing the definition of the run()
method. The class RepeatedExecutor implements the Executor interface by providing the definition of the
execute(Runnable) method.
Both Runnable and Executor are similar in the sense that they provide a single method for implementation. In
this definition you may have noticed that Exectutor by itself is not a thread, and you must create a Thread object
to execute the Runnable object passed in the execute() method. However, the main difference between Runnable
and Exectutor is that Executor is meant to abstract how the thread is executed. For example, depending on the
implementation of Executor, Exectutor may schedule a thread to run at a certain time, or execute the thread after a
certain delay period.
In this program, you have overloaded the execute() method with an additional argument to create and execute
threads a certain number of times. In the main() method, you first create a Thread object and schedule it for running.
After that, you instantiate RepeatedExectutor to execute the thread three times.
*/

// This class spawns a Task thread and explicitly calls start() method.
// It also shows how to execute a Thread using Executor

class ExecutorTest {

    public static void main(String[] args) {
        Runnable runnable = new Task();
        System.out.println("Calling Task.run() by directly creating a Thread object");
        Thread thread = new Thread(runnable);
        thread.start();
        RepeatedExecutor executor = new RepeatedExecutor();
        executor.execute(runnable, 3);
    }
}
