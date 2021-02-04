package model.vendors;

import model.Inventory;
import model.InventoryItem;
import model.Vendor;

// A vendor subtype whose inventory contains an inventory holding weapons items
public class WeaponVendor extends Vendor {

    // NOTE: If non-initial functionality is added to this vendor subclass, the vendor type will need to be written
    //       to the market file along with the rest of the vendor's data

    // Initialize weapon vendor and add items to inventory
    // REQUIRES: initMoney and initBarterPower must each be >= 0
    // MODIFIES: this
    // EFFECTS: provides initial values for the vendor's fields
    public WeaponVendor(String name, Inventory inventory, int initMoney, int initBarterPoints) {
        super(name, inventory, initMoney, initBarterPoints);

        trySetIcon("./data/icons/weaponVendorIcon.bmp");

        inventory.add(new InventoryItem("Sword", "A standard weapon.", 5, 5));
        inventory.add(new InventoryItem("Sword", "A standard weapon.", 5, 5));
        inventory.add(new InventoryItem("Bow", "Standard high tension bow.", 2, 5));
        inventory.add(new InventoryItem("Rifle", "Standard M1 Grand, reliable.", 3, 10));
        inventory.add(new InventoryItem("Brick", "A standard weapon.", 7, 1));
        inventory.add(new InventoryItem("Pen", "Mightier than the sword.", 1, 2));
        inventory.add(new InventoryItem("Lighter", "Comes with plenty of fuel.", 1, 1));
        inventory.add(new InventoryItem("Wet Stone", "Good for keeping sharp.", 5, 5));
        inventory.add(new InventoryItem("Light Armor", "Leather breast plate.", 15, 20));
    }
}
