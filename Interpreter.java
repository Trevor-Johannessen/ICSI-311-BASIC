

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import interpreter2.MathOpNode.Operation;

public class Interpreter {

	public static HashMap<String, Integer> varInteger = new HashMap<String, Integer>();
	public static HashMap<String, Float> varFloat = new HashMap<String, Float>();
	public static HashMap<String, String> varString = new HashMap<String, String>();
	
	public static HashMap<String, Node> labels = new HashMap<String, Node>();
	
	public static ArrayList<Node> dataList = new ArrayList<Node>();
	
	public static StatementsNode statements;
	
	public static boolean debug = false; // change this for some print statements
	
	private static Stack<Node> nodeStack = new Stack<Node>();
	
	private static int currentStatementPosition;
	private static StatementNode currentNode;
	private static StatementNode lastNode;
	
	public static void interpreterSetup(StatementsNode inStatements) throws Exception {
		
		statements = inStatements;
		if(debug)
			statements.read();
		
		initalize();
		
		if(debug)
			System.out.println("Statements: \n" +statements.toString() +"\n\n\n");
		
	
		//for(currentStatementPosition = 0; currentStatementPosition < statements.size(); currentStatementPosition++)
			//interpret(statements.getNode(currentStatementPosition));
			
		currentNode = statements.getNode(0);
		interpret(currentNode);
		do {
			lastNode = currentNode;
			currentNode = currentNode.getNext();
			
			interpret(currentNode);

		}while(currentNode.getNext() != null);
		
	
		
			
	} // end of interpreter (main)
	
	public static void interpret(StatementNode statement) throws Exception {
		
		if(debug) {
			System.out.println("Current Node: " +statement.toString());
			System.out.println("Current Type: " +statement.getClass());
		}
		
		if(statement.getClass().equals(ReadNode.class)) { // read node
			read((ReadNode)statement);
		}
		else if(statement.getClass().equals(AssignmentNode.class)) { // assign node
			assign((AssignmentNode)statement);
		}
		else if(statement.getClass().equals(InputNode.class)) { // input node
			input((InputNode)statement);
		}
		else if(statement.getClass().equals(PrintNode.class)) { // print node
			print((PrintNode) statement);
		}
		else if(statement.getClass().equals(IfNode.class)) { // print node
			ifStatement((IfNode) statement);
		}
		else if(statement.getClass().equals(GotoNode.class)) { // print node
			goTo((GotoNode) statement);
		}
		else if(statement.getClass().equals(GosubNode.class)) { // print node
			goSub((GosubNode) statement);
		}
		else if(statement.getClass().equals(ReturnNode.class)) { // print node
			returnStatement((ReturnNode) statement);
		}
		else if(statement.getClass().equals(ForNode.class)) {
			forStatement((ForNode) statement);
		}
		else if(statement.getClass().equals(CurledStatementNode.class)) {
			curledStatement((CurledStatementNode) statement);
		}
		// disclaimer:
		// I know none of this does anything, this was my first implementation, its like half practice, half not crashing if you just place a random function somewhere
		else if(statement.getClass().equals(FunctionNode.class)) { // function call
			FunctionNode node = (FunctionNode) statement;
			
			// find the name of the function and call the function
			
			if(node.getName().equals("RANDOM")) {
				callRandom(node); 
			}
			else if(node.getName().equals("LEFT$")) {
				callLeft(node);
			}
			else if(node.getName().equals("RIGHT$")) {
				callRight(node);
			}
			else if(node.getName().equals("NUM$")) {
				callNum(node);
			}
			else if(node.getName().equals("VAL")) {
				callValInt(node);
			}
			else if(node.getName().equals("VAL%")) {
				callValFloat(node);
			}
		}
		
		
	}
	/*
	public static void goSub(GosubNode input) throws Exception {
		currentStatementPosition++; // set to next node in statementsNode
		nodeStack.push(statements.getNode(currentStatementPosition)); // push the node to the stack
		statements.getNodeArray().remove(currentStatementPosition); // remove the node from the list
		currentStatementPosition--;
		interpret((StatementNode)labels.get(input.getLabel())); // set node to the labeled node
	}
	*/
	
	public static void goSub(GosubNode input) throws Exception {
		//nodeStack.push(currentNode.getNext()); // push the node to the stack
		//currentNode.setNext(currentNode.getNext().getNext());
		if(debug) {
			System.out.println("CurrentNode = " +currentNode.toString());
			System.out.println("CurrentNode = " +currentNode.getNext().toString());
			System.out.println("CurrentNode = " +currentNode.getNext().getNext().toString());
			System.out.println("label = " +input.getLabel());
			System.out.println("\ninterpreting labeled statement\n");
			interpret((StatementNode) labels.get(input.getLabel()));
			System.out.println();
		

			System.out.println("pushing " +currentNode.getNext().toString());
		}
		nodeStack.push(currentNode.getNext()); // push the node to the stack
		lastNode.setNext(currentNode.getNext().getNext());
		currentNode = (StatementNode) labels.get(input.getLabel());
	}
	
