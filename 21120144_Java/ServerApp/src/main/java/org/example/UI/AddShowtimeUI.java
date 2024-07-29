package org.example.UI;

import org.example.Global;
import org.example.Models.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddShowtimeUI extends JFrame {
    private JLabel title;
    private JPanel topPanel, middlePanel, buttonPanel, zoneDetailDynamicPanel;
    private JTextField zoneTF, startHourTF, endHourTF, startMinuteTF, endMinuteTF;
    private List<JTextField> rowTFList, seatTFList, priceTFList;

    private JButton confirmButton, cancelButton;


    public AddShowtimeUI(){
        setTitle("Server Application");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        rowTFList = new ArrayList<>();
        seatTFList = new ArrayList<>();
        priceTFList = new ArrayList<>();

        EmptyBorder contentPadding = new EmptyBorder(5,100,5,100);
        //top
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        //JLabel
        title = new JLabel("ADD NEW SHOWTIME");
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 18));

        //Gbc
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(title, gbc);

        //border
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        //add
        this.add(topPanel, BorderLayout.NORTH);

        //------
        //center
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());

        GridBagConstraints middleGbc = new GridBagConstraints();
        middleGbc.gridx = 0;
        //holder
        JPanel titleHolder = new JPanel();
        titleHolder.setLayout(new GridLayout(3,1));

        JPanel startTimePanel = new JPanel();
        startTimePanel.setLayout(new BoxLayout(startTimePanel, BoxLayout.X_AXIS));

        JLabel startLabel = new JLabel("Start Time: ");
        startHourTF = new JTextField(3);
        startHourTF.setHorizontalAlignment(JTextField.CENTER);

        JLabel startSubLabel = new JLabel(" : ");
        startMinuteTF = new JTextField(3);
        startMinuteTF.setHorizontalAlignment(JTextField.CENTER);


        startTimePanel.add(startLabel);
        startTimePanel.add(startHourTF);
        startTimePanel.add(startSubLabel);
        startTimePanel.add(startMinuteTF);

        startTimePanel.setBorder(contentPadding);

        //add
//        titleHolder.add(startTimePanel);

        middleGbc.gridx = 0;
        middleGbc.gridy = 0;
//        middleGbc.weighty = 0.1;
        middleGbc.fill = GridBagConstraints.BOTH;

        middlePanel.add(startTimePanel, middleGbc);

        //endTimePanel
        JPanel endTimePanel = new JPanel();
        endTimePanel.setLayout(new BoxLayout(endTimePanel, BoxLayout.X_AXIS));

        JLabel endLabel = new JLabel("End Time: ");
        endHourTF = new JTextField(3);
        endHourTF.setHorizontalAlignment(JTextField.CENTER);

        JLabel endSubLabel = new JLabel(" : ");
        endMinuteTF = new JTextField(3);
        endMinuteTF.setHorizontalAlignment(JTextField.CENTER);


        endTimePanel.add(endLabel);
        endTimePanel.add(endHourTF);
        endTimePanel.add(endSubLabel);
        endTimePanel.add(endMinuteTF);

        endTimePanel.setBorder(contentPadding);

//     add   titleHolder.add(endTimePanel);
        middleGbc.gridx = 0;
        middleGbc.gridy = 1;
//        middleGbc.weighty = 0.1;
        middleGbc.fill = GridBagConstraints.BOTH;

        middlePanel.add(endTimePanel, middleGbc);

        //number of zones:
        JPanel zonePanel = new JPanel();
        zonePanel.setLayout(new GridLayout(1,2));

        JLabel zoneTitle = new JLabel("Number of Zones: ");
        zoneTF = new JTextField(20);
        zoneTF.setHorizontalAlignment(JTextField.CENTER);

        zonePanel.add(zoneTitle);
        zonePanel.add(zoneTF);

        zonePanel.setBorder(contentPadding);

//        titleHolder.add(zonePanel);
        middleGbc.gridx = 0;
        middleGbc.gridy = 2;
        middleGbc.fill = GridBagConstraints.BOTH;
