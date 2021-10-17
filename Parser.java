

import java.util.ArrayList;
import java.util.List;

public class Parser {

	private ArrayList<Token> tokenList = new ArrayList<Token>();
	private boolean inIfStatement = false;
	private DataNode heap = new DataNode();
	public Parser (ArrayList<Token> inTokenList) {
		tokenList = inTokenList;
	}
	
	
	/*
	 * Method to prase the token list
	 * returns the head of the AST
	 */
	public StatementsNode parse() throws Exception {
		return statements();
	}
	
	// finds all the statements in a line and creates nodes for them
	private  StatementsNode statements() throws Exception{
		StatementNode statementNode = null;
		ArrayList<StatementNode> nodeList = new ArrayList<StatementNode>(); 
		do { // while there are still nodes to create statements
			statementNode = statement(); // get statement
			if(statementNode != null) // if its not null add it to the list
				nodeList.add(statementNode);
			/*else {
				WrappedNode expression = new WrappedNode(expression());
				if(expression.getNode() != null) // if its not null add it to the list
					nodeList.add(expression);
			}*/
		}while(statementNode != null);
		/*
		Node nodeArray[] = new Node[nodeList.size()]; // convert the arraylist to an array (arrayList.toArray() returns an object for some reason)
		for(int i = 0; i < nodeList.size(); i++)
			nodeArray[i] = nodeList.get(i);
		
		StatementsNode returnNode = new StatementsNode(nodeArray);
		
		return returnNode;
		*/
		return new StatementsNode(nodeList);
	}
	
	/*
	 * gets a statement, if there is no statement returns null
	 */
	private StatementNode statement() throws Exception{
		
		if(Interpreter.debug)
			System.out.println("Called statement node. Next token = " +peek().toString());
		
		StatementNode output = null; // node to be returned

		// find if theres a variable followed by an equals
		if((matchAndPeek(Token.State.IDENTIFIER)) && (matchAndPeek(Token.State.EQUALS, 1))) {
			output = assignment();
		}
		else if((matchAndPeek(Token.State.IDENTIFIER)) && (matchAndPeek(Token.State.LPARAN, 1))) {
			output = function();
		}
		else if(matchAndPeek(Token.State.LABEL)) {
			output = labeledStatement();
		}
		else if(matchAndRemove(Token.State.GOSUB) != null) {
			output = gosubStatement();
		}
		else if(matchAndRemove(Token.State.GOTO) != null) {
			output = gotoStatement();
		}
		else if(matchAndRemove(Token.State.RETURN) != null) {
			output = new ReturnNode();
		}
		else if(matchAndRemove(Token.State.FOR) != null) {
			output = forStatement();
		}
		else if(matchAndRemove(Token.State.PRINT) != null) {
			output = printStatement();
		}
		else if(matchAndRemove(Token.State.NEXT) != null){
			output = nextStatement();
		}
		else if(matchAndRemove(Token.State.READ) != null) {
			output = readStatement();
		}
		else if(matchAndRemove(Token.State.DATA) != null) {
			output = dataStatement();
		}
		else if(matchAndRemove(Token.State.INPUT) != null) {
			output = inputStatement();
		}
		else if(matchAndRemove(Token.State.IF) != null){
			output = ifStatement();
		}
		else if(matchAndRemove(Token.State.LCURL) != null) {
			output = curledStatement();
		}
		else if(matchAndPeek(Token.State.STRING))
			output = stringExpression();
		// if there is no print or identifier it must be an expression
		
		return output;
	}

	private IfNode ifStatement() throws Exception{
		inIfStatement = true;
		BooleanOperationNode bool = booleanParser();
		inIfStatement = false;
		
		if(matchAndRemove(Token.State.THEN) == null)
			throw new Exception("Invalid IF, no THEN");
		
		
		if(!matchAndPeek(Token.State.LABEL))
			throw new Exception("Invalid IF, no label.");
		LabeledStatementNode expression = labeledStatement();
		
		return new IfNode(bool, expression);
	}
	
