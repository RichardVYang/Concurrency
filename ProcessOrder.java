
import java.util.concurrent.*;

/*
• Semaphore controls access to one or more shared resources.
• Phaser is used to support a synchronization barrier.
• CountDownLatch allows threads to wait for a countdown to complete.
• Exchanger supports exchanging data between two threads.
• CyclicBarrier enables threads to wait at a predefined execution point.

Phaser
Phaser is a useful feature when few independent threads have to work in phases to complete a task. So, a
synchronization point is needed for the threads to work on a part of a task, wait for others to complete other part of
the task, and do a sync-up before advancing to complete the next part of the task
*/
// ProcessOrder thread is the master thread overlooking to make sure that the Cook, Helper,
// and Attendant are doing their part of the work to complete preparing the food items
// and complete order delivery
// To simplify the logic, we assume that each delivery order consists of exactly three food items

class ProcessOrder {

    public static void main(String[] args) throws InterruptedException {
// the Phaser is the synchronizer to make food items one-by-one,
// and deliver it before moving to the next item
        Phaser deliveryOrder = new Phaser(1);
        System.out.println("Starting to process the delivery order ");
        new Worker(deliveryOrder, "Cook");
        new Worker(deliveryOrder, "Helper");
        new Worker(deliveryOrder, "Attendant");
        for (int i = 1; i <= 3; i++) {
// Prepare, mix and deliver this food item
            deliveryOrder.arriveAndAwaitAdvance();
            System.out.println("Deliver food item no. " + i);
        }
// work completed for this delivery order, so deregister
        deliveryOrder.arriveAndDeregister();
        System.out.println("Delivery order completed... give it to the customer");
    }
}
// The work could be a Cook, Helper, or Attendant. Though the three work independently, the
// should all synchronize their work together to do their part and complete preparing a food item

class Worker extends Thread {

    Phaser deliveryOrder;

    Worker(Phaser order, String name) {
        deliveryOrder = order;
        this.setName(name);
        deliveryOrder.register();
        start();
    }

    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("\t" + getName() + " doing his work for order no. " + i);
            if (i == 3) {
// work completed for this delivery order, so deregister
                deliveryOrder.arriveAndDeregister();
            } else {
                deliveryOrder.arriveAndAwaitAdvance();
            }
            try {
                Thread.sleep(3000); // simulate time for preparing the food item
            } catch (InterruptedException ie) {
                /* ignore exception */
                ie.printStackTrace();
            }
        }
    }
}
