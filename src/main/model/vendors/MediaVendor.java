package model.vendors;

import model.Inventory;
import model.InventoryItem;
import model.Vendor;

// A vendor subtype whose inventory contains an inventory holding various types of media items
public class MediaVendor extends Vendor {

    // NOTE: If non-initial functionality is added to this vendor subclass, the vendor type will need to be written
    //       to the market file along with the rest of the vendor's data

    // Initialize media vendor and add items to inventory
    // REQUIRES: initMoney and initBarterPower must each be >= 0
    // MODIFIES: this
    // EFFECTS: provides initial values for the vendor's fields
    public MediaVendor(String name, Inventory inventory, int initMoney, int initBarterPoints) {
        super(name, inventory, initMoney, initBarterPoints);

        trySetIcon("./data/icons/mediaVendorIcon.bmp");

        inventory.add(new InventoryItem("Sims 3", "Another simulation game.", 1, 20));
        inventory.add(new InventoryItem("Fallout 76", "10 lbs of patches...", 10, 1));
        inventory.add(new InventoryItem("Adventures of Food Boy", "HSM w/ food.", 2, 50));
        inventory.add(new InventoryItem("Blade Runner", "Noir Sci-Fi movie.", 3, 30));
        inventory.add(new InventoryItem("Dracula", "Could be a book or a movie.", 7, 25));
        inventory.add(new InventoryItem("Dracula", "Could be a book or a movie.", 7, 25));
    }
}