	private CurledStatementNode curledStatement() throws Exception {
		ArrayList<StatementNode> nodeList = new ArrayList<StatementNode>();
		
		do {
			nodeList.add(statement());
			if(Interpreter.debug)
				System.out.println("Added " +nodeList.get(nodeList.size()-1));
		}while(matchAndRemove(Token.State.RCURL) == null);
		
		
		return new CurledStatementNode(nodeList);
	}
	private BooleanOperationNode booleanParser() throws Exception {
		Node leftNode = expression();
		Token operator = remove();
		if(Interpreter.debug) {
			System.out.println("leftNode = " +leftNode.toString());
			System.out.println("Operator State = " +operator.getState().toString());
		}
		switch(operator.getState()) {
			case LESSTHAN: 
			case GREATERTHAN:
			case LESSOREQUAL:
			case GREATEROREQUAL:
			case NOTEQUAL:
			case EQUALS:
				// this is what happens when you write a switch statement, and then realize you only needed an If
				// but an if statement has so many parenthesis and boolean operators and at this point it would just be easier to use a rolling switch
				// honestly its not like its unreadable or anything, I'd think this is more readable than a == 1 or a == 2 or a==3 etc etc
				break;
			default:
				throw new Exception("Bad boolean statement, no operator.");
		}
		
		
		Node rightNode = expression();
		return new BooleanOperationNode(leftNode, operator, rightNode);
	}
	
	
	// allows you to label a statement
	private LabeledStatementNode labeledStatement() throws Exception{

		String label = matchAndRemove(Token.State.LABEL).getValue(); // gets the label
		
		return new LabeledStatementNode(label, statement()); // returns a labeledStatement object with the label and the statement
	}
	
	private GosubNode gosubStatement() throws Exception {
		GosubNode node;
		
		if(matchAndPeek(Token.State.LABEL))
			node = new GosubNode(matchAndRemove(Token.State.LABEL));
		else
			throw new Exception("Invalid GoSub Syntax. No label.");
		
		return node;
	}
	
	private GotoNode gotoStatement() throws Exception{
		String label; 
		
		label = matchAndRemove(Token.State.LABEL).getValue();
		
		if(label == null)
			throw new Exception("Invalid GoTo Syntax, no label");
		return new GotoNode(label);
	}
	
	
	
	// first part of forLoop is FOR
	private ForNode forStatement() throws Exception {
		
		VariableNode initialization = new VariableNode(matchAndRemove(Token.State.IDENTIFIER).toString()); // the second part of the forloop is an assignment
		if(initialization.getValue().contains("IDENTIFIER("))
			initialization.setValue(initialization.getValue().substring(11, initialization.getValue().length()-1));
		
		
		if(matchAndRemove(Token.State.EQUALS) == null)
			throw new Exception("Bad forloop, missing =");
		
		IntegerNode assignment = new IntegerNode(Integer.parseInt(matchAndRemove(Token.State.NUMBER).getValue()));
		
		if(matchAndRemove(Token.State.TO) == null)
			throw new Exception("Bad forloop, missing TO"); // third part is TO
		
		Node limit = expression(); // incase theres an expression as the limit and not just a factor
		Node step;
		
		if(matchAndRemove(Token.State.STEP) != null) { // fifth-ish part is STEP
			
			step = expression(); // same thing with expression, may or may not be a factor
		}
		else
			step = new IntegerNode(1); // if theres no step returns integerNode with 1
		
		return new ForNode(initialization, assignment, limit, step); // returns forloop node
		
	}
	
	// declares the end for a forloop
	private NextNode nextStatement() throws Exception{
		if(matchAndPeek(Token.State.IDENTIFIER)) // if there is a variable after NEXT (ex. NEXT a ends the a forloop)
			return new NextNode(new VariableNode(matchAndRemove(Token.State.IDENTIFIER).getValue())); // return a NextNode that holds a VariableNode
		else
			throw new Exception("Bad Next Statement"); // bad syntax (if you forget a variable after NEXT)
	}
	
