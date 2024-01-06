package ActionListeners;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import Main.Manager;

public class StartProcessesActionListener implements ActionListener {
    JComboBox<ComboBoxItem> processesList;
    JSpinner processesCountField;
    JButton button;


    public StartProcessesActionListener(JButton button, JSpinner processesCountField, JComboBox<ComboBoxItem> processesList) {
        this.button = button;
        this.processesCountField = processesCountField;
        this.processesList = processesList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int count = (int) this.processesCountField.getValue();

        Manager manager = Manager.getInstance();

        manager.createProcesses(count);
        List<String> process = manager.getProcessNamesList();

        for (int i = 0; i < process.size(); i++) {
            //add process to select box
            this.processesList.addItem(new ComboBoxItem(process.get(i), process.get(i)));
        }

        this.button.setEnabled(false);
    }
}
