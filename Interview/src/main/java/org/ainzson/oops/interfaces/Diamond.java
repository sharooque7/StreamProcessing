package org.ainzson.oops.interfaces;

public class Diamond implements DiamondA, DiamondB{
    @Override
    public void display() {
//        DiamondB.super.display();
        System.out.println("Diamond");
    }

}
