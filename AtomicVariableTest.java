
import java.util.concurrent.atomic.*;

/*
Let’s try out an example to understand how to use AtomicInteger or AtomicLong. Assume that you have a
counter value that is public and accessible by all threads. How do you update or access this common counter value
safely without introducing the data race problem (discussed in the previous chapter)? Obviously, you can use the
synchronized keyword to ensure that the critical section (the code that modifies the counter value) is accessed by
only one thread at a given point in time. The critical section will be very small, as in
public void run() {
synchronized(SharedCounter.class) {
SharedCounter.count++;
}
}
However, this code is inefficient since it acquires and releases the lock every time just to increment the value of
count. Alternatively, if you declare count as AtomicInteger or AtomicLong (whichever is suitable), then there is no
need to use a lock with synchronized keyword.
*/
// Class to demonstrate how incrementing "normal" (i.e., thread unsafe) integers and incrementing
// "atomic" (i.e., thread safe) integers are different: Incrementing a shared Integer object without locks can result
// in a data race; however, incrementing a shared AtomicInteger will not result in a data race.
class AtomicVariableTest {
// Create two integer objects – one normal and another atomic – with same initial value

    private static Integer integer = new Integer(0);
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    static class IntegerIncrementer extends Thread {

        public void run() {
            System.out.println("Incremented value of integer is: " + ++integer);
        }
    }

    static class AtomicIntegerIncrementer extends Thread {

        public void run() {
            System.out.println("Incremented value of atomic integer is: "
                    + atomicInteger.incrementAndGet());
        }
    }

    public static void main(String[] args) {
// create three threads each for incrementing atomic and "normal" integers
        for (int i = 0; i < 5; i++) {
            new IntegerIncrementer().start();
            new AtomicIntegerIncrementer().start();
        }
    }
}
