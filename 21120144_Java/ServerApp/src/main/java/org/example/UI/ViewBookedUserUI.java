package org.example.UI;

import org.example.Models.Seat;
import org.example.Models.ShowTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewBookedUserUI extends JFrame {
    private JLabel title;
    private JPanel topPanel, middlePanel, buttonPanel;
    private JTextField nameTF, phoneTF, locationTF;
    private JLabel nameLabel, phoneLabel, locationLabel;
    private JButton confirmButton, cancelButton;
    private int[] locationData;
    private ShowTime choseShowtime;

    private Seat choseSeat;
    public ViewBookedUserUI(ShowTime showtime, int[] data, Seat seat) { //0 = zone, 1 = row, 2 = seat

        locationData = new int[3];
        locationData[0] = data[0];
        locationData[1] = data[1];
        locationData[2] = data[2];

        choseShowtime = showtime;

        choseSeat = seat;

        setTitle("Server Application");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Top panel
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        title = new JLabel("BOOKED USER DETAIL");
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 18));

        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;

        topPanel.add(title, titleGbc);
        topPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        //add
        this.add(topPanel, BorderLayout.NORTH);

        //--------
        //Middle Panel
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2));

        nameLabel = new JLabel("Name: ");
        phoneLabel = new JLabel("Phone number: ");
        locationLabel = new JLabel("Location: ");

        nameTF = new JTextField(choseSeat.getBookedUserInformation().getName());
        phoneTF = new JTextField(choseSeat.getBookedUserInformation().getPhoneNumber());
        locationTF = new JTextField("Zone " + locationData[0] + " - Row " + locationData[1] + " - Seat " + locationData[2]);
        locationTF.setEditable(false);
        phoneTF.setEditable(false);
        nameTF.setEditable(false);

        formPanel.add(nameLabel);
        formPanel.add(nameTF);
        formPanel.add(phoneLabel);
        formPanel.add(phoneTF);
        formPanel.add(locationLabel);
        formPanel.add(locationTF);

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
        cancelButton = new JButton("Cancel");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));

        buttonPanel.add(new JLabel());
        buttonPanel.add(new JLabel());
        buttonPanel.add(cancelButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(new JLabel());

        this.add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewBookedUserUI.this.dispose();
            }
        });
    }

    public void showUI(){
        this.setVisible(true);
    }
}
