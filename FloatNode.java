

public class FloatNode extends Node{

	private float value;
	
	public FloatNode(float inValue){
		value = inValue;
	}

	public Float getValue() {
		return value;
	}
	
	
	@Override
	public String toString() {
		return "FLOAT";
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(this);
	}
	
}
