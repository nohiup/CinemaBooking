package org.example.Models;

import org.example.Global;

import java.io.Serializable;

public class ShowTime implements Serializable {
    int startHour;
    int startMinutes;
    int endHour;
    int endMinutes;

    Stage stage;

    public ShowTime(int startHour, int startMinutes, int endHour, int endMinutes, Stage stage) {
        this.startHour = startHour;
        this.startMinutes = startMinutes;
        this.endHour = endHour;
        this.endMinutes = endMinutes;
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinutes() {
        return startMinutes;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    public void updateBookedUser(BookedUserInformation bui){
        for (Zone zone: stage.getZones()){
            if (zone.getId() == bui.getLocation()[0]){

                for (Row row: zone.getRowList()){
                    if (row.getId() == bui.getLocation()[1]){
                        Seat seat = row.getSeatById(bui.getLocation()[2]);
                        seat.setBookedUserInformation(bui);
                        seat.setBooked(true);

                        //update rowList
                        row.getSeatList().set(seat.getSeatId(), seat);
                        zone.getRowList().set(row.getId(), row);
                        stage.getZones().set(zone.getId(), zone);

                        return;
                    }
                }
            }
        }
    }

    public boolean isEqualTo(ShowTime showTime){
        if (this.startHour != showTime.startHour) return false;
        if (this.endHour != showTime.endHour) return false;
        if (this.startMinutes != showTime.startMinutes) return false;
        if (this.endMinutes != showTime.endMinutes) return false;
        return true;
    }
}
