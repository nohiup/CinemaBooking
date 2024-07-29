package org.example.Models;

import java.io.Serializable;
import java.util.List;

public class Zone implements Serializable {
    private List<Row> rowList;
    private int id;
    private long price;

    public Zone(List<Row> rowList, int id, long price) {
        this.rowList = rowList;
        this.id = id;
        this.price = price;
    }

    public List<Row> getRowList() {
        return rowList;
    }

    public int getId() {
        return id;
    }

    public long getPrice() {
        return price;
    }
}