	/*
	public static void forStatement(ForNode input) throws Exception{
		String initial = input.getInitalize().getValue(); // the initial variable
		//System.out.println("initial = " +initial);
		varInteger.put(initial, input.getAssignment().getValue()); // put the initial variable and its assignment in the int var map
		int limit;
		int step;
		//System.out.println("limit class = " +input.getLimit().getClass());
		if(input.getLimit().getClass().equals(IntegerNode.class)) // if the limit is just an integer
			limit = ((IntegerNode)input.getLimit()).getValue();
		else if(input.getLimit().getClass().equals(VariableNode.class)) // if the limit is an int variable
			limit = varInteger.get(((VariableNode)input.getLimit()).getName());
		else if(input.getLimit().getClass().equals(MathOpNode.class)) // if the limit is an expression
			limit = evaluateIntMathOp((MathOpNode)input.getLimit());
		else
			throw new Exception("Something wrong with limit in forloop, not integer, intmathop or variable"); // if the limit is none of the above
	
		if(input.getIncrement().getClass().equals(IntegerNode.class)) // if the increment is an integer
			step = ((IntegerNode)input.getIncrement()).getValue();
		else if(input.getIncrement().getClass().equals(VariableNode.class)) // if the increment is an int variable
			step = varInteger.get(((VariableNode)input.getIncrement()).getName());
		else if(input.getIncrement().getClass().equals(MathOpNode.class)) // if the increment is an expression
			step = evaluateIntMathOp((MathOpNode)input.getIncrement());
		else
			throw new Exception("Something wrong with step in forloop, not integer, intmathop or variable"); // if the icnremenet is none of the above
		
		boolean loop = true; // while loop is true, continue looping through the forloop
		
		currentStatementPosition++; // increment current statement
		if(debug)
			System.out.println("Current StatementPosition = " +currentStatementPosition);
		
		while(loop) {
			
			interpret(statements.getNode(currentStatementPosition)); // interpret the next statement
			
			// if the condition is met set loop to false;
			
			if(varInteger.get(initial).intValue()+2 > limit) // if the for variable is greater than the limit exit the loop
				loop = false;
			else
				varInteger.put(initial, varInteger.get(initial).intValue()+step); // else increment the var variable
			
			
		}
	}
	*/
	
	public static void forStatement(ForNode input) throws Exception{
		String initial = input.getInitalize().getValue(); // the initial variable
		//System.out.println("initial = " +initial);
		varInteger.put(initial, input.getAssignment().getValue()); // put the initial variable and its assignment in the int var map
		int limit;
		int step;
		//System.out.println("limit class = " +input.getLimit().getClass());
		if(input.getLimit().getClass().equals(IntegerNode.class)) // if the limit is just an integer
			limit = ((IntegerNode)input.getLimit()).getValue();
		else if(input.getLimit().getClass().equals(VariableNode.class)) // if the limit is an int variable
			limit = varInteger.get(((VariableNode)input.getLimit()).getName());
		else if(input.getLimit().getClass().equals(MathOpNode.class)) // if the limit is an expression
			limit = evaluateIntMathOp((MathOpNode)input.getLimit());
		else
			throw new Exception("Something wrong with limit in forloop, not integer, intmathop or variable"); // if the limit is none of the above
	
		if(input.getIncrement().getClass().equals(IntegerNode.class)) // if the increment is an integer
			step = ((IntegerNode)input.getIncrement()).getValue();
		else if(input.getIncrement().getClass().equals(VariableNode.class)) // if the increment is an int variable
			step = varInteger.get(((VariableNode)input.getIncrement()).getName());
		else if(input.getIncrement().getClass().equals(MathOpNode.class)) // if the increment is an expression
			step = evaluateIntMathOp((MathOpNode)input.getIncrement());
		else
			throw new Exception("Something wrong with step in forloop, not integer, intmathop or variable"); // if the icnremenet is none of the above
		
		boolean loop = true; // while loop is true, continue looping through the forloop
		
		currentNode = currentNode.getNext(); // increment current statement
		
		
		while(loop) {
			
			interpret(currentNode); // interpret the next statement
			
			// if the condition is met set loop to false;
			
			if(varInteger.get(initial).intValue()+2 > limit) // if the for variable is greater than the limit exit the loop
				loop = false;
			else
				varInteger.put(initial, varInteger.get(initial).intValue()+step); // else increment the var variable
			
			
		}
	}
	/*
	public static void returnStatement(ReturnNode input) throws Exception {
		interpret((StatementNode)nodeStack.pop());
	}
	*/
	
