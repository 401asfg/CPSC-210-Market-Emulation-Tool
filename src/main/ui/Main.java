package ui;

import ui.jframes.gui.MarketGUI;

// Creates a new Market Emulator, running the program
// CITATION: the general structure of the Market, Vendor, Inventory, and InventoryItem classes and their tests are
//           based on what I have learned in CPSC 210 and the sample projects I have worked with in that class
// CITATION: the code used to save and load the Market's data was heavily modelled after that found in the
//           JsonSerializationDemo provided by UBC's CPSC 210 course
public class Main {
    public static void main(String[] args) {
        //new MarketCommandLine();
        new MarketGUI();
    }
}
