package ActionListeners;

import Main.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KillProcessActionListener implements ActionListener {
    JComboBox<ComboBoxItem> processesList;

    public KillProcessActionListener(JComboBox<ComboBoxItem> processesList) {
        this.processesList = processesList;
        //inject process manager
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.processesList.getSelectedItem() == null) {
            return;
        }

        Long processId = Long.valueOf(this.processesList.getSelectedItem().toString());

        if (Manager.getInstance().destroyProcess(processId)) {
            this.processesList.removeItemAt(this.processesList.getSelectedIndex());
        }
    }
}
