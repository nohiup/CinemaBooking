package org.example.UI;

import org.example.Global;
import org.example.Models.ShowTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowtimeListUI extends JFrame {
    private JLabel title;
    private JPanel topPanel, middlePanel, buttonPanel;
    private JTable showTimeTable;
    private JButton viewButton, backButton;
    private ConnectingUI parentUI;
    private ViewShowtimeUI viewShowtimeUI;
    public ShowtimeListUI(ConnectingUI parentUI){

        this.parentUI = parentUI;

        setTitle("Client Application");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //TopPanel
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        //JLabel
        title = new JLabel("SHOWTIME LIST");
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 18));
        JLabel subTitle = new JLabel("Event: T1 fan meeting event");
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

        middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));

        showTimeTable = new JTable();
        showTimeTable.setModel(Global.showtimeTableModel);

        JScrollPane tablePane = new JScrollPane(showTimeTable);
        tablePane.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Time List of Show"));
        middlePanel.add(tablePane);
        //border
        middlePanel.setBorder(new EmptyBorder(10,10,10,10));

        //add
        this.add(middlePanel, BorderLayout.CENTER);

        //------
        //button panel
        buttonPanel = new JPanel(new GridLayout(1,5));

        viewButton = new JButton("View");
        backButton = new JButton("Back");

        buttonPanel.add(new JLabel());
        buttonPanel.add(viewButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(backButton);
        buttonPanel.add(new JLabel());

        this.add(buttonPanel, BorderLayout.SOUTH);
        //-----
        //Event
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showTimeTable == null) return;
                int rowId = showTimeTable.getSelectedRow();
                if (rowId < 0) return;

                ShowTime showtime = Global.showtimeTableModel.getDataAt(rowId);

                viewShowtimeUI = new ViewShowtimeUI(showtime);
                viewShowtimeUI.showUI();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowtimeListUI.this.parentUI.setVisible(true);
                ShowtimeListUI.this.dispose();
            }
        });
    }

    public void showUI(){
        this.setVisible(true);
    }
}
