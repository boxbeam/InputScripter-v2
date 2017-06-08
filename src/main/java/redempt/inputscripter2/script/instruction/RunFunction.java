package redempt.inputscripter2.script.instruction;

import redempt.inputscripter2.script.Script;
import redempt.inputscripter2.script.function.Function;

public class RunFunction implements Instruction {
	
	private Function function;
	private String args;
	
	public RunFunction(Function function, String args) {
		this.function = function;
		this.args = args;
	}

	@Override
	public void run(Script script) {
		function.run(script, args.trim());
	}
	
}
