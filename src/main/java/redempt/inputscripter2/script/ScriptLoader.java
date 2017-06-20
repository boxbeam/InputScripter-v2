package redempt.inputscripter2.script;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jnativehook.keyboard.NativeKeyEvent;

import javafx.application.Platform;
import redempt.inputscripter2.gui.Indicator;
import redempt.inputscripter2.script.body.Body;
import redempt.inputscripter2.script.body.BodyDefinition;
import redempt.inputscripter2.script.body.If;
import redempt.inputscripter2.script.body.Ifnot;
import redempt.inputscripter2.script.body.Repeat;
import redempt.inputscripter2.script.function.Function;
import redempt.inputscripter2.script.instruction.EndBody;
import redempt.inputscripter2.script.instruction.Instruction;
import redempt.inputscripter2.script.instruction.RunFunction;
import redempt.inputscripter2.script.instruction.SetVariable;
import redempt.inputscripter2.script.instruction.StartBody;
import redempt.inputscripter2.script.variable.StandardVariable;
import redempt.inputscripter2.script.variable.Variable;
import redempt.inputscripter2.utils.KeyListener;
import redempt.inputscripter2.utils.MouseListener;

public class ScriptLoader {
	
	private Robot robot;
	public Map<String, Function> functions = new HashMap<>();
	public Map<String, BodyDefinition> bodies = new HashMap<>();
	
