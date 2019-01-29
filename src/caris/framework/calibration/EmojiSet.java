package caris.framework.calibration;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

public class EmojiSet {
			
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
	
	public static final Emoji AFFIRMATIVE = EmojiManager.getForAlias("thumbsup");
	public static final Emoji NEGATIVE = EmojiManager.getForAlias("thumbsdown");
	
	public static final Emoji LEFT = EmojiManager.getForAlias("arrow_left");
	public static final Emoji RIGHT = EmojiManager.getForAlias("arrow_right");
	
	public static final Emoji STOP = EmojiManager.getForAlias("octagonal_sign");
}
