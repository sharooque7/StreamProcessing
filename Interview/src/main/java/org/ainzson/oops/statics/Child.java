package org.ainzson.oops.statics;

public class Child extends Parent{

    @Override
    public void display() {
        System.out.println("Hello from Child");
    }
    public static void displayStatic() {
        System.out.println("Hello from Child Static");
    }
}
