package model.vendors;

import model.Inventory;
import model.InventoryItem;
import model.Vendor;

// A vendor subtype whose inventory contains an inventory holding food items
public class FoodVendor extends Vendor {

    // NOTE: If non-initial functionality is added to this vendor subclass, the vendor type will need to be written
    //       to the market file along with the rest of the vendor's data

    // Initialize food vendor and add items to inventory
    // REQUIRES: initMoney and initBarterPower must each be >= 0
    // MODIFIES: this
    // EFFECTS: provides initial values for the vendor's fields
    public FoodVendor(String name, Inventory inventory, int initMoney, int initBarterPoints) {
        super(name, inventory, initMoney, initBarterPoints);

        trySetIcon("./data/icons/foodVendorIcon.bmp");

        inventory.add(new InventoryItem("Hamburger", "1 pounder.", 1, 5));
        inventory.add(new InventoryItem("Book", "A good read.", 5, 32));
        inventory.add(new InventoryItem("Spaghetti", "An Italian dish.", 2, 23));
        inventory.add(new InventoryItem("Chocolate", "A product of coco beans", 1, 12));
        inventory.add(new InventoryItem("Apple", "A regular apple.", 1, 5));
        inventory.add(new InventoryItem("Pizza", "Italian dish?", 3, 10));
        inventory.add(new InventoryItem("Ice Cream", "Good on a summer's day.", 1, 3));
        inventory.add(new InventoryItem("Alcohol", "+18 only!", 5, 50));
    }
}
