package ui.jframes.gui;

import ui.buttons.ConfirmConscriptionButton;
import ui.jframes.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Objects;

// A GUI where the user can design a vendor to be conscripted
public class ConscriptionGUI extends GUI {
    // UI Design
    private static final int FIELD_WIDTH = 225;
    private static final int FIELD_HEIGHT = 30;

    private JFormattedTextField nameField;
    private JComboBox<String> typeField;
    private JFormattedTextField invMaxWeightField;
    private JFormattedTextField initMoneyField;
    private JFormattedTextField initBPField;

    private final MarketGUI marketGUI;

    // Initialize conscription window
    // MODIFIES: this
    // EFFECTS: sets initial fields
    public ConscriptionGUI(MarketGUI marketGUI) {
        super("Vendor Conscription Form", 325, 375, DISPOSE_ON_CLOSE);

        this.marketGUI = marketGUI;
        setupInteractables();
    }

    // Confirm conscription of a new vendor
    // MODIFIES: this
    // EFFECTS: conscripts a new vendor to the market, through the marketGUI, based on the values of the fields, and
    //          closes this window
    public void doConfirmConscription() {
        String name = nameField.getText();
        String vendorType = Objects.requireNonNull(typeField.getSelectedItem()).toString();
        int invSize = positiveInt(invMaxWeightField.getText());
        int initMoney = positiveInt(initMoneyField.getText());
        int initBP = positiveInt(initBPField.getText());

        marketGUI.doConscriptVendor(name, vendorType, invSize, initMoney, initBP);
        closeWindow();
    }

    // Closes this window
    // MODIFIES: this
    // EFFECTS: closes this window
    private void closeWindow() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    // Makes sure string is positive integer
    // EFFECTS: if the given string is a positive integer, returns that integer, otherwise returns 0
    private int positiveInt(String str) {
        try {
            int i = Integer.parseInt(str);

            return Math.max(i, 0);

        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Setup interactable fields and buttons
    // MODIFIES: this
    // EFFECTS: creates a panel of size FRAME_WIDTH by FRAME_HEIGHT, adds it to this frame, then sets up the fields and
    //          confirm button
    private void setupInteractables() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, getWidth(), getHeight());
        this.add(panel);

        setupFields(panel);
        setupConfirmButton(panel);
    }

    // Setup confirm button
    // MODIFIES: this
    // EFFECTS: initializes the confirm button, and adds it to the given panel
    private void setupConfirmButton(JPanel panel) {
        panel.add(new ConfirmConscriptionButton(this));
    }

    // Setup fields
    // MODIFIES: this
    // EFFECTS: initializes and sets up the fields that will be displayed on this window
    private void setupFields(JPanel panel) {
        nameField = new JFormattedTextField();
        setupField(nameField, "Name:", panel);

        typeField = new JComboBox<>();
        typeField.setEditable(false);

        typeField.addItem("Basic");

        for (int i = 0; i < marketGUI.vendorTypeAmount(); i++) {
            typeField.addItem(marketGUI.getVendorType(i));
        }

        setupField(typeField, "Vendor Type:", panel);

        invMaxWeightField = new JFormattedTextField();
        setupField(invMaxWeightField, "Carry Weight:", panel);

        initMoneyField = new JFormattedTextField();
        setupField(initMoneyField, "Money:", panel);

        initBPField = new JFormattedTextField();
        setupField(initBPField, "Barter Points:", panel);
    }

    // Setup field
    // MODIFIES: this
    // EFFECTS: sets up a field based on the given parameters, adds it and its new label to the given panel
    private void setupField(JComponent field, String fieldText, JPanel panel) {
        JLabel label = new JLabel(fieldText);
        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        panel.add(label);
        panel.add(field);
    }
}
