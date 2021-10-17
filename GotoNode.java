

public class GotoNode extends StatementNode{
	private String label;
	
	public GotoNode(String inLabel) {
		label =  inLabel.substring(0, inLabel.length()-1);
	}
	
	public String getLabel() {
		return label;
	}
	
	public String toString() {
		return "Goto(" +label +")";
	}
	
}
