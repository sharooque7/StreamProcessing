package org.ainzson.oops.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Address implements Cloneable , Serializable {
    String city;

    public Address(String city) {
        this.city = city;
    }

    @Override
    public Address clone() {
        //           Shallow
//            Address clone = (Address) super.clone();
        Address clone = new Address(this.city);
        return clone;
    }
}
