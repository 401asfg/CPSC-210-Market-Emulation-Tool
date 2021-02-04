package model;

import model.vendors.WeaponVendor;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonTest;

import static org.junit.jupiter.api.Assertions.*;

public class VendorTest {
    private Vendor vendorA;

    private Vendor vendorB;
    private Inventory invB;

    private InventoryItem rock;
    private InventoryItem rock2;
    private InventoryItem blackRock;
    private InventoryItem sapphire;

    @BeforeEach
    public void runBefore() {
        Inventory invA = new Inventory(20);
        vendorA = new Vendor("Vendy", invA, 20, 10);

        rock = new InventoryItem("Rock", "A rock.", 1, 1);
        rock2 = new InventoryItem("Rock 2", "Another rock.", 1, 1);
        blackRock = new InventoryItem("Black Rock", "A dark rock", 2, 9);
        sapphire = new InventoryItem("Sapphire", "A gemstone.", 2, 10);

        invB = new Inventory(30);
        invB.add(rock);
        invB.add(rock2);
        invB.add(blackRock);
        invB.add(sapphire);

        vendorB = new Vendor("The Other", invB, 50, 5);
    }

    @Test
    public void testInit() {
        assertEquals("Vendy", vendorA.getName());
        assertEquals(20, vendorA.getMoney());
        assertEquals(10, vendorA.getBarterPoints());
    }

    @Test
    public void testBuyAllConditionsMetOneVendor() {
        // Buying when inventory's empty
        assertTrue(vendorA.buy(vendorB, sapphire));

        assertEquals(1, vendorA.getInventorySize());
        assertEquals(sapphire, vendorA.getItemFromInventory("Sapphire"));
        assertEquals(10, vendorA.getMoney());

        assertEquals(3, vendorB.getInventorySize());
        assertEquals(rock, vendorB.getItemFromInventory("Rock"));
        assertEquals(rock2, vendorB.getItemFromInventory("Rock 2"));
        assertEquals(blackRock, vendorB.getItemFromInventory("Black Rock"));
        assertEquals(60, vendorB.getMoney());

        // Buying when there's another item in the inventory
        assertTrue(vendorA.buy(vendorB, rock));

        assertEquals(2, vendorA.getInventorySize());
        assertEquals(rock, vendorA.getItemFromInventory("Rock"));
        assertEquals(sapphire, vendorA.getItemFromInventory("Sapphire"));
        assertEquals(9, vendorA.getMoney());

        assertEquals(2, vendorB.getInventorySize());
        assertEquals(rock2, vendorB.getItemFromInventory("Rock 2"));
        assertEquals(blackRock, vendorB.getItemFromInventory("Black Rock"));
        assertEquals(61, vendorB.getMoney());
    }

    @Test
    public void testBuyAllConditionsMetTwoVendors() {
        assertTrue(vendorA.buy(vendorB, sapphire));
        assertTrue(vendorA.buy(vendorB, rock));

        Inventory invC = new Inventory(20);
        Vendor vendorC = new Vendor("Vendor C", invC, 10, 10);
        invC.add(blackRock);

        // Buy an item from a different vendor, costing the last of vendorA's money
        assertTrue(vendorA.buy(vendorC, blackRock));

        assertEquals(3, vendorA.getInventorySize());
        assertEquals(blackRock, vendorA.getItemFromInventory("Black Rock"));
        assertEquals(rock, vendorA.getItemFromInventory("Rock"));
        assertEquals(sapphire, vendorA.getItemFromInventory("Sapphire"));
        assertEquals(0, vendorA.getMoney());

        assertEquals(0, vendorC.getInventorySize());
        assertEquals(19, vendorC.getMoney());

        assertEquals(2, vendorB.getInventorySize());
        assertEquals(rock2, vendorB.getItemFromInventory("Rock 2"));
        assertEquals(blackRock, vendorB.getItemFromInventory("Black Rock"));
        assertEquals(61, vendorB.getMoney());
    }

    @Test
    public void testBuyUnderfunded() {
        InventoryItem diamond = new InventoryItem("Diamond", "A valuable gemstone.", 1, 19);
        InventoryItem goldBrick = new InventoryItem("Gold Brick", "Brick of gold.", 1, 30);

        invB.add(diamond);
        invB.add(goldBrick);

        // Item (goldBrick) exceeds vendorA money when nothing else has been bought yet
        assertFalse(vendorA.buy(vendorB, goldBrick));

        assertEquals(0, vendorA.getInventorySize());
        assertEquals(20, vendorA.getMoney());

        assertEquals(6, vendorB.getInventorySize());
        assertEquals(50, vendorB.getMoney());

        // With new items in the inventory, buying previously affordable item (diamond) now costs too much
        assertTrue(vendorA.buy(vendorB, rock));
        assertTrue(vendorA.buy(vendorB, sapphire));
        assertFalse(vendorA.buy(vendorB, diamond));

        assertEquals(2, vendorA.getInventorySize());
        assertNull(vendorA.getItemFromInventory("Diamond"));
        assertEquals(9, vendorA.getMoney());

        assertEquals(4, vendorB.getInventorySize());
        assertEquals(61, vendorB.getMoney());
    }

    @Test
    public void testBuyNoMoney() {
        Inventory invC = new Inventory(30);
        Vendor vendorC = new Vendor("C", invC, 0, 10);

        assertFalse(vendorC.buy(vendorB, rock2));

        assertEquals(0, vendorC.getInventorySize());
        assertNull(vendorC.getItemFromInventory("Rock 2"));
        assertEquals(0, vendorC.getMoney());

        assertEquals(4, vendorB.getInventorySize());
        assertEquals(50, vendorB.getMoney());
    }

