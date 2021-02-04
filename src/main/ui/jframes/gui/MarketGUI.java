package ui.jframes.gui;

import persistence.MarketSaveSystem;
import model.Market;
import model.Vendor;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import ui.buttons.marketui.*;
import ui.jframes.GUI;
import ui.jframes.InfoPopup;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// CITATION: Basic structure of the frame-button code was derived from the wikipedia page:
//           https://en.wikipedia.org/wiki/Swing_(Java)
// CITATION: The structure of some inter-method and inter-class relationships, as well as the composition of certain
//           entire methods are based on that found in the SpaceInvaders project provided by UBC's CPSC department
// CITATION: the structure and method composition of this class is heavily based on the DrawingEditor class
//           found in the SimpleDrawingPlayer project, provided by UBC's CPSC department
// Runs the market application's GUI
public class MarketGUI extends GUI {
    // UI Design
    private static final int MARKET_PANEL_HEIGHT = 150;
    private static final Color MARKET_PANEL_COLOR = new Color(45, 45, 45);

    // Sound Paths
    private static final String CONSCRIPTION_SOUND_PATH = "./data/sounds/conscriptionComplete.wav";
    private static final String EXPULSION_SOUND_PATH = "./data/sounds/expulsionComplete.wav";
    private static final String SUCCESS_SOUND_PATH = "./data/sounds/success.wav";
    private static final String ERROR_SOUND_PATH = "./data/sounds/error.wav";

    private Market market;
    private final MarketSaveSystem marketSaveSystem;
    private Vendor selectedVendor;

    private JPanel marketButtonPanel;
    private JPanel vendorButtonPanel;
    private ExpelButton expelButton;
    private ViewButton viewButton;

    // Initialize market gui
    // MODIFIES: this
    // EFFECTS: initializes fields and calls initial methods
    public MarketGUI() {
        super("MET (Market Emulation Tool)", 1024, 768, EXIT_ON_CLOSE);

        market = new Market();
        marketSaveSystem = new MarketSaveSystem();

        createMarketPanel();
        createVendorPanel();
        updateDynamicMarketButtons();
        updateVendorButtons();
    }

    // Begins vendor conscription to the market
    // MODIFIES: this
    // EFFECTS: creates a new conscription window
    public void doBeginConscription() {
        new ConscriptionGUI(this);
    }

    // Conscript new vendor to the market
    // MODIFIES: this
    // EFFECTS: conscript a new vendor to the market, if conscription succeeded, update the vendor buttons, otherwise
    //          create an error popup
    public void doConscriptVendor(String name, String vendorType, int invSize, int initMoney, int initBP) {
        if (market.conscriptVendor(name, vendorType, invSize, initMoney, initBP)) {
            updateVendorButtons();
            playAudio(CONSCRIPTION_SOUND_PATH);
        } else {
            createErrorMsg("Unable to conscript: " + name);
        }
    }

    // Expel vendor from the market
    // REQUIRES: selectedVendor must be contained inside market's list of vendors
    // MODIFIES: this
    // EFFECTS: expels the selectedVendor from the market, and sets the selectedVendor to null
    public void doExpelSelectedVendor() {
        String vendorName = selectedVendor.getName();
        market.expelVendor(selectedVendor);
        updateVendorButtons();
        unselectVendor();
        new InfoPopup("Expulsion Complete!", vendorName + " has been removed from the market.");
        playAudio(EXPULSION_SOUND_PATH);
    }

    // View vendor's data
    // EFFECTS: creates a window that lists all of the selected vendor's data, its inventory data, and the data of the
    //          items in the inventory
    public void doViewSelectedVendor() {
        new ViewGUI(selectedVendor);
    }

    // Saves the market
    // EFFECTS: tries to save the market, creates info popup indicating success or failure
    public void doSaveMarket() {
        try {
            marketSaveSystem.save(market);
            createSuccessMsg("Market Saved to " + MarketSaveSystem.JSON_STORE);
        } catch (FileNotFoundException exception) {
            createErrorMsg("Unable to write to file: " + MarketSaveSystem.JSON_STORE);
        }
    }

    // Loads the market
    // MODIFIES: this
    // EFFECTS: loads the market, creates info popup indicating success or failure
    public void doLoadMarket() {
        try {
            market = marketSaveSystem.loadAndGet();
            updateVendorButtons();
            unselectVendor();
            createSuccessMsg("Market Loaded from " + MarketSaveSystem.JSON_STORE);
        } catch (IOException exception) {
            createErrorMsg("Unable to read from file: " + MarketSaveSystem.JSON_STORE);
        }
    }

    // Selects vendor
    // MODIFIES: this
    // EFFECTS: sets selectedVendor to a vendor of the given name and updates the dynamic market buttons, if such a
    //          vendor is in the market
    public void doSelectVendor(String vendorName) {
        if (market.vendorExists(vendorName)) {
            selectedVendor = market.getVendor(vendorName);
            updateDynamicMarketButtons();
        }
    }