	// takes a list of expressions and makes a print list
	private PrintNode printStatement() throws Exception{
		
		
		ArrayList<Node> printArray = new ArrayList<Node>();
		// Print statements only carry Strings and Expressions
		do {
			if(matchAndPeek(Token.State.STRING)) // if String
				printArray.add(stringExpression());
			else // if Expression 
				printArray.add(expression());
			
		}while(matchAndRemove(Token.State.COMMA) != null); // while there are more members of the printlist
		
		PrintNode returnNode = new PrintNode(printArray); // create a printNode object with the printArray 
		
		return returnNode;
	}
	
	// takes a list of comma seperated variables and makes a read list
	private ReadNode readStatement() throws Exception{
		List<VariableNode> readArray = new ArrayList<VariableNode>(); // creates a List of VariableNodes which will be read
		
		do {
			if(matchAndPeek(Token.State.IDENTIFIER)) // if theres a variable, add it to the read list
				readArray.add(new VariableNode(matchAndRemove(Token.State.IDENTIFIER).getValue()));
			
		}while(matchAndRemove(Token.State.COMMA) != null); // while there are more statements to read
		
		return new ReadNode(readArray);
	}
	
	// takes a list of Strings, Integers, and Floats and constructs a data list. Also maintains a global datalist
	private DataNode dataStatement() throws Exception{
		List<Node> outputList = new ArrayList<Node>(); // list for output
		do {
			if(matchAndPeek(Token.State.STRING)) { // String handling
				heap.addNode(new StringNode(matchAndRemove(Token.State.STRING).getValue())); // add to the global datalist
				outputList.add(heap.getValue(heap.getList().size()-1)); // add it to the returend datalist
				}
			else if((matchAndPeek(Token.State.IDENTIFIER)) && (matchAndPeek(Token.State.LPARAN, 1))) {
				heap.addNode(expression());
				outputList.add(heap.getValue(heap.getList().size()-1));
			}
			else if(matchAndPeek(Token.State.NUMBER)) // Int/Float handling
			{
				float number = Float.parseFloat(matchAndRemove(Token.State.NUMBER).getValue()); // first assume its a float
				if(number == (int)number) { // if its an integer, this will return true
					heap.addNode(new IntegerNode((int)number)); // add integer to the global list
					outputList.add(new IntegerNode((int)number)); // add integer to the returned list
				}
				else {
					heap.addNode(new FloatNode(number)); // add float to the global list
					outputList.add(new FloatNode(number)); // add float to the returend list
				}
			}
			else // bad data sad times
				throw new Exception("Exception: Tried to input something other than String or Number into Data{}");
			
			
		}while(matchAndRemove(Token.State.COMMA) != null); // while there are more elements in data
		return new DataNode(outputList);
	}
	
	// handeling of INPUT
	private InputNode inputStatement() throws Exception{
		if(Interpreter.debug)
			System.out.println("\n\n\nINPUT STATEMENT TRIGGERED\n\n");
		String question = null;																					// the string at the beginning of the INPUT statement (if there is no String, it stays null)
		List<VariableNode> varList = new ArrayList<VariableNode>(); // create a list for variables in INPUT
		
		if(matchAndPeek(Token.State.STRING)) {  // see if theres a String (Prompt)
			question = matchAndRemove(Token.State.STRING).getValue(); // set the question String and remove the String Token
			matchAndRemove(Token.State.COMMA);
		}
		
		else if(matchAndPeek(Token.State.IDENTIFIER)) { // if there is an identifier assign it to the list manually so everything is in position for while
			varList.add(new VariableNode(matchAndRemove(Token.State.IDENTIFIER).getValue()));
			matchAndRemove(Token.State.COMMA);
		}
		
		do{ // while there are still commas
			
			if(matchAndPeek(Token.State.IDENTIFIER)) // if its an identifier add it to the list
				varList.add(new VariableNode(matchAndRemove(Token.State.IDENTIFIER).getValue()));
			
			else // if a variable isnt given throw an exception
				throw new Exception("Exception: non-variable passed to INPUT\t" +question);
		}while(matchAndRemove(Token.State.COMMA) != null);
		
		if(question == null) // if there was no question prompt
			return new InputNode(varList); // use the Question-less constructor
		else // if there was a question prompt
			return new InputNode(question, varList); // use the Question constructor
		
		
	}
	
	
	// creates a variable node with the proper value and name
	private AssignmentNode assignment() throws Exception{
		
		VariableNode varNode = new VariableNode(matchAndRemove(Token.State.IDENTIFIER).getValue()); // get the name of the variable
		//System.out.println("VarNode = " +varNode.toString());
		
		if(matchAndRemove(Token.State.EQUALS) == null) // dont really need the token for anything
			throw new Exception("Forloop has bad assignment statement.");
		
		AssignmentNode output = new AssignmentNode(varNode, expression()); // get the value of the variable
		
		return output;
	}
	
