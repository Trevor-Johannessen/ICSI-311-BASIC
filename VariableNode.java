

public class VariableNode extends Node{

	private String name;
	
	public VariableNode(String inName) {
		name = inName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return name;
	}
	
	public void setValue(String inValue) {
		name = inValue;
	}
	
	public String toString() {
		return name;
	}
	
	public String cleanString() {
		String output = name;
		if(output.contains("IDENTIFIER(")) {
			output = output.replace("IDENTIFIER(", "");
			output = output.substring(0, output.length()-1);
		}
			
			
		return output;	
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(this);
	}
}
