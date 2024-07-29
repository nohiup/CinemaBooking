package org.example;

import org.example.Models.ShowTime;
import org.example.Models.ShowtimeTableModel;

import java.util.List;

public class Global {
    public static volatile List<ShowTime> showtimeList;
    public static volatile ShowtimeTableModel showtimeTableModel;

    public static volatile boolean isServerRunning;


}
