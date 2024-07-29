package org.example.Models;

import java.io.Serializable;

public class BookedUserInformation implements Serializable {
    String name;
    String phoneNumber;
    int[] location; //booked location (seat code)

    public BookedUserInformation(String name, String phoneNumber, int[] location) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int[] getLocation() {
        return location;
    }
}
