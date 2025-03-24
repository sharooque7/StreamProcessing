package org.ainzson.oops.shallowDeepCopy;

import org.ainzson.oops.model.Address;
import org.ainzson.oops.model.Person;

import java.io.IOException;

public class ShallowCopy {
    public static void shallowCopy() throws IOException, ClassNotFoundException {
        Address address =  new Address("CBE");
        Person person1 = new Person("Sharooque",address);

//        Person person2 = new Person(person1.getName(),person1.getAddress());
//        Person person2 = (Person) person1.clone();
        Person person2 = person1.deepCopy();

        System.out.println("Before modification:");
        System.out.println(person1.getName() + " - " + person1.getAddress().getCity());
        System.out.println(person2.getName() + " - " + person2.getAddress().getCity());

        person2.getAddress().setCity("San Francisco");
        System.out.println("After modification:");
        System.out.println(person1.getName() + " - " + person1.getAddress().getCity());
        System.out.println(person2.getName() + " - " + person2.getAddress().getCity());
    }
}

