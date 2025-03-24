package org.ainzson.oops.model;

import lombok.Getter;
import lombok.Setter;

import java.io.*;

@Getter
@Setter
public  class Person  implements Cloneable, Serializable
{
    @Getter
    private String name;
    @Getter
    private Address address;

    public Person() {}

    public Person(String name, Address address) {
        this.name = name;
        this.address = address ;
    }

    private void sayHello(String message) {
        System.out.println(name + " says: " + message);
    }

    @Override
    public Person clone() {
//        Person person = (Person) super.clone();
        //            Person clone = (Person) super.clone();
        return new Person(this.name, (Address) this.address.clone());
    }

    public Person deepCopy() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        ByteArrayInputStream byteArrayInputStream =new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream =  new ObjectInputStream(byteArrayInputStream);
        return (Person) objectInputStream.readObject();
    }
}