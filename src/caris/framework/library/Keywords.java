package caris.framework.library;

import caris.framework.utilities.TokenUtilities;

public class Keywords {
	
	public static final String[] CREATE = new String[] {
			"create",
			"make",
			"spawn",
			"form",
			"produce",
			"fabricate",
			"build",
			"construct",
			"fashion",
			"conceive",
			"establish",
			"generate",
			"set up",
	};
	
	public static final String[] START = new String[] {
			"start",
			"commence",
			"open",
			"begin",
			"set up",
			"initiate"
	};
	
	public static final String[] ENABLE = new String[] {
			"enable",
			"activate",
			"turn on",
			"switch on",
			"start up",
	};
	
	public static final String[] POSITIVE = TokenUtilities.combineStringArrays(CREATE, START, ENABLE);
	
	public static final String[] DESTROY = new String[] {
			"remove",
			"delete",
			"destroy",
			"trash",
			"disestablish",
			"do away with",
			"kill",
			"terminate",
			"demolish",
			"dismantle",
			"raze",
	};
	
	public static final String[] END = new String[] {
			"end",
			"finish",
			"close",
			"complete",
			"conclude",
			"stop",
			"terminate",
			"halt",
			"wrap up",
	};
	
	public static final String[] DISABLE = new String[] {
			"disable",
			"deactivate",
			"turn off",
			"switch off",
			"shut down",
	};
	
	public static final String[] NEGATIVE = TokenUtilities.combineStringArrays(DESTROY, END, DISABLE);
	
	public static final String[] CANCEL = new String[] {
			"cancel",
			"abort",
			"call off",
			"dismiss"
	};
	
	public static final String[] YES_INPUT = new String[] {
			"y",
			"ye",
			"yes",
			"yea",
			"yeah",
			"yup",
			"yep",
			"affirmative",
			"okay",
			"fine",
			"okay",
			"mhm",
			"mmhm",
			"aye",
			"sure"
	};
	
	public static final String[] NO_INPUT = new String[] {
			"n",
			"no",
			"nah",
			"nay",
			"nope",
			"negative",
			"not",
			"dont",
			"don't",
	};
}
