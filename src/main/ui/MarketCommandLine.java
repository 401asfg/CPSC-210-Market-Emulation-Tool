package ui;

import persistence.MarketSaveSystem;
import model.InventoryItem;
import model.Market;
import model.Vendor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Market emulation tool (runs command line)
// CITATION: the general method structure and flow between them (Constructor -> runX -> init and interpretCommand ->
//           doX) is based on that found in the TellerApp class of the sample Teller project.
// CITATION: the save system implemented in this interface is heavily based upon that found in the JsonSerializationDemo
public class MarketCommandLine {
    private Market market;
    private MarketSaveSystem marketSaveSystem;

    // Run market emulation tool
    // MODIFIES: this
    // EFFECTS: runs the market emulation tool
    public MarketCommandLine() {
        runMarketEmulator();
    }

    // MARKET COMMAND LOOP METHODS:

    // Run market emulation
    // MODIFIES: this
    // EFFECTS: gets the raw user input
    private void runMarketEmulator() {
        init();
        Scanner input = new Scanner(System.in);
        String cmd = "";

        System.out.println("Welcome to MET (Market Emulation Tool)\n");
        printMarketCommands();

        while (!cmd.equals("exit")) {
            cmd = input.nextLine();
            System.out.println();
            interpretMarketCommand(cmd);
        }
    }

    // Initialize class objects
    // MODIFIES: this
    // EFFECTS: initializes market and market command
    private void init() {
        market = new Market();
        marketSaveSystem = new MarketSaveSystem();
    }

    // Interpret market level commands
    // MODIFIES: this
    // EFFECTS: gets the users command and triggers the command's corresponding action
    private void interpretMarketCommand(String command) {
        String cmd = getFirstWord(command);
        String arg = getSecondWord(command);

        if (cmd.equals("conscript")) {
            doConscriptVendor();
        } else if (cmd.equals("expel")) {
            if (vendorExists(arg)) {
                doExpelVendor(market.getVendor(arg));
            }
        } else if (cmd.equals("control")) {
            if (vendorExists(arg)) {
                doControlVendor(market.getVendor(arg));
            }
        } else if (cmd.equals("list")) {
            doPrintVendorList();
        } else if (cmd.equals("exit")) {
            System.out.println("Goodbye");
        } else if (cmd.equals("save")) {
            doSaveMarket();
        } else if (cmd.equals("load")) {
            doLoadMarket();
        } else {
            System.out.println("Unknown command: " + command);
        }
    }

    // MARKET COMMAND METHODS:

    // Execute command to create and conscript a vendor
    // MODIFIES: this
    // EFFECTS: allows the user to fill out a new model.vendors fields, and decides which subtype of vendor to use (or
    //          even the base Vendor class itself). with user's input, constructs new vendor and conscripts it to the
    //          market (cannot create a vendor if it has the same name as an already existing vendor)
    private void doConscriptVendor() {
        Scanner input = new Scanner(System.in);
        System.out.println("Beginning conscription process...\n");

        String name = getPromptedStringInput(input, "Vendor name:");

        StringBuilder vendorTypePrompt = new StringBuilder("\nVendor Type:");

        for (int i = 0; i < market.vendorTypeAmount(); i++) {
            vendorTypePrompt.append("\n - ").append(market.getVendorType(i));
        }

        String vendorType = getPromptedStringInput(input, vendorTypePrompt.toString());
        int invSize = getPromptedPosIntInput(input, "\nInventory Max Weight:");
        int initMoney = getPromptedPosIntInput(input, "\nEnter Initial Money:");
        int initBP = getPromptedPosIntInput(input, "\nEnter Initial Barter Points:");

        if (market.conscriptVendor(name, vendorType, invSize, initMoney, initBP)) {
            System.out.println("\nConscription of " + name + " complete.");
        } else {
            System.out.println("\nVendor already exists, aborting conscription process.");
        }
    }

    // Execute command to expel a vendor
    // MODIFIES: this
    // EFFECTS: removes specified vendor from the market, and provides console feedback
    protected void doExpelVendor(Vendor vendor) {
        System.out.println("Expelling " + vendor.getName() + "...");
        market.expelVendor(vendor);
        System.out.println(vendor.getName() + " has been expelled from the Market.");
    }

    // Execute command to print vendors in market
    // EFFECTS: prints a list of all the vendors in the market to the console
    private void doPrintVendorList() {
        System.out.println("Vendors in the Market:");

        for (int i = 0; i < market.population(); i++) {
            System.out.println(" - " + market.getVendor(i).getName());
        }
    }

