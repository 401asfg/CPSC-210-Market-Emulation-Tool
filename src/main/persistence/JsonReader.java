package persistence;

import model.Inventory;
import model.InventoryItem;
import model.Market;
import model.Vendor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// CITATION: This class was based heavily on the class of the same name found in the JsonSerializationDemo project
// Represents a reader that reads market from JSON data stored in file
public class JsonReader {
    private final String source;

    // Initialize JsonReader
    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // Gets market from file
    // EFFECTS: reads market from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Market read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMarket(jsonObject);
    }

    // Reads file
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // Parses market from JSONObject
    // EFFECTS: parses market from JSON object and returns it
    private Market parseMarket(JSONObject jsonObject) {
        Market market = new Market();
        addVendors(market, jsonObject);
        return market;
    }

    // Adds vendors to the market from json
    // MODIFIES: market
    // EFFECTS: parses vendors from JSON object and adds them to market
    private void addVendors(Market market, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("vendors");
        for (Object json : jsonArray) {
            JSONObject vendorJson = (JSONObject) json;
            addVendor(market, vendorJson);
        }
    }

    // Adds a vendor to the market from json
    // MODIFIES: market
    // EFFECTS: parses vendor from JSON object and adds it to market
    private void addVendor(Market market, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Inventory inventory = parseInventory(jsonObject.getJSONObject("inventory"));
        int money = jsonObject.getInt("money");
        int barterPoints = jsonObject.getInt("barterPoints");
        String iconDescription = jsonObject.getString("iconDescription");

        Vendor vendor = new Vendor(name, inventory, money, barterPoints);
        vendor.trySetIcon(iconDescription);
        market.conscriptVendor(vendor);
    }

    // Parses inventory from JSONObject
    // EFFECTS: parses inventory from JSON object and returns it
    private Inventory parseInventory(JSONObject jsonObject) {
        Inventory inventory = new Inventory(jsonObject.getInt("maxWeight"));
        addItems(inventory, jsonObject);
        return inventory;
    }

    // Adds items to the inventory from json
    // MODIFIES: inventory
    // EFFECTS: parses items from JSON object and adds them to inventory
    private void addItems(Inventory inventory, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for (Object json : jsonArray) {
            JSONObject itemJson = (JSONObject) json;
            addItem(inventory, itemJson);
        }
    }

    // Adds an item to the inventory from json
    // MODIFIES: inventory
    // EFFECTS: parses item from JSON object and adds it to inventory
    private void addItem(Inventory inventory, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        int weight = jsonObject.getInt("weight");
        int value = jsonObject.getInt("value");
        int price = jsonObject.getInt("price");

        InventoryItem item = new InventoryItem(name, description, weight, value);
        item.setPrice(price);
        inventory.add(item);
    }
}