	public ScriptLoader() {
		registerFunctions();
		registerBodies();
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	private void registerBodies() {
		bodies.put("repeat", new BodyDefinition() {

			@Override
			public Body getBody() {
				return new Repeat();
			}
			
		});
		bodies.put("if", new BodyDefinition() {

			@Override
			public Body getBody() {
				return new If();
			}
			
		});
		bodies.put("ifnot", new BodyDefinition() {

			@Override
			public Body getBody() {
				return new Ifnot();
			}
			
		});
	}
	
	private void registerFunctions() {
		functions.put("print", new Function() {

			@Override
			public boolean run(Script script, String args) {
				System.out.println(eval(script, args));
				return false;
			}
			
		});
		functions.put("wait", new Function() {

			@Override
			public boolean run(Script script, String args) {
				try {
					Thread.sleep(Integer.parseInt(eval(script, args)));
					return true;
				} catch (NumberFormatException | InterruptedException e) {
					e.printStackTrace();
					return false;
				}
			}
			
		});
		functions.put("scroll", new Function() {

			@Override
			public boolean run(Script script, String args) {
				robot.mouseWheel(Integer.parseInt(eval(script, args)));
				return false;
			}
			
		});
		functions.put("click", new Function() {

			@Override
			public boolean run(Script script, String args) {
				if (args.equals("left")) {
					robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
				}
				if (args.equals("right")) {
					robot.mousePress(MouseEvent.BUTTON3_DOWN_MASK);
				}
				if (args.equals("middle")) {
					robot.mousePress(MouseEvent.BUTTON2_DOWN_MASK);
				}
				return false;
			}
			
		});
		functions.put("unclick", new Function() {

			@Override
			public boolean run(Script script, String args) {
				if (args.equals("left")) {
					robot.mouseRelease(MouseEvent.BUTTON1_MASK);
				}
				if (args.equals("right")) {
					robot.mouseRelease(MouseEvent.BUTTON3_MASK);
				}
				if (args.equals("middle")) {
					robot.mouseRelease(MouseEvent.BUTTON2_MASK);
				}
				return false;
			}
			
		});
		functions.put("press", new Function() {

			@Override
			public boolean run(Script script, String args) {
				String keyname = "VK_" + args.toUpperCase();
				try {
					Field field = KeyEvent.class.getField(keyname);
					int key = (int) field.get(null);
					robot.keyPress(key);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				return false;
			}
			
		});
		functions.put("unpress", new Function() {

			@Override
			public boolean run(Script script, String args) {
				String keyname = "VK_" + args.trim().toUpperCase();
				try {
					Field field = KeyEvent.class.getField(keyname);
					int key = field.getInt(null);
					robot.keyRelease(key);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				return false;
			}
			
		});
		functions.put("break", new Function() {

			@Override
			public boolean run(Script script, String args) {
				int amount;
				if (args == null || args.equals("")) {
					amount = 1;
				} else {
					amount = Integer.parseInt(args);
				}
				for (int i = script.getLineNumber() + 1; i < script.instructions.length; i++) {
					if (script.instructions[i] instanceof EndBody) {
						amount--;
						if (amount == 0) {
							script.gotoLine(i);
							return false;
						}
					}
				}
				script.kill();
				return false;
			}
			
		});
		functions.put("keypressed", new Function() {

			@Override
			public boolean run(Script script, String args) {
				String keyname = "VC_" + args.toUpperCase().trim();
				try {
					Field field = NativeKeyEvent.class.getField(keyname);
					int key = (int) field.get(null);
					return KeyListener.isPressed(key);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				return false;
			}
			
		});
		functions.put("mousepressed", new Function() {

			@Override
			public boolean run(Script script, String args) {
				return MouseListener.isPressed(args.trim());
			}
			
		});
	}
	
	private void registerSystemVariables(Map<String, Variable> variables) {
		variables.put("clipboard", new Variable() {

			@Override
			public String get(Script script) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				try {
					return clipboard.getData(DataFlavor.stringFlavor).toString();
				} catch (UnsupportedFlavorException | IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public void set(Script script, String value) {
				StringSelection selection = new StringSelection(value);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
		
		});
		variables.put("mousex", new Variable() {

			@Override
			public String get(Script script) {
				return String.valueOf(Math.round(MouseInfo.getPointerInfo().getLocation().getX()));
			}

			@Override
			public void set(Script script, String value) {
				double mousey = MouseInfo.getPointerInfo().getLocation().getY();
				robot.mouseMove(Integer.parseInt(value), (int) mousey);
			}
			
		});
		variables.put("mousey", new Variable() {

			@Override
			public String get(Script script) {
				return String.valueOf(Math.round(MouseInfo.getPointerInfo().getLocation().getY()));
			}

			@Override
			public void set(Script script, String value) {
				double mousex = MouseInfo.getPointerInfo().getLocation().getX();
				robot.mouseMove((int) mousex, Integer.parseInt(value));
			}
			
		});
		variables.put("terminated", new Variable() {

			@Override
			public String get(Script script) {
				return String.valueOf(script.isTerminated());
			}

			@Override
			public void set(Script script, String value) {
				if (value.equals("true")) {
					script.terminate();
				}
			}
			
		});
		variables.put("random", new Variable() {

			@Override
			public String get(Script script) {
				return String.valueOf((int) Math.round(Math.random() * 100));
			}

			@Override
			public void set(Script script, String value) {
			}
			
		});
		variables.put("indicator", new Variable() {

			@Override
			public String get(Script script) {
				if (script.getIndicator() != null) {
					return script.getIndicator().getText();
				}
				return "none";
			}

			@Override
			public void set(Script script, String value) {
				value = eval(script, value);
				String text = value;
				if (script.getIndicator() == null) {
					Platform.runLater(() -> {
						script.setIndicator(new Indicator(text));
					});
				} else {
					if (value.equals("none")) {
						script.getIndicator().dispose();
						script.setIndicator(null);
						return;
					}
					script.getIndicator().setText(value);
				}
			}
			
		});
	}
	
	public Script compileScript(String script) {
		String source = script;
		script = script.replace("	", "");
		String[] lines = script.split("\n");
		Map<String, Variable> variables = new HashMap<>();
		registerSystemVariables(variables);
		Instruction[] instructions = new Instruction[lines.length];
		int lineNumber = 0;
		int depth = 0;
		for (String line : lines) {
			if (line.equals("")) {
				continue;
			}
			line = line.trim();
			String[] funcsplit = split(line, ' ', false);
			if (funcsplit.length == 0) {
				continue;
			}
			String funcname = funcsplit[0];
			Function function = functions.get(funcname);
			if (function != null) {
				String combine = "";
				for (int i = 1; i < funcsplit.length; i++) {
					combine += funcsplit[i] + " ";
				}
				instructions[lineNumber] = new RunFunction(function, combine);
			}
			bodyLoop:
			for (String bodyName : bodies.keySet()) {
				if (line.startsWith(bodyName)) {
					String args = line.replaceAll("^" + bodyName, "").trim();
					int startingDepth = depth;
					depth++;
					int recursedDepth = depth;
					int startLine = lineNumber;
					for (int i = startLine + 1; i < lines.length; i++) {
						for (String bodyNameRecursed : bodies.keySet()) {
							if (lines[i].startsWith(bodyNameRecursed)) {
								recursedDepth++;
							}
						}
						if (lines[i].equals("end")) {
							recursedDepth--;
							if (recursedDepth == startingDepth) {
								Body body = bodies.get(bodyName).getBody();
								instructions[startLine] = new StartBody(body, startLine, i, args);
								instructions[i] = new EndBody(body, startLine, i);
								break bodyLoop;
							}
						}
					}
				}
			}
			if (line.equals("end")) {
				depth--;
			}
			if (line.startsWith("$")) {
				String var = line.replaceAll("^\\$", "").split("=")[0].trim();
				if (!variables.containsKey(var)) {
					variables.put(var, new StandardVariable());
				}
				Variable variable = variables.get(var);
				if (variable != null) {
					instructions[lineNumber] = new SetVariable(variable, line.split("=")[1]);
				}
			}
			lineNumber++;
		}
		return new Script(this, instructions, variables, source);
	}
	
	public String eval(Script script, String expression) {
		expression = expression.trim();
		String[] vars = split(expression, '$', true);
		for (String var : vars) {
			if (!var.startsWith("$")) {
				continue;
			}
			var = split(var, ' ', false)[0].trim();
			Variable variable = script.variables.get(var.replace("$", ""));
			if (variable != null) {
				expression = expression.replace(var, variable.get(script).replace("-", "~"));
			}
		}
		if (!expression.contains("+") && !expression.contains("-") && !expression.contains("/") && !expression.contains("*")) {
			return expression;
		}
		expression = expression.replace(" - ", " -- ");
		expression = expression.replace(" ", "");
		String[] plusSplit = expression.split("\\+");
		if (plusSplit.length > 1) {
			int first = Integer.parseInt(plusSplit[0].trim().replace(" ", "").replace("~", "-"));
			int second = Integer.parseInt(plusSplit[1].trim().replace(" ", "").replace("~", "-"));
			expression = String.valueOf(first + second);
		}
		String[] minusSplit = expression.split("\\-\\-");
		if (minusSplit.length > 1) {
			int first = Integer.parseInt(minusSplit[0]);
			int second = Integer.parseInt(minusSplit[1]);
			expression = String.valueOf(first - second);
			return expression;
		}
		String[] divideSplit = split(expression, '/', false);
		if (divideSplit.length > 1) {
			int first = Integer.parseInt(divideSplit[0]);
			int second = Integer.parseInt(divideSplit[1]);
			expression = String.valueOf(first / second);
			return expression;
		}
		String[] timesSplit = split(expression, '*', false);
		if (timesSplit.length > 1) {
			int first = Integer.parseInt(timesSplit[0]);
			int second = Integer.parseInt(timesSplit[1]);
			expression = String.valueOf(first * second);
			return expression;
		}
		return expression;
	}
	
	public boolean evalConditional(Script script, String expression) {
		expression = expression.trim();
		if (expression.contains(" and ")) {
			String[] andSplit = expression.split(" and ");
			for (String string : andSplit) {
				if (!evalConditional(script, string)) {
					return false;
				}
			}
			return true;
		}
		if (expression.contains(" or ")) {
			String[] orSplit = expression.split(" or ");
			for (String string : orSplit) {
				if (evalConditional(script, string)) {
					return true;
				}
			}
			return false;
		}
		expression = expression.replace("\\and", "and");
		expression = expression.replace("\\or", "or");
		String[] vars = split(expression, '$', true);
		for (String var : vars) {
			if (!var.startsWith("$")) {
				continue;
			}
			Variable variable = script.variables.get(var.replace("$", ""));
			if (variable != null) {
				expression = expression.replace("$" + var, variable.get(script));
			}
		}
		if (expression.equals("true")) {
			return true;
		}
		if (expression.equals("false")) {
			return false;
		}
		String funcname = expression.split(" ")[0];
		Function function = functions.get(funcname);
		if (function != null) {
			String args = expression.replace(funcname, "");
			return function.run(script, args);
		}
		expression = expression.replace("= ", "=");
		expression = expression.replace(" =", "=");
		String[] equalsSplit = split(expression, '=', false);
		if (equalsSplit.length > 1) {
			return eval(script, equalsSplit[0]).equals(eval(script, equalsSplit[1]));
		}
		expression = expression.replace(" ", "");
		String[] greaterSplit = split(expression, '>', false);
		if (greaterSplit.length > 1) {
			int first = Integer.parseInt(eval(script, greaterSplit[0]));
			int second = Integer.parseInt(eval(script, greaterSplit[1]));
			return first > second;
		}
		String[] lesserSplit = split(expression, '<', false);
		if (lesserSplit.length > 1) {
			int first = Integer.parseInt(eval(script, lesserSplit[0]));
			int second = Integer.parseInt(eval(script, lesserSplit[1]));
			return first < second;
		}
		return false;
	}
	
	public String[] split(String string, char split, boolean includeSplitCharacter) {
		List<String> strings = new ArrayList<>();
		String combine = "";
		char[] chars = string.toCharArray();
		for (int pos = 0; pos < string.length(); pos++) {
			if (chars[pos] != split) {
				combine += chars[pos];
			} else {
				if (!combine.equals("")) {
					strings.add(combine);	
				}
				combine = "";
				if (includeSplitCharacter) {
					combine += split;
				}
			}
		}
		strings.add(combine);
		return strings.toArray(new String[strings.size()]);
	}
	
}
