


import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Lexer {
	
	public static HashMap<String, Token.State> knownWords = new HashMap<String, Token.State>();

	
	
	
	
	public enum LoopState 									// states for iterating through the string
	{
		PASS, FAIL;
	}
	
	public static ArrayList<Token> lex(String input) throws Exception
	{
		ArrayList<Token> tokenList = new ArrayList<Token>(); 																					// tokens to be returned
		
		LoopState state = LoopState.PASS; 																										// set the state for the loop
		
		if(input.length() > 0) 																													// if the loop is empty then just skip everything and add an EndOfLine Token
			state = LoopState.FAIL;

		int word = 0;																															// the current word
		int i = 0; 																																// the current character
		boolean acceptableToken = false; 																										// if the character is acceptable 
		
		while(state != LoopState.PASS)
		{
			//System.out.println("loop " +i);
			char letter = input.charAt(i);																										// get character
			tokenList.add(new Token("", null));
			
			/*
			 * Have hard coded hash map of words like Print
			 * Words not in the map are labelled as Identifiers
			 * If word is followed by : its a LABEL
			 * 
			 * Need to make sure Words cannot be followed by two % or $
			 */
			// word start
			if(((65 <= letter) && (letter <= 90))     ||     ((97 <= letter) && (letter <= 122))) // if the character is a letter
			{
				boolean found_word_end = false; 																								// has the end of the word been found
				String str = ""; 																												// the string used to create the token
				
				while(!found_word_end)																											// while we haven't found the word end
				{
					if (
						(i >= input.length()) || 																								// if we have gone through the entire string
						(input.charAt(i) == ' ') || 
							!((65 <= input.charAt(i)) && (input.charAt(i) <= 90) || 															// if it is NOT an letter or symbol
							((97 <= input.charAt(i)) && (input.charAt(i) <= 122)) ||
							((input.charAt(i) == '%') || (input.charAt(i) == '$') || (input.charAt(i) == ':'))))
							found_word_end = true;							
					else 
					{
						if((input.charAt(i) == '%') || (input.charAt(i) == '$') || (input.charAt(i) == ':'))									// if a Symbol is found, this word is done
							found_word_end = true;
							//ending_characters++;
							
						str = str.concat(Character.toString(input.charAt(i)));																	// add the character to the String
						i++;																													// move to next character
					}

				}
				
				
				
			
				tokenList.get(word).setValue(str);																								// Set the String of the Token
				tokenList.get(word).setState(knownWords.get(str));																				// set its token to a Known Word if it has one, if not, sets to null
				if(tokenList.get(word).getState() == null)																						// if there is no known word
				{
					if(str.charAt(str.length()-1) == ':')																						// if its a label
						tokenList.get(word).setState(Token.State.LABEL);
					else
					tokenList.get(word).setState(Token.State.IDENTIFIER);
				}
				word++;
			}
			
			
			
			
			
			else
			{
				switch(letter) 																													// assign the token based on character
				{
				case ' ':
					tokenList.remove(tokenList.size()-1);
					i++;
					break;
					
				case ',':
					tokenList.get(word).setValue(",");
					tokenList.get(word).setState(Token.State.COMMA);
					i++;
					word++;
					break;
					
				case '+': 																														// set a PLUS token
					tokenList.get(word).setValue("+");
					tokenList.get(word).setState(Token.State.PLUS);
					i++; 																														// skip forward two characters (The current character and the space
					word++;
					break;
					
				case '*': 																														// set a TIMES token
					tokenList.get(word).setValue("*");
					tokenList.get(word).setState(Token.State.TIMES);
					i++;
					word++;
					break;
					
				case '/': 																														// set a DIVIDE token
					tokenList.get(word).setValue("/");
					tokenList.get(word).setState(Token.State.DIVIDE);
					i++;
					word++;
					break;
				
				case '=':																														// set an EQUAL token
					tokenList.get(word).setValue("/");
					tokenList.get(word).setState(Token.State.EQUALS);
					i++;
					word++;
					break;
				
				case '<':																														// set a LESSTHAN or LESSOREQUAL Token
					if((i < input.length()-1) && (input.charAt(i+1) == '>'))
					{
						tokenList.get(word).setValue("<>");
						tokenList.get(word).setState(Token.State.NOTEQUAL);
						i = i + 2;		
					}else if((i < input.length()-1) && (input.charAt(i+1) == '='))
					{
						tokenList.get(word).setValue("<=");
						tokenList.get(word).setState(Token.State.LESSOREQUAL);
						i = i + 2;
					}else
					{
						tokenList.get(word).setValue("<");
						tokenList.get(word).setState(Token.State.LESSTHAN);
						i++;
					}
					
					word++;
					break;
					
				case '>':																														// set a GREATERTHAN or GREATEROREQUAL Token
					if((i < input.length()-1) && (input.charAt(i+1) == '='))
					{
						tokenList.get(word).setValue(">=");
						tokenList.get(word).setState(Token.State.GREATEROREQUAL);
						i = i + 2;
					}else
					{
						tokenList.get(word).setValue(">");
						tokenList.get(word).setState(Token.State.GREATERTHAN);
						i++;
					}
					
					word++;
					break;
				
				case '(':																														// set a LPARAN Token
					tokenList.get(word).setValue("(");
					tokenList.get(word).setState(Token.State.LPARAN);
					i++;
					word++;
					break;
				
				case ')':																														// set a RPARAN Token
					tokenList.get(word).setValue(")");
					tokenList.get(word).setState(Token.State.RPARAN);
					i++;
					word++;
					break;
					
				case '{':
					tokenList.get(word).setValue("{");
					tokenList.get(word).setState(Token.State.LCURL);
					i++;
					word++;
					break;
					
				case '}':
					tokenList.get(word).setValue("}");
					tokenList.get(word).setState(Token.State.RCURL);
					i++;
					word++;
					break;
					
				case '"':																														// set a STRING Token
						boolean found_close_quote = false;
						i++;
						String str = "";
						while(!found_close_quote)																								// while we haven't found the closing quote
						{
							if(input.charAt(i) == '"') 																							// if we find the closing quote
								found_close_quote = true;
							else
								str = str.concat(Character.toString(input.charAt(i)));
							
							i++;
							if((i == input.length()) && (found_close_quote == false))																							// if we run out of characters then there is no closing quote
								throw new Exception("Incomplete String");
						}
						tokenList.get(word).setValue(str);
						tokenList.get(word).setState(Token.State.STRING);
						word++;
					break;
				
				case '-': 																															// set a MINUS token
					if(	(i == input.length()-1) ||  ((i < input.length()-1) && ((word > 0) && ((tokenList.get(word-1).getState() == Token.State.NUMBER)) ||	(tokenList.get(word-1).getState() == Token.State.IDENTIFIER)	)	) )
					{
						tokenList.get(word).setValue("-");
						tokenList.get(word).setState(Token.State.MINUS);
						i++;
						word++;
						break;
					}
					
				default: 																															// for either Numbers or invalid characters

					if(((input.charAt(i) >= '0') && (input.charAt(i) <= '9')) || ((input.charAt(i) == '.') || (input.charAt(i) == '-')))			// if its a number, decimal, or negative sign
						do
						{
							
							acceptableToken = false; 
							//System.out.println("Entered While");
							
							if (input.length() > i)																									// if the token is acceptable, add it to the list
								{
								tokenList.get(word).setValue(tokenList.get(word).getValue() +input.substring(i, i+1));								// deals with numbers with multiple positions
								tokenList.get(word).setState(Token.State.NUMBER);
								}
							else
							{
								tokenList.get(word).setValue(tokenList.get(word).getValue() +input.substring(i));									// deals with single digit numbers
								tokenList.get(word).setState(Token.State.NUMBER);
							}
								
								
								
								
							i++;																													// increment the character
						}while (
								(input.length() > i) &&																								// if there are more characters
								(
									((input.charAt(i) >= '0') && (input.charAt(i) <= '9')) || 														// if its a 0-9 number
									((input.charAt(i) == '.'))															// if its a decimal or negative sign
								)
							  );	// loop to add all adjacent numbers into one token
					else
						throw new Exception("Invalid Input");																						// invalid character
					word++; 																														
					
					break;
			
				}
			}
			if(i >= input.length()) 																												// if I has gone beyond the string, end the loop
				state = LoopState.PASS;

				
		}
		tokenList.add(new Token("", Token.State.EndOfLine)); 																						// add end of line token
		return tokenList;
	}
}
