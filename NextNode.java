

public class NextNode extends StatementNode{

	private VariableNode var;
	private ForNode parent; // i can't think of a good name atm
	
	public NextNode(VariableNode inVar /*like the metal*/) {
		var = inVar;
	}
	
	public VariableNode getValue() {
		return var;
	}
	
	public String toString() {
		return "NEXT(" +var.toString() +")";
	}
	
	public void setParent(ForNode inParent) {
		parent = inParent;
	}
	
	public ForNode getParent() {
		return parent;
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(var);
		visitor.visit(this);
	}
}
