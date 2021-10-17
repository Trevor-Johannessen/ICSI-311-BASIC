

import java.util.ArrayList;

public class CurledStatementNode extends StatementNode{
	private ArrayList<StatementNode> nodeList = new ArrayList<StatementNode>();
	
	public CurledStatementNode(ArrayList<StatementNode> input) {
		nodeList = input;
	}
	
	public int getSize() {
		return nodeList.size();
	}
	
	public StatementNode getNode(int i) {
		return nodeList.get(i);
	}
	
}