    // Get vendor type
    // EFFECTS: returns vendor type at given index in the market
    public String getVendorType(int index) {
        return market.getVendorType(index);
    }

    // Get amount of vendor types
    // EFFECTS: returns the amount of vendor types in the market
    public int vendorTypeAmount() {
        return market.vendorTypeAmount();
    }

    // Unselects vendor
    // MODIFIES: this
    // EFFECTS: sets selectedVendor to null and removes the dynamic market buttons
    private void unselectVendor() {
        selectedVendor = null;
        removeDynamicMarketButtons();
    }

    // Updates the dynamic market buttons
    // MODIFIES: this
    // EFFECTS: if the selectedVendor contains a vendor, changes the titles of the expel and view buttons to include the
    //          name of the selected vendor, if the buttons aren't in the market button panel, adds them to it,
    //          regardless of the aforementioned conditions, the market panel will always be redrawn
    private void updateDynamicMarketButtons() {
        if (selectedVendor != null) {
            expelButton.setText(ExpelButton.INIT_TEXT + " " + selectedVendor.getName());
            viewButton.setText(ViewButton.INIT_TEXT + " " + selectedVendor.getName());

            boolean hasExpel = false;
            boolean hasView = false;

            for (Component button : marketButtonPanel.getComponents()) {
                if (button.equals(expelButton)) {
                    hasExpel = true;
                } else if (button.equals(viewButton)) {
                    hasView = true;
                }
            }

            if (!hasExpel) {
                marketButtonPanel.add(expelButton);
            }

            if (!hasView) {
                marketButtonPanel.add(viewButton);
            }
        }

        redrawPanel(marketButtonPanel);
    }

    // Removes the dynamic market buttons
    // MODIFIES: this
    // EFFECTS: removes the expel and view buttons from the market button panel
    private void removeDynamicMarketButtons() {
        marketButtonPanel.remove(expelButton);
        marketButtonPanel.remove(viewButton);
        redrawPanel(marketButtonPanel);
    }

    // Updates vendor buttons
    // MODIFIES: this
    // EFFECTS: adds vendor buttons to the vendor panel based on the market's list of vendors
    private void updateVendorButtons() {
        vendorButtonPanel.removeAll();

        for (int i = 0; i < market.population(); i++) {
            vendorButtonPanel.add(new VendorButton(this, market.getVendor(i).getName()));
        }

        redrawPanel(vendorButtonPanel);
    }

    // Creates market panel
    // MODIFIES: this
    // EFFECTS: creates all the market buttons on a panel at the bottom of the screen
    private void createMarketPanel() {
        marketButtonPanel = new JPanel();
        expelButton = new ExpelButton(this);
        viewButton = new ViewButton(this);

        add(marketButtonPanel);
        marketButtonPanel.setBounds(0, getHeight() - MARKET_PANEL_HEIGHT, getWidth(), MARKET_PANEL_HEIGHT);
        marketButtonPanel.setBackground(MARKET_PANEL_COLOR);

        marketButtonPanel.add(new SaveButton(this));
        marketButtonPanel.add(new LoadButton(this));
        marketButtonPanel.add(new ConscriptButton(this));
    }

    // Creates vendor panel
    // MODIFIES: this
    // EFFECTS: creates all the vendor buttons on a panel at the bottom of the screen
    private void createVendorPanel() {
        vendorButtonPanel = new JPanel();
        add(vendorButtonPanel);
        vendorButtonPanel.setBounds(0, 0, getWidth(), getHeight() - MARKET_PANEL_HEIGHT);
    }

    // Redraw the panel
    // MODIFIES: this
    // EFFECTS: redraws the given JPanel
    private void redrawPanel(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }

    // Tries to play audio file
    // MODIFIES: this
    // EFFECTS: tries to play audio file located at given file path
    private void playAudio(String filePath) {
        try {
            AudioPlayer.player.start(new AudioStream(new FileInputStream(filePath)));
        } catch (IOException e) {
            errorInfoPopup("Unable to load audio file: " + filePath);
        }
    }

    // Create success info popup with success sound
    // EFFECTS: creates a new info popup with a title reading: Success! and plays the success sound
    private void createSuccessMsg(String msg) {
        playAudio(SUCCESS_SOUND_PATH);
        new InfoPopup("Success!", msg);
    }

    // Create error info popup with error sound
    // EFFECTS: creates a new info popup with a title reading: Error! and plays the error sound
    private void createErrorMsg(String msg) {
        playAudio(ERROR_SOUND_PATH);
        errorInfoPopup(msg);
    }

    // Create error info popup
    // EFFECTS: creates a new info popup with a title reading: Error!
    private void errorInfoPopup(String msg) {
        new InfoPopup("Error!", msg);
    }
}
