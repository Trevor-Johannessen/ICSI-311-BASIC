

public class MathOpNode extends StatementNode{

	public enum Operation
	{
		ADD, SUBTRACT, MULTIPLY, DIVIDE;
	}
	
	private Operation operation;
	private Node rightNode;
	private Node leftNode;
	
	public MathOpNode(Node inLastNode, Operation inOp, Node inNextNode)
	{
		operation = inOp;
		rightNode = inNextNode;
		leftNode = inLastNode;
	}
	
	public Operation getOperation() {
		return operation;
	}

	public Node getLeftNode() {
		return leftNode;
	}
	
	public Node getRightNode() {
		return rightNode;
	}
	
	@Override
	public String toString() {
		String str = "MathNode(";
		str= str +leftNode.toString() +" ";
		str = str +operation.toString() +" ";
		str = str +rightNode.toString() +")";
		return str;
	}

	public Object getValue() {
		return this.toString();
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(rightNode);
		visitor.visit(leftNode);
		visitor.visit(this);
	}
}
