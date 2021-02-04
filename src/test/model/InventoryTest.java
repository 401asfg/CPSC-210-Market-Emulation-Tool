package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    private Inventory inventory;
    private InventoryItem itemA;
    private InventoryItem itemB;
    private InventoryItem itemC;
    private InventoryItem itemD;

    @BeforeEach
    public void runBefore() {
        inventory = new Inventory(20);

        // Combined weight of A, B, and C must equal the maxWeight of the inventory
        itemA = new InventoryItem("A", "Description", inventory.getMaxWeight() / 2, 0);
        itemB = new InventoryItem("B", "Description", inventory.getMaxWeight() / 4, 0);
        itemC = new InventoryItem("C", "Description", inventory.getMaxWeight() / 4, 0);
        itemD = new InventoryItem("D", "Description", 1, 0);
    }

    @Test
    public void testInit() {
        assertEquals(0, inventory.size());
        assertEquals(20, inventory.getMaxWeight());
    }

    @Test
    public void testAddEmptyToOverCap() {
        // Exceeds weight, not added (testing that add takes the new item's weight into account and won't add to  an
        //                            empty inventory if the weight will be exceeded)
        InventoryItem item = new InventoryItem("", "", inventory.getMaxWeight() + 1, 0);
        assertFalse(inventory.add(item));
        assertEquals(0, inventory.size());
    }

    @Test
    public void testAddEmptyToUnderCapToOverCap() {
        // Under weight, added (testing that items can be added)
        assertTrue(inventory.add(itemA));
        assertEquals(1, inventory.size());
        assertEquals(itemA, inventory.getItem("A"));

        // Added another, still under weight (testing that inventory will accept additional items when under capacity)
        assertTrue(inventory.add(itemB));
        assertEquals(2, inventory.size());
        assertEquals(itemB, inventory.getItem("B"));

        // Added itemB again (testing that inventory will not accept the same item twice)
        assertFalse(inventory.add(itemB));
        assertEquals(2, inventory.size());

        // Added another, at weight (testing that inventory will accept items when combined weight meets capacity)
        assertTrue(inventory.add(itemC));
        assertEquals(3, inventory.size());
        assertEquals(itemC, inventory.getItem("C"));

        // Exceeds weight, not added (testing that one item will be rejected from inventory when exceeds capacity)
        assertFalse(inventory.add(itemD));
        assertEquals(3, inventory.size());
    }

    @Test
    public void testRemove() {
        inventory.add(itemA);
        inventory.add(itemB);
        inventory.add(itemC);

        inventory.remove(itemB);
        assertEquals(2, inventory.size());                  // Size has been decreased
        assertEquals(itemA, inventory.getItem("A"));       // Makes sure that index are adjusted upon removal
        assertEquals(itemC, inventory.getItem("C"));       // Makes sure that index are adjusted upon removal
        assertFalse(inventory.contains(itemB));                     // No longer contains itemB

        inventory.remove(itemA);
        assertEquals(1, inventory.size());                  // Size has been decreased
        assertEquals(itemC, inventory.getItem("C"));       // Makes sure that index are adjusted upon removal
        assertFalse(inventory.contains(itemA));                     // No longer contains itemA

        inventory.remove(itemC);
        assertEquals(0, inventory.size());                  // Size has been decreased
        assertFalse(inventory.contains(itemC));                     // No longer contains itemC
    }

    @Test
    public void testGetItemByString() {
        // Not in inventory
        assertNull(inventory.getItem("Not Here"));
        inventory.add(itemA);
        assertNull(inventory.getItem("B"));

        // In Inventory
        inventory.add(itemB);
        assertEquals(itemB, inventory.getItem("B"));
        assertEquals(itemA, inventory.getItem("A"));
    }

    @Test
    public void testGetItemByIndex() {
        inventory.add(itemB);
        inventory.add(itemA);
        assertEquals(itemB, inventory.getItem(0));
        assertEquals(itemA, inventory.getItem(1));
    }

    @Test
    public void testGetWeight() {
        // Test that inventory starts as weightless
        assertEquals(0, inventory.getWeight());

        // Test that adding an item to an inventory increases the inventory's weight
        inventory.add(itemA);
        assertEquals(itemA.getWeight(), inventory.getWeight());

        // Test that item weights stack in an inventory
        inventory.add(itemB);
        assertEquals(itemA.getWeight() + itemB.getWeight(), inventory.getWeight());

        // Test that removing an item from an inventory reduces the inventory's weight by that item's weight
        inventory.remove(itemA);
        assertEquals(itemB.getWeight(), inventory.getWeight());
    }

    @Test
    public void testCanHoldNoItems() {
        // Under capacity
        assertTrue(inventory.canHold(itemA));

        // At capacity
        InventoryItem item1 = new InventoryItem("", "", inventory.getMaxWeight(), 0);
        assertTrue(inventory.canHold(item1));

        // Over capacity
        InventoryItem item2 = new InventoryItem("", "", inventory.getMaxWeight() + 4, 0);
        assertFalse(inventory.canHold(item2));
    }

    @Test
    public void testCanHoldWithItems() {
        inventory.add(itemA);
        inventory.add(itemB);

        // Under capacity
        assertTrue(inventory.canHold(itemD));

        // At capacity
        assertTrue(inventory.canHold(itemC));

        // Over capacity
        inventory.add(itemC);
        assertFalse(inventory.canHold(itemD));
    }

    @Test
    public void testToJson() {
        inventory.add(itemA);
        inventory.add(itemB);
        inventory.add(itemC);

        JSONObject invJson = inventory.toJson();
        JSONArray jsonItemsArray = invJson.getJSONArray("items");

        int maxWeight = invJson.getInt("maxWeight");
        assertEquals(20, maxWeight);

        JsonTest jsonTest = new JsonTest();

        List<String> names = new ArrayList<>();
        names.add("A");
        names.add("B");
        names.add("C");

        List<Integer> values = new ArrayList<>();
        values.add(inventory.getMaxWeight() / 2);
        values.add(inventory.getMaxWeight() / 4);
        values.add(inventory.getMaxWeight() / 4);

        jsonTest.checkArray(jsonItemsArray, names, "weight", values);
    }
}