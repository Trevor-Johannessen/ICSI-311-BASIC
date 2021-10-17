

public class BooleanOperationNode extends Node {
	private Node leftNode;
	private Token operator;
	private Node rightNode;
	
	public BooleanOperationNode(Node inLeftNode, Token inOperator, Node inRightNode) {
		leftNode = inLeftNode;
		operator = inOperator;
		rightNode = inRightNode;
	}
	
	public Token getOperator() {
		return operator;
	}
	
	// can't lie, I dont know what to name these
	public Node getLeft() {
		return leftNode;
	}
	
	public Node getRight() {
		return rightNode;
	}
	
	public String toString() {
		return leftNode.toString() +" " +operator.toString() +" " +rightNode.toString();
	}
	
	@Override
	public Object getValue() {
		return this;
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(leftNode);
		visitor.visit(rightNode);
		visitor.visit(this);
	}

	

}