	private StringNode stringExpression() throws Exception{
		StringNode returnNode = null;
		if(matchAndPeek(Token.State.STRING))
			returnNode = new StringNode(matchAndRemove(Token.State.STRING).getValue());
		return returnNode;
	}
	
	
	private FunctionNode function() throws Exception{
		
		String name = matchAndRemove(Token.State.IDENTIFIER).getValue(); // get the name of the function
		List<Node> argsList = new ArrayList<Node>(); // create a list to store the arguments in
		if(matchAndRemove(Token.State.LPARAN) == null) // check if there isn't a L paran (shouldn't happen)
			throw new Exception("Invalid function, no '(' (note this should not be possible)");
		if(matchAndRemove(Token.State.RPARAN) == null) { // if the function has arguments
			do{
				
				if(matchAndPeek(Token.State.STRING)) // if its an String add it to the list
					argsList.add(new StringNode(matchAndRemove(Token.State.STRING).getValue()));
				else  // if its an expression, add it to the arg list
					argsList.add(expression());
				if(argsList.get(argsList.size()-1) == null) // if there is no string or expression
					throw new Exception("Exception: bad String or Expression passed to Function");
			}while(matchAndRemove(Token.State.COMMA) != null);  // while there are still commas
		}
		matchAndRemove(Token.State.RPARAN);
		//System.out.println("name = " +name);
		if(Interpreter.debug)
			System.out.println("next token in function = " +peek().toString());
		
		return new FunctionNode(name, argsList); // return the function node with the name and arguments 
	}
	
	private Node expression() throws Exception {
		Node leftNode;
		Node rightNode;
		//boolean foundOperator = true;
		
		//System.out.println("next token brefore expression check = " +peek().toString());
		if((matchAndPeek(Token.State.IDENTIFIER)) && (matchAndPeek(Token.State.LPARAN, 1))) { // check if there is a function
			leftNode = function(); // call the function method
			if((leftNode != null) && (Interpreter.debug))
				System.out.println("left node is not null");
		}
		else {
			leftNode = term(); // sets the left Node as a term
			
			
			Token sign = matchAndRemove(Token.State.PLUS); // if the next token is a +
			if(sign == null)
				sign = matchAndRemove(Token.State.MINUS); // if the next token is a -
			if(sign != null) {
				rightNode = term(); // if there was a + or - that means there should be another term in the Expression
				MathOpNode expressionOperation = new MathOpNode(leftNode, convert(sign.getState()), rightNode); // create new MathOpNode with the left, right, and operation nodes
				
				while((sign !=null) && (matchAndRemove(Token.State.RPARAN) == null)) { // Handles successive additive operations (1 + 1 + 1 + 1)
					sign = null;
					sign = matchAndRemove(Token.State.PLUS); // if the next token is a plus
					if(sign == null)
						sign = matchAndRemove(Token.State.MINUS); // if the next token in a minus
					if(sign != null) {
						expressionOperation = new MathOpNode(expressionOperation, convert(sign.getState()), term()); // if the next token is an operation, create a new MathNode which will expand the previous mathNode
					}
					
				}
				
				return expressionOperation; // return the completed additive block
			}
			
		}
		
		return leftNode; // if there is only a number and no +- operation
		
	}
	
