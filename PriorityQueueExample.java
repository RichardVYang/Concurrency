
import java.util.*;

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
If you run this program, it throws an exception like this:

Exception in thread "Thread-0" java.util.NoSuchElementException
at java.util.AbstractQueue.remove(AbstractQueue.java:117)
at PriorityQueueExample$1.run(QueueExample.java:10)
Successfully added an element to the queue

This output indicates that the first thread attempted removing an element from an empty priority queue, and
hence it results in a NoSuchElementException.
However, consider a slight modification of this program (Listing 14-7) that uses a PriorityBlockingQueue
instead of PriorityQueue.
*/

class PriorityQueueExample {

    public static void main(String[] args) {
        final PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
// spawn a thread that removes an element from the priority queue
        new Thread() {
            public void run() {
// Use remove() method in PriorityQueue to remove the element if available
                System.out.println("The removed element is: " + priorityQueue.remove());
            }
        }.start();
// spawn a thread that inserts an element into the priority queue
        new Thread() {
            public void run() {
// insert Integer value 10 as an entry into the priority queue
                priorityQueue.add(10);
                System.out.println("Successfully added an element to the queue ");
            }
        }.start();
    }
}