	public static void returnStatement(ReturnNode input) throws Exception {
		lastNode.setNext(currentNode.getNext()); // remove the return node from the list
		currentNode = (StatementNode)nodeStack.pop(); // set the current node to the last node on the stack

		interpret(currentNode);
	}
	public static void read(ReadNode input) throws Exception {
		char lastChar;
		List<VariableNode> varList = input.getList();
		System.out.println("Enter Read");
		//System.out.println(dataList.toString());
		
		for(int i = 0; i < varList.size(); i++) {
			lastChar = varList.get(i).getName().charAt(varList.get(i).getName().length()-1);
			if(debug) {
			System.out.println("Next data type = " +dataList.get(0).getClass());
			System.out.println("Next Variable type = " +varList.get(i));
			System.out.println("Variable name = " +varList.get(i).getName());
			System.out.println("lastChar = " +lastChar);
			}
			
			if(lastChar == '$') { // expecting string
				if(debug)
					System.out.println("In StringNode");
				
				if(!dataList.get(0).getClass().equals(StringNode.class)) // if not string throw exception
					throw new Exception("Exception, data requested not String");
				
				varString.put(varList.get(i).getName(), ((StringNode)dataList.get(0)).getValue()); // else puts string into variable
			}
			else if(lastChar == '%') { // exception float
				if(debug) 
					System.out.println("In FloatNode");
				
				if(!dataList.get(0).getClass().equals(FloatNode.class)) // if no float throw exception
					throw new Exception("Exception, data requested not Float");
					
				varFloat.put(varList.get(i).getName(), ((FloatNode)dataList.get(0)).getValue()); // else put float in map
			}
			else if(dataList.get(0).getClass().equals(FunctionNode.class)) { // if function
				
				// finds what function based on name
				// places the variable in whatever map it belongs to
				if(((FunctionNode)dataList.get(0)).getName().equals("RANDOM"))
					varInteger.put(varList.get(i).getName(), random());
				else if(((FunctionNode)dataList.get(0)).getName().equals("LEFT$"))
					varString.put(varList.get(i).getName(), callLeft((FunctionNode)dataList.get(0)));
				else if(((FunctionNode)dataList.get(0)).getName().equals("RIGHT$"))
					varString.put(varList.get(i).getName(), callRight((FunctionNode)dataList.get(0)));
				else if(((FunctionNode)dataList.get(0)).getName().equals("MID$"))
					varString.put(varList.get(i).getName(), callMid((FunctionNode)dataList.get(0)));
				else if(((FunctionNode)dataList.get(0)).getName().equals("NUM$"))
					varString.put(varList.get(i).getName(), callNum((FunctionNode)dataList.get(0)));
				else if(((FunctionNode)dataList.get(0)).getName().equals("VAL"))
					varInteger.put(varList.get(i).getName(), callValInt((FunctionNode)dataList.get(0)));
				else if(((FunctionNode)dataList.get(0)).getName().equals("VAL%"))
					varFloat.put(varList.get(i).getName(), callValFloat((FunctionNode)dataList.get(0)));
			}
			else{ // expecting integer
				if(debug)
					System.out.println("In IntegerNode");
				if(dataList.get(0).getClass().equals(IntegerNode.class)) // if integer add to map
					varInteger.put(varList.get(i).getName(), ((IntegerNode)dataList.get(0)).getValue());

				
				
			}
			dataList.remove(0); // remove from the data list
		}
		
		lastNode.setNext(currentNode.getNext());
	}

	public static void assign(AssignmentNode input) throws Exception {
		VariableNode var = input.getVarNode();
		Node node = input.getValue();
		char lastChar = var.getName().charAt(var.getName().length()-1); // get last chartacter (looking for $ or %)
		
		if(lastChar == '$') // if string
			varString.put(var.getName(), ((StringNode)node).getValue()); // put string in map
		else if(lastChar == '%') // if float
			varFloat.put(var.getName(), ((FloatNode)node).getValue()); // put float in map
		else if(node.getClass().equals(FunctionNode.class)) { // this assigns functions to their correct maps, im not explaning each one.
			if(((FunctionNode)node).getName().equals("RANDOM")) 				// ^
				varInteger.put(var.getName(), callRandom((FunctionNode)node));	// ^
			else if(((FunctionNode)node).getName().equals("LEFT$"))				// ^
				varString.put(var.getName(), callLeft((FunctionNode)node));		// ^
			else if(((FunctionNode)node).getName().equals("RIGHT$"))			// ^
				varString.put(var.getName(), callRight((FunctionNode)node));	// ^
			else if(((FunctionNode)node).getName().equals("MID$"))				// ^
				varString.put(var.getName(), callMid((FunctionNode)node));		// ^
			else if(((FunctionNode)node).getName().equals("NUM$"))				// ^
				varString.put(var.getName(), callNum((FunctionNode)node));		// ^
			else if(((FunctionNode)node).getName().equals("VAL"))				// ^
				varInteger.put(var.getName(), callValInt((FunctionNode)node));	// ^
			else if(((FunctionNode)node).getName().equals("VAL%"))				// ^
				varFloat.put(var.getName(), callValFloat((FunctionNode)node));	// ^
		}
		else  // if int
			varInteger.put(var.getName(), ((IntegerNode)node).getValue()); // put int in map
		
	}
	
