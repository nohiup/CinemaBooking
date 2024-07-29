package org.example.UI;

import org.example.Global;
import org.example.Models.ShowTime;
import org.example.Models.ShowtimeTableModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.jar.JarEntry;

public class EventConfigUI extends JFrame {
    private JLabel title;
    private JPanel topPanel, middlePanel, buttonPanel;
    private JTable showTimeTable;
    private JButton addButton, viewButton, saveButton, loadButton;

    private AddShowtimeUI addShowtimeUI;
    private ViewShowtimeUI viewShowtimeUI;

    //public static
    public static List<ShowTime> showTimeListTableData;
    public EventConfigUI(){
        setTitle("Server Application");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //TopPanel
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        //JLabel
        title = new JLabel("EVENT CONFIGURATION");
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

        //------
        //middlePanel
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
        //Button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,7));
        //Button List
        addButton = new JButton("Add");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        viewButton = new JButton("View");

        //add to panel
        buttonPanel.add(addButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(viewButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(saveButton);
        buttonPanel.add(new JLabel());
        buttonPanel.add(loadButton);

        //add
        this.add(buttonPanel, BorderLayout.SOUTH);

        //button action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addShowTime();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewShowtime();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Global.showtimeList.isEmpty()) return;
                saveConfig();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadConfig();
            }
        });
    }

    public void showUI(){
        this.setVisible(true);
    }

    private void addShowTime(){
        if (Global.isServerRunning){
            JOptionPane.showMessageDialog(this, "Cannot add showtime when server is running");
            return;
        }
        addShowtimeUI = new AddShowtimeUI();
        addShowtimeUI.showUI();
    }

    private void viewShowtime(){
        if (showTimeTable == null) return;
        int rowId = showTimeTable.getSelectedRow();
        if (rowId < 0) return;

        ShowTime showtime = Global.showtimeTableModel.getDataAt(rowId);

        viewShowtimeUI = new ViewShowtimeUI(showtime);
        viewShowtimeUI.showUI();
    }

    private void saveConfig(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Event Config");

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION){
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".bin")) {
                fileToSave = new File(fileToSave + ".bin");
            }
            try (ObjectOutputStream binaryOut = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                binaryOut.writeObject(Global.showtimeList);
                JOptionPane.showMessageDialog(this, "Config saved successfully");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Save failed, error: " + e.getMessage());
            }
        }
    }

    private void loadConfig(){
        if (Global.isServerRunning){
            JOptionPane.showMessageDialog(this, "Cannot load config when server is running.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Event Config");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary Files", "bin");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION){
            File fileToOpen = fileChooser.getSelectedFile();
            if (fileToOpen.getAbsolutePath().endsWith(".bin")){
                //Read data to global list
                try (ObjectInputStream binaryIn = new ObjectInputStream(new FileInputStream(fileToOpen))) {
                    List<ShowTime> input = (List<ShowTime>) binaryIn.readObject();

                    if (!input.isEmpty()){
                        Global.showtimeList.clear();
                        Global.showtimeList.addAll(input);
                        Global.showtimeTableModel.updateTable();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Invalid file type. Please select a binary file (.bin)");
            }
        }
    }

}
