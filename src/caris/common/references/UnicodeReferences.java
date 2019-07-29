package caris.common.references;

import java.util.HashMap;
import java.util.Map;

public class UnicodeReferences {

	@SuppressWarnings("serial")
	public static final Map<String, String> UNICODE_CONVERSIONS = new HashMap<String, String>() {{
		put("201c", "\"");
		put("201d", "\"");
	}};
	
}
