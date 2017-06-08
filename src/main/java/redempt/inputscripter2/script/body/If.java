package redempt.inputscripter2.script.body;

import redempt.inputscripter2.script.Script;

public class If implements Body {

	@Override
	public void start(Script script, int startLine, int endLine, String args) {
		if (!script.getHandler().evalConditional(script, args)) {
			script.gotoLine(endLine);
		}
	}

	@Override
	public void end(Script script, int startLine, int endLine) {
	}
	
}
