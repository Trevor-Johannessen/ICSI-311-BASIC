


public class Token {

	
	public static enum State
	{
		NUMBER, PLUS, MINUS, TIMES, DIVIDE, EndOfLine, LPARAN, RPARAN, LCURL, RCURL, LESSTHAN, GREATERTHAN, LESSOREQUAL, GREATEROREQUAL, NOTEQUAL, STRING, WORD, LABEL, EQUALS, PRINT, IDENTIFIER, COMMA, READ, DATA, INPUT, GOSUB, GOTO, RETURN, FOR, TO, STEP, NEXT, IF, THEN, PASS, FAIL, STATELESS;
	}
	
	private String value;
	private State currentState;
	
	public Token(String inValue, State inState)
	{
		
		
		if(inValue != null)
			value = inValue;
		else
			value = "";
		if(inState != null)
			currentState = inState;
		else
			currentState = State.STATELESS;
	}
	
	/*
	 * Returns the current Tokens String value
	 * @return The Token Objects String value
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * Changes the Token's String value
	 * @param newValue The new String value for the Token
	 */
	public void setValue(String newValue)
	{
		value = newValue;
	}

	/**
	 * Returns Tokens State
	 * @return Current State of the Token
	 */
	public State getState()
	{
		return currentState;
	}
	
	/**
	 * Sets a new State for the Token
	 * @param inState the new State
	 */
	public void setState(State inState)
	{
		currentState = inState;
	}
	
	
	/**
	 * Overrides toString to provide correct output of Token
	 */
	public String toString()
	{
		switch(currentState)
		{
		
			case NUMBER: return "NUMBER(".concat(value).concat(")");
			
			case STRING: return "STRING(".concat(value).concat(")");
			
			case WORD: return "WORD(".concat(value).concat(")");
			
			case IDENTIFIER: return "IDENTIFIER(".concat(value).concat(")");
			
			case LABEL: return "LABEL(".concat(value).concat(")");
			
			case EndOfLine: return "EndOfLine";
			
			default: return this.currentState.toString();
		}
		

	}
	
}
