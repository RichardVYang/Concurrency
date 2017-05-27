/*

Some Concurrent Collection Classes in the java.util.concurrent Package
Class/Interface Short Description

1. BlockingQueue This interface extends the Queue interface. In BlockingQueue, if the queue is empty,
it waits (i.e., blocks) for an element to be inserted, and if the queue is full, it waits
for an element to be removed from the queue.

2. ArrayBlockingQueue This class provides a fixed-sized array based implementation of the
BlockingQueue interface.

3. LinkedBlockingQueue This class provides a linked-list-based implementation of the BlockingQueue
interface.

4. DelayQueue This class implements BlockingQueue and consists of elements that are of type
Delayed. An element can be retrieved from this queue only after its delay period.

5. PriorityBlockingQueue Equivalent to java.util.PriorityQueue, but implements the BlockingQueue
interface.

6. SynchronousQueue This class implements BlockingQueue. In this container, each insert() by a thread
waits (blocks) for a corresponding remove() by another thread and vice versa.

7. LinkedBlockingDeque This class implements BlockingDeque where insert and remove operations could
block; uses a linked-list for implementation.

8. ConcurrentHashMap Analogous to Hashtable, but with safe concurrent access and updates.

9. ConcurrentSkipListMap Analogous to TreeMap, but provides safe concurrent access and updates.

10. ConcurrentSkipListSet Analogous to TreeSet, but provides safe concurrent access and updates.

11. CopyOnWriteArrayList Similar to ArrayList, but provides safe concurrent access. When the ArrayList is
updated, it creates a fresh copy of the underlying array.

12. CopyOnWriteArraySet A Set implementation, but provides safe concurrent access and is implemented
using CopyOnWriteArrayList. When the container is updated, it creates a fresh
copy of the underlying array.

*/
// Simple PriorityQueue example. Here, we create two threads in which one thread inserts an element,
// and another thread removes an element from the priority queue.

/*
This program will not result in a crash as in the previous case (Listing 14-6). This is because the take() method
will block until an element gets inserted by another thread; once inserted, the take() method will return that value.
In other words, if you’re using a PriorityQueue object, you need to synchronize the threads such that insertion of an
element always occurs before removing an element. However, in PriorityBlockingQueue, the order does not matter,
and no matter which operation (insertion or removal of an element) is invoked first, the program works correctly. In
this way, concurrent collections provide support for safe use of collections in the context of multiple threads without
the need for you to perform explicit synchronization operations.
*/


// Illustrates the use of PriorityBlockingQueue. In this case, if there is no element available in the priority queue
// the thread calling take() method will block (i.e., wait) until another thread inserts an element
import java.util.concurrent.*;



class PriorityBlockingQueueExample {

    public static void main(String[] args) {
        final PriorityBlockingQueue<Integer> priorityBlockingQueue
                = new PriorityBlockingQueue<>();
        new Thread() {
            public void run() {
                try {
// use take() instead of remove()
// note that take() blocks, whereas remove() doesn't block
                    System.out.println("The removed element is: "
                            + priorityBlockingQueue.take());
                } catch (InterruptedException ie) {
// its safe to ignore this exception
                    ie.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            public void run() {
// add an element with value 10 to the priority queue
                priorityBlockingQueue.add(10);
                System.out.println("Successfully added an element to the queue ");
            }
        }.start();
    }
}
