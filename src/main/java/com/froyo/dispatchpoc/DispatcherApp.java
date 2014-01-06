package com.froyo.dispatchpoc;

import static org.fusesource.hawtdispatch.Dispatch.createQueue;
import static org.fusesource.hawtdispatch.Dispatch.getGlobalQueue;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.fusesource.hawtdispatch.Dispatch;
import org.fusesource.hawtdispatch.DispatchQueue;
import org.fusesource.hawtdispatch.Metrics;

public class DispatcherApp {

    public DispatcherApp() {
    }

    public void globalSubmitAndRunNow() {

        final CountDownLatch latch = new CountDownLatch(2);
        System.out.println("GlobalQueue: Execution...");
        DispatchQueue queue = getGlobalQueue();
        queue.execute(new Runnable() {

            public void run() {
                System.out.println("Hello World");
                latch.countDown();
            }
        });

        queue.execute(new Runnable() {

            public void run() {
                System.out.println("Go Pigs!");
                latch.countDown();
            }
        });
        
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Global Dispatch Queue - run on any of the available event handling threads 
        
//        System.out.println("Metrics: [");
//        List<Metrics> metrics = Dispatch.metrics();
//        for (Metrics m: metrics) {
//            System.out.println("enqueued: " + m.enqueued + " dequeued: " + m.dequeued);
//        }
//        System.out.println("]");
    }
    
    public void globalSubmitAndRunLater() {
        
        System.out.println("\nGlobalQueue: Submit and run later...");
        final CountDownLatch latch = new CountDownLatch(1);
        DispatchQueue queue = getGlobalQueue();
        queue.executeAfter(2, TimeUnit.SECONDS, new Runnable() {
            
            public void run() {
                System.out.println("Executed boo after 2 seconds");
                latch.countDown();
            }
        });
        
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }
    
    public void serialDispatchExample() {

        System.out.println("\nSerialQueue: dispatch queue example...");
        DispatchQueue queue = createQueue("My queue");
        queue.execute(new Runnable() {

            public void run() {
                System.out.println("Rah!");
            }
        });
    }
    

    public static void main(String[] args) {
        
        DispatcherApp app = new DispatcherApp();
        
        app.globalSubmitAndRunNow();
        
        app.globalSubmitAndRunLater();
        
        app.serialDispatchExample();
        
        // suspend and resume
        
        // custom dispatch source (multiple application events merged to trigger async)
    }

}