	public static void input(InputNode input) {
		System.out.println(input.getQuestion()); // ask question
		
		Scanner in = new Scanner(System.in);
		String response;
		char lastChar;
		boolean cont;
		
		for(int i = 0; i < input.getList().size(); i++) { // for each expected response (# of variables)
			cont = false; // loops until acceptable answer is input (inputed?)
			while(!cont) {
				try {
					response = in.nextLine(); // get line from console
					lastChar = input.getList().get(i).getName().charAt(input.getList().get(i).getName().length()-1); // last char of the current variable (looking for $ or %)
					if(debug) {
						System.out.println("Last char = " +lastChar);
						System.out.println("Input name = " +input.getList().get(i).getName());
					}
					
					if(lastChar == '$'){ // expecting String

						varString.put(input.getList().get(i).getName(), response); // put string in map
					}
					else if(lastChar == '%') { // expecting float

						varFloat.put(input.getList().get(i).getName(), Float.parseFloat(response)); // putting float in map
					}
					else {

						varInteger.put(input.getList().get(i).getName(), Integer.parseInt(response)); // put int in map
					}
					cont = true; // got a good answer
				}catch(java.lang.NumberFormatException e) { // bad input
					System.out.println("Bad input, please input data matching: " +input.getList().get(i).getName()); 
					System.out.println(input.getQuestion()); // repeat question
				} // end of catch
			} // end of while
		} // end of for
		
		
	} // end of method
	
	public static void print(PrintNode input) throws Exception {
		List<Node> printList = input.getList();
		for(int i = 0; i < printList.size(); i++) { // for each thing to be printed
			if(debug)
				System.out.println("\n PRINT: current class = " +printList.get(i).getClass());
			
			if(printList.get(i).getClass().equals(StringNode.class))  // if printing a string
				System.out.print(printList.get(i).toString().replace("\"", ""));
			
			else if(printList.get(i).getClass().equals(VariableNode.class)) { // if printing a variable
				VariableNode node = (VariableNode)printList.get(i);
				
				if(node.cleanString().charAt(node.cleanString().length()-1) == '$') // print string
					System.out.print(varString.get(node.cleanString()));
				
				else if(node.cleanString().charAt(node.cleanString().length()-1) == '%') // print float
					System.out.print(varFloat.get(node.cleanString()));
				
				else
					System.out.print(varInteger.get(node.cleanString()));
			}
			else if(printList.get(i).getClass().equals(FunctionNode.class)) { // if printing a function
				//System.out.println("Printing function " +((FunctionNode)printList.get(i)).getName());
				
				// same thing as all other function blocks, finds what function to call based on name
				// please refer to any other block, its a lot to comment
				if(((FunctionNode)printList.get(i)).getName().equals("RANDOM"))
					System.out.print(callRandom((FunctionNode)printList.get(i)));
				else if(((FunctionNode)printList.get(i)).getName().equals("LEFT$"))
					System.out.print(callLeft((FunctionNode)printList.get(i)));
				else if(((FunctionNode)printList.get(i)).getName().equals("RIGHT$"))
					System.out.print(callRight((FunctionNode)printList.get(i)));
				else if(((FunctionNode)printList.get(i)).getName().equals("MID$"))
					System.out.print(callMid((FunctionNode)printList.get(i)));
				else if(((FunctionNode)printList.get(i)).getName().equals("NUM$"))
					System.out.print(callNum((FunctionNode)printList.get(i)));
				else if(((FunctionNode)printList.get(i)).getName().equals("VAL"))
					System.out.print(callValInt((FunctionNode)printList.get(i)));
				else if(((FunctionNode)printList.get(i)).getName().equals("VAL%"))
					System.out.print(callValFloat((FunctionNode)printList.get(i)));
			}
			
			else if(printList.get(i).getClass().equals(MathOpNode.class)) { // printing an expression
				boolean type = mathOpPrimitive((MathOpNode)printList.get(i));
				if(type) // if integer
					System.out.print(evaluateIntMathOp(printList.get(i)));
				else // if float
					System.out.print(evaluateFloatMathOp(printList.get(i)));
			}
			else if(printList.get(i).getClass().equals(IntegerNode.class)) // if int
				System.out.print(evaluateIntMathOp(printList.get(i)));
			else if(printList.get(i).getClass().equals(FloatNode.class)) // if float
				System.out.print(evaluateFloatMathOp(printList.get(i)));
			System.out.print(" "); // add space
		}
		System.out.println(); // add line
	}
	
	// calls random, nothing confusing here
	public static int callRandom(FunctionNode node) {
		return random();
	}
	public static int random() {
		Random r = new Random();
		return r.nextInt();
	}
	