//        middleGbc.weighty = 0.1;
        middlePanel.add(zonePanel, middleGbc);

        //zone details panel
        zoneDetailDynamicPanel = new JPanel();
        zoneDetailDynamicPanel.setLayout(new BoxLayout(zoneDetailDynamicPanel, BoxLayout.Y_AXIS));
        zoneDetailDynamicPanel.setBorder(new EmptyBorder(5,5,5,5));
        JScrollPane zoneDetailScrollPane = new JScrollPane(zoneDetailDynamicPanel);

        //gbc layout edit
        middleGbc.gridx = 0;
        middleGbc.gridy = 3;
        middleGbc.weighty = 1;
        middlePanel.add(zoneDetailScrollPane, middleGbc);

        zoneTF.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateDetailField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateDetailField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateDetailField();
            }
        });
        //add
        this.add(middlePanel, BorderLayout.CENTER);

        //--------
        //button Panel
        confirmButton = new JButton("Confirm");
        cancelButton = new JButton("Cancel");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,5));

        buttonPanel.add(new JLabel());
        buttonPanel.add(confirmButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(cancelButton);
        buttonPanel.add(new JLabel());

        //add
        this.add(buttonPanel, BorderLayout.SOUTH);

        //------
        //click event handler
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Global.isServerRunning){
                    JOptionPane.showMessageDialog(AddShowtimeUI.this, "Failed. Cannot add showtime when server is running.");
                    return;
                }
                if (!confirmButtonHandler()) {
                    return;
                }
                AddShowtimeUI.this.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddShowtimeUI.this.dispose();
            }
        });

    }

    public void showUI(){
        this.setVisible(true);
    }

    private void updateDetailField(){
        zoneDetailDynamicPanel.removeAll();

        priceTFList.clear();
        seatTFList.clear();
        rowTFList.clear();

        String text = zoneTF.getText();

        int numZones = 0;
        try {
            numZones = Integer.parseInt(text);
        } catch (NumberFormatException e){
            return;
        }
        System.out.println(numZones);
        //Add input field for each zone:
        for (int i = 1; i<= numZones; i++){
            JPanel detailForm = makeZoneDetailForm(i);
            zoneDetailDynamicPanel.add(detailForm);
        }
        zoneDetailDynamicPanel.revalidate();
        zoneDetailDynamicPanel.repaint();
    }

    private JPanel makeZoneDetailForm(int zoneId){
        JPanel zoneDetailPanel = new JPanel();
        zoneDetailPanel.setLayout(new BoxLayout(zoneDetailPanel, BoxLayout.Y_AXIS));
        //on right
        JPanel zoneInputForm = new JPanel(new GridLayout(3, 2));

        JLabel rowLabel = new JLabel("Number of rows in zone: ");
        JTextField rowTF = new JTextField(20);
        rowTF.setHorizontalAlignment(JTextField.CENTER);
        this.rowTFList.add(rowTF);
        //add
        zoneInputForm.add(rowLabel);
        zoneInputForm.add(rowTF);

        JLabel seatLabel = new JLabel("Number of seats per row:");
        JTextField seatTF = new JTextField(20);
        seatTF.setHorizontalAlignment(JTextField.CENTER);
        this.seatTFList.add(seatTF);
        //add
        zoneInputForm.add(seatLabel);
        zoneInputForm.add(seatTF);

        JLabel priceLabel = new JLabel("Price of seat in zone: ");
        JTextField priceTF = new JTextField(20);
        priceTF.setHorizontalAlignment(JTextField.CENTER);
        this.priceTFList.add(priceTF);
        //add
        zoneInputForm.add(priceLabel);
        zoneInputForm.add(priceTF);
        //add to panel
        zoneDetailPanel.add(zoneInputForm);

        zoneDetailPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLUE, 1),
                "Zone " + zoneId ));
        return zoneDetailPanel;
    }

    private boolean isValidData(JTextField tf){
        String content = tf.getText();
        if (content.isBlank() || content.isEmpty()) {
            return false;
        }

        int num = 0;
        try {
            num = Integer.parseInt(content);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    private boolean confirmButtonHandler(){
        if (!isValidData(startHourTF) || !isValidData(startMinuteTF) || !isValidData(endHourTF)
        || !isValidData(endMinuteTF) || !isValidData(zoneTF)) {
            JOptionPane.showMessageDialog(this, "Invalid input data!", "Event Configuration", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        for (JTextField rowTF: rowTFList) {
            if (!isValidData(rowTF)) {
                JOptionPane.showMessageDialog(this, "Invalid input data!", "Event Configuration", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        for (JTextField seatTF: seatTFList) {
            if (!isValidData(seatTF)) {
                JOptionPane.showMessageDialog(this, "Invalid input data!", "Event Configuration", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        for (JTextField priceTF: priceTFList) {
            if (!isValidData(priceTF)) {
                JOptionPane.showMessageDialog(this, "Invalid input data!", "Event Configuration", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        int startHour = Integer.parseInt(startHourTF.getText());
        int startMinute = Integer.parseInt(startMinuteTF.getText());
        int endHour = Integer.parseInt(endHourTF.getText());
        int endMinute = Integer.parseInt(endMinuteTF.getText());
        int numberOfZone = Integer.parseInt(zoneTF.getText());

        List<Zone> zoneListInShowtime = new ArrayList<>();
        for (int i = 0; i < numberOfZone; i++){
            int numberOfRow = Integer.parseInt(rowTFList.get(i).getText());
            int seatPerRow = Integer.parseInt(seatTFList.get(i).getText());
            int priceOfZone = Integer.parseInt(priceTFList.get(i).getText());

            List<Row> rowListInZone = new ArrayList<>();
            for (int j =0; j < numberOfRow; j++) {
                List<Seat> seatListInRow = new ArrayList<>();
                for (int k = 0; k < seatPerRow; k++){
                    Seat seat = new Seat(k, false, null);
                    seatListInRow.add(seat);
                }
                Row row = new Row(seatListInRow, j);
                rowListInZone.add(row);
            }
            Zone zone = new Zone(rowListInZone, i, priceOfZone);
            zoneListInShowtime.add(zone);
        }
        Stage stage = new Stage(zoneListInShowtime);
        ShowTime showtime = new ShowTime(startHour, startMinute, endHour, endMinute, stage);
        Global.showtimeList.add(showtime);
        Global.showtimeTableModel.updateTable();
        return true;
    }

    private void clearTextField(){
        zoneTF.setText("");
        startHourTF.setText("");
        endHourTF.setText("");
        startMinuteTF.setText("");
        endMinuteTF.setText("");

        seatTFList.clear();
        rowTFList.clear();
        priceTFList.clear();

    }
}
