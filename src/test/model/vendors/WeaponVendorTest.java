package model.vendors;

import model.Inventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeaponVendorTest {
     @Test
    public void testInit() {
        Inventory inventoryA = new Inventory(100);
        WeaponVendor vendorA = new WeaponVendor("A", inventoryA, 10, 20);

        Inventory inventoryB = new Inventory(1);
        WeaponVendor vendorB = new WeaponVendor("B", inventoryB, 10, 20);

        assertEquals("A", vendorA.getName());
        assertEquals(100, vendorA.getInventoryMaxWeight());
        assertEquals(10, vendorA.getMoney());
        assertEquals(20, vendorA.getBarterPoints());
        assertEquals(9, vendorA.getInventorySize());
        assertEquals("./data/icons/weaponVendorIcon.bmp", vendorA.getIcon().getDescription());

        assertEquals("B", vendorB.getName());
        assertEquals(1, vendorB.getInventoryMaxWeight());
        assertEquals(10, vendorB.getMoney());
        assertEquals(20, vendorB.getBarterPoints());
        assertEquals(1, vendorB.getInventorySize());
        assertEquals("./data/icons/weaponVendorIcon.bmp", vendorB.getIcon().getDescription());
    }
}
