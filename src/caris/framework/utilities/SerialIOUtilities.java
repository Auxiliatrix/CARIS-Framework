package caris.framework.utilities;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

public class SerialIOUtilities {
	
	public static String JSONOut(String filename, JSONObject object) {
		try {
			FileWriter file = new FileWriter(filename);
			file.write(object.toString());
			file.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
		return object.toString();
	}
	
}