	// calls the left function with correct parameters
	public static String callLeft(FunctionNode node) throws Exception {
		if(node.getArgc() != 2) { // left takes exactly 2 parameters
			throw new Exception("LEFT takes 2 arguments.");
		}
		String arg1; // first arg must be String
		int arg2; // second arg must be int
		
		// arg1 can be either String or String variable
		if(node.getArg(0).getClass().equals(StringNode.class))
			arg1 = ((StringNode)node.getArg(0)).getValue();
		else if(node.getArg(0).getClass().equals(VariableNode.class))
			arg1 = varString.get(((VariableNode)node.getArg(0)).getValue());
		else // if arg 1 is neither String nor variable
			throw new Exception("LEFT$ arg 1 is not String nor Variable");
		
		// arg2 must be either int or int variable
		if(node.getArg(1).getClass().equals(IntegerNode.class))
			arg2 = ((IntegerNode)node.getArg(1)).getValue();
		else if(node.getArg(1).getClass().equals(VariableNode.class))
			arg2 = varInteger.get(((StringNode)node.getArg(1)).getValue());
		else // if there is no int or variable int then throw exception bad input
			throw new Exception("LEFT$ arg 2 is not integer nor Variable");
		
	return left(arg1, arg2); // call the actual left function
	}
	// get the character n places into the string (from the left)
	public static String left(String input, int place) {
		return Character.toString(input.charAt(place));
	}
	
	// calls the right function with correct parameters
	public static String callRight(FunctionNode node) throws Exception {
		if(node.getArgc() != 2) { // right takes exactly 2 arguments
			throw new Exception("RIGHT takes 2 arguments.");
		}
		String arg1; // first arg must be string
		int arg2; // second arg must be int
		
		// check arg 1 for string or string variable
		if(node.getArg(0).getClass().equals(StringNode.class))
			arg1 = ((StringNode)node.getArg(0)).getValue();
		else if(node.getArg(0).getClass().equals(VariableNode.class))
			arg1 = varString.get(((StringNode)node.getArg(0)).getValue());
		else // if there is no string or string var then throw exception for bad input
			throw new Exception("RIGHT$ arg 1 is not String nor Variable");
		
		// arg2 can be either int or int variable
		if(node.getArg(1).getClass().equals(IntegerNode.class))
			arg2 = ((IntegerNode)node.getArg(1)).getValue();
		else if(node.getArg(1).getClass().equals(VariableNode.class))
			arg2 = varInteger.get(((StringNode)node.getArg(1)).getValue());
		else // if there is no int or int var then bad data, throw exception
			throw new Exception("RIGHT$ arg 2 is not integer nor Variable");
		
	return right(arg1, arg2); // call actual right function
	}
	// returns character n places from the right of the string
	public static String right(String input, int place) {
		return Character.toString(input.charAt(input.length()-place-1));
	}
	
	
	// calls the mid function with the proper arguments
	public static String callMid(FunctionNode node) throws Exception {
		if(node.getArgc() != 3) { // if there is not exactly 3 arguments throw exception
			throw new Exception("MID takes 3 arguments.");
		}
		String arg1; // first arg is always string
		int arg2; // second arg is always int
		int arg3; // third arg is alawys int
		
		// arg 1
		// first arg is either a variable or a String
		if(node.getArg(0).getClass().equals(StringNode.class))
			arg1 = ((StringNode)node.getArg(0)).getValue();
		else if(node.getArg(0).getClass().equals(VariableNode.class))
			arg1 = varString.get(((StringNode)node.getArg(0)).getValue());
		else // if there is no variable or String throw exception for bad input
			throw new Exception("MID$ arg 1 is not String nor Variable");
		
		// arg 2
		// arg 2 must be an integer or variable
		if(node.getArg(1).getClass().equals(IntegerNode.class))
			arg2 = ((IntegerNode)node.getArg(1)).getValue();
		else if(node.getArg(1).getClass().equals(VariableNode.class))
			arg2 = varInteger.get(((StringNode)node.getArg(1)).getValue());
		else // if there's no int or var throw exception for bad input
			throw new Exception("MID$ arg 2 is not integer nor Variable");
		
		// arg 3
		// arg 3 must be an integer or variable
		if(node.getArg(2).getClass().equals(IntegerNode.class))
			arg3 = ((IntegerNode)node.getArg(2)).getValue();
		else if(node.getArg(2).getClass().equals(VariableNode.class))
			arg3 = varInteger.get(((StringNode)node.getArg(2)).getValue());
		else // if there's no int or var throw exception for bad input
			throw new Exception("MID$ arg 3 is not integer nor Variable");
		
	return mid(arg1, arg2, arg3); // call actual mid function
	}
	public static String mid(String input, int start, int end) {
		return input.substring(start, end);
	}
	
	// since num(int) and num(float) both return String we can combine their call in callNum
	public static String callNum(FunctionNode node) throws Exception {
		
		int arg1 = 0; // argument if integer
		float arg2 = 0f; // argument if float
		
		// arg 1
		try {
			if(node.getArg(0).getClass().equals(IntegerNode.class)) // if theres an integer
				arg1 = ((IntegerNode)node.getArg(0)).getValue(); // set the integer arg to its value
			else if(node.getArg(0).getClass().equals(VariableNode.class)) // if theres a varaible
				arg1 = varInteger.get(((StringNode)node.getArg(0)).getValue()); // try to get variable, if it doens't exist, will null pointer
			return num(arg1); // return the Int -> String method
		}catch(java.lang.NullPointerException e){} // catch bad variable call (as if its a float Variable)
		
		// arg 2
		try {
			if(node.getArg(0).getClass().equals(FloatNode.class)) // if there's a float
				arg2 = ((FloatNode)node.getArg(0)).getValue(); // set float arg to its value
			else if(node.getArg(0).getClass().equals(VariableNode.class)) // if there's a variable
				arg2 = varFloat.get(((StringNode)node.getArg(0)).getValue()); // try to find variable in hashmap, if none throws null pointer
			return num(arg2); // return Float -> String method
		}catch(java.lang.NullPointerException e){
			throw new Exception("NUM$ arg 1 is not integer nor Variable nor float"); // if neither variable works, throw exception
		}
	
	}
	
