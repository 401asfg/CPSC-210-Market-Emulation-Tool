package model;

import org.json.JSONObject;
import persistence.Writable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

// A vendor that has money and an inventory full of items, which the vendor can use in trade with other vendors
public class Vendor implements Writable {
    // NOTE: If non-initial functionality is added to a vendor subclass, the vendor type will need to be written
    //       to the market file along with the rest of the vendor's data

    public static final int BARTER_POINT_TRANSFER = 5;
    public static final int BARTER_PRICE_DIVISOR = 2;
    private static final String DEFAULT_ICON_PATH = "./data/icons/basicVendorIcon.bmp";

    private final String name;
    private final Inventory inventory;
    private int money;
    private int barterPoints;
    private ImageIcon icon;

    // Initialize vendor
    // REQUIRES: initMoney and initBarterPower must each be >= 0
    // MODIFIES: this
    // EFFECTS: provides initial values for the vendor's fields
    public Vendor(String name, Inventory inventory, int initMoney, int initBarterPoints) {
        this.name = name;
        this.inventory = inventory;
        this.money = initMoney;
        this.barterPoints = initBarterPoints;

        trySetIcon(DEFAULT_ICON_PATH);
    }

    // Buy item from other vendor
    // REQUIRES: item must be inside the fromVendor's inventory
    // MODIFIES: this, fromVendor
    // EFFECTS: if this vendor has money >= price of item, and this vendor's inventory can add the item, then item will
    //          move from fromVendor's inventory to this vendor's inventory, money equal to item value will be given
    //          from this vendor to fromVendor, resets the item's price to its value, and returns true, otherwise false
    public boolean buy(Vendor fromVendor, InventoryItem item) {
        if (money >= item.getPrice() && inventory.add(item)) {
            money -= item.getPrice();
            fromVendor.inventory.remove(item);
            fromVendor.money += item.getPrice();

            item.resetPrice();

            return true;
        }

        return false;
    }

    // Barter with other vendor
    // REQUIRES: item must be inside the fromVendor's inventory
    // MODIFIES: this, withVendor, item
    // EFFECTS: if this vendor has barter points >= fromVendor, and the item can be bought, and barter
    //          points >= BARTER_POINT_TRANSFER, then the price of the item is divided by
    //          BARTER_PRICE_DIVISOR (rules of integer division apply), this vendor gives barter points equal to
    //          BARTER_POINT_TRANSFER to fromVendor, then returns true, otherwise false
    public boolean barter(Vendor withVendor, InventoryItem item) {
        if (barterPoints >= withVendor.getBarterPoints() && barterPoints >= 5) {
            int retainedMoney = item.getPrice() - (item.getPrice() / BARTER_PRICE_DIVISOR);
            item.decreasePrice(retainedMoney);

            barterPoints -= BARTER_POINT_TRANSFER;
            withVendor.barterPoints += BARTER_POINT_TRANSFER;

            return true;
        }

        return false;
    }

    // Get item from inventory by index
    // REQUIRES: index must be >= 0 and < size of the inventory
    // EFFECTS: returns the InventoryItem with the given index, that is in the inventory
    public InventoryItem getItemFromInventory(int index) {
        return inventory.getItem(index);
    }

    // Get item from inventory by name
    // REQUIRES: itemName must be the name of an item in the inventory
    // EFFECTS: returns the InventoryItem with the name name, that is in the inventory, otherwise null
    public InventoryItem getItemFromInventory(String itemName) {
        return inventory.getItem(itemName);
    }

    // Get vendor's inventory size
    // EFFECTS: returns the size of the inventory
    public int getInventorySize() {
        return inventory.size();
    }

    // Get vendor's name
    // EFFECTS: returns the name of the vendor
    public String getName() {
        return name;
    }

    // Get vendor's inventory's weight
    // EFFECTS: returns the weight of the all the items in the inventory
    public int getInventoryWeight() {
        return inventory.getWeight();
    }

    // Get vendor's inventory's max carry weight
    // EFFECTS: returns the max carry weight of the inventory
    public int getInventoryMaxWeight() {
        return inventory.getMaxWeight();
    }

    // Get vendor's money
    // EFFECTS: returns the amount of money the vendor has
    public int getMoney() {
        return money;
    }

    // Get vendor's barter points
    // EFFECTS: returns the amount of barter points the vendor has
    public int getBarterPoints() {
        return barterPoints;
    }

    // Set vendor's icon
    // MODIFIES: this
    // EFFECTS: tries to changes the vendor's icon to the icon found at the given filePath
    public void trySetIcon(String filePath) {
        try {
            icon = new ImageIcon(ImageIO.read(new File(filePath)));
            icon.setDescription(filePath);
        } catch (IOException e) {
            icon = null;
        }
    }

    // Get vendor's icon
    // EFFECTS: returns the icon the vendor has
    public ImageIcon getIcon() {
        return icon;
    }

    // Gets the vendor as JSONObject
    // EFFECTS: returns vendor as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", name);
        jsonObject.put("money", money);
        jsonObject.put("barterPoints", barterPoints);
        jsonObject.put("inventory", inventory.toJson());
        jsonObject.put("iconDescription", icon.getDescription());

        return jsonObject;
    }
}
