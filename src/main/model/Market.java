package model;

import model.vendors.FoodVendor;
import model.vendors.MediaVendor;
import model.vendors.RockVendor;
import model.vendors.WeaponVendor;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.WritableCollection;

import java.util.ArrayList;
import java.util.List;

// Represents a Market that can contain multiple Vendors
public class Market implements WritableCollection {
    public static final int MAX_POPULATION = 200;

    private final List<Vendor> vendors;
    private final List<String> vendorTypes = new ArrayList<>();

    // Initialize market
    // MODIFIES: this
    // EFFECTS: new ArrayList vendors is created empty
    public Market() {
        vendors = new ArrayList<>();

        // !!! Needs to be updated when a new vendor subtype is added to the project
        vendorTypes.add("Weapon");
        vendorTypes.add("Media");
        vendorTypes.add("Food");
        vendorTypes.add("Rock");
    }

    // Conscript vendor to market
    // MODIFIES: this
    // EFFECTS: if vendor of same name isn't already in the market and the market can hold another vendor, adds given
    //          vendor to the market's list of vendors and returns the vendor, otherwise just returns null
    public Vendor conscriptVendor(Vendor vendor) {
        if (!vendorExists(vendor.getName()) && population() + 1 <= MAX_POPULATION) {
            vendors.add(vendor);
            return vendor;
        }

        return null;
    }

    // Create and conscript vendor to market
    // MODIFIES: this
    // EFFECTS: if vendor of same name isn't in the market, constructs new vendor with a new inventory and conscripts it
    //          to the market and returns true, otherwise false
    public boolean conscriptVendor(String name, String vendorType, int invSize, int initMoney, int initBP) {
        Inventory inv = new Inventory(invSize);
        Vendor vendor;

        // !!! Needs to be updated when a new vendor subtype is added to the project
        if (vendorType.equals(getVendorType(0))) {
            vendor = new WeaponVendor(name, inv, initMoney, initBP);
        } else if (vendorType.equals(getVendorType(1))) {
            vendor = new MediaVendor(name, inv, initMoney, initBP);
        } else if (vendorType.equals(getVendorType(2))) {
            vendor = new FoodVendor(name, inv, initMoney, initBP);
        } else if (vendorType.equals(getVendorType(3))) {
            vendor = new RockVendor(name, inv, initMoney, initBP);
        } else {
            vendor = new Vendor(name, inv, initMoney, initBP);
        }

        return conscriptVendor(vendor) != null;
    }

    // Expel vendor from market
    // REQUIRES: vendor must be contained inside ArrayList vendors
    // MODIFIES: this
    // EFFECTS: removes given vendor from the market's list of vendors
    public void expelVendor(Vendor vendor) {
        vendors.remove(vendor);
    }

    // Checks if vendor is in the market
    // EFFECTS: returns true if a vendor of the given name is in the market, otherwise false
    public boolean vendorExists(String vendorName) {
        return getVendor(vendorName) != null;
    }

    // Get vendor in market by name
    // EFFECTS: if the vendorName is the name of a vendor in the market's list of vendors, returns that vendor otherwise
    //          returns null
    public Vendor getVendor(String vendorName) {
        for (Vendor vendor : vendors) {
            if (vendorName.equals(vendor.getName())) {
                return vendor;
            }
        }

        return null;
    }

    // Get vendor in market by index
    // REQUIRES: index must be >= 0 and < size of vendors list
    // EFFECTS: returns the vendor located at the given index of vendors list
    public Vendor getVendor(int index) {
        return vendors.get(index);
    }

    // Gets a vendor type from list
    // REQUIRES: index must be between 0 and the length of the vendor type list minus 1
    // EFFECTS: returns the vendor type at index from the vendor type list
    public String getVendorType(int index) {
        return vendorTypes.get(index);
    }

    // The amount of vendors
    // EFFECTS: returns the size of the vendor type list
    public int vendorTypeAmount() {
        return vendorTypes.size();
    }

    // Get number of vendors in market
    // EFFECTS: returns the number of vendors in the market's list of vendors
    public int population() {
        return vendors.size();
    }

    // Gets market's vendor array as a JSONArray
    // EFFECTS: returns vendors in market's vendor array as a JSONArray
    @Override
    public JSONArray arrayToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Vendor vendor : vendors) {
            jsonArray.put(vendor.toJson());
        }

        return jsonArray;
    }

    // Gets the market as JSONObject
    // EFFECTS: returns market as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vendors", arrayToJson());
        return jsonObject;
    }
}
