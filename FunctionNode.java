

import java.util.List;

public class FunctionNode extends StatementNode{

	private String name;
	private List<Node> args;
	
	public FunctionNode(String inName, List<Node> inArgs) {
		name = inName;
		args = inArgs;
	}
	
	public FunctionNode(String inName) {
		name = inName;
		args = null;
	}
	
	public List<Node> getArgs() {
		return args;
	}
	
	public Node getArgs(int i) {
		return args.get(i);
	}
	
	public Node getArg(int i) {
		return args.get(i);
	}
	
	public int getArgc() {
		return args.size();
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		String str = name +"(";
		for(int i = 0; i < args.size(); i++)
			str+=args.get(i) +", ";
		if(args.size() > 0)
			str = str.substring(0, str.length()-2) +")";
		else
			str+=")";
		return str;
	}


	public Object getValue() {
		return null;
	}
	
	@Override
	public void accept(VisitorInterface visitor) {
		for(Node currentNode : args)
			visitor.visit(currentNode);
		visitor.visit(this);
	}
}
