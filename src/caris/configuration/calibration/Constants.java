package caris.configuration.calibration;

public class Constants {
	public static final String INVOCATION_PREFIX = "s";	
	public static final String NAME = "SCOUT";

	public static final long[] DEVELOPER_IDS = new long[] {
			Long.parseLong("246562987651891200"),
			Long.parseLong("365715538166415362"),
	};
	
	public static final String[] MODULE_PACKAGES = new String[] {
			"caris.modular.handlers",
	};
	
	public static final int REACTION_EXECUTE_DELAY = 250;

	public final static int STUBBORNNESS = 2;
	
	public final static int EMBED_AUTHOR_SIZE = 32;
	public final static int EMBED_TITLE_SIZE = 256;
	public final static int EMBED_DESCRIPTION_SIZE = 2048;
	public final static int EMBED_FOOTER_SIZE = 256;

	public final static int EMBED_FIELDS_SIZE = 10;
	public final static int EMBED_FIELD_TITLE_SIZE = 256;
	public final static int EMBED_FIELD_VALUE_SIZE = 256;	
}
