package persistence;

import model.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MarketSaveSystemTest {
    private MarketSaveSystem marketSaveSystem;
    private JsonReader reader;
    private JsonWriter writer;
    private Market savedMarket;

    @BeforeEach
    public void runBefore() {
        marketSaveSystem = new MarketSaveSystem();
        writer = new JsonWriter(MarketSaveSystem.JSON_STORE);
        reader = new JsonReader(MarketSaveSystem.JSON_STORE);

        try {
            savedMarket = reader.read();
        } catch (IOException e) {
            fail("Unable to read the original saved market");
        }
    }

    @Test
    public void testSave() {
        buildAndSaveMarket();
        restoreMarketSave();
    }

    @Test
    public void testLoadAndGet() {
        buildAndSaveMarket();

        try {
            Market market = marketSaveSystem.loadAndGet();
            checkMarket(market, "A", "B");
        } catch (IOException e) {
            fail("Was unable to load the recently saved market");
        }

        restoreMarketSave();
    }

    // Restores market to save
    // EFFECTS: writes the savedMarket data back into the market save file, erasing any prior entries
    private void restoreMarketSave() {
        try {
            writer.open();
            writer.write(savedMarket);
            writer.close();
        } catch (FileNotFoundException e) {
            fail("Was unable to restore the saved market");
        }
    }

    // Save a new market
    // EFFECTS: builds and saves a new market to the market save file (overwrites save data) and checks if this process
    //          succeeded
    private void buildAndSaveMarket() {
        Market market = buildMarket();

        try {
            marketSaveSystem.save(market);
            Market jsonReadMarket = reader.read();
            checkMarket(jsonReadMarket, "A", "B");
        } catch (FileNotFoundException e) {
            fail("Was unable to save the newly created market to the market save file");
        } catch (IOException e) {
            fail("JsonReader was unable to read the file");
        }
    }

    // Checks given market against given values
    // EFFECTS: checks that the first 2 vendors within market have the same names as those given
    private void checkMarket(Market market, String firstVendorName, String secondVendorName) {
        String firstJsonName = market.getVendor(0).getName();
        String secondJsonName = market.getVendor(1).getName();
        assertEquals(firstVendorName, firstJsonName);
        assertEquals(secondVendorName, secondJsonName);
    }

    // Build a new market
    // EFFECTS: creates and returns a new market with 2 vendors in it
    private Market buildMarket() {
        Market market = new Market();
        market.conscriptVendor("A", "Weapon", 100, 1, 1);
        market.conscriptVendor("B", "Food", 40, 23, 2);
        return market;
    }
}
