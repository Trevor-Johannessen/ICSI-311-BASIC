

import java.util.ArrayList;
import java.util.List;

public class DataNode extends StatementNode{

	private List<Node> dataList;
	
	// create a new DataNode with some data
	public DataNode(List<Node> inDataList) {
		dataList = inDataList;
	}
	
	// create a new empty DataNode
	public DataNode() {
		dataList = new ArrayList<Node>();
	}
	
	// gets the List of data
	public List<Node> getList(){
		return dataList;
	}
	
	// gets a specific element of the dataList
	public Node getValue(int i) {
		return dataList.get(i);
	}
	
	// adds a Node to the dataList
	public void addNode(Node input) {
		dataList.add(input);
	}
	
	// prints the elements of the dataList 
	public String toString() {
		String str = "DATA{ ";
		for(int i = 0; i < dataList.size(); i++)
			str = str +dataList.get(i).getValue() + ", ";
		str = str.substring(0, str.length()-2) +" }";
		return str;
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		for(Node currentNode : dataList)
			visitor.visit(currentNode);
		visitor.visit(this);
	}
}
