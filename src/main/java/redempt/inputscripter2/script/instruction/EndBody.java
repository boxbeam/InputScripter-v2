package redempt.inputscripter2.script.instruction;

import redempt.inputscripter2.script.Script;
import redempt.inputscripter2.script.body.Body;

public class EndBody implements Instruction {
	
	private Body body;
	private int startLine;
	private int endLine;
	private String args;
	
	public EndBody(Body body, int startLine, int endLine, String args) {
		this.body = body;
		this.startLine = startLine;
		this.endLine = endLine;
		this.args = args;
	}
	
	@Override
	public void run(Script script) {
		body.end(script, startLine, endLine, args);
	}

}
