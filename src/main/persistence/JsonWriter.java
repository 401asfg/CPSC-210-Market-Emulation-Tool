package persistence;

import model.Market;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// CITATION: This class was based heavily on the class of the same name found in the JsonSerializationDemo project
// Represents a writer that writes JSON representation of market to file
public class JsonWriter {
    private final String destination;
    private PrintWriter writer;
    private static final int TAB = 4;

    // Initialize JsonWriter
    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // Opens file for writing
    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    //          be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // Writes to file
    // MODIFIES: this
    // EFFECTS: writes JSON representation of market to file
    public void write(Market market) {
        JSONObject json = market.toJson();
        saveToFile(json.toString(TAB));
    }

    // Closes file
    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // Saves to file
    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
