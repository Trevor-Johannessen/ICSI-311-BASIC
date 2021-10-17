

public class StringNode extends StatementNode{

	private String value;
	
	
	public StringNode(String inValue) {
		value = inValue;
	}
	
	public String toString() {
		return "\"" +value +"\"";
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(this);
	}
	
}
