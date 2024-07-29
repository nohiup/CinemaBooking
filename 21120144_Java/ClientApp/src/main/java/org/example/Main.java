package org.example;

import org.example.Models.ShowtimeTableModel;
import org.example.UI.ConnectingUI;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Global.showtimeList = new ArrayList<>();
        Global.showtimeTableModel = new ShowtimeTableModel(Global.showtimeList);

        ConnectingUI connectingUI = new ConnectingUI();
        connectingUI.showUI();

    }
}