


public class Visitor implements VisitorInterface{
	   
	@Override
	public void visit(StatementsNode node) {
		System.out.println(node.toString());
	}

	   
	@Override
	public void visit(AssignmentNode node) {
		System.out.println(node.toString());
	}


	@Override
	public void visit(BooleanOperationNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(DataNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(FloatNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(ForNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(FunctionNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(GosubNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(IfNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(InputNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(IntegerNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(LabeledStatementNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(MathOpNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(NextNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(PrintNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(ReadNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(ReturnNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(StatementNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(VariableNode node) {
		System.out.println(node.toString());
		
	}


	@Override
	public void visit(Node node) {
		// TODO Auto-generated method stub
		
	}
}
