package org.example.UI;

import org.example.Global;
import org.example.Models.ShowTime;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.List;

public class ConnectingUI extends JFrame {
    private JLabel title;
    private JPanel topPanel, middlePanel, buttonPanel;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private JButton connectButton;

    private JTextArea logArea;

    public ConnectingUI(){
        setTitle("Client Application");
        setSize(500,400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //custom padding
        EmptyBorder customPadding = new EmptyBorder(5,50,5,50);


        //Top panel
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        //JLabel
        title = new JLabel("CLIENT CONNECT");
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 18));

        //Gbc
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(title, gbc);
        //add
        this.add(topPanel, BorderLayout.NORTH);

        //-------
        //Middle Panel
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());

        GridBagConstraints middleGbc = new GridBagConstraints();



        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2,2));

        JLabel hostLabel = new JLabel("Host: ");
        JTextField hostTF = new JTextField("localhost");
        hostTF.setEditable(false);
        hostLabel.setBorder(customPadding);
        hostTF.setBorder(customPadding);

        JLabel portLabel = new JLabel("Port: ");
        JTextField portTF = new JTextField("8001");
        portLabel.setBorder(customPadding);
        portTF.setBorder(customPadding);

        infoPanel.add(hostLabel);
        infoPanel.add(hostTF);
        infoPanel.add(portLabel);
        infoPanel.add(portTF);

        infoPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Server Info"));

        middleGbc.gridx = 0;
        middleGbc.gridy = 0;
        middleGbc.fill = GridBagConstraints.BOTH;
        middlePanel.add(infoPanel,middleGbc);

        logArea = new JTextArea();
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Connecting Log"));

        middleGbc.gridx = 0;
        middleGbc.gridy = 1;
        middleGbc.weightx = 1;
        middleGbc.weighty = 1;
        middleGbc.fill = GridBagConstraints.BOTH;

        middlePanel.add(logScroll, middleGbc);
        //middlePanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Connect Info"));
        //add
        this.add(middlePanel, BorderLayout.CENTER);

        //------
        //Button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,3));

        connectButton = new JButton("Connect");

        buttonPanel.add(new JLabel());
        buttonPanel.add(connectButton);
        buttonPanel.add(new JLabel());

        this.add(buttonPanel, BorderLayout.SOUTH);
        //------
        //event
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Connect to server, then send request to server to get showtime list
                String host = hostTF.getText();
                int port = Integer.parseInt(portTF.getText());
                if (!connectToServer(host, port)) {
                    return;
                }
                //Start UI
                ShowtimeListUI showtimeListUI = new ShowtimeListUI(ConnectingUI.this);
                showtimeListUI.showUI();

                ConnectingUI.this.setVisible(false);
            }
        });
    }

    public void showUI(){
        this.setVisible(true);
    }

    private boolean connectToServer(String host, int port){
        try {
            if (Global.socket == null && Global.in == null && Global.out == null){
                Global.socket = new Socket(host, port);

                Global.out = new ObjectOutputStream(Global.socket.getOutputStream());
                Global.out.flush();
                Global.in = new ObjectInputStream(Global.socket.getInputStream());
                logArea.append("Connected to server \n");
            }
            else {
                logArea.append("Reconnect to server \n");
            }
            //fetch data
            fetchShowtimeList();
            return true;

        } catch (IOException e){
            logArea.append("Error connecting to server: "+ e.getMessage() + "\n");
            return false;
        }
    }

    private void fetchShowtimeList(){
        try{
            Global.out.writeObject("<-GET DATA->");
            Global.out.flush();

            List<ShowTime> showtimes = (List<ShowTime>) Global.in.readObject();
            Global.showtimeList.clear();
            Global.showtimeList.addAll(showtimes);
        }catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
