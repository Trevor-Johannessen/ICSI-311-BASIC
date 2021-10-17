

public class LabeledStatementNode extends StatementNode{
	
	private String label;
	private StatementNode labeledStatement;

	public LabeledStatementNode(String inLabel, StatementNode inNode) {
		label = inLabel.substring(0, inLabel.length()-1);
		labeledStatement = inNode; 
	}

	public StatementNode getNode() {
		return labeledStatement;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String toString() {
		return label +"[" +labeledStatement.toString() +"]";
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(labeledStatement);
		visitor.visit(this);
	}
	
}
