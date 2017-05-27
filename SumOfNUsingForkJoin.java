/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yang.parallel.forkjoint;

import java.util.concurrent.*;

/* 
Use the Parallel Fork/Join Framework
The Fork/Join framework in the java.util.concurrent package helps simplify writing parallelized code. The
framework is an implementation of the ExecutorService interface and provides an easy-to-use concurrent platform
in order to exploit multiple processors. This framework is very useful for modeling divide-and-conquer problems.
This approach is suitable for tasks that can be divided recursively and computed on a smaller scale; the computed
results are then combined. Dividing the task into smaller tasks is forking, and merging the results from the smaller
tasks is joining.
The Fork/Join framework uses the work-stealing algorithm: when a worker thread completes its work and is free,
it takes (or “steals”) work from other threads that are still busy doing some work. Initially, it will appear to you that
using Fork/Join is a complex task. Once you get familiar with it, however, you’ll realize that it is conceptually easy and
that it significantly simplifies your job. The key is to recursively subdivide the task into smaller chunks that can be
processed by separate threads.

Using the Fork/Join Framework
Let’s ascertain how you can use Fork/Join framework in problem solving. Here are the steps to use the framework:
First, c • heck whether the problem is suitable for the Fork/Join framework or not. Remember:
the Fork/Join framework is not suitable for all kinds of tasks. This framework is suitable if your
problem fits this description:
• The problem can be designed as a recursive task where the task can be subdivided into
smaller units and the results can be combined together.
• The subdivided tasks are independent and can be computed separately without the need
for communication between the tasks when computation is in process. (Of course, after
the computation is over, you will need to join them together.)
• If the problem you want to solve can be modeled recursively, then define a task class that
extends either RecursiveTask or RecursiveAction. If a task returns a result, extend from
RecursiveTask; otherwise extend from RecursiveAction.
• Override the compute() method in the newly defined task class. The compute() method
actually performs the task if the task is small enough to be executed; or split the task into
subtasks and invoke them. The subtasks can be invoked either by invokeAll() or fork()
method (use fork() when the subtask returns a value). Use the join() method to get the
computed results (if you used fork() method earlier).
• Merge the results, if computed from the subtasks.
• Then instantiate ForkJoinPool, create an instance of the task class, and start the execution of
the task using the invoke() method on the ForkJoinPool instance.
• That’s it—you are done.
Now let’s try solving the problem of how to sum 1..N where N is a large number. In Listing 14-16, you subdivided
the sum computation task iteratively into ten sub-ranges; then you computed the sum for each sub-range and then
computed the sum-of-the-partial sums. Alternatively, you can solve this problem rescursively using the Fork/Join
framework


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
*/

// This class illustrates how we can compute sum of 1..N numbers using fork/join framework.
// The range of numbers are divided into half until the range can be handled by a thread.
// Once the range summation completes, the result gets summed up together.

class SumOfNUsingForkJoin {

    private static long N = 1000_000; // one million - we want to compute sum
// from 1 .. one million
    private static final int NUM_THREADS = 10; // number of threads to create for
// distributing the effort
// This is the recursive implementation of the algorithm; inherit from RecursiveTask
// instead of RecursiveAction since we're returning values.

    static class RecursiveSumOfN extends RecursiveTask<Long> {

        long from, to;
// from and to are range of values to sum-up

        public RecursiveSumOfN(long from, long to) {
            this.from = from;
            this.to = to;
        }
// the method performs fork and join to compute the sum.
// if the range of values can be summed by a thread
// (remember that we want to divide the summation task equally among NUM_THREADS)
// then, sum the range of numbers from..to using a simple for loop
// otherwise, fork the range and join the results

        public Long compute() {
            if ((to - from) <= N / NUM_THREADS) {
// the range is something that can be handled by a thread, so do summation
                long localSum = 0;
// add in range 'from' .. 'to' inclusive of the value 'to'
                for (long i = from; i <= to; i++) {
                    localSum += i;
                }
                System.out.printf("\t Summing of value range %d to %d is %d %n",
                        from, to, localSum);
                return localSum;
            } else { // no, the range is big for a thread to handle, so fork the computation
// we find the mid-point value in the range from..to
                long mid = (from + to) / 2;
                System.out.printf("Forking computation into two ranges: "
                        + "%d to %d and %d to %d %n", from, mid, mid, to);
// determine the computation for first half with the range from..mid
                RecursiveSumOfN firstHalf = new RecursiveSumOfN(from, mid);
// now, fork off that task
                firstHalf.fork();
// determine the computation for second half with the range mid+1..to
                RecursiveSumOfN secondHalf = new RecursiveSumOfN(mid + 1, to);
                long resultSecond = secondHalf.compute();
// now, wait for the first half of computing sum to
// complete, once done, add it to the remaining part
                return firstHalf.join() + resultSecond;
            }
        }
    }

    public static void main(String[] args) {
// Create a fork-join pool that consists of NUM_THREADS
        ForkJoinPool pool = new ForkJoinPool(NUM_THREADS);
// submit the computation task to the fork-join pool
        long computedSum = pool.invoke(new RecursiveSumOfN(0, N));
// this is the formula sum for the range 1..N
        long formulaSum = (N * (N + 1)) / 2;
// Compare the computed sum and the formula sum
        System.out.printf("Sum for range 1..%d; computed sum = %d, formula sum = %d %n", N,
                computedSum, formulaSum);
    }
}
