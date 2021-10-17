

public class IntegerNode extends Node{

	private int value;

	public IntegerNode (int inValue)
	{
		value = inValue;
		
	}
	
	public Integer getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "INTEGER(" +value +")";
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(this);
	}
}
