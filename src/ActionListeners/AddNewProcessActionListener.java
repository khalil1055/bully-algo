package ActionListeners;

import Main.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddNewProcessActionListener implements ActionListener {
    JComboBox<ComboBoxItem> processesList;

    public AddNewProcessActionListener(JComboBox<ComboBoxItem> processesList) {
        this.processesList = processesList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Long processId = Manager.getInstance().addProcess();
        this.processesList.addItem(new ComboBoxItem(String.valueOf(processId), String.valueOf(processId)));
    }
}