	public static String num(int a) {
		return Integer.toString(a);
	}
	public static String num(float a) {
		return Float.toString(a);
	}
	
	// makes it easier to call the real function, parses the parameters
	public static int callValInt(FunctionNode node) throws Exception {
		String arg1;
		
		if(node.getArg(0).getClass().equals(StringNode.class)) // checks if we're passed a string
			arg1 = ((StringNode)node.getArg(0)).getValue();
		else if(node.getClass().equals(VariableNode.class)) // checks if we're passed a variable
			arg1 = varString.get(((StringNode)node.getArg(0)).getValue());
		else
			throw new Exception("VAL arg 1 is not String nor Variable");
		 
		return valInt(arg1); // runs the real function
	}
	public static int valInt(String input) {
		return Integer.parseInt(input);
	}
	
	// converts BASIC input to Java input, makes it easier to call function
	public static float callValFloat(FunctionNode node) throws Exception {
		String arg1; // VAL only takes one argument
		
		if(node.getArg(0).getClass().equals(StringNode.class)) // checks if we're pasesed a String
			arg1 = ((StringNode)node.getArg(0)).getValue();
		else if(node.getClass().equals(VariableNode.class)) // checks if we're passed a variable
			arg1 = varString.get(((StringNode)node.getArg(0)).getValue());
		else
			throw new Exception("VAL% arg 1 is not String nor Variable"); // bad data
		
		return valFloat(arg1); // call the real function and return its value
	}
	public static float valFloat(String input) {
		return Float.parseFloat(input);
	}
	
	
	
	// returns true if the math op should use an integer, false if math op should use float
	public static boolean mathOpPrimitive(MathOpNode input) throws Exception {
		if(input.getLeftNode().getClass().equals(IntegerNode.class)) // if we're dealing with an int
			return true; 
		else if(input.getLeftNode().getClass().equals(FloatNode.class)) // if we're dealing with a float
			return false;
		else if(input.getLeftNode().getClass().equals(MathOpNode.class)) // if its an operation
			return mathOpPrimitive((MathOpNode)input.getLeftNode()); // recursively go down until we find a int/float/variable
		else if(input.getLeftNode().getClass().equals(VariableNode.class)) { // if its a variable
			if(varInteger.containsKey(((VariableNode)input.getLeftNode()).getName())) // check both hashmaps with bias towards integers
				return true;
			else if(varFloat.containsKey(((VariableNode)input.getLeftNode()).getName())) // if its found in one it will return true/false
				return false;
			else
				throw new Exception("Somehow left node of MathOpNode is not integer, float, mathOpNode, or Int/Float variable"); // bad data
		}
		else
			throw new Exception("Somehow left node of MathOpNode is not integer, float, mathOpNode, or Int/Float variable"); // bad data
	}
	
	public static boolean variablePrimitive(VariableNode input) throws Exception {
		if(varInteger.containsKey(input.getName()))
			return true;
		else if(varFloat.containsKey(input.getName()))
			return false;
		else
			throw new Exception("Variable is not contianed within the int or float hashmaps");
	}
	
	
	// evaluate that returns an int
	public static int evaluateIntMathOp(Node input) throws Exception {
		if(input.getClass().equals(IntegerNode.class)) // if its an integer with no operation
			return ((IntegerNode)input).getValue(); // return the integer
		else if(input.getClass().equals(FloatNode.class)) // if its a float
			return Math.round(((FloatNode)input).getValue()); // convert the float to an int
		else if(input.getClass().equals(VariableNode.class)) // if its a variable
			return varInteger.get(((VariableNode)input).getName()); // get the variable from the integer map
		else if(input.getClass().equals(MathOpNode.class)) { // if its an operation
			int left = evaluateIntMathOp(((MathOpNode)input).getLeftNode()); // get left node as int
			int right = evaluateIntMathOp(((MathOpNode)input).getRightNode()); // get right node as int
			Operation mathOp = ((MathOpNode)input).getOperation(); // get operatrion
			
			switch(mathOp) { // preform operation
			case ADD:
				return left + right;
			case SUBTRACT:
				return left - right;
			case MULTIPLY:
				return left * right;
			case DIVIDE:
				return left / right;
			}
			
			return 0; // this shouldn't return
		}
		else
			throw new Exception("Something went wrong in evaluateIntMathOp INT"); // this shouldn't really return
	}
	
