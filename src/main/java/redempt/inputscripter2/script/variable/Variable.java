package redempt.inputscripter2.script.variable;

import redempt.inputscripter2.script.Script;

public interface Variable {
	
	public String get(Script script);
	public void set(Script script, String value);
	
}
