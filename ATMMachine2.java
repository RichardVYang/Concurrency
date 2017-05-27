
import java.util.concurrent.locks.*;

/*
In this example, you use a Lock object and pass it to threads to
synchronize them on this Lock object. This program is a simple variation of the program using Semaphores. You simulate accessing an ATM machine, which is a shared resource. Of course, only one
person can use an ATM machine at a time, hence the code for accessing the machine is a critical section.
*/

/*
Another difference between implicit locks and explicit Lock objects is that you can do a “non-blocking attempt”
to acquire locks with Locks. Well, what does “non-blocking attempt” mean here? You get a lock if that lock is available
for locking, or you can back out from requesting the lock using the tryLock() method on a Lock object. Isn’t it
interesting? If you acquire the lock successfully, then you can carry out the task to be carried out in a critical section;
otherwise you execute an alternative action. It is noteworthy that an overloaded version of the tryLock() method
takes the timeout value as an argument so that you can wait to acquire the lock for the specified time.

As you can observe from the output, the machine is accessed by only one person at a time, though there may be
others waiting to access it. In this program, the class ATMMachine creates a Lock object representing an ATM machine.
There are five people waiting to access the machine, which is simulated by creating five instances of the Person class.
The Person class extends the Thread and remembers the Lock object on which it has to acquire and release the lock.
The run() method simply acquires the lock, accesses the shared resource, and releases the lock in a finally
block. The Lock object (machine variable here) ensures that only one thread accesses it at a given point in time. Other
threads block while one thread is accessing the lock.
Note that you may get a different order of people accessing the machine if you try running this program. This is
because the access order depends on how the scheduler in the JVM schedules the threads to run.
*/
// This class simulates a situation where only one ATM machine is available and
// and five people are waiting to access the machine. Since only one person can
// access an ATM machine at a given time, others wait for their turn

class ATMMachine2 {

    public static void main(String[] args) {
// A person can use a machine again, and hence using a "reentrant lock"
        Lock machine = new ReentrantLock();
// list of people waiting to access the machine
        new Person2(machine, "Mickey");
        new Person2(machine, "Donald");
        new Person2(machine, "Tom");
        new Person2(machine, "Jerry");
        new Person2(machine, "Casper");
    }
}
// Each Person is an independent thread; their access to the common resource
// (the ATM machine in this case) needs to be synchronized using a lock

class Person2 extends Thread {

    private Lock machine;

    public Person2(Lock machine, String name) {
        this.machine = machine;
        this.setName(name);
        this.start();
    }

    public void run() {
        try {
            System.out.println(getName() + " waiting to access an ATM machine");
            machine.lock();
            System.out.println(getName() + " is accessing an ATM machine");
            Thread.sleep(1000); // simulate the time required for withdrawing amount
        } catch (InterruptedException ie) {
            System.err.println(ie);
        } finally {
            System.out.println(getName() + " is done using the ATM machine");
            machine.unlock();
        }
    }
}
