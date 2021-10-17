

import java.util.List;

public class PrintNode extends StatementNode{

	private List<Node> nodeList;
	
	
	public PrintNode(List<Node> inNodeList) {
		nodeList = inNodeList;
	}

	
	public List<Node> getList() {
		return nodeList;
	}
	
	public Node getValue(int i) {
		return nodeList.get(i);
	}
	
	public String toString() {
		String str = "PRINT{ ";
		for(int i = 0; i < nodeList.size(); i++)
			str = str +nodeList.get(i).toString() + ", ";
		str = str.substring(0, str.length()-2) +" }";
		return str;
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		for(Node currentNode : nodeList)
			visitor.visit(currentNode);
		visitor.visit(this);
	}
	
}
