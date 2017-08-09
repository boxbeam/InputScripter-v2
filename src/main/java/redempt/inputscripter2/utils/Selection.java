package redempt.inputscripter2.utils;

public class Selection {
	
	private int startLine;
	private int endLine;
	
	public Selection(int startLine, int endLine) {
		this.startLine = startLine;
		this.endLine = endLine;
	}
	
	public int getStartLine() {
		return startLine;
	}
	
	public int getEndLine() {
		return endLine;
	}
	
}
