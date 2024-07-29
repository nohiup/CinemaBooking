package org.example.Models;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ShowtimeTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Start Time", "End Time", "Number of Seats", "Booked Seats"};
    private List<ShowTime> data;

    public ShowtimeTableModel(List<ShowTime> data){
        System.out.println("Pass");
        this.data = data;
    }

    public List<ShowTime> getList(){
        return this.data;
    }


    @Override
    public int getRowCount() {
        if (data == null) return 0;
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ShowTime showTime = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return convertNumberToString(showTime.startHour)+":"+convertNumberToString(showTime.startMinutes);
            case 1:
                return convertNumberToString(showTime.endHour)+":"+convertNumberToString(showTime.endMinutes);
            case 2:
                return this.getFullSeat(showTime);
            case 3:
                return this.getBookedSeat(showTime);
            default: return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // No edit
    }

    public void updateTable(){
        fireTableDataChanged();
    }

    public ShowTime getDataAt(int rowId){
        return this.data.get(rowId);
    }

    private int getBookedSeat(ShowTime time){
        int bookedSeat = 0;
        Stage stage = time.getStage();
        for (Zone zone: stage.getZones()){
            for (Row row: zone.getRowList()){
                for (Seat seat: row.getSeatList()){
                    if (seat.isBooked()){
                        bookedSeat++;
                    }
                }
            }
        }
        return bookedSeat;
    }

    private int getFullSeat(ShowTime time){
        int count = 0;
        Stage stage = time.getStage();
        for (Zone zone: stage.getZones()){
            for (Row row: zone.getRowList()){
                count += row.getSeatList().size();
            }
        }
        return count;
    }

    private String convertNumberToString(int number){
        if (number < 10) return '0'+ String.valueOf(number);
        else return String.valueOf(number);
    }
}
