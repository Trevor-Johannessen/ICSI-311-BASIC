

import java.util.ArrayList;

public class StatementsNode extends StatementNode{

	private ArrayList<StatementNode> nodeArr = new ArrayList<StatementNode>();
	
	public StatementsNode(ArrayList<StatementNode> inNodeArr) {
		nodeArr = inNodeArr;
	}
	
	public StatementNode getNode(int i) {
		return nodeArr.get(i);
	}
	
	public void setNode(int i, StatementNode inNode) {
		nodeArr.set(i, inNode);
	}
	
	public ArrayList<StatementNode> getNodeArray() {
		return nodeArr;
	}
	
	public int size() {
		return nodeArr.size();
	}
	
	public String toString() {
		String str = "";
		for(int i = 0; i < nodeArr.size(); i++) {
			str = str +nodeArr.get(i).toString() +",\n";
		}
		return str;
	}
	
	public void read() {
		System.out.println("READ:");
		for(int i = 0; i < nodeArr.size(); i++)
			System.out.println(i +": " +nodeArr.get(i).toString());
		System.out.println();
	}
	
	public void readAsLinkedList() {
		System.out.println(nodeArr.get(0).toString() +" -> ");
		nodeArr.get(0).displayAsLinkedList(nodeArr.get(0).getNext());
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		 for (int i = 0; i < nodeArr.size(); i++) {
	         nodeArr.get(i).accept(visitor);
	      }
	      visitor.visit(this);
	}
}
