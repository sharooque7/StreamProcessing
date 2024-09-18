package org.ainzson;

import java.io.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Space ringframe40 = new Space(1,"asset-123", (long) 9.999);

        // Serialize to a file
        FileOutputStream fileOut = new FileOutputStream("ringframe.ser");
        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fileOut));
        out.writeObject(ringframe40);
        out.close();
        fileOut.close();
        System.out.println("Serialized data is saved in ringframe.ser");

        // Deserialize from a file
        FileInputStream fileIn = new FileInputStream("ringframe.ser");
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(fileIn));
        Space deserialized = (Space) in.readObject();
        in.close();
        fileIn.close();

        System.out.println("Deserialized Object: " + deserialized.getSpaceStation());

    }
}