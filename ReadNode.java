

import java.util.List;

public class ReadNode extends StatementNode{

	private List<VariableNode> varList;
	
	public ReadNode(List<VariableNode> inVarList) {
		varList = inVarList;
	}
	
	// returns the list of variableNodes
	public List<VariableNode> getList(){
		return varList;
	}
	
	// returns a specific variableNode
	public VariableNode getValue(int i) {
		return varList.get(i);
	}
	
	// formats READ into a readable format.
	public String toString() {
		String str = "READ{ ";
		for(int i = 0; i < varList.size(); i++)
			str = str +varList.get(i).toString() + ", ";
		str = str.substring(0, str.length()-2) +" }";
		return str;
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		for(VariableNode currentNode : varList)
			visitor.visit(currentNode);
		visitor.visit(this);
	}
	
}
