package org.example.Models;

import java.io.Serializable;

public class Seat implements Serializable {
    private int seatId;
    private boolean isBooked;
    private BookedUserInformation bookedUserInformation;

    public Seat(int seatId, boolean isBooked, BookedUserInformation bookedUserInformation) {
        this.seatId = seatId;
        this.isBooked = isBooked;
        this.bookedUserInformation = bookedUserInformation;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public BookedUserInformation getBookedUserInformation() {
        return bookedUserInformation;
    }

    public void setBookedUserInformation(BookedUserInformation bookedUserInformation) {
        this.bookedUserInformation = bookedUserInformation;
    }
}
