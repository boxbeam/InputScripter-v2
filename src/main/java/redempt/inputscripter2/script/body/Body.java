package redempt.inputscripter2.script.body;

import redempt.inputscripter2.script.Script;

public interface Body {
	
	public void start(Script script, int startLine, int endLine, String args);
	public void end(Script script, int startLine, int endLine);
	
}