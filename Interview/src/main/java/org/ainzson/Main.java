package org.ainzson;

import lombok.extern.slf4j.Slf4j;
import org.ainzson.oops.io.inputstream.*;
import org.ainzson.oops.statics.Child;
import org.ainzson.oops.statics.Parent;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {
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

        Parent parent = new Child();
        Child child = new Child();
        Parent p = new Child();
        parent.displayStatic();
        p.displayStatic();
        child.displayStatic();
        parent.display();



    }



//    @LogExecutionAnnotation("notify")
//    public static void createConfig() throws InterruptedException {
//        Thread.sleep(1000);
//    }



}

