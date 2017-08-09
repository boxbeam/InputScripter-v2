package redempt.inputscripter2.script.instruction;

import redempt.inputscripter2.script.Script;
import redempt.inputscripter2.script.function.Function;

public class RunFunction implements Instruction {
	
	private String function;
	private Function func;
	private String args;
	
	public RunFunction(String function, String args) {
		this.function = function.trim();
		this.args = args;
	}

	@Override
	public void run(Script script) {
		if (func == null) {
			if ((func = script.functions.get(function)) == null) {
				return;
			}
		}
		func.run(script, args.trim());
	}
	
}
