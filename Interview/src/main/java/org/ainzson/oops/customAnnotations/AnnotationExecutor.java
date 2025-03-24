package org.ainzson.oops.customAnnotations;

import java.lang.reflect.Method;

    public class AnnotationExecutor {
        public static void annotationProcessor(Object object) throws Exception {
            Class<?> clazz = object.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(LogExecutionAnnotation.class)) {
                    LogExecutionAnnotation annotation = method.getAnnotation(LogExecutionAnnotation.class);
                    String value = annotation.value();
                    long startTime = System.nanoTime();
                    method.invoke(object);
                    long endTime = System.nanoTime();

                    switch (value) {
                        case "proceed":
                            System.out.println("This is from proceed");
                            break;
                        case "notify":
                            System.out.println("This is based on notify");
                            break;
                        default:
                            System.out.println("This is from default");
                            break;
                    }

                    System.out.println("Execution time of" + method.getName() + " " + startTime + " " + endTime);
                }
            }
        }
    }