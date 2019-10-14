package caris.framework.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class SaveDataUtilities {
	
	public static String JSONCreateAndOut(String[] fileNames, JSONObject object) {
		String filePath = "";
		for( int f=0; f<fileNames.length-1; f++ ) {
			filePath += fileNames[f];
			File directory = new File(filePath);
			if( !directory.exists() ) {
				directory.mkdir();
			}
			filePath += File.separator;
		}
		filePath += fileNames[fileNames.length-1];
		return JSONOut(filePath, object);
	}
	
	public static String JSONOut(String fileName, JSONObject object) {
		File filePath = new File(fileName);
		try {
			filePath.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileWriter file = new FileWriter(fileName);
			file.write(object.toString());
			file.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
		return object.toString();
	}
	
	public static JSONObject JSONIn(String fileName) {
		JSONObject json = null;
		try {
			InputStream is = new FileInputStream(fileName);
			String jsonString = IOUtils.toString(is, "UTF-8");
			json = new JSONObject(jsonString);
		} catch (Exception e){
			e.printStackTrace();
		}
		return json;
	}
	
}
