package org.ainzson;

import lombok.extern.slf4j.Slf4j;
import org.ainzson.oops.Test;
import org.ainzson.oops.collections.iterator.PrimeNumber;
import org.ainzson.oops.collections.lists.Task;
import org.ainzson.oops.collections.lists.TaskManager;
import org.ainzson.oops.collections.queue.*;
import org.ainzson.oops.enums.Day;
import org.ainzson.oops.generics.Pipeline;
import org.ainzson.oops.generics.transform.TransformationStage;
import org.ainzson.oops.generics.validations.ValidationStage;
import org.ainzson.oops.io.inputstream.*;
import org.ainzson.oops.statics.Child;
import org.ainzson.oops.statics.Parent;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.*;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
//        Custom Annotations
//            AnnotationExecutor.annotationProcessor(new Main());

//        Get Class Loader of Class
//            ClassLoader classLoader = Main.class.getClassLoader();
//            System.out.println(classLoader); // Prints which class loader loaded this class


//        Reflection
//        SimpleReflection.triggerReflection();

// Shallow Deepcopy
//        ShallowCopy.shallowCopy();

//        Diamond Problem
//        Diamond diamond = new Diamond();
//        diamond.display();


//        Log Management
//        ReadLogFileInputStream readLogFileInputStream = new ReadLogFileInputStream();
//        readLogFile.ReadLogs();
//        readLogFileInputStream.writeLogs();

//        ReadLogBufferedInputStream readLogBufferedInputStream = new ReadLogBufferedInputStream();
//        readLogBufferedInputStream.readLogs();
//        readLogBufferedInputStream.writeLogs();

//        BinaryReadWriteLogsFiles binaryReadWriteLogsFiles = new BinaryReadWriteLogsFiles();
//        binaryReadWriteLogsFiles.writeLogsToBinary();
//        binaryReadWriteLogsFiles.readLogsFromBinary();

//        ObjectInputStreamReadLog objectInputStreamReadLog = new ObjectInputStreamReadLog();
//        LogEntry log1 = new LogEntry("INFO", "Application started");
//        LogEntry log2 = new LogEntry("INFO", "Application started");
//        List<LogEntry> logs =List.of(log1,log2);
//        objectInputStreamReadLog.wirteLogObject(logs);
//        objectInputStreamReadLog.readLogObject();


//        PipedOutputStream pipedOutputStream = new PipedOutputStream();
//        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
//        LogProducer logProducer = new LogProducer(pipedOutputStream);
//        LogConsumer logConsumer =  new LogConsumer(pipedInputStream);
//
//        logProducer.start();
//        logConsumer.start();

//        SequentialLogMerger sequentialLogMerger = new SequentialLogMerger();
//        sequentialLogMerger.mergeLogs();

//        Parent parent = new Child();
//        Child child = new Child();
//        Parent p = new Child();
//        parent.displayStatic();
//        p.displayStatic();
//        child.displayStatic();
//        parent.display();

//        PrimeNumber primeNumber = new PrimeNumber(1,2,3,4,5,6);
//        Iterator<Integer> iterator = primeNumber.iterator();
//
//        while (iterator.hasNext()) {
//            iterator.next();
//        }
//        primeNumber.displayPrimes();

//        TaskManager manager = new TaskManager();
//        manager.addTask(new Task("Complete Java Project", 1));
//        manager.addTask(new Task("Review PRs", 3));
//        manager.addTask(new Task("Write Documentation", 2));
//
//        manager.viewTasks();
//        manager.completedTask("Review PRs");
//        manager.viewCompletedTasks();

//        PriorityQueue<Customer> queue = new PriorityQueue<>();
//        queue.offer(new Customer("Alice", 2));  // Normal user
//        queue.offer(new Customer("Bob", 1));    // VIP user
//        queue.offer(new Customer("Charlie", 3)); // Normal use
//        while (!queue.isEmpty()) {
//            System.out.println(queue.poll().getName()); // VIPs served first
//        }

//        RateLimiter rateLimiter = new RateLimiter(5, 1000); // 5 requests per second
//        String user = "user123";
//
//        for (int i = 1; i <= 100; i++) {
//            boolean allowed = rateLimiter.allowRequest(user);
//            System.out.println("Request " + i + " allowed: " + allowed);
//            Thread.sleep(200); // Simulate time gap between requests
//        }
//
//        MessageQueue messageQueue = new MessageQueue();
//        messageQueue.start();

//        UndoRedoManager undoRedoManager = new UndoRedoManager();
//        undoRedoManager.performAction("Type Hello");
//        undoRedoManager.performAction("Type World");
//        undoRedoManager.performAction("Delete World");
//
//        undoRedoManager.undo();
//        undoRedoManager.undo();
//        undoRedoManager.redoAction();

//        LRUCache<Integer, String> lru = new LRUCache<>(3);
//
//        lru.put(1, "A");
//        lru.put(2, "B");
//        lru.put(3, "C");
//        lru.displayCache();
//
//        lru.get(1);
//        lru.put(4, "D");
//        lru.displayCache();
//
//        lru.put(5, "E");
//        lru.displayCache();

//        LRUCacheLinkedHashMap<Integer, String> cache = new LRUCacheLinkedHashMap<>(3);
//
//        Object o = new Object();
//        cache.put(1, "A");
//        cache.put(2, "B");
//        cache.put(3, "C");
//        System.out.println(cache); // {1=A, 2=B, 3=C}
//
//        cache.get(1); // Access 1, moves to most recently used
//        cache.put(4, "D"); // Removes LRU (2)
//        System.out.println(cache); // {3=C, 1=A, 4=D}
//
//        cache.put(5, "E"); // Removes LRU (3)
//        System.out.println(cache); // {1=A, 4=D, 5=E}

//        System.out.println(Day.SUNDAY);
//        for(Day day: Day.values()) {
//            System.out.println(day);
//        }
//
//        Day s1 = Day.SUNDAY;
//        Day s2 = Day.MONDAY;
//
//        System.out.println(s1==s2);
//
//        Pipeline<String> stringPipeline = new Pipeline<>();
//        stringPipeline
//                .addStage(new ValidationStage<>())
//                .addStage(new TransformationStage());
//        String result = stringPipeline.execute("hello generics");
//        System.out.println("Processed Output: "+ result);


//    @LogExecutionAnnotation("notify")
//    public static void createConfig() throws InterruptedException {
//        Thread.sleep(1000);
//    }

    }

}

