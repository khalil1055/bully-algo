package ActionListeners;

import Main.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KillCoordinatorActionListener implements ActionListener {
    JComboBox<ComboBoxItem> processesList;

    public KillCoordinatorActionListener(JComboBox<ComboBoxItem> processesList) {
        this.processesList = processesList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.processesList.getItemCount() == 0) {
            return;
        }

        Long processId = Long.valueOf(0);

        int ndx = 0;
        for (int i = 0; i < processesList.getItemCount(); i++) {
            if (Long.valueOf(this.processesList.getItemAt(i).toString()) > processId) {
                processId = Long.valueOf(this.processesList.getItemAt(i).toString());
                ndx = i;
            }
        }


        System.out.println(processesList.getItemCount() + "  procc " + processId);
        Manager.getInstance().destroyProcess(processId);
        processesList.removeItemAt(ndx);
    }
}
