


public class NodeTypeVisitor implements VisitorInterface{
	   
	@Override
	public void visit(StatementsNode node) {
		System.out.println("StatementsNode");
	}

	   
	@Override
	public void visit(AssignmentNode node) {
		System.out.println("AssignmentNode");
	}


	@Override
	public void visit(BooleanOperationNode node) {
		System.out.println("BooleanOperationNode");
		
	}


	@Override
	public void visit(DataNode node) {
		System.out.println("DataNode");
		
	}


	@Override
	public void visit(FloatNode node) {
		System.out.println("FloatNode");
		
	}


	@Override
	public void visit(ForNode node) {
		System.out.println("ForNode");
		
	}


	@Override
	public void visit(FunctionNode node) {
		System.out.println("FunctionNode");
		
	}


	@Override
	public void visit(GosubNode node) {
		System.out.println("GosubNode");
		
	}


	@Override
	public void visit(IfNode node) {
		System.out.println("IfNode");
		
	}


	@Override
	public void visit(InputNode node) {
		System.out.println("InputNode");
		
	}


	@Override
	public void visit(IntegerNode node) {
		System.out.println("IntegerNode");
		
	}


	@Override
	public void visit(LabeledStatementNode node) {
		System.out.println("LabeledStatementNode");
		
	}


	@Override
	public void visit(MathOpNode node) {
		System.out.println("MathOpNode");
		
	}


	@Override
	public void visit(NextNode node) {
		System.out.println("NextNode");
		
	}


	@Override
	public void visit(PrintNode node) {
		System.out.println("PrintNode");
		
	}


	@Override
	public void visit(ReadNode node) {
		System.out.println("ReadNode");
		
	}


	@Override
	public void visit(ReturnNode node) {
		System.out.println("ReturnNode");
		
	}


	@Override
	public void visit(StatementNode node) {
		System.out.println("StatementNode");
		
	}


	@Override
	public void visit(VariableNode node) {
		System.out.println("VariableNode");
		
	}


	@Override
	public void visit(Node node) {
		// TODO Auto-generated method stub
		
	}
}
