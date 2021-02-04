package persistence;

import model.Market;

import java.io.FileNotFoundException;
import java.io.IOException;

// Saves and loads and gets market data to and from a JSON file
public class MarketSaveSystem {
    public static final String JSON_STORE = "./data/saves/market.json";

    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // Initialize market save system
    // MODIFIES: this
    // EFFECTS: initializes fields
    public MarketSaveSystem() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // Save the Market
    // EFFECTS: saves the market to a JSON file
    public void save(Market market) throws FileNotFoundException {
        jsonWriter.open();
        jsonWriter.write(market);
        jsonWriter.close();
    }

    // Load the Market
    // EFFECTS: loads the market from a JSON file and returns it
    public Market loadAndGet() throws IOException {
        return jsonReader.read();
    }
}
