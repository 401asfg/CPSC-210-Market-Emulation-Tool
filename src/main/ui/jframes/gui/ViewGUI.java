package ui.jframes.gui;

import model.InventoryItem;
import model.Vendor;
import ui.jframes.GUI;

import javax.swing.*;
import java.awt.*;

// A GUI where the user can view a vendor's data
public class ViewGUI extends GUI {
    private static final Color VENDOR_DATA_PANEL_COLOR = new Color(196, 196, 196);
    private final Vendor vendor;

    // Initialize view window
    // MODIFIES: this
    // EFFECTS: sets initial fields and calls initial methods
    public ViewGUI(Vendor vendor) {
        super(vendor.getName(), 325, 375, DISPOSE_ON_CLOSE);
        this.vendor = vendor;
        displayVendorData();
    }

    // Displays the given vendor's data
    // EFFECTS: writes out the given vendor's fields, its inventory's fields, and the fields of all the items in that
    //          inventory
    private void displayVendorData() {
        // Setup panel hierarchy
        JPanel mainPanel = new JPanel();
        this.add(mainPanel);
        mainPanel.setBounds(0, 0, getWidth(), getHeight());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel vendorDataPanel = new JPanel();
        mainPanel.add(vendorDataPanel);
        vendorDataPanel.setBackground(VENDOR_DATA_PANEL_COLOR);

        JPanel itemsPanel = new JPanel();

        JScrollPane itemsScrollPane = new JScrollPane(itemsPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(itemsScrollPane);

        // Add data from vendor
        JLabel vendorIconLabel = new JLabel(vendor.getIcon());
        vendorDataPanel.add(vendorIconLabel);

        addNewLabelWithTextToPanel(vendorDataPanel, listVendorData());
        addNewLabelWithTextToPanel(itemsPanel, listInventoryItems());
    }

    // Add a new label with text to panel
    // EFFECTS: creates a new label, adds the given string to the label, then adds the label to the given panel
    private void addNewLabelWithTextToPanel(JPanel panel, String text) {
        JLabel label = new JLabel();
        label.setText(text);
        panel.add(label);
    }

    // Lists vendor's data
    // EFFECTS: returns an html formatted list of the vendor's data
    private String listVendorData() {
        String money = "Money: $" + vendor.getMoney() + "<br>";
        String barterPoints = "Barter Points: " + vendor.getMoney() + "<br><br>";
        int invWeight = vendor.getInventoryWeight();
        int invMaxWeight = vendor.getInventoryMaxWeight();
        String inventory = "Inventory (" + invWeight + "kg / " + invMaxWeight + "kg):<br>";

        return "<html>" + money + barterPoints + inventory + "</html>";
    }

    // Lists vendor's inventory items
    // EFFECTS: returns an html formatted list of the vendor's inventory's items and their data
    private String listInventoryItems() {
        StringBuilder itemData = new StringBuilder("<html>");
        String itemDataIndent = "&nbsp;&nbsp;&nbsp;";

        for (int i = 0; i < vendor.getInventorySize(); i++) {
            InventoryItem item = vendor.getItemFromInventory(i);

            itemData.append(item.getName()).append("<br>");
            itemData.append(itemDataIndent).append("- Description: ").append(item.getDescription()).append("<br>");
            itemData.append(itemDataIndent).append("- Weight: ").append(item.getWeight()).append("kg<br>");
            itemData.append(itemDataIndent).append("- Price: $").append(item.getPrice()).append("<br>");
        }

        itemData.append("</html>");

        return itemData.toString();
    }
}
