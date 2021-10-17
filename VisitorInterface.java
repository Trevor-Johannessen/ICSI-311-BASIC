

public interface VisitorInterface {
	
	public void visit(Node node);
	
	public void visit(StatementsNode visitNode);

	public void visit(AssignmentNode assignmentNode);

	public void visit(BooleanOperationNode booleanOperationNode);

	public void visit(DataNode dataNode);

	public void visit(FloatNode floatNode);

	public void visit(ForNode forNode);

	public void visit(FunctionNode functionNode);

	public void visit(GosubNode gosubNode);

	public void visit(IfNode ifNode);

	public void visit(InputNode inputNode);

	public void visit(IntegerNode integerNode);

	public void visit(LabeledStatementNode labeledStatementNode);

	public void visit(MathOpNode mathOpNode);

	public void visit(NextNode nextNode);

	public void visit(PrintNode printNode);

	public void visit(ReadNode readNode);

	public void visit(ReturnNode returnNode);

	public void visit(StatementNode statementNode);

	public void visit(VariableNode variableNode);

}
