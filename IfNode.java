



public class IfNode extends StatementNode{

	private BooleanOperationNode bool;
	private LabeledStatementNode expression;
	
	public IfNode(BooleanOperationNode inBool, LabeledStatementNode inExpression) {
		bool = inBool;
		expression = inExpression;
	}
	
	public BooleanOperationNode getBool() {
		return bool;
	}
	
	public LabeledStatementNode getExpression() {
		return expression;
	}
	
	public String toString() {
		return "IF " +bool.toString() +" THEN " +expression.toString();
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(bool);
		visitor.visit(expression);
		visitor.visit(this);
	}
}
