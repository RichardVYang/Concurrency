/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yang.concurrentcollection.callable;

import java.util.*;
import java.util.concurrent.*;

/*
here’s a complex example. Assume
that your task is to find the sum of numbers from 1 to N where N is a large number (a million in our case). Of course,
you can use the formula [(N * (N + 1)) / 2] to find out the sum. Yes, you’ll make use of this formula to check if the
summation from 1 . . . N is correct or not. However, just for illustration, you’ll divide the range 1 to 1 million to N
sub-ranges and by spawn N threads to sum up numbers in that sub-range


Let’s now analyze how this program works. In this program, you need to find the sum of 1..N where N is one
million (a large number). The class SumCalc implements Callable<Long> to sum the values in the range from to
to. The call() method performs the actual computation of the sum by looping from from to to and returns the
intermediate sum value as a Long value.
In this program, you divide the summation task among multiple threads. You can determine the number of
threads based on the number of cores available in your processor; however, for the sake of keeping the program
simpler, use ten threads.
In the main() method, you create a ThreadPool with ten threads. You are going to create ten summation tasks, so
you need a container to hold the references to those tasks. Use ArrayList to hold the Future<Long> references.
In the first for loop in main(), you create ten tasks and submit them to the ExecutorService. As you submit a
task, you get a Future<Long> reference and you add it to the ArrayList.
Once you’ve created the ten tasks, you traverse the array list in the next for loop to get the results of the tasks. You
sum up the partial results of the individual tasks to compute the final sum.
Once you get the computed sum of values from one to one million, you use the simple formula N * (N + 1)/2
to find the formula sum. From the output, you can see that the computed sum and the formula sum are equal, so you
can ascertain that your logic of dividing the tasks and combining the results of the tasks worked correctly.
Now, before we move on to discuss the fork/join framework, we’ll quickly discuss a few classes that are useful for
concurrent programming.
*/

// We create a class SumOfN that sums the values from 1..N where N is a large number.
// We divide the task
// to sum the numbers to 10 threads (which is an arbitrary limit just for illustration).
// Once computation is complete, we add the results of all the threads,
// and check if the calculation is correct by using the formula (N * (N + 1))/2.

class SumOfN {

    private static long N = 1_000_000L; // one million
    private static long calculatedSum = 0; // value to hold the sum of values in range 1..N
    private static final int NUM_THREADS = 10; // number of threads to create for distributing the effort
// This Callable object sums numbers in range from..to

    static class SumCalc implements Callable<Long> {

        long from, to, localSum = 0;

        public SumCalc(long from, long to) {
            this.from = from;
            this.to = to;
        }

        public Long call() {
// add in range 'from' .. 'to' inclusive of the value 'to'
            for (long i = from; i <= to; i++) {
                localSum += i;
            }
            return localSum;
        }
    }
// In the main method we implement the logic to divide the summation tasks to
// given number of threads and finally check if the calculated sum is correct

    public static void main(String[] args) {
// Divide the task among available fixed number of threads
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
// store the references to the Future objects in a List for summing up together
        List<Future<Long>> summationTasks = new ArrayList<>();
        long nByTen = N / 10; // divide N by 10 so that it can be submitted as 10 tasks
        for (int i = 0; i < NUM_THREADS; i++) {
// create a summation task
// starting from (10 * 0) + 1 .. (N/10 * 1) to (10 * 9) + 1 .. (N/10 * 10)
            long fromInInnerRange = (nByTen * i) + 1;
            long toInInnerRange = nByTen * (i + 1);
            System.out.printf("Spawning thread for summing in range %d to %d %n",
                    fromInInnerRange, toInInnerRange);
// Create a callable object for the given summation range
            Callable<Long> summationTask
                    = new SumCalc(fromInInnerRange, toInInnerRange);
// submit that task to the executor service
            Future<Long> futureSum = executorService.submit(summationTask);
// it will take time to complete, so add it to the list to revisit later
            summationTasks.add(futureSum);
        }
// now, find the sum from each task
        for (Future<Long> partialSum : summationTasks) {
            try {
// the get() method will block (i.e., wait) until the computation is over
                calculatedSum += partialSum.get();
            } catch (CancellationException | ExecutionException | InterruptedException exception) {
// unlikely that you get an exception - exit in case something goes wrong
                exception.printStackTrace();
                System.exit(-1);
            }
        }
// now calculate the sum using formula (N * (N + 1))/2 without doing the hard-work
        long formulaSum = (N * (N + 1)) / 2;
// print the sum using formula and the ones calculated one by one
// they must be equal!
        System.out.printf("Sum by threads = %d, sum using formula = %d",
                calculatedSum, formulaSum);
    }
}
