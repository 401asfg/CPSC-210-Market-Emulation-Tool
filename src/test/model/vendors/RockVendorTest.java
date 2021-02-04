package model.vendors;

import model.Inventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RockVendorTest {
    @Test
    public void testInit() {
        Inventory inventoryA = new Inventory(10000);
        RockVendor vendorA = new RockVendor("A", inventoryA, 10, 20);

        Inventory inventoryB = new Inventory(1);
        RockVendor vendorB = new RockVendor("B", inventoryB, 10, 20);

        assertEquals("A", vendorA.getName());
        assertEquals(10000, vendorA.getInventoryMaxWeight());
        assertEquals(10, vendorA.getMoney());
        assertEquals(20, vendorA.getBarterPoints());
        assertEquals(9, vendorA.getInventorySize());
        assertEquals("./data/icons/rockVendorIcon.bmp", vendorA.getIcon().getDescription());

        assertEquals("B", vendorB.getName());
        assertEquals(1, vendorB.getInventoryMaxWeight());
        assertEquals(10, vendorB.getMoney());
        assertEquals(20, vendorB.getBarterPoints());
        assertEquals(1, vendorB.getInventorySize());
        assertEquals("./data/icons/rockVendorIcon.bmp", vendorB.getIcon().getDescription());
    }
}
