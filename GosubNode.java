

public class GosubNode extends StatementNode{
	private Token identifier;
	
	public GosubNode(Token inIdentifier) {
		identifier = inIdentifier;
	}
	
	public Token getValue() {
		return identifier;
	}
	
	public String getLabel() {
		return identifier.getValue().substring(0, identifier.getValue().length()-1);
	}
	
	public String toString() {
		return "GOSUB(" +identifier.getValue() +")";
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(this);
	}
}