	// evaluate that returns a float
	public static float evaluateFloatMathOp(Node input) throws Exception {
		if(input.getClass().equals(FloatNode.class)) // if we have just a float, no operation
			return ((FloatNode)input).getValue(); // return the float
		else if(input.getClass().equals(IntegerNode.class)) // if we have just an integer
			return ((IntegerNode)input).getValue(); // convert int to float and return value
		else if(input.getClass().equals(VariableNode.class)) // if we have a variable
			return varFloat.get(((VariableNode)input).getName()); // get the variable from the float list, if its an int variable, please stop trying to break my program I have 2 tests in the next week
		else if(input.getClass().equals(MathOpNode.class)) { // if its an operation
			float left = evaluateFloatMathOp(((MathOpNode)input).getLeftNode()); // get left node
			float right = evaluateFloatMathOp(((MathOpNode)input).getRightNode()); // get right node
			Operation mathOp = ((MathOpNode)input).getOperation(); // get operation
			
			switch(mathOp) { // preform operation
			case ADD:
				return left + right; 
			case SUBTRACT:
				return left - right;
			case MULTIPLY:
				return left * right;
			case DIVIDE:
				return left / right;
			}
			
			return 0f; // this should not happen
		}
		else
			throw new Exception("Something went wrong in evaluateFloatMathOp FLOAT"); // this will return if you are mean
	}

	
	public static boolean evaluateBoolean(BooleanOperationNode input) throws Exception {
		boolean arg1Type;
		boolean arg2Type;
		Node arg1 = input.getLeft();
		Node arg2 = input.getRight();
		Token operator = input.getOperator();
		
		if(debug) {
			System.out.println("Arg1 = " +arg1.toString());
			System.out.println("Arg2 = " +arg2.toString());
		}
		
		// arg1 type check
		if(arg1.getClass().equals(MathOpNode.class)) // if arg1 is an operation
			arg1Type = mathOpPrimitive((MathOpNode)arg1); // set type to the mathOpPrimitive result (checks if operation is int or float)
		else if(arg1.getClass().equals(IntegerNode.class)) // if arg1 is an integer
			arg1Type = true; // set type to integer
		else if(arg1.getClass().equals(FloatNode.class)) // if arg1 is float
			arg1Type = false; // set type to float
		else if(arg1.getClass().equals(VariableNode.class)) // if arg1 is a variable node
			arg1Type = variablePrimitive((VariableNode)arg1);
		else
			throw new Exception("arg1 is not a mathOp, integer, or float Node");
		
		// arg2 type check
		if(arg2.getClass().equals(MathOpNode.class)) // if arg2 is an operation
			arg2Type = mathOpPrimitive((MathOpNode)arg2);  // set type to the mathOpPrimitive result (checks if operation is int or float)
		else if(arg2.getClass().equals(IntegerNode.class)) // if arg2 is an integer
			arg2Type = true;// set type to integer
		else if(arg2.getClass().equals(FloatNode.class)) // if arg2 is float
			arg2Type = false; // set type to float
		else if(arg1.getClass().equals(VariableNode.class)) // if arg2 is a variable node
			arg2Type = variablePrimitive((VariableNode)arg2);
		else
			throw new Exception("arg2 is not a mathOp, integer, or float Node");
		
		
		if(arg1Type != arg2Type) // if arguemnts are not both one type (both int or both float)
			throw new Exception("Boolean arguments are not of the same type");
		
		// int boolean checking
		if(arg1Type) {
			switch(operator.getState()) {
			case LESSTHAN:
				return evaluateIntMathOp(arg1) < evaluateIntMathOp(arg2);
			case GREATERTHAN:
				return evaluateIntMathOp(arg1) > evaluateIntMathOp(arg2);
			case LESSOREQUAL:
				return evaluateIntMathOp(arg1) <= evaluateIntMathOp(arg2);
			case  GREATEROREQUAL:
				return evaluateIntMathOp(arg1) >= evaluateIntMathOp(arg2);
			case EQUALS:
				return evaluateIntMathOp(arg1) == evaluateIntMathOp(arg2);
			case NOTEQUAL:
				return evaluateIntMathOp(arg1) != evaluateIntMathOp(arg2);
			}
		}
		else {
			switch(operator.getState()) {
			case LESSTHAN:
				return evaluateFloatMathOp(arg1) < evaluateFloatMathOp(arg2);
			case GREATERTHAN:
				return evaluateFloatMathOp(arg1) > evaluateFloatMathOp(arg2);
			case LESSOREQUAL:
				return evaluateFloatMathOp(arg1) <= evaluateFloatMathOp(arg2);
			case  GREATEROREQUAL:
				return evaluateFloatMathOp(arg1) >= evaluateFloatMathOp(arg2);
			case EQUALS:
				return evaluateFloatMathOp(arg1) == evaluateFloatMathOp(arg2);
			case NOTEQUAL:
				return evaluateFloatMathOp(arg1) != evaluateFloatMathOp(arg2);
			}
		}
		throw new Exception("Something went very wrong in boolean evaluation, bad operator maybe?");
		
	}
	
