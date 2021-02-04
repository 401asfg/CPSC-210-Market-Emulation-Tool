package model;

import org.json.JSONObject;
import persistence.Writable;

// An item that can be added to a vendor's inventory.
public class InventoryItem implements Writable {
    private final String name;
    private final String description;
    private final int weight;
    private final int value;
    private int price;

    // Initialize inventory item
    // REQUIRES: weight and value must each be >= 0
    // MODIFIES: this
    // EFFECTS: provides values for the fields weight and value
    public InventoryItem(String name, String description, int weight, int value) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.value = value;
        this.price = value;
    }

    // Get item name
    // EFFECTS: gets the name of the item
    public String getName() {
        return name;
    }

    // Get item description
    // EFFECTS: get the description of the item
    public String getDescription() {
        return description;
    }

    // Get item weight
    // EFFECTS: gets the weight of the item
    public int getWeight() {
        return weight;
    }

    // Get item price
    // EFFECTS: gets the price of the item
    public int getPrice() {
        return price;
    }

    // Decrease item price
    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: decreases the price of the item by given amount
    public void decreasePrice(int amount) {
        price -= amount;
    }

    // Reset item price to item value
    // MODIFIES: this
    // EFFECTS: sets the price equal to the value
    public void resetPrice() {
        price = value;
    }

    // Set item price
    // MODIFIES: this
    // EFFECTS: sets the price equal to the given value
    public void setPrice(int price) {
        this.price = price;
    }

    // Gets the item as JSONObject
    // EFFECTS: returns item as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", name);
        jsonObject.put("description", description);
        jsonObject.put("weight", weight);
        jsonObject.put("value", value);
        jsonObject.put("price", price);

        return jsonObject;
    }
}
