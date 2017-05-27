
import java.util.concurrent.locks.*;

/*
Conditions
A Condition supports thread notification mechanism. When a certain condition is not satisfied, a thread can wait
for another thread to satisfy that condition; that other thread could notify once the condition is met. A condition is
bound to a lock. A Condition object offers three methods to support wait/notify pattern: await(), signal(), and
signalAll(). These three methods are analogous to the wait(), notify(), and notifyAll() methods supported by
the Object class.

Now let’s look at an example that makes use of Condition objects. Assume that you’re waiting for a person
named Joe to come on train IC1122, which is from Madrid to Paris. When Joe’s train arrives at the station, he informs
you; you pick him up and go home.
Assuming that multiple trains can arrive at a railway station, you need to wait for a specific train to arrive. Once
the train arrives that you’re interested in, you get a “notification” or “signal” from that train. This scenario is a good
candidate for using the wait/notify pattern. There are two ways to implement this pattern. The first option is to use
implicit locks and make use of the wait() and notifyAll() methods in the Object class. The second option—shown
in Listing 14-10—is to use the explicit Lock and Condition objects and use the await() and signalAll() methods in
the Condition object.

Let’s analyze how this program works. In the RailwayStation class you have a common Lock object named
station. From that station object, you obtain a Condition object (remember that a condition is always associated
with a lock) named joeArrival. You used the newCondition() method, so the resulting Condition object is an
interruptible condition; you have not specified any time-out, so the awaiting thread will wait forever until it gets
the signal.
The Train class is a Thread that simulates arrival of a train in the railway station. The run() method in Train first
obtains the lock before announcing that the train has arrived, and it releases before the method exits. Note that if you
call await() on the Condition object without acquiring a lock, you’ll get an IllegalMonitorStateException. In the
run() method, if the Train name is IC1122, it will signal us that Joe has arrived by calling joeArrival.signalAll();.
Your wait in the railway station for Joe is simulated by this WaitForJoe thread. In the run() method, you acquire
the lock and wait for the joeArrival condition to be signaled. Once you are notified (i.e., signaled) that he has arrived,
you pick him up and go home.

In multithreading, a common need is to wait for a condition to be satisfied by one thread before another
thread can proceed. Using polling (i.e., repeatedly checking for a condition using a while loop) is a bad
solution because this solution wastes CPU cycles; further, it is also prone to data races. Use guarded
blocks using wait/notify or await/signal instead.
*/
// This class simulates arrival of trains in a railway station.

class RailwayStation {
// A common lock for synchronization

    private static Lock station = new ReentrantLock();
// Condition to wait or notify the arrival of Joe in the station
    private static Condition joeArrival = station.newCondition();
// Train class simulates arrival of trains independently

    static class Train extends Thread {

        public Train(String name) {
            this.setName(name);
        }

        public void run() {
            station.lock();
            try {
                System.out.println(getName() + ": I've arrived in station ");
                if (getName().startsWith("IC1122")) {
// Joe is coming in train IC1122 - he announces it to us
                    joeArrival.signalAll();
                }
            } finally {
                station.unlock();
            }
        }
    }
// Our wait in the railway station for Joe is simulated by this thread. Once we get notification from Joe // that he has arrived, we pick-him up and go home


    static class WaitForJoe extends Thread {

        public void run() {
            System.out.println("Waiting in the station for IC1122 in which Joe is coming");
            station.lock();
            try {
// await Joe's train arrival
                joeArrival.await();
                // if this statement executes, it means we got a train arrival signal

                System.out.println("Pick up Joe and go home");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } finally {
                station.unlock();
            }
        }
    }
// first create a thread that waits for Joe to arrive and then create new Train threads

    public static void main(String[] args) throws InterruptedException {
// we are waiting before the trains start coming
        new WaitForJoe().start();
// Trains are separate threads - they can arrive in any order
        new Train("IC1234 - Paris to Munich").start();
        new Train("IC2211 - Paris to Madrid").start();
        new Train("IC1122 - Madrid to Paris").start();
        new Train("IC4321 - Munich to Paris").start();
    }
}
