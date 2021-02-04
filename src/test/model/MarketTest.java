package model;

import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MarketTest {
    private Market market;

    @BeforeEach
    public void runBefore() {
        market = new Market();
    }

    @Test
    public void testConscriptVendorByObject() {
        conscriptSam();

        assertEquals(1, market.population());
        assertEquals("Sam", market.getVendor("Sam").getName());
        assertEquals(20, market.getVendor("Sam").getInventoryMaxWeight());
        assertEquals(30, market.getVendor("Sam").getMoney());
        assertEquals(10, market.getVendor("Sam").getBarterPoints());

        conscriptJim();

        assertEquals(2, market.population());
        assertEquals("Jim", market.getVendor("Jim").getName());
        assertEquals(33, market.getVendor("Jim").getInventoryMaxWeight());
        assertEquals(11, market.getVendor("Jim").getMoney());
        assertEquals(20, market.getVendor("Jim").getBarterPoints());
    }

    @Test
    public void testConscriptVendorExceedMaxPop() {
        for(int i = 0; i < Market.MAX_POPULATION; i++) {
            String name = "Vendor " + i;
            market.conscriptVendor(name, "Basic", 1, 1,1);
        }

        assertEquals(Market.MAX_POPULATION, market.population());
        assertNull(conscriptSam());

        market.expelVendor(market.getVendor(Market.MAX_POPULATION - 1));
        assertNotEquals(null, conscriptSam());
        assertNull(conscriptDan());
    }

    @Test
    public void testConscriptVendorByParameters() {
        assertTrue(market.conscriptVendor("Sam", "Food", 20, 30, 10));

        assertEquals(1, market.population());
        assertEquals("Sam", market.getVendor("Sam").getName());
        assertEquals("Hamburger", market.getVendor("Sam").getItemFromInventory(0).getName());

        assertTrue(market.conscriptVendor("Jim", "Media", 5, 2, 11));

        assertEquals(2, market.population());
        assertEquals("Jim", market.getVendor("Jim").getName());
        assertEquals("Sims 3", market.getVendor("Jim").getItemFromInventory(0).getName());

        assertTrue(market.conscriptVendor("Dam", "Rock", 1, 1, 1));

        assertEquals(3, market.population());
        assertEquals("Dam", market.getVendor("Dam").getName());
        assertEquals("Small Rock", market.getVendor("Dam").getItemFromInventory(0).getName());

        assertTrue(market.conscriptVendor("Will", "Weapon", 20, 1, 1));

        assertEquals(4, market.population());
        assertEquals("Will", market.getVendor("Will").getName());
        assertEquals("Sword", market.getVendor("Will").getItemFromInventory(0).getName());

        assertTrue(market.conscriptVendor("Guy", "Basic", 1, 1, 1));

        assertEquals(5, market.population());
        assertEquals("Guy", market.getVendor("Guy").getName());
        assertEquals(0, market.getVendor("Guy").getInventorySize());

        assertFalse(market.conscriptVendor("Guy", "Weapon", 11, 11, 11));
    }

    @Test
    public void testConscriptSameNamedVendor() {
        conscriptSam();

        assertEquals(1, market.population());
        assertEquals("Sam", market.getVendor("Sam").getName());
        assertEquals(20, market.getVendor("Sam").getInventoryMaxWeight());

        assertNull(market.conscriptVendor(new Vendor("Sam", new Inventory(1), 1, 1)));
        assertEquals(1, market.population());
        assertEquals(20, market.getVendor("Sam").getInventoryMaxWeight());
    }

    @Test
    public void testExpelVendor() {
        Vendor sam = conscriptSam();
        conscriptJim();
        Vendor dan = conscriptDan();

        market.expelVendor(market.getVendor("Jim"));
        assertEquals(2, market.population());
        assertEquals(sam, market.getVendor("Sam"));
        assertEquals(dan, market.getVendor("Dan"));

        market.expelVendor(market.getVendor("Sam"));
        assertEquals(1, market.population());
        assertEquals(dan, market.getVendor("Dan"));

        market.expelVendor(market.getVendor("Dan"));
        assertEquals(0, market.population());
    }

    @Test
    public void testGetItemByString() {
        // Not in market
        assertNull(market.getVendor("Sam"));
        Vendor sam = conscriptSam();
        assertNull(market.getVendor("Jim"));

        // In market
        Vendor jim = conscriptJim();
        assertEquals(jim, market.getVendor("Jim"));
        assertEquals(sam, market.getVendor("Sam"));
    }

    @Test
    public void testGetItemByIndex() {
        Vendor sam = conscriptSam();
        Vendor jim = conscriptJim();
        assertEquals(jim, market.getVendor(1));
        assertEquals(sam, market.getVendor(0));
    }

    @Test
    public void testVendorTypeAmount() {
        assertEquals(4, market.vendorTypeAmount());
    }

    @Test
    public void testToJson() {
        conscriptDan();
        conscriptJim();
        conscriptSam();

        JSONArray jsonVendorsArray = market.toJson().getJSONArray("vendors");
        JsonTest jsonTest = new JsonTest();

        List<String> names = new ArrayList<>();
        names.add("Dan");
        names.add("Jim");
        names.add("Sam");

        List<Integer> values = new ArrayList<>();
        values.add(52);
        values.add(11);
        values.add(30);

        jsonTest.checkArray(jsonVendorsArray, names, "money", values);
    }

    // Conscripts a vendor called Sam into the market
    // MODIFIES: this
    // EFFECTS: a new inventory is assigned to a new vendor called Sam, who is conscripted into the market
    private Vendor conscriptSam() {
        Inventory inventory = new Inventory(20);
        Vendor vendor = new Vendor("Sam", inventory, 30, 10);
        return market.conscriptVendor(vendor);
    }

    // Conscripts a vendor called Jim into the market
    // MODIFIES: this
    // EFFECTS: a new inventory is assigned to a new vendor called Jim, who is conscripted into the market
    private Vendor conscriptJim() {
        Inventory inventory = new Inventory(33);
        Vendor vendor = new Vendor("Jim", inventory, 11, 20);
        return market.conscriptVendor(vendor);
    }

    // Conscripts a vendor called Dan into the market
    // MODIFIES: this
    // EFFECTS: a new inventory is assigned to a new vendor called Dan, who is conscripted into the market
    private Vendor conscriptDan() {
        Inventory inventory = new Inventory(44);
        Vendor vendor = new Vendor("Dan", inventory, 52, 3);
        return market.conscriptVendor(vendor);
    }
}
