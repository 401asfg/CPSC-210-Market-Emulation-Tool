package persistence;

import org.json.JSONArray;

// CITATION: This class was based heavily on the thingiesToJson method found in the WorkRoom class in the
//           JsonSerializationDemo project
// An object, with an array as a field, that can be written into a JSON file
public interface WritableCollection extends Writable {
    // Gets class' array as a JSONArray
    // EFFECTS: returns objects in a class' array as a JSONArray
    JSONArray arrayToJson();
}
