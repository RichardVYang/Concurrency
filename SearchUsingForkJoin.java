/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yang.parallel.forkjoint;

import java.util.concurrent.*;

/* 

Let’s analyze how this program works. In this program, you want to compute the sum of the values in the range
1..1,000,000. For the sake of simplicity, you decide to use ten threads to execute the tasks. The class RecursiveSumOfN
extends RecursiveTask<Long>. In RecursiveTask<Long>, you use <Long> because the sum of numbers in each
sub-range is a Long value. In addition, you chose RecursiveTask<Long> instead of plain RecursiveAction because
each subtask returns a value. If the subtask does not return a value, you can use RecursiveAction instead.
In the compute() method, you decide whether to compute the sum for the range or subdivide the task further
using following condition:
(to - from) <= N/NUM_THREADS)
You use this “threshold” value in this computation. In other words, if the range of values is within the threshold
that can be handled by a task, then you perform the computation; otherwise you recursively divide the task into two
parts. You use a simple for loop to find the sum of the values in that range. In the other case, you divide the range
similarly to how you divide the range in a binary search algorithm: for the range from .. to, you find the mid-point
and create two sub-ranges from .. mid and mid + 1 .. to. Once you call fork(), you wait for the first task to
complete the computation of the sum and spawn another task for the second half of the computation.
In the main() method, you create a ForkJoinPool with number of threads given by NUM_THREADS. You submit
the task to the fork/join pool and get the computed sum for 1..1,000,000. Now you also calculate the sum using the
formula to sum N continuous numbers.
From the output of the program, you can observe how the task got subdivided into subtasks. You can also verify
from the output that the computed sum and sum computed from the formula are the same, indicating that your
division of tasks for summing the sub-ranges is correct.
In this program, you arbitrarily assumed the number of threads to use was ten threads. This was to simplify the
logic of this program. A better approach to decide the threshold value is to divide the data size length by the number
of available processors. In other words,
threshold value = (data length size) / (number of available processors);
How do you programmatically get the number of available processors? For that you can use the method
Runtime.getRuntime().availableProcessors()).
In Listing 14-17, you used RecursiveTask; however, if a task is not returning a value, then you should use
RecursiveAction. Let’s implement a search program using RecursiveAction. Assume that you have a big array (say
of 10,000 items) and you want to search a key item. You can use the Fork/Join framework to split the task into several
subtasks and execute them in parallel. Listing 14-18 contains the program implementing the solution.

The key difference between Listings 14-14 and 14-15 is that you used RecursiveAction in the latter instead of
RecursiveTask. You made several changes to extend the task class from RecursiveAction. The first change is that
the compute() method is not returning anything. Another change is that you used the invokeAll() method to submit
the subtasks to execute. Another obvious change is that you carried out search in the compute() method instead of
summation in earlier case. Apart from these changes, the program in Listing 14-17 works much like the program in
Listing 14-18.


Points to Remember
Remember these points for your exam:
It is possible to achieve wh • at the Fork/Join framework offers using basic concurrency
constructs such as start() and join(). However, the Fork/Join framework abstracts many
lower-level details and thus is easier to use. In addition, it is much more efficient to use
the Fork/Join framework instead handling the threads at lower levels. Furthermore, using
ForkJoinPool efficiently manages the threads and performs much better than conventional
threads pools. For all these reasons, you are encouraged to use the Fork/Join framework.
• Each worker thread in the Fork/Join framework has a work queue, which is implemented
using a Deque. Each time a new task (or subtask) is created, it is pushed to the head of its
own queue. When a task completes a task and executes a join with another task that is not
completed yet, it works smart. The thread pops a new task from the head of its queue and
starts executing rather than sleeping (in order to wait for another task to complete). In fact,
if the queue of a thread is empty, then the thread pops a task from the tail of the queue
belonging to another thread. This is nothing but a work-stealing algorithm.
• It looks obvious to call fork() for both the subtasks (if you are splitting in two subtasks) and
call join() two times. It is correct—but inefficient. Why? Well, basically you are creating more
parallel tasks than are useful. In this case, the original thread will be waiting for the other
two tasks to complete, which is inefficient considering task creation cost. That is why you call
fork() once and call compute() for the second task.
• The placement of fork() and join() calls are very important. For instance, let’s assume that
you place the calls in following order:
first.fork();
resultFirst = first.join();
resultSecond = second.compute();
This usage is a serial execution of two tasks, since the second task starts executing only after the first is
complete. Thus, it is less efficient even than its sequential version since this version also includes cost of
the task creation. The take-away: watch your placement of fork/join calls.
• Performance is not always guaranteed while using the Fork/Join framework. One of the
reasons we mentioned earlier is the placement of fork/join calls.

*/


//This class illustrates how we can search a key within N numbers using fork/join framework
// (using RecursiveAction).
//The range of numbers are divided into half until the range can be handled by a thread.

class SearchUsingForkJoin {

    private static int N = 10000;
    private static final int NUM_THREADS = 10; // number of threads to create for
// distributing the effort
    private static int searchKey = 100;
    private static int[] arrayToSearch;
// This is the recursive implementation of the algorithm;
// inherit from RecursiveAction

    static class SearchTask extends RecursiveAction {

        private static final long serialVersionUID = 1L;
        int from, to;
// from and to are range of values to search

        public SearchTask(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public void compute() {
//If the range is smaller enough to be handled by a thread,
//we search in the range
            if ((to - from) <= N / NUM_THREADS) {
// add in range 'from' .. 'to' inclusive of the value 'to'
                for (int i = from; i <= to; i++) {
                    if (arrayToSearch[i] == searchKey) {
                        System.out.println("Search key: " + searchKey + " found at index:" + i);
                    }
                }
            } else {
// no, the range is big for a thread to handle,
// so fork the computation
// we find the mid-point value in the range from..to
                int mid = (from + to) / 2;
                System.out.printf("Forking computation into two ranges: "
                        + "%d to %d and %d to %d %n", from, mid, mid, to);
//invoke all the subtasks
                invokeAll(new SearchTask(from, mid), new SearchTask(mid + 1, to));
            }
        }
    }

    public static void main(String[] args) {
//intantiate the array to be searched
        arrayToSearch = new int[N];
//fill the array with random numbers
        for (int i = 0; i < N; i++) {
            arrayToSearch[i] = ThreadLocalRandom.current().nextInt(0, 1000);
        }
// Create a fork-join pool that consists of NUM_THREADS
        ForkJoinPool pool = new ForkJoinPool(NUM_THREADS);
// submit the computation task to the fork-join pool
        pool.invoke(new SearchTask(0, N - 1));
    }
}
