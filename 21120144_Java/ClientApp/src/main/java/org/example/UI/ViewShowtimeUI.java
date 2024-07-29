package org.example.UI;

import org.example.Models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ViewShowtimeUI extends JFrame {
    private JLabel title;
    private JLabel subTitle;
    private JPanel topPanel, middlePanel, buttonPanel;
    private ShowTime showtime;
    private JComboBox<String> zoneChoiceList;
    private Stage stage;
    private List<Zone> zoneList;

    public ViewShowtimeUI(ShowTime showtime){
        this.showtime = showtime;

        setTitle("Client Application");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Top Panel
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        //JLabel
        title = new JLabel("VIEW SHOWTIME");
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 18));

        subTitle = new JLabel();
        subTitle.setText("Showtime: " + showtimeConverter(showtime));
        subTitle.setFont(new Font("Palatino Linotype", Font.PLAIN, 14));

        //Gbc
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(title, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(subTitle, gbc);

        //Add to this
        this.add(topPanel, BorderLayout.NORTH);
        //----------
        //middle:
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());

        stage = this.showtime.getStage();
        zoneList = stage.getZones();
        zoneChoiceList = new JComboBox<>();

        JPanel infoLine = new JPanel();
        infoLine.setLayout(new GridLayout(1,2));

        for (int i =0; i < zoneList.size(); i++){
            zoneChoiceList.addItem("Zone no."+i);
        }

        infoLine.add(new JLabel("Zone: "));
        infoLine.add(zoneChoiceList);

        GridBagConstraints middleGbc = new GridBagConstraints();
        middleGbc.gridx = 0;
        middleGbc.gridy = 0;
        middleGbc.fill = GridBagConstraints.BOTH;

        infoLine.setBorder(new EmptyBorder(5,5,5,5));
        middlePanel.add(infoLine, middleGbc);

        //Zone view
        JPanel zoneView = new JPanel();
        zoneView.setLayout(new BoxLayout(zoneView, BoxLayout.Y_AXIS));
        JScrollPane zoneScroll = new JScrollPane(zoneView);

        middleGbc.gridx = 0;
        middleGbc.gridy = 1;
        middleGbc.weighty = 1;
        middleGbc.weightx = 1;
        middleGbc.fill = GridBagConstraints.BOTH;
        middlePanel.add(zoneScroll, middleGbc);

        //add
        this.add(middlePanel, BorderLayout.CENTER);

        //-------
        //Click handler
        zoneChoiceList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoneView.removeAll();
                String selectedItem = (String) zoneChoiceList.getSelectedItem();
                assert selectedItem != null;
                int zoneId = convertStringToZoneId(selectedItem);
                for (Zone zone: zoneList){
                    if (zone.getId() == zoneId){
                        //Label first
                        JLabel priceTitle = new JLabel("Price: " + zone.getPrice());
                        zoneView.add(priceTitle);
                        zoneView.add(setZoneView(zone));
                    }
                }
                zoneView.revalidate();
                zoneView.repaint();
            }
        });
    }

    public void showUI(){
        this.setVisible(true);
    }

    private int convertStringToZoneId(String string){
        return Integer.parseInt(string.split("\\.")[1]);
    }

    private String showtimeConverter(ShowTime showtime){
        int startHour = showtime.getStartHour();
        int endHour = showtime.getEndHour();
        int startMinute = showtime.getStartMinutes();
        int endMinute = showtime.getEndMinutes();

        return convertNumberToString(startHour) + ":" + convertNumberToString(startMinute) + " - " +
                convertNumberToString(endHour) + ":" + convertNumberToString(endMinute);

    }

    private String convertNumberToString(int number){
        if (number < 10) return '0'+ String.valueOf(number);
        else return String.valueOf(number);
    }

    private JPanel setZoneView(Zone zone){
        JPanel zoneViewSubPanel = new JPanel();
        zoneViewSubPanel.setLayout(new BoxLayout(zoneViewSubPanel, BoxLayout.Y_AXIS));

        List<Row> rowList = zone.getRowList();
        for (Row row: rowList){

            List<Seat> seatList = row.getSeatList();
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new GridLayout(1, seatList.size()));

            for (Seat seat: seatList){
                JButton seatButton = new JButton(row.getId() + " - " + seat.getSeatId());
                if (seat.isBooked()) {
                    seatButton.setBackground(Color.GREEN);
                }
                rowPanel.add(seatButton);

                seatButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (seat.isBooked()){
                            //Show booked user information.
                        }
                        else {
                            int[] data = new int[3];
                            data[0] = zone.getId();
                            data[1] = row.getId();
                            data[2] = seat.getSeatId();
                            //Show booking UI
                            BookingUI bookingUI = new BookingUI(ViewShowtimeUI.this, ViewShowtimeUI.this.showtime, data);
                            bookingUI.showUI();
                        }
                    }
                });
            }
            rowPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Row " + row.getId()));
            zoneViewSubPanel.add(rowPanel);
        }
        return zoneViewSubPanel;
    }


}
