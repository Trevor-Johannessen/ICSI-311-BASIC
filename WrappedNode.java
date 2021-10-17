

public class WrappedNode extends StatementNode{
	
	private Node wrappedNode;
	
	public WrappedNode(Node inNode) {
		wrappedNode = inNode;
	}
	
	public Node getNode() {
		return wrappedNode;
	}
}
