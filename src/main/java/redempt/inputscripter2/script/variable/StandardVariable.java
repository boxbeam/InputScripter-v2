package redempt.inputscripter2.script.variable;

import redempt.inputscripter2.script.Script;

public class StandardVariable implements Variable {
	
	private String value = "";
	
	@Override
	public String get(Script script) {
		return value;
	}

	@Override
	public void set(Script script, String value) {
		this.value = value;
	}

}
