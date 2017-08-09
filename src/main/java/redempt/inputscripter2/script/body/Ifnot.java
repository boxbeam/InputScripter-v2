package redempt.inputscripter2.script.body;

import redempt.inputscripter2.script.Script;

public class Ifnot implements Body {

	@Override
	public void start(Script script, int startLine, int endLine, String args) {
		if (script.getLoader().evalConditional(script, args)) {
			script.gotoLine(endLine);
		}
	}

	@Override
	public void end(Script script, int startLine, int endLine, String args) {
	}
	
}
