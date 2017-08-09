package redempt.inputscripter2.script.function;

import redempt.inputscripter2.script.Script;
import redempt.inputscripter2.script.body.Body;
import redempt.inputscripter2.script.variable.StandardVariable;

public class UserFunction implements Function, Body {
	
	private int startLine = 0;
	private int lastLine = 0;
	
	@Override
	public void start(Script script, int startLine, int endLine, String args) {
		this.startLine = startLine;
		script.gotoLine(endLine);
		script.functions.put(args, this);
	}

	@Override
	public void end(Script script, int startLine, int endLine, String args) {
		script.gotoLine(lastLine);
	}

	@Override
	public boolean run(Script script, String args) {
		StandardVariable var = new StandardVariable();
		var.set(script, args);
		script.variables.put("args", var);
		lastLine = script.getLineNumber();
		script.gotoLine(startLine);
		return false;
	}

}
