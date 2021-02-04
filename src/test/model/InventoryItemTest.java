package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonTest;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {
    private InventoryItem item;

    @BeforeEach
    public void runBefore() {
        item = new InventoryItem("Name", "Description", 5, 4);
    }

    @Test
    public void testInit() {
        assertEquals("Name", item.getName());
        assertEquals("Description", item.getDescription());
        assertEquals(5, item.getWeight());
        assertEquals(4, item.getPrice());
    }

    @Test
    public void testSetPrice() {
        assertEquals(4, item.getPrice());
        item.setPrice(2);
        assertEquals(2, item.getPrice());
    }

    @Test
    public void testToJson() {
        JSONObject itemJson = item.toJson();

        InventoryItem itemB = new InventoryItem("B", "B sentence", 12, 11);
        JSONObject itemBJson = itemB.toJson();

        JsonTest jsonTest = new JsonTest();
        jsonTest.checkObject(itemJson, "Name", "weight", 5);
        jsonTest.checkObject(itemBJson, "B", "weight", 12);
    }
}