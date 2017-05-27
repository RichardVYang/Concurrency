
import java.util.concurrent.*;

/*
• Semaphore controls access to one or more shared resources.
• Phaser is used to support a synchronization barrier.
• CountDownLatch allows threads to wait for a countdown to complete.
• Exchanger supports exchanging data between two threads.
• CyclicBarrier enables threads to wait at a predefined execution point.

CountDownLatch
This synchronizer allows one or more threads to wait for a countdown to complete. This countdown could be for a set
of events to happen or until a set of operations being performed in other threads completes

When you create a CountDownLatch, you initialize it with an integer, which represents a count value. Threads
would wait (by calling the await() method) for this count to reach zero. Once zero is reached, all threads are
released; any other calls to await() would return immediately since the count is already zero. The counter value can
be decremented by one by calling the countDown() method. You can get the current value of the counter using the
getCount() method.
*/
// this class simulates the start of a running race by counting down from 5. It holds
// three runner threads to be ready to start in the start line of the race and once the count down
// reaches zero, all the three runners start running...

class RunningRaceStarter {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch counter = new CountDownLatch(5);
// count from 5 to 0 and then start the race
// instantiate three runner threads
        new Runner(counter, "Carl");
        new Runner(counter, "Joe");
        new Runner(counter, "Jack");
        System.out.println("Starting the countdown ");
        long countVal = counter.getCount();
        while (countVal > 0) {
            Thread.sleep(1000); // 1000 milliseconds = 1 second
            System.out.println(countVal);
            if (countVal == 1) {
// once counter.countDown(); in the next statement is called,
// Count down will reach zero; so shout "Start"
                System.out.println("Start");
            }
            counter.countDown(); // count down by 1 for each second
            countVal = counter.getCount();
        }
    }
}
// this Runner class simulates a track runner in a 100-meter dash race. The runner waits until the
// count down timer gets to zero and then starts running

class Runner extends Thread {

    private CountDownLatch timer;

    public Runner(CountDownLatch cdl, String name) {
        timer = cdl;
        this.setName(name);
        System.out.println(this.getName() + " ready and waiting for the count down to start");
        start();
    }

    public void run() {
        try {
// wait for the timer count down to reach 0
            timer.await();
        } catch (InterruptedException ie) {
            System.err.println("interrupted -- can't start running the race");
        }
        System.out.println(this.getName() + " started running");
    }
}
