package org.example.UI;

import org.example.Global;
import org.example.Models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class BookingUI extends JFrame {
    private JLabel title;
    private JPanel topPanel, middlePanel, buttonPanel;
    private JTextField nameTF, phoneTF, locationTF;
    private JLabel nameLabel, phoneLabel, locationLabel;
    private JButton confirmButton, cancelButton;
    private int[] locationData;
    private ShowTime choseShowtime;

    private ViewShowtimeUI parentScreen;

    public BookingUI(ViewShowtimeUI parentScreen, ShowTime showtime, int[] data){ //0 = zone, 1 = row, 2 = seat

        locationData = new int[3];
        locationData[0] = data[0];
        locationData[1] = data[1];
        locationData[2] = data[2];

        choseShowtime = showtime;
        this.parentScreen = parentScreen;

        setTitle("Client Application");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Top panel
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        title = new JLabel("BOOKING INPUT FORM");
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 18));

        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;

        topPanel.add(title, titleGbc);
        topPanel.setBorder(new EmptyBorder(5,10,5,10));

        //add
        this.add(topPanel, BorderLayout.NORTH);

        //--------
        //Middle Panel
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3,2));

        nameLabel = new JLabel("Name: ");
        phoneLabel = new JLabel("Phone number: ");
        locationLabel = new JLabel("Location: ");

        nameTF = new JTextField();
        phoneTF = new JTextField();
        locationTF = new JTextField("Zone "+ locationData[0] +" - Row "+ locationData[1] +" - Seat "+ locationData[2]);
        locationTF.setEditable(false);

        formPanel.add(nameLabel); formPanel.add(nameTF);
        formPanel.add(phoneLabel); formPanel.add(phoneTF);
        formPanel.add(locationLabel); formPanel.add(locationTF);

        GridBagConstraints middleGbc = new GridBagConstraints();
        middleGbc.gridx = 0;
        middleGbc.gridy = 0;
        middleGbc.anchor = GridBagConstraints.NORTH;
        middleGbc.fill = GridBagConstraints.BOTH;
        middleGbc.weighty = 1;

        formPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Booking Form"));
        middlePanel.add(formPanel, middleGbc);

        //add
        this.add(middlePanel, BorderLayout.CENTER);

        //---------
        //buttonPanel
        confirmButton = new JButton("Confirm");
        cancelButton = new JButton("Cancel");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,5));

        buttonPanel.add(new JLabel());
        buttonPanel.add(confirmButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(cancelButton);
        buttonPanel.add(new JLabel());

        this.add(buttonPanel, BorderLayout.SOUTH);

        //=------------
        //Event handler

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isLegitInfo(nameTF) || !isLegitInfo(phoneTF)){
                    JOptionPane.showMessageDialog(BookingUI.this, "Invalid input data!", "Booking Progress", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                sendBookingData(nameTF.getText(), phoneTF.getText(), locationData);
                try {
                    if (isSuccessResponse()){
                        JOptionPane.showMessageDialog(BookingUI.this, "Done!", "Booking Status", JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(BookingUI.this, "Book failed, someone took the seat first!", "Booking Status", JOptionPane.WARNING_MESSAGE);
                    }
                    BookingUI.this.dispose();
                    parentScreen.dispose();
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookingUI.this.dispose();
            }
        });
    }

    public void showUI(){
        this.setVisible(true);
    }

    private boolean isLegitInfo(JTextField textField){
        if (textField == null) return false;
        return !textField.getText().isEmpty();
    }

    private void sendBookingData(String name, String phone, int[] data){
        BookedUserInformation bookedUserInformation = new BookedUserInformation(name, phone, data);
        try {
            Global.out.flush();
            Global.out.writeObject("<-BOOK SEAT->");
            Global.out.flush();
            Global.out.writeObject(bookedUserInformation);
            Global.out.flush();
            Global.out.writeObject(choseShowtime);
            Global.out.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(BookingUI.this, "Connection lost. The server might be closed","Booking Status", JOptionPane.WARNING_MESSAGE);
            BookingUI.this.dispose();
            this.parentScreen.dispose();
        }
    }

    private boolean isSuccessResponse() throws IOException, ClassNotFoundException {

        Object inputCommand = Global.in.readObject();
        if (inputCommand instanceof String){
            String cmd = (String) inputCommand;
            if (cmd.contains("<-UPDATE DATA->")){

                //Request for update
                Global.out.flush();
                Global.out.writeObject("<-GET DATA->");
                Global.out.flush();

                //update after it
                List<ShowTime> inp = (List<ShowTime>) Global.in.readObject();
                updateTableData(inp);
            }
            return isSuccessCommandStatus(cmd);
        }
        return false;
    }
    private void updateTableData(List<ShowTime> newList){
        Global.showtimeList.clear();
        Global.showtimeList.addAll(newList);
        Global.showtimeTableModel.updateTable();
    }
    private boolean isSuccessCommandStatus(String command){
        String commandStatus = command.split(":")[1];
        return commandStatus.equals("success");
    }
}
