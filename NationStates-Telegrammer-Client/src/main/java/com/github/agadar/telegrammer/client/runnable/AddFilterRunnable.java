package com.github.agadar.telegrammer.client.runnable;

import com.github.agadar.telegrammer.client.NSTelegramForm;
import com.github.agadar.telegrammer.client.NSTelegramForm.Status;

import com.github.agadar.telegrammer.core.manager.TelegramManager;
import com.github.agadar.telegrammer.core.filter.abstractfilter.Filter;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

/**
 * Runnable used by NSTelegramForm to add a new filter to the TelegramManager.
 *
 * @author Agadar (https://github.com/Agadar/)
 */
public class AddFilterRunnable implements Runnable {

    private final NSTelegramForm Form;
    private final TelegramManager Tm;
    private final Filter F;
    private final String TextForList;

    public AddFilterRunnable(NSTelegramForm form, TelegramManager tm, Filter f, String textForList) {
        this.Form = form;
        this.Tm = tm;
        this.F = f;
        this.TextForList = textForList;
    }

    @Override
    public void run() {
        try {
            // Add filter to telegram manager, then update GUI.
            Tm.addFilter(F);
            SwingUtilities.invokeLater(()
                    -> {
                ((DefaultListModel) Form.JListFilters.getModel()).addElement(TextForList);
                Form.TextAreaOutput.setText(Form.duration());
            });

        } catch (Exception | OutOfMemoryError ex) {
            // If an exception occured, print it to the output textarea.
            SwingUtilities.invokeLater(()
                    -> {
                Form.printToOutput("a fatal error occured:\n" + ex.getMessage(), false);
            });
        } finally {
            // Always re-enable the 'add filter' and 'start sending' buttons.
            SwingUtilities.invokeLater(()
                    -> {
                Form.updateGui(Status.Idle);
            });
        }
    }
}
