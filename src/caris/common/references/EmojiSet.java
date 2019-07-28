package caris.common.references;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

public class EmojiSet {

	/**
	 * Emojis that represent numbers.
	 */
	public static final Emoji[] NUMBERS = new Emoji[] {
			EmojiManager.getForAlias("zero"),
			EmojiManager.getForAlias("one"),
			EmojiManager.getForAlias("two"),
			EmojiManager.getForAlias("three"),
			EmojiManager.getForAlias("four"),
			EmojiManager.getForAlias("five"),
			EmojiManager.getForAlias("six"),
			EmojiManager.getForAlias("seven"),
			EmojiManager.getForAlias("eight"),
			EmojiManager.getForAlias("nine"),
			EmojiManager.getForAlias("ten"),
		};
	
	/**
	 * The Emoji to use to represent a affirmation.
	 */
	public static final Emoji AFFIRMATIVE = EmojiManager.getForAlias("thumbsup");
	
	/**
	 * The Emoji to use to represent negation.
	 */
	public static final Emoji NEGATIVE = EmojiManager.getForAlias("thumbsdown");
	
	/**
	 * The Emoji to use to represent backwards reference.
	 * @see caris.framework.interactives.PagedInteractive
	 */
	public static final Emoji LEFT = EmojiManager.getForAlias("arrow_left");
	
	/**
	 * The Emoji to use to represent forwards reference.
	 * @see caris.framework.interactives.PagedInteractive
	 */
	public static final Emoji RIGHT = EmojiManager.getForAlias("arrow_right");
	
	/**
	 * The Emoji to use to represent cancellation.
	 */
	public static final Emoji STOP = EmojiManager.getForAlias("octagonal_sign");
	
}