	public static void ifStatement(IfNode input) throws Exception {
		if(debug) {
			System.out.println("Entered If Statement");
			System.out.println("Boolean = " +input.getBool().toString());
		}
		boolean evaluation = evaluateBoolean(input.getBool()); // evaluate boolean
		
		 
		if(evaluation) { // if boolean, do thing
			interpret(input.getExpression().getNode());
		}

	}
	
	/*
	public static void goTo(GotoNode input) throws Exception { // go to a specified labeled statement
		if(debug) {
			System.out.println("Entered GoTo Statement");
			System.out.println("Key = " +input.getLabel());
			System.out.println((StatementNode)labels.get(input.getLabel()));
		}
		
		interpret((StatementNode)labels.get(input.getLabel())); // interpret the labeled statement
	}
	*/
	
	public static void goTo(GotoNode input) throws Exception { // go to a specified labeled statement
		if(debug) {
			System.out.println("Entered GoTo Statement");
			System.out.println("Key = " +input.getLabel());
			System.out.println((StatementNode)labels.get(input.getLabel()));
		}
		currentNode = (StatementNode)labels.get(input.getLabel()); // set current node to the labeled node
	}
	
	// initalize the interpreters StatementsNode
	public static void initalize() {
		swapLabels(); // swap the labels
		matchLoops(); // match forloops with their Next counterparts
		inputData(); // store inputted data
		linkStatements(); // link all statements in a linked list
		
		//for(int i = 0; i < dataList.size(); i++)
			//System.out.println(dataList.get(i).getValue());
	}
	
	// notes labels and replaces them with statements in the list
	public static void swapLabels() {
		for(int i = 0; i < statements.getNodeArray().size(); i++) { // for each node in the statements array
			//System.out.println("current Node: " +statements.getNode(i).toString());
			if(statements.getNode(i).getClass().equals(LabeledStatementNode.class)) { // if the node is an instance of the LabeledStatementNode class
				//System.out.println("IS LABELED STATEMENT");
				LabeledStatementNode labeled = (LabeledStatementNode) statements.getNode(i); // convert the Node (generic) to labeled statement node
				labels.put(labeled.getLabel(), labeled.getNode()); // add the node to the hashmap
				statements.setNode(i, labeled.getNode()); // add the unlabeled statement back into the list
			}
		}
	}
	
	// matches forloops with their NEXT counterparts
	public static void matchLoops() {
		Stack<ForNode> forNodeStack = new Stack<ForNode>(); // stack to match forloops
		
		for(int i = 0; i < statements.getNodeArray().size(); i++) { // for each node in the statements array
			
			//System.out.println("current Node: " +statements.getNo de(i).toString());
			if(statements.getNode(i).getClass().equals(ForNode.class)) { // if the node is an instance of the ForNode class
				//System.out.println("IS FOR NODE");
				forNodeStack.push((ForNode) statements.getNode(i)); // store the forNode so we can use it later
			}
			else if(statements.getNode(i).getClass().equals(NextNode.class)){ // if the node is an instance of the NextNode class
				//System.out.println("IS NEXT NODE");
				NextNode next = (NextNode) statements.getNode(i); // convert the Node (generic) to NextStatement node
				next.setParent(forNodeStack.peek()); // set the parent of the Next Node as the top forLoop on the stack
				forNodeStack.pop().setAfter(statements.getNode(i+1)); // set the ForNodes after statement
			}
			//System.out.println("Stack Size: " +forNodeStack.size());
		}
		
	}
	
	// stores all inputted data into the public variables
	public static void inputData() {
		//System.out.println("length = " +statements.getNodeArray().size());
		for(int i = 0; i < statements.getNodeArray().size(); i++) { // for each node in the statements array
			//System.out.println("current Node: " +statements.getNode(i).toString());
			if(statements.getNode(i).getClass().equals(DataNode.class)) { // if the node is an instance of the DataNode class
				//System.out.println("IS DATA NODE");
				DataNode data = (DataNode) statements.getNode(i);// convert the Node (generic) to DataStatement node
				
				for(Node current : data.getList()) // for all data in the dataNode
					dataList.add(current); // add each piece of data
				statements.getNodeArray().remove(i); // remove the DataNode
				i-=1; // backtrack one since the array will have moved back due to .remove()
			}
		}
		if(debug)
			System.out.println("DataArrSize = " +dataList.size());
	}
	
	// sets the next reference to the next statement in the list
	public static void linkStatements() {
		

		for(int i = 0; i < statements.getNodeArray().size()-1; i++) { // if its not the end of the list
			statements.getNode(i).setNext(statements.getNode(i+1)); // set the reference of each node to its successor
		}
		// print out each statements Next Node (as an array)
		/*
		for(int i = 0; i < statements.size(); i++) 
			System.out.println(i +": " +(statements.getNode(i).getNext() == null));
		*/
		
		// reads each node as a linked list
		//statements.readAsLinkedList();
	}

	
	public static void curledStatement(CurledStatementNode input) throws Exception {
		
		for(int i = 0; i < input.getSize(); i++)
			interpret(input.getNode(i));
		
	}
	
	
	
} // end of class
