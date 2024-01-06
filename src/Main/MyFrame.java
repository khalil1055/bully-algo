package Main;

import ActionListeners.*;

import javax.swing.*;
import java.awt.*;
import Main.Manager;

public class MyFrame extends JFrame {
    private int width = 640;
    private int height = 600;
    private JTextArea log;

    MyFrame(){

        SpinnerModel numberModel = new SpinnerNumberModel(4, 4, 30, 1);

        JSpinner processesCount = new JSpinner(numberModel);
        processesCount.setBounds(5, 5, 135, 40);

        JButton createButton = new JButton("Create");
        createButton.setBounds(145,5,100,40);

        JLabel errorLabel = new JLabel("Error Label");
        errorLabel.setForeground(new Color(255, 0, 0));
        errorLabel.setBounds(5, 520, 100, 40);
        errorLabel.setVisible(false);

        this.log = new JTextArea("Log");
        this.log.setEditable(false);
        this.log.setLineWrap(true);
        this.log.setWrapStyleWord(true);
        this.log.setFont(new Font("Arial Black", Font.BOLD, 14));

        JScrollPane scrollLog = new JScrollPane(log);
        scrollLog.setBounds(250, 5, 385, 550);

        JComboBox<ComboBoxItem> processes = new JComboBox<>();
        processes.setBounds(5, 50, 240, 40);

        JButton killProcessButton = new JButton("Kill Process");
        killProcessButton.setBounds(5, 95, 240, 40);

        JButton killCoordinatorButton = new JButton("Kill Coordinator");
        killCoordinatorButton.setBounds(5, 140, 240, 40);

        JButton addNewProcessButton = new JButton("Add New Process");
        addNewProcessButton.setBounds(5, 185, 240, 40);

        addNewProcessButton.addActionListener(new AddNewProcessActionListener(processes));
        killProcessButton.addActionListener(new KillProcessActionListener(processes));
        killCoordinatorButton.addActionListener(new KillCoordinatorActionListener(processes));
        createButton.addActionListener(new StartProcessesActionListener(createButton,processesCount, processes));


        this.addWindowListener(
                new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        Manager manager = Manager.getInstance();
                        manager.destroyAllProcesses();
                    }
                });


        add(createButton);
        add(processesCount);
        add(scrollLog);
        add(processes);
        add(killProcessButton);
        add(killCoordinatorButton);
        add(addNewProcessButton);
        add(errorLabel);
        setSize(this.width, this.height);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void addLog(String message){
        this.log.append("\n" + message);
    }
}
