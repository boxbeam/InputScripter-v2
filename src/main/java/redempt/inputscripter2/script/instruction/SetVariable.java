package redempt.inputscripter2.script.instruction;

import redempt.inputscripter2.script.Script;
import redempt.inputscripter2.script.variable.Variable;

public class SetVariable implements Instruction {
	
	private Variable target;
	private String value;
	
	public SetVariable(Variable target, String value) {
		this.target = target;
		this.value = value;
	}

	@Override
	public void run(Script script) {
		target.set(script, script.getLoader().eval(script, value));
	}
	
}