    // Save the Market
    // EFFECTS: saves the market to a JSON file
    protected void doSaveMarket() {
        System.out.println("Saving Market...\n");

        try {
            marketSaveSystem.save(market);
            System.out.println("Market Saved to " + MarketSaveSystem.JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + MarketSaveSystem.JSON_STORE);
        }
    }

    // Load the Market
    // EFFECTS: loads the market from a JSON file
    protected void doLoadMarket() {
        System.out.println("Loading Market...\n");

        try {
            market = marketSaveSystem.loadAndGet();
            System.out.println("Market Loaded from " + MarketSaveSystem.JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + MarketSaveSystem.JSON_STORE);
        }
    }

    // VENDOR COMMAND LOOP METHODS:

    // Execute command to control a vendor
    // MODIFIES: this, vendor
    // EFFECTS: allows user to view the vendor's data, view the vendor's inventory, and trade, barter, and browse as the
    //          vendor
    private void doControlVendor(Vendor vendor) {
        System.out.println("Taking control of " + vendor.getName() + "...\n");

        Scanner input = new Scanner(System.in);
        String cmd = "";
        printVendorCommands();

        while (!cmd.equals("exit")) {
            printVendorData(vendor);

            cmd = input.nextLine();
            System.out.println();
            interpretVendorCommand(vendor, cmd);
        }

        printMarketCommands();
    }

    // Interpret vendor level commands
    // MODIFIES: this, vendor
    // EFFECTS: gets the users command and triggers the command's corresponding action
    private void interpretVendorCommand(Vendor vendor, String command) {
        String cmd = getFirstWord(command);
        String arg = getSecondWord(command);

        if (cmd.equals("buy")) {
            if (vendorExists(arg)) {
                doBuy(vendor, market.getVendor(arg));
            }
        } else if (cmd.equals("barter")) {
            if (vendorExists(arg)) {
                doBarter(vendor, market.getVendor(arg));
            }
        } else if (cmd.equals("sell")) {
            if (vendorExists(arg)) {
                doBuy(market.getVendor(arg), vendor);
            }
        } else if (cmd.equals("browse")) {
            if (vendorExists(arg)) {
                doBrowseInventory(market.getVendor(arg));
            }
        } else if (cmd.equals("exit")) {
            System.out.println("Returning to the Market...\n");
        } else {
            System.out.println("Unknown command: " + command + "\n");
        }
    }

    // VENDOR COMMAND METHODS:

    // Execute command to buy item from vendor
    // MODIFIES: buyingVendor, sellingVendor
    // EFFECTS: prints out inventory of selling vendor, allows the user to select an item for purchase, if buying
    //          conditions of vendor were met, buys item, otherwise prints failure message
    private void doBuy(Vendor buyingVendor, Vendor sellingVendor) {
        InventoryItem item = chooseVendorInventoryItem(sellingVendor);

        if (item != null) {
            if (buyingVendor.buy(sellingVendor, item)) {
                String buyVendorName = buyingVendor.getName();
                String sellVendorName = sellingVendor.getName();
                System.out.println(buyVendorName + " bought a " + item.getName() + " from " + sellVendorName + "\n");

                return;
            }

            String failMsg = "Transaction failed (buying vendor had: insufficient funds or overburdened inventory).\n";
            System.out.println(failMsg);
            return;
        }

        System.out.println("Item is not in " + sellingVendor.getName() + "'s inventory.\n");
    }

    // Execute command to barter with vendor over item
    // MODIFIES: thisVendor, otherVendor
    // EFFECTS: prints out inventory of selling vendor, allows the user to select an item for barter, attempts to reduce
    //          the price of the item in exchange for barter points, prints failure message if barter failed
    private void doBarter(Vendor thisVendor, Vendor otherVendor) {
        InventoryItem item = chooseVendorInventoryItem(otherVendor);

        if (item != null) {
            if (thisVendor.barter(otherVendor, item)) {
                System.out.println("Price of " + item.getName() + " reduced to " + item.getPrice() + "\n");
                return;
            }

            System.out.println("Unable to barter the price down.\n");
            return;
        }

        System.out.println("Item is not in " + otherVendor.getName() + "'s inventory.\n");
    }

    // Execute command to browse a vendor's inventory
    // EFFECTS: prints out every item (their name, description, weight, and value) in vendor's inventory
    private void doBrowseInventory(Vendor vendor) {
        String weightGage = "(" + vendor.getInventoryWeight() + " / " + vendor.getInventoryMaxWeight() + ")";
        System.out.println(vendor.getName() + "'s Inventory " + weightGage + ":");

        for (int i = 0; i < vendor.getInventorySize(); i++) {
            InventoryItem item = vendor.getItemFromInventory(i);

            System.out.println(" - " + item.getName());
            System.out.println("    - Description: " + item.getDescription());
            System.out.println("    - Weight: " + item.getWeight());
            System.out.println("    - Price: " + item.getPrice());
        }

        System.out.println();
    }

