package org.example;

import org.example.Models.*;
import org.example.UI.MainUI;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private MainUI serverUI;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Socket socket, MainUI serverUI){
        this.socket = socket;
        this.serverUI = serverUI;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object input = in.readObject();
                System.out.println(input.toString());
                String command = (String) input;
                handleCommand(command);
            }
        } catch (IOException | ClassNotFoundException e) {
            serverUI.log("Client disconnected: " + socket.getInetAddress());
            serverUI.log("Error: " + e.getMessage());
        }
    }

    public void stop(){
        if (socket!=null && !socket.isClosed()){
            try {
                socket.close();
            } catch (IOException e) {
                serverUI.log("Client close failed: " + e.getMessage());
            }
        }
    }

    private void handleCommand(String command) throws IOException {
        if (command.equals("<-GET DATA->")) {
            out.flush();
            out.reset();
            out.writeObject(Global.showtimeList);
            out.flush();
        }
        else if (command.equals("<-BOOK SEAT->")){
            BookedUserInformation bui = null;
            try {
                bui = (BookedUserInformation) in.readObject();
                ShowTime showtime = (ShowTime) in.readObject();
                System.out.println(showtime.getStartHour());

                for (ShowTime showTime: Global.showtimeList){
                    if (showtime.isEqualTo(showTime)) {
                        //Seat is booked before
                        if (this.isBooked(showTime, bui)){
                            sendUpdateCommand(this.out, "failed");
                            break;
                        }

                        //Empty seat
                        int i = Global.showtimeList.indexOf(showTime);
                        serverUI.log("Updating booking at: "+ String.valueOf(i) +"-" + bui.getLocation()[0]
                        + "-" + bui.getLocation()[1] + "-" + bui.getLocation()[2]);
                        //Update showtimeList
                        showTime.updateBookedUser(bui);
                        Global.showtimeList.set(i, showTime);
                        Global.showtimeTableModel.updateTable();

                        sendUpdateCommand(this.out, "success");
                        break;
                    }
                };

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void sendUpdateCommand(ObjectOutputStream out, String message){
        //send update command
        try {
            out.flush();
            out.writeObject("<-UPDATE DATA->:"+message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ObjectOutputStream getOutputStream(){
        return this.out;
    }

    public boolean isBooked(ShowTime showTime, BookedUserInformation bookedUserInformation){
        for (Zone zone: showTime.getStage().getZones()){
            for (Row row: zone.getRowList()){
                for (Seat seat: row.getSeatList()){
                    if (zone.getId() == bookedUserInformation.getLocation()[0]
                    && row.getId() == bookedUserInformation.getLocation()[1]
                    && seat.getSeatId() == bookedUserInformation.getLocation()[2]){
                        return seat.isBooked();
                    }
                }
            }
        }
        return false;
    }
}
