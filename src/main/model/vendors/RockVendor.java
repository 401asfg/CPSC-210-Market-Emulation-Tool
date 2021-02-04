package model.vendors;

import model.Inventory;
import model.InventoryItem;
import model.Vendor;

// A vendor subtype whose inventory contains an inventory holding rock items
public class RockVendor extends Vendor {

    // NOTE: If non-initial functionality is added to this vendor subclass, the vendor type will need to be written
    //       to the market file along with the rest of the vendor's data

    // Initialize rock vendor and add items to inventory
    // REQUIRES: initMoney and initBarterPower must each be >= 0
    // MODIFIES: this
    // EFFECTS: provides initial values for the vendor's fields
    public RockVendor(String name, Inventory inventory, int initMoney, int initBarterPoints) {
        super(name, inventory, initMoney, initBarterPoints);

        trySetIcon("./data/icons/rockVendorIcon.bmp");

        inventory.add(new InventoryItem("Small Rock", "Can be skipped.", 1, 1));
        inventory.add(new InventoryItem("Boulder", "Massive rock!", 40, 20));
        inventory.add(new InventoryItem("Medium Rock", "Can be carried... maybe.", 20, 5));
        inventory.add(new InventoryItem("Medium Rock", "Can be carried... maybe.", 20, 5));
        inventory.add(new InventoryItem("Small Rock", "Can be skipped.", 1, 1));
        inventory.add(new InventoryItem("Medium Rock", "Can be carried... maybe.", 20, 5));
        inventory.add(new InventoryItem("Small Rock", "Can be skipped.", 1, 1));
        inventory.add(new InventoryItem("Boulder", "Massive rock!", 40, 20));
        inventory.add(new InventoryItem("Boulder", "Massive rock!", 40, 20));
    }
}
