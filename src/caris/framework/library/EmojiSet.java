package caris.framework.library;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import sx.blah.discord.handle.obj.IGuild;

public class EmojiSet {
		
	public IGuild guild;
	
	public Emoji[] numbers;
	
	public Emoji[] affirmative;
	
	public Emoji[] negative;
	
	public EmojiSet(IGuild guild) {
		this.guild = guild;
		initialize();
	}
	
	public void initialize() {
		numbers = new Emoji[] {
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
		
		affirmative = new Emoji[] {
				EmojiManager.getForAlias("thumbsup"),
			};
		
		negative = new Emoji[] {
				EmojiManager.getForAlias("thumbsdown"),
			};
	}
	
}
