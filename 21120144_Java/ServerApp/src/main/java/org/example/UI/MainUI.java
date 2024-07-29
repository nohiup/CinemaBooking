package org.example.UI;

import org.example.Global;
import org.example.Models.ShowTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.example.ClientHandler;
public class MainUI extends JFrame {
    private JTextArea logArea;
    private JButton startButton, eventConfigButton, stopServerButton;
    private JTextField hostTF, portTF;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private EventConfigUI eventConfigUI;

//    private List<ClientHandler> clients = new ArrayList<>();
//    private EventManager eventManager;
    public MainUI(){
        setTitle("Server Application");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (serverSocket !=null){
                    stopServer();
                }
            }
        });
        clients = new ArrayList<>();

        //topPanel setup
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        //title
        JLabel title = new JLabel("SERVER CONTROLLER");
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 18));
        //Vertical Center alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(title, gbc);
        //border
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        //add
        this.add(topPanel, BorderLayout.NORTH);
        //-----------
        //middle Panel
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridBagLayout());

        //info layout
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2,2, 200, 5));
        //info layout border
        infoPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Server Info"));
        JLabel hostLabel, portLabel;
        hostLabel = new JLabel("Host: ");
        portLabel = new JLabel("Port: ");

        hostTF = new JTextField("localhost");
        portTF = new JTextField("8001");
        hostTF.setEditable(false);

        infoPanel.add(hostLabel);
        infoPanel.add(hostTF);
        infoPanel.add(portLabel);
        infoPanel.add(portTF);

        //Log zone
        logArea = new JTextArea();
        logArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(new EmptyBorder(20,20,20,20));
        scrollPane.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 1), "Server Log"));
        //middleZone gridBagConstraint
        GridBagConstraints middleGbc = new GridBagConstraints();

        //add serverInfo
        middleGbc.gridx = 0;
        middleGbc.gridy = 0;
        middleGbc.fill = GridBagConstraints.BOTH;
        middlePanel.add(infoPanel, middleGbc);

        //add logArea
        middleGbc.gridx = 0;
        middleGbc.gridy = 1;
        middleGbc.weighty = 1;
        middleGbc.weightx = 1;

        middleGbc.fill = GridBagConstraints.BOTH;

        middlePanel.add(scrollPane, middleGbc);
        //add
        this.add(middlePanel, BorderLayout.CENTER);


        //StartButton
        startButton = new JButton("Start Server");
        eventConfigButton = new JButton("Event Config");
        stopServerButton = new JButton("Stop Server");

        //Button Bottom panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();

        //empty
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonPanel.add(new JLabel(), buttonGbc);

        buttonGbc.gridx = 1;
        buttonGbc.gridy = 0;
        buttonPanel.add(startButton, buttonGbc);

        buttonGbc.gridx = 3;
        buttonGbc.gridy = 0;
        buttonPanel.add(new JLabel(), buttonGbc);

        buttonGbc.gridx = 4;
        buttonGbc.gridy = 0;
        buttonPanel.add(eventConfigButton, buttonGbc);

        buttonGbc.gridx = 6;
        buttonGbc.gridy = 0;
        buttonPanel.add(new JLabel(), buttonGbc);

        buttonGbc.gridx = 7;
        buttonGbc.gridy = 0;
        buttonPanel.add(stopServerButton, buttonGbc);

        buttonGbc.gridx = 9;
        buttonGbc.gridy = 0;
        buttonPanel.add(new JLabel(), buttonGbc);
//
//        buttonPanel.add(new JLabel());
//        buttonPanel.add(eventConfigButton);
//        buttonPanel.add(new JLabel());
//        buttonPanel.add(stopServerButton);

        //add
        this.add(buttonPanel, BorderLayout.SOUTH);


//        JLabel label = new JLabel("Event Configuration");
//        label.setFont(new Font("Arial", Font.PLAIN, 18));

        //add(startButton);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        eventConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (eventConfigUI == null){
                    eventConfigUI = new EventConfigUI();
                }
                eventConfigUI.showUI();
            }
        });
        stopServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
    }

    public void showUI(){
        this.setVisible(true);
    }

    private void startServer(){
        int port = Integer.parseInt(portTF.getText());

        Thread openServer = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                Global.isServerRunning = true;
                log("Server started on port " + port);

                while (true){
                    Socket clientSocket = serverSocket.accept();
                    log("Client connected: " + clientSocket.getInetAddress());
                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                }
            }
            catch (IOException e) {
                log("Start server failed: "+ e.getMessage());
            }
        });
        openServer.start();
    }

    private void stopServer(){
        try {
            serverSocket.close();
            Global.isServerRunning = false;
            for (ClientHandler client: clients){
                client.stop();
            }
            log("Server stopped.");
        } catch (IOException e) {
            log("Close failed: " + e.getMessage());
        }
    }
    public synchronized void log(String message) {
        logArea.append(message + "\n");
    }

}
