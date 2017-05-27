
import java.util.concurrent.Semaphore;

/*
• Semaphore controls access to one or more shared resources.
• Phaser is used to support a synchronization barrier.
• CountDownLatch allows threads to wait for a countdown to complete.
• Exchanger supports exchanging data between two threads.
• CyclicBarrier enables threads to wait at a predefined execution point.

Semaphore
A semaphore controls access to shared resources. A semaphore maintains a counter to specify the number of
resources that the semaphore controls. Access to the resource is allowed if the counter is greater than zero,while a zero
value of the counter indicates that no resource is available at the moment and so the access is denied.
The methods acquire() and release() are for acquiring and releasing resources from a semaphore. If a thread
calls acquire() and the counter is zero (i.e., resources are unavailable), the thread waits until the counter is non-zero
and then gets the resource for use. Once the thread is done using the resource, it calls release() to increment the
resource availability counter.
Note if the number of resources is 1, then at a given time only one thread can access the resource; in this case,
using the semaphore is similar to using a lock.
*/



// This class simulates a situation where an ATM room has only two ATM machines
// and five people are waiting to access the machine. Since only one person can access
// an ATM machine at a given time, others wait for their turn

class ATMRoom {

    public static void main(String[] args) {
// assume that only two ATM machines are available in the ATM room
        Semaphore machines = new Semaphore(2);
// list of people waiting to access the machine
        new Person(machines, "Mickey");
        new Person(machines, "Donald");
        new Person(machines, "Tom");
        new Person(machines, "Jerry");
        new Person(machines, "Casper");
    }
}
// Each Person is an independent thread; but their access to the common resource
// (two ATM machines in the ATM machine room in this case) needs to be synchronized.

class Person extends Thread {

    private Semaphore machines;

    public Person(Semaphore machines, String name) {
        this.machines = machines;
        this.setName(name);
        this.start();
    }

    public void run() {
        try {
            System.out.println(getName() + " waiting to access an ATM machine");
            machines.acquire();
            System.out.println(getName() + " is accessing an ATM machine");
            Thread.sleep(1000); // simulate the time required for withdrawing amount
            System.out.println(getName() + " is done using the ATM machine");
            machines.release();
        } catch (InterruptedException ie) {
            System.err.println(ie);
        }
    }
}
