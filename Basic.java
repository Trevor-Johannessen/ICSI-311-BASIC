
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * PROGRAM FUNCTION:
 * 	
 * Take in ONE argument
 * this argument will be treated as a file (arg.txt)
 * 
 *
 */
public class Basic {

	
	public static void main(String[] argv) throws Exception
	{
		// if there are more or less than 1 argument
		if(argv.length != 1)
		{
			System.out.println("Invalid Input, there are " +argv.length +" arguments, there must only be 1.");
		
		}
		// if there is only one argument
		else
		{
			
			String fileName = get_file_name(argv[0]);						// gets filename from args
			Path test = Paths.get(fileName); 								// creates path for readAllLines
			List<String> strList = Files.readAllLines(test); 				// read all the lines from the file
			
			Lexer.knownWords.put("PRINT", Token.State.PRINT);
			Lexer.knownWords.put("READ", Token.State.READ);
			Lexer.knownWords.put("DATA", Token.State.DATA);
			Lexer.knownWords.put("INPUT", Token.State.INPUT);
			Lexer.knownWords.put("GOSUB", Token.State.GOSUB);
			Lexer.knownWords.put("GOTO", Token.State.GOTO);
			Lexer.knownWords.put("RETURN", Token.State.RETURN);
			Lexer.knownWords.put("FOR", Token.State.FOR);
			Lexer.knownWords.put("TO", Token.State.TO);
			Lexer.knownWords.put("STEP", Token.State.STEP);
			Lexer.knownWords.put("NEXT", Token.State.NEXT);
			Lexer.knownWords.put("IF", Token.State.IF);
			Lexer.knownWords.put("THEN", Token.State.THEN);
			ArrayList<Token> tokenList = new ArrayList<Token>(); 			// list for each line from lex

			
			String program = "";
			for(int i = 0; i < strList.size(); i++)
				program += strList.get(i) +" ";
			if(Interpreter.debug) {
				System.out.println("program = ");
				System.out.println(program);
			}
			boolean validList = true;
			try {
				tokenList = Lexer.lex(program); 						// call the Lex function to parse the line
			}catch(java.lang.Exception e) {
				
				System.out.println(e);
				validList = false;
				System.out.println();
			}
				
				
				
			if(validList)												// if the list is made of valid characters
			{
				for(int j = 0; j < tokenList.size(); j++)	
					if(Interpreter.debug)											// for each token returned print out its state
						System.out.print(tokenList.get(j).toString() +" ");
				System.out.println();
				
				if(Interpreter.debug)
					System.out.println("PARSER TIME");						// its parser time
					
					
				Parser parser = new Parser(tokenList);
				StatementsNode statements = parser.parse();
				if(Interpreter.debug)
					System.out.println("\n" +statements.toString() +"\n\n");
				
				Interpreter.interpreterSetup(statements);
				//parser.parse().accept(new Visitor());
				//System.out.println(parser.parse().toString());
				System.out.println();
			}
				
			
			
			
		}
		
		
	}
	
	/**
	 * Checks if file has .txt file extension, if not adds one
	 * @param input inputed file name
	 * @return formatted file name
	 */
	public static String get_file_name(String input)
	{
		if((input.length() > 4) && (input.substring(input.length()-4).equals(".txt")))
			return input;
		return input.concat(".txt");
	}
	
}
