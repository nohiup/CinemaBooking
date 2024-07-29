package org.example.Models;

import java.io.Serializable;
import java.util.List;

public class Row implements Serializable {
    private List<Seat> seatList;
    private int id;

    public Row(List<Seat> seatList, int id) {
        this.seatList = seatList;
        this.id = id;
    }

    public Seat getSeatById(int id){
        for (Seat seat: seatList){
            if (seat.getSeatId() == id) return seat;
        }
        return null;
    }

    public int getId(){
        return this.id;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }
}
