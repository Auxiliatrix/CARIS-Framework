package caris.common.references;

import alina.utilities.parsing.WordParsing;

public class KeywordReferences {
	
	/**
	 * The keywords that represent creation.
	 */
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
	
	/**
	 * The keywords that represent initialization.
	 */
	public static final String[] START = new String[] {
			"start",
			"commence",
			"open",
			"begin",
			"set up",
			"initiate"
	};
	
	/**
	 * The keywords that represent activation.
	 */
	public static final String[] ENABLE = new String[] {
			"enable",
			"activate",
			"turn on",
			"switch on",
			"start up",
	};
	
	/**
	 * The keywords that denote positive action.
	 */
	public static final String[] POSITIVE = WordParsing.combineStringArrays(CREATE, START, ENABLE);
	
	/**
	 * The keywords that represent destruction.
	 */
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
	
	/**
	 * The keywords that represent cessation.
	 */
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
	
	/**
	 * The keywords that represent deactivation.
	 */
	public static final String[] DISABLE = new String[] {
			"disable",
			"deactivate",
			"turn off",
			"switch off",
			"shut down",
	};
	
	/**
	 * The keywords that denote negative action.
	 */
	public static final String[] NEGATIVE = WordParsing.combineStringArrays(DESTROY, END, DISABLE);
	
	/**
	 * The keywords that represent cancellation.
	 */
	public static final String[] CANCEL = new String[] {
			"cancel",
			"abort",
			"call off",
			"dismiss"
	};
	
	/**
	 * The keywords that indicate affirmation.
	 */
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
	
	/**
	 * The keywords that indicate negation.
	 */
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
