package caris.framework.scripts.comparators;

import caris.framework.events.MessageEventWrapper;
import caris.framework.scripts.Conditional;
import caris.framework.scripts.Context;
import caris.framework.scripts.ScriptCompiler;

public class Conditional_EQUALS extends Conditional {

	private String variable1;
	private String variable2;
	
	public Conditional_EQUALS(String variable1, String variable2) {
		this.variable1 = variable1;
		this.variable2 = variable2;
	}

	@Override
	public boolean resolve(MessageEventWrapper mew, Context context) {
		String compiledVariable1 = ScriptCompiler.compileStringVariable(mew, context, variable1);
		String compiledVariable2 = ScriptCompiler.compileStringVariable(mew, context, variable2);
		return compiledVariable1.equalsIgnoreCase(compiledVariable2);
	}
	
}
