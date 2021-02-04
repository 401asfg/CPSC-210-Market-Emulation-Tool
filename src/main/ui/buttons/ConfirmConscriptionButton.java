package ui.buttons;

import ui.Button;
import ui.jframes.gui.ConscriptionGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// A button that, when pressed, confirms the conscription of a new vendor
public class ConfirmConscriptionButton extends Button {
    private final ConscriptionGUI conscriptionGUI;

    // Initializes conscript button
    // MODIFIES: this
    // EFFECTS: initializes fields
    public ConfirmConscriptionButton(ConscriptionGUI conscriptionGUI) {
        super("Confirm");
        this.conscriptionGUI = conscriptionGUI;
    }

    // Adds action listener to button
    // MODIFIES: this
    // EFFECTS: adds a new action listener to the button, allows button to confirm the conscription of a new vendor
    @Override
    protected void addListener() {
        addActionListener(new ActionListener() {
            // Perform button action
            // MODIFIES: this
            // EFFECTS: performs the buttons action: confirm a the conscription of a new vendor
            @Override
            public void actionPerformed(ActionEvent event) {
                conscriptionGUI.doConfirmConscription();
            }
        });
    }
}
