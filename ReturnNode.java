

public class ReturnNode extends StatementNode{

	
	public ReturnNode() {
		
	}
	
	public String toString() {
		return "RETURN";
	}
	
	public String getValue() {
		return "RETURN";
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(this);
	}
}
