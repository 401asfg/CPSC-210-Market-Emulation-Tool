package persistence;

import org.json.JSONObject;

// CITATION: This class was based heavily on the class of the same name found in the JsonSerializationDemo project
// An object that can be written into a JSON file
public interface Writable {
    // Gets object as JSONObject
    // EFFECTS: returns this as a JSONObject
    JSONObject toJson();
}