    // PROMPTED INPUT HELPER METHODS:

    // Give prompt and get string input
    // EFFECTS: prompts user to provide input and returns the user's input as a string
    private String getPromptedStringInput(Scanner input, String prompt) {
        System.out.println(prompt);
        return input.nextLine();
    }

    // Give prompt and get integer input
    // EFFECTS: prompts user to provide input and returns the user's input as an int, if input is a positive int
    private int getPromptedPosIntInput(Scanner input, String prompt) {
        System.out.println(prompt);
        return nextPosInt(input);
    }

    // VENDOR EXISTS HELPER METHODS:

    // Check if vendor exists and print message if doesn't
    // EFFECTS: produces true if vendor is in the market, otherwise false and prints message
    private boolean vendorExists(String vendorName) {
        if (market.vendorExists(vendorName)) {
            return true;
        }

        System.out.println(vendorName + " is not in the Market.\n");
        return false;
    }

    // GENERIC HELPER METHODS:

    // Show vendor inventory and let user select an item from it
    // EFFECTS: prints out model.vendors inventory and allows user to choose an item by its name, if the item is in the
    //          inventory, it is returned, otherwise null is returned
    private InventoryItem chooseVendorInventoryItem(Vendor vendor) {
        doBrowseInventory(vendor);
        Scanner input = new Scanner(System.in);
        String itemName = getPromptedStringInput(input, "Enter Item Name:");

        return vendor.getItemFromInventory(itemName);
    }

    // Get first word in string
    // EFFECTS: returns the first word in the given string
    private String getFirstWord(String string) {
        if (string.contains(" ")) {
            string = string.substring(0, string.indexOf(' ')).trim();
        }

        return string;
    }

    // Get second word in string
    // EFFECTS: returns the second word in the given string
    private String getSecondWord(String string) {
        String arg = "";

        if (string.contains(" ")) {
            arg = string.substring(string.indexOf(' ')).trim();
            arg = getFirstWord(arg);
        }

        return arg;
    }

    // Get integer from input
    // EFFECTS: if the scanner has a next int that's positive, returns that next int, otherwise returns 0
    private int nextPosInt(Scanner input) {
        if (input.hasNextInt()) {
            int nextInt = input.nextInt();

            if (nextInt >= 0) {
                return nextInt;
            }

            System.out.println("Negative value used, parameter set to 0.");
            return 0;
        }

        System.out.println("Letters used instead of numbers, parameter set to 0.");
        return 0;
    }

    // PRINT HELPER METHODS:

    // Display market level commands
    // EFFECTS: prints the applications welcome message and command info
    private void printMarketCommands() {
        System.out.println("MARKET COMMANDS:");
        System.out.println(" - conscript                    Define and add a new Vendor to the Market");
        System.out.println(" - expel <vendor name>          Remove <vendor name> from the Market");
        System.out.println(" - control <vendor name>        Make <vendor name> perform actions");
        System.out.println(" - list                         List all the Vendors in the Market");
        System.out.println(" - save                         Saves the Market as it currently is");
        System.out.println(" - load                         Loads the Market that was most recently saved");
        System.out.println(" - exit                         End application");
    }

    // Display vendor level commands
    // EFFECTS: prints the commands available to the controlled vendor
    private void printVendorCommands() {
        System.out.println("VENDOR COMMANDS:");
        System.out.println(" - buy <vendor name>           Buy an item from <vendor name>");
        System.out.println(" - sell <vendor name>          Sell an item to <vendor name>");
        System.out.println(" - barter <vendor name>        Barter with <vendor name> over an item to reduce its price");
        System.out.println(" - browse <vendor name>        Display the inventory of <vendor name> (CAN GIVE OWN NAME)");
        System.out.println(" - exit                        Return to list of model.vendors\n");
    }

    // Display vendor data
    // EFFECTS: prints the data of the controlled vendor
    private void printVendorData(Vendor vendor) {
        System.out.println("Vendor: " + vendor.getName());
        System.out.println("Inventory Weight: " + vendor.getInventoryWeight() + " / " + vendor.getInventoryMaxWeight());
        System.out.println("Money: " + vendor.getMoney());
        System.out.println("Barter Points: " + vendor.getBarterPoints());
    }
}
