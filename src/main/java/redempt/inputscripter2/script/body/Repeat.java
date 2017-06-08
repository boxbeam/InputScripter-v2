package redempt.inputscripter2.script.body;

import redempt.inputscripter2.script.Script;

public class Repeat implements Body {

	int times;
	
	@Override
	public void start(Script script, int startLine, int endLine, String args) {
		try {
			times = Integer.parseInt(script.getHandler().eval(script, args));
		} catch (NumberFormatException e) {
			times = -1;
		}
	}

	@Override
	public void end(Script script, int startLine, int endLine) {
		if (times > 0) {
			times--;
		}
		if (times > 0 || times == -1) {
			script.gotoLine(startLine);
		}
	}

}
