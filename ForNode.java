

public class ForNode extends StatementNode{

	private VariableNode initalize;
	private IntegerNode assignment;
	private Node limit;
	private Node increment;
	private Node after;
	
	public ForNode(VariableNode inInitalize, IntegerNode inAssignment, Node inLimit, Node inIncrement) {
		initalize = inInitalize;
		assignment = inAssignment;
		limit = inLimit;
		increment = inIncrement; 
	}
	
	public VariableNode getInitalize() {
		return initalize;
	}
	
	public IntegerNode getAssignment() {
		return assignment;
	}
	
	public Node getLimit() {
		return limit;
	}
	
	public Node getIncrement() {
		return increment;
	}
	
	public String toString() {
		return"for(" +initalize.toString() +" = " +assignment.toString() +" TO " +limit.toString() +" STEP " +increment.toString() +")";
	}
	
	public void setAfter(Node inAfter) {
		after = inAfter;
	}
	
	public Node getAfter() {
		return after;
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(initalize);
		visitor.visit(assignment);
		visitor.visit(limit);
		visitor.visit(increment);
		visitor.visit(this);
	}
}
