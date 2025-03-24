package org.ainzson.oops.reflection;

import org.ainzson.oops.model.Person;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Period;

public class SimpleReflection {

    public static void triggerReflection() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
//        Get class and initialize
        Class<?> clazz = Class.forName("org.ainzson.oops.model.Person");
// Breaking encapsulation
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        Method nameMethod = clazz.getDeclaredMethod("sayHello", String.class);
        nameMethod.setAccessible(true);
        System.out.println(clazz.getName());


//        Constructor
        Constructor<?> [] constructors =  clazz.getDeclaredConstructors();

        for (Constructor<?> constructor: constructors) {
            System.out.println("Constructor" + constructor);
        }
//
        Constructor<?> constructor = clazz.getConstructor(String.class);
        Object obk = constructor.newInstance("Sharooque");

//        Methods
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Method " + method.getName());
        }

        Method method = clazz.getDeclaredMethod("sayHello", String.class);
        method.setAccessible(true);
        method.invoke(obk,"Sharooque");

    }
}
