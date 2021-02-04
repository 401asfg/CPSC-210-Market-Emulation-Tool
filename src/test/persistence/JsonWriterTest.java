package persistence;

import model.Inventory;
import model.InventoryItem;
import model.Market;
import model.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// CITATION: This class was based heavily on the class of the same name found in the JsonSerializationDemo project
public class JsonWriterTest {
    private Market market;

    @BeforeEach
    public void runBefore() {
        market = new Market();
    }

    @Test
    public void testInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testEmptyMarket() {
        try {
            JsonWriter writer = new JsonWriter("./data/saves/testWriterEmptyMarket.json");
            writer.open();
            writer.write(market);
            writer.close();

            JsonReader reader = new JsonReader("./data/saves/testWriterEmptyMarket.json");
            market = reader.read();
            assertEquals(0, market.population());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testGeneralMarket() {
        createMarketVendor("Sam", "Pod", 20);
        createMarketVendor("Jim", "Rock", 1);
        createMarketVendor("Lance", "Cable", 99);

        try {
            JsonWriter writer = new JsonWriter("./data/saves/testWriterGeneralMarket.json");
            writer.open();
            writer.write(market);
            writer.close();

            JsonReader jsonReader = new JsonReader("./data/saves/testWriterGeneralMarket.json");
            Market marketFromJson = jsonReader.read();

            assertEquals(3, marketFromJson.population());

            JsonTest jsonTest = new JsonTest();
            jsonTest.checkVendorFromJson(marketFromJson.getVendor(0), "Sam", 20, "Pod");
            jsonTest.checkVendorFromJson(marketFromJson.getVendor(1), "Jim", 1, "Rock");
            jsonTest.checkVendorFromJson(marketFromJson.getVendor(2), "Lance", 99, "Cable");
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    // Conscripts a new vendor with a new item into the market
    // MODIFIES: this
    // EFFECTS: creates a new vendor, with a new inventory, with a new item, and conscripts the vendor into the market
    private void createMarketVendor(String name, String itemName, int maxWeight) {
        Inventory inventory = new Inventory(maxWeight);
        inventory.add(new InventoryItem(itemName, "", 1, 1));
        Vendor vendor = new Vendor(name, inventory, 1, 1);
        market.conscriptVendor(vendor);
    }
}
