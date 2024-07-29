package org.example;

import org.example.Models.ShowTime;
import org.example.Models.ShowtimeTableModel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Global {
    public static List<ShowTime> showtimeList;
    public static ShowtimeTableModel showtimeTableModel;

    public static Socket socket;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;

}
