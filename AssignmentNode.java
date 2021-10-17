

public class AssignmentNode extends StatementNode{

	private Node value;
	private VariableNode varNode;
	
	/*
	 * Holds a variable and what its assignment should be
	 */
	public AssignmentNode(VariableNode inVarNode, Node inValue) {
		value = inValue;
		varNode = inVarNode;
	}
	
	// returns the variable (the A in a = 20)
	public VariableNode getVarNode() {
		return varNode;
	}
	
	// returns the value (the 20 in a = 20)
	public Node getValue() {
		return value;
	}
	
	// to string
	public String toString() {
		return varNode.toString() +" = " +value.toString();
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(value);
		visitor.visit(varNode);
		visitor.visit(this);
	}
}
