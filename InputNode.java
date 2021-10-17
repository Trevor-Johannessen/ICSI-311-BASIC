

import java.util.List;

public class InputNode extends StatementNode {

	private String question;
	private List<VariableNode> varList;
	
	public InputNode(String inStr, List<VariableNode> inVarList) {
		question = inStr;
		varList = inVarList;
	}
	
	public InputNode(List<VariableNode> inVarList) {
		question = "";
		varList = inVarList;
	}
	
	public String toString() {
		String str = "INPUT{ ";
		if(!question.equals(""))
			str = str +"\"" +question +"\", ";
		for(int i = 0; i < varList.size(); i++)
			str = str +varList.get(i).toString() + ", ";
		str = str.substring(0, str.length()-2) +" }";
		return str;
	}
	
	
	public List<VariableNode> getList(){
		return varList;
	}
	
	public VariableNode getValue(int i ) {
		return varList.get(i);
	}
	
	public String getQuestion() {
		return question;
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		for(VariableNode currentNode : varList)
			visitor.visit(currentNode);
		visitor.visit(this);
	}
}
