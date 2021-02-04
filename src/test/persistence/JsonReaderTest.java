package persistence;

import model.Market;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// CITATION: This class was based heavily on the class of the same name found in the JsonSerializationDemo project
public class JsonReaderTest {
    @Test
    public void testFileNotFound() {
        JsonReader jsonReader = new JsonReader("./data/saves/notRealFile.json");

        try {
            jsonReader.read();
            fail("Was able to read a file that doesn't exist");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testEmptyMarket() {
        JsonReader jsonReader = new JsonReader("./data/saves/testReaderEmptyMarket.json");

        try {
            Market market = jsonReader.read();
            assertEquals(0, market.population());
        } catch (IOException e) {
            fail("Couldn't find empty market json");
        }
    }

    @Test
    public void testGeneralMarket() {
        JsonReader jsonReader = new JsonReader("./data/saves/testReaderGeneralMarket.json");

        try {
            Market market = jsonReader.read();
            assertEquals(3, market.population());

            JsonTest jsonTest = new JsonTest();
            jsonTest.checkVendorFromJson(market.getVendor(0), "Owen", 100, "Sword");
            jsonTest.checkVendorFromJson(market.getVendor(1), "Jackson", 30, "Sims 3");
            jsonTest.checkVendorFromJson(market.getVendor(2), "Mike", 1000, "Small Rock");
        } catch (IOException e) {
            fail("Couldn't find general market json");
        }
    }
}
