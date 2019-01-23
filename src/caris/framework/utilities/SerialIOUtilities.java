package caris.framework.utilities;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

public class SerialIOUtilities {
	
	public static void JSONOut(String filename, Object object) {
		try {
			FileWriter file = new FileWriter(filename);
			JSONObject jsonObject = new JSONObject(object);
			System.out.println(jsonObject.toString());
			file.write(new JSONObject(object).toString());
			file.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
}
