package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.WritableCollection;

import java.util.ArrayList;
import java.util.List;

// Represents the inventory of a vendor. Holds all the items that a vendor can use.
public class Inventory implements WritableCollection {
    private final List<InventoryItem> items;
    private final int maxWeight;

    // Initialize inventory
    // REQUIRES: maxWeight must be >= 0
    // MODIFIES: this
    // EFFECTS: new items list is created empty, and provides a value for maxWeight
    public Inventory(int maxWeight) {
        items = new ArrayList<>();
        this.maxWeight = maxWeight;
    }

    // Add item to inventory
    // MODIFIES: this
    // EFFECTS: if adding the InventoryItem item to the inventory will not put the inventory's overall carry weight
    //          over its max carry weight, and InventoryItem item is not already in this inventory, then the item will
    //          be added to the end of the items list, and will return true, otherwise false
    public boolean add(InventoryItem item) {
        if (!contains(item)) {
            if (canHold(item)) {
                items.add(item);
                return true;
            }
        }

        return false;
    }

    // Remove item from inventory
    // REQUIRES: InventoryItem item must be contained inside items list
    // MODIFIES: this
    // EFFECTS: InventoryItem item will be removed from the items list
    public void remove(InventoryItem item) {
        items.remove(item);
    }

    // Get item from inventory by index
    // REQUIRES: index must be >= 0 and < size of items list
    // EFFECTS: returns the item located at the given index of items list
    public InventoryItem getItem(int index) {
        return items.get(index);
    }

    // Get item from inventory by name
    // EFFECTS: if itemName is the name of an InventoryItem in the items list, returns that InventoryItem,
    //          otherwise returns null
    public InventoryItem getItem(String itemName) {
        for (InventoryItem item : items) {
            if (itemName.equals(item.getName())) {
                return item;
            }
        }

        return null;
    }

    // Get inventory weight
    // EFFECTS: gets the combined weight of all the items inside the inventory
    public int getWeight() {
        int totalWeight = 0;

        for (InventoryItem item : items) {
            totalWeight += item.getWeight();
        }

        return totalWeight;
    }

    // Get inventory max carry weight
    // EFFECTS: gets the max weight that the inventory can carry
    public int getMaxWeight() {
        return maxWeight;
    }

    // Check if inventory can hold new item
    // REQUIRES: InventoryItem item must not already be contained within the items list
    // EFFECTS: returns true if the weight of the inventory plus that of InventoryItem item is <= the max carry weight
    // of the inventory
    public boolean canHold(InventoryItem item) {
        return getWeight() + item.getWeight() <= maxWeight;
    }

    // Check if inventory contains item
    // EFFECTS: returns true if the InventoryItem item is located inside the items list, otherwise false
    public boolean contains(InventoryItem item) {
        return items.contains(item);
    }

    // Get number of items in inventory
    // EFFECTS: returns the number of InventoryItems in the items list
    public int size() {
        return items.size();
    }

    // Gets inventory's item array as a JSONArray
    // EFFECTS: returns items in inventory's item array as a JSONArray
    @Override
    public JSONArray arrayToJson() {
        JSONArray jsonArray = new JSONArray();

        for (InventoryItem item : items) {
            jsonArray.put(item.toJson());
        }

        return jsonArray;
    }

    // Gets the inventory as JSONObject
    // EFFECTS: returns inventory as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("maxWeight", maxWeight);
        jsonObject.put("items", arrayToJson());

        return jsonObject;
    }
}
