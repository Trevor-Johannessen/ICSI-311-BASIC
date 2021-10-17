

public class StatementNode extends Node{
	

	private StatementNode next;
	
	public String toString() {
		return null;
	}

	public Object getValue() {
		return null;
	}
	
	public boolean hasNext() {
		if(next != null)
			return true;
		return false;
	}

	@Override
	public void accept(VisitorInterface visitor) {
		visitor.visit(this);
	}
	
	public void setNext(StatementNode inNext) {
		next = inNext;
	}
	
	public StatementNode getNext() {
		return next;
	}
	
	public void displayAsLinkedList(StatementNode inNext) {
		System.out.println(inNext.toString() +" -> ");
		if(inNext.next != null)
			displayAsLinkedList(inNext.next);
		
	}
	
}
