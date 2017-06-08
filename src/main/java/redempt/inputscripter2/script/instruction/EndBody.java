package redempt.inputscripter2.script.instruction;

import redempt.inputscripter2.script.Script;
import redempt.inputscripter2.script.body.Body;

public class EndBody implements Instruction {
	
	private Body body;
	private int startLine;
	private int endLine;
	
	public EndBody(Body body, int startLine, int endLine) {
		this.body = body;
		this.startLine = startLine;
		this.endLine = endLine;
	}
	
	@Override
	public void run(Script script) {
		body.end(script, startLine, endLine);
	}

}
