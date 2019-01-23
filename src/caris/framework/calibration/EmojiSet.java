package caris.framework.calibration;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

public class EmojiSet {
			
	public static Emoji[] numbers = new Emoji[] {
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
	
	public static Emoji[] affirmative = new Emoji[] {
			EmojiManager.getForAlias("thumbsup"),
		};
	
	public static Emoji[] negative = new Emoji[] {
			EmojiManager.getForAlias("thumbsdown"),
		};
}