    @Test
    public void testBuyAddFails() {
        Inventory invC = new Inventory(1);
        Vendor vendorC = new Vendor("C", invC, 100, 100);

        assertFalse(invC.add(sapphire));
        assertFalse(vendorC.buy(vendorB, sapphire));
        assertEquals(0, vendorC.getInventorySize());
        assertEquals(100, vendorC.getMoney());
    }

    @Test
    public void testBuyResetsPrice() {
        int initSapphirePrice = sapphire.getPrice();
        sapphire.decreasePrice(5);

        assertEquals(initSapphirePrice - 5, sapphire.getPrice());
        assertTrue(vendorA.buy(vendorB, sapphire));
        assertEquals(initSapphirePrice, sapphire.getPrice());
    }

    @Test
    public void testBarterMoreOrEqualBP() {
        // Barter with more points
        int sapphireInitPrice = sapphire.getPrice();
        assertTrue(vendorA.barter(vendorB, sapphire));

        assertEquals(sapphireInitPrice / Vendor.BARTER_PRICE_DIVISOR, sapphire.getPrice());
        assertEquals(10 - Vendor.BARTER_POINT_TRANSFER, vendorA.getBarterPoints());
        assertEquals(5 + Vendor.BARTER_POINT_TRANSFER, vendorB.getBarterPoints());

        // Barter with equal points
        Inventory invC = new Inventory(20);
        Vendor vendorC = new Vendor("C", invC, 100, 5 + Vendor.BARTER_POINT_TRANSFER);

        int blackRockInitPrice = blackRock.getPrice();
        assertTrue(vendorC.barter(vendorB, blackRock));

        assertEquals(blackRockInitPrice / Vendor.BARTER_PRICE_DIVISOR, blackRock.getPrice());
        assertEquals(5, vendorC.getBarterPoints());
        assertEquals(5 + Vendor.BARTER_POINT_TRANSFER * 2, vendorB.getBarterPoints());

        // 2 different model.vendors barter for the same item
        Inventory invD = new Inventory(20);
        Vendor vendorD = new Vendor("D", invD, 100, Vendor.BARTER_POINT_TRANSFER * 10);

        blackRockInitPrice = blackRock.getPrice();
        assertTrue(vendorD.barter(vendorB, blackRock));

        assertEquals(blackRockInitPrice / Vendor.BARTER_PRICE_DIVISOR, blackRock.getPrice());
        int venDPointLoss = Vendor.BARTER_POINT_TRANSFER * 10 - Vendor.BARTER_POINT_TRANSFER;
        assertEquals(venDPointLoss, vendorD.getBarterPoints());
        assertEquals(5 + Vendor.BARTER_POINT_TRANSFER * 3, vendorB.getBarterPoints());
    }

    @Test
    public void testBarterFewerBP() {
        Vendor vendorC = new Vendor("C", new Inventory(100), 100, 1);

        int initSapphirePrice = sapphire.getPrice();
        assertFalse(vendorC.barter(vendorB, sapphire));

        assertEquals(initSapphirePrice, sapphire.getPrice());
        assertEquals(1, vendorC.getBarterPoints());
        assertEquals(5, vendorB.getBarterPoints());
    }

    @Test
    public void testBarterUnderBPTransfer() {
        Vendor vendorC = new Vendor("C", new Inventory(100), 100, 4);
        Inventory invD = new Inventory(30);
        Vendor vendorD = new Vendor("D", invD, 100, 2);

        invD.add(rock);

        int initRockPrice = rock.getPrice();
        assertFalse(vendorC.barter(vendorD, rock));

        assertEquals(initRockPrice, rock.getPrice());
        assertEquals(4, vendorC.getBarterPoints());
        assertEquals(2, vendorD.getBarterPoints());
    }

    @Test
    public void testGetItemFromInventoryByIndex() {
        assertEquals(rock, vendorB.getItemFromInventory(0));
        assertEquals(rock2, vendorB.getItemFromInventory(1));
    }

    @Test
    public void testGetInventoryWeight() {
        assertEquals(6, vendorB.getInventoryWeight());
        invB.remove(rock);
        assertEquals(5, vendorB.getInventoryWeight());

        Vendor vendorC = new Vendor("New", new Inventory(1), 2, 2);
        assertEquals(0, vendorC.getInventoryWeight());
    }

    @Test
    public void testTrySetIcon() {
        String filePath = "./data/icons/foodVendorIcon.bmp";
        vendorA.trySetIcon(filePath);
        assertEquals(filePath, vendorA.getIcon().getDescription());

        vendorA.trySetIcon("");
        assertNull(vendorA.getIcon());
    }

    @Test
    public void testToJson() {
        JSONObject vendorAJson = vendorA.toJson();
        JSONObject vendorBJson = vendorB.toJson();

        WeaponVendor weaponVendor = new WeaponVendor("Weapon Vendor", new Inventory(100), 2, 3);
        JSONObject weaponVendorJson = weaponVendor.toJson();

        JsonTest jsonTest = new JsonTest();
        jsonTest.checkObject(vendorAJson, "Vendy", "money", 20);
        jsonTest.checkObject(vendorBJson, "The Other", "money", 50);

        jsonTest.checkObject(weaponVendorJson, "Weapon Vendor", "money", 2);
        jsonTest.checkObject(weaponVendorJson, "Weapon Vendor", "barterPoints", 3);
        assertEquals("./data/icons/weaponVendorIcon.bmp", weaponVendorJson.getString("iconDescription"));
    }
}