	private Node term() throws Exception {
		Node leftNode = factor(); // get the left Node
		Node rightNode;
		
		Token sign = matchAndRemove(Token.State.TIMES); // if the next token is a *
		if(sign == null)
			sign = matchAndRemove(Token.State.DIVIDE); // if the next token is a /
		if(sign != null) {
			rightNode = factor(); // if there is a * or / there must be another factor
			MathOpNode termOperation = new MathOpNode(leftNode, convert(sign.getState()), rightNode); // create a MathOpNode with the two factors and the operation
			
			while((sign !=null) && (matchAndRemove(Token.State.RPARAN) == null)) { // whle for successive operations
				sign = null;
				sign = matchAndRemove(Token.State.TIMES); // find *
				if(sign == null)
					sign = matchAndRemove(Token.State.DIVIDE); // find /
				if(sign != null) {
					termOperation = new MathOpNode(termOperation, convert(sign.getState()), factor()); // create a chain of multiplication or division ( 1 * 1 * 1 * 1 )
				}
				
			}
			
			return termOperation; // return completed multiplicative statement
		}
		
		
		
		return leftNode; // if there is no operation just return the factor
	}
	
	// returns a FloatNode or IntegerNode or the value returned from EXPRESSION
	private Node factor() throws Exception {
		
		
		Node factorNode = null; // node must be a number or expression
		
		Token factor = matchAndRemove(Token.State.LPARAN); // check for left paran
		if(factor != null)
			factorNode = expression(); // if there is one call expression
		else if(matchAndPeek(Token.State.NUMBER)) { // if theres no left paran it must be a number
			factor = matchAndRemove(Token.State.NUMBER); // find the number
			if(factor.getValue().contains(".")) // if it contains a . its a float, the lexer should assure we have correct form (no 1.2.3 will appear)
				factorNode = new FloatNode(Float.parseFloat(factor.getValue())); // assign the float value
			else
				factorNode = new IntegerNode(Integer.parseInt(factor.getValue())); // assign the interger value
		}
		else if((matchAndPeek(Token.State.IDENTIFIER)) && ((!matchAndPeek(Token.State.EQUALS, 1)) || (inIfStatement == true))	) {
			factorNode = new VariableNode(matchAndRemove(Token.State.IDENTIFIER).getValue());
			
		}
		return factorNode; // return the node
	}
	
	
	/*
	 * finds the next token, if its what we request it will return and remove it, else it returns null
	 */
	private Token matchAndRemove(Token.State inState) throws Exception {
		if(tokenList.get(0).getState() == inState) { // see if the next token is the token we want
			Token lexeme = tokenList.get(0); // if so then grab and return that
			tokenList.remove(0);
			return lexeme;
		}
			
		else // no token returns null
			return null;
	}
	
	private Token remove() throws Exception{
		Token lexeme = tokenList.get(0);
		tokenList.remove(0);
		return lexeme;
	}
	
	private Token peek() throws Exception{
		return tokenList.get(0);
	}
	
	private boolean matchAndPeek(Token.State inState) throws Exception {
		if(tokenList.get(0).getState() == inState) { // see if the next token is the token we want
			return true;
		}
		else // no token returns null
			return false;
	}
	
	private boolean matchAndPeek(Token.State inState, int offset) throws Exception {
		if(tokenList.get(0+offset).getState() == inState) { // see if the next token is the token we want
			return true;
		}
		else // no token returns null
			return false;
	}
	
	
	/*
	 * converts a Token State Enum to a MathOpNode Operation Enum
	 */
	private MathOpNode.Operation convert(Token.State inState) {
		switch(inState) {
		case PLUS: return MathOpNode.Operation.ADD;
		case MINUS: return MathOpNode.Operation.SUBTRACT;
		case TIMES: return MathOpNode.Operation.MULTIPLY;
		case DIVIDE: return MathOpNode.Operation.DIVIDE;
		default: return null;
		}
	}
	
}
