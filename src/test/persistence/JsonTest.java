package persistence;

import model.Vendor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// CITATION: This class was based heavily on the class of the same name found in the JsonSerializationDemo project
public class JsonTest {

    // Checks that JSONObject carries same data as that given
    // EFFECTS: checks to see if JSONObject has the same name as that given, and same value for the given key as the
    //          value given
    public void checkObject(JSONObject jsonObject, String name, String key, int value) {
        String objName = jsonObject.getString("name");
        int objValue = jsonObject.getInt(key);

        assertEquals(name, objName);
        assertEquals(value, objValue);
    }

    // Checks that JSONArray carries same data as that given
    // INVARIANT: the names and values lists must be of equal length
    // EFFECTS: checks to see if JSONArray has the same names as that given, and same values for the given keys as the
    //          values given
    public void checkArray(JSONArray jsonArray, List<String> names, String key, List<Integer> values) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = names.get(i);
            int value = values.get(i);

            checkObject(jsonObject, name, key, value);
        }
    }

    // Checks that the vendor from the file has been read correctly
    // REQUIRES: vendorFromJSON is a vendor composed of data that was read from the market.json file
    // EFFECTS: checks that the Vendor, which was read from the market.json file, has the same data as that given
    public void checkVendorFromJson(Vendor vendorFromJSON, String name, int maxWeight, String firstItemName) {
        assertEquals(name, vendorFromJSON.getName());
        assertEquals(maxWeight, vendorFromJSON.getInventoryMaxWeight());
        assertEquals(firstItemName, vendorFromJSON.getItemFromInventory(0).getName());
    }
}
