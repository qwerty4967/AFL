package qwerty4967.AFL;

import qwerty4967.AFL.Function.JavaFunction;
import qwerty4967.AFL.Function.MethodCode;
import qwerty4967.AFL.Interpret.Namespace;
import qwerty4967.AFL.Lang.TokenType;
import qwerty4967.AFL.ParseTree.Token;

public class JavaFunctionInitializer 
{
	// this code is responsible for governing every function in AFL that is not written with AFL code.
	// this includes operators and other junk
	// notably, it does not include certain basic functions known as control statements.
	// these functions include, but may not be limited to: if, else, while, end, function, return, break, continue, and = (assignment operator).
	// Those functions require special access to the internal workings of the interpreter.
	
	// this code is very repetitive.
	// there is probably a better way to do this.
	
	protected static void initJavaFunctions()
	{
		initOperators();
	}
	
	private static void initOperators()
	{
		// here operators are ordered by their priority tier.
		// organize operators ('(' and ')') are notable exceptions, because they are not represented by functions.
		// another exception is the assignment operator, '=', which is a control function.
		
		//##### MULTIPLY CATEGORY OPERATORS #####
		
		// * (Multiplication) operator
		MethodCode mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue*bValue);
				
			}
			Shell.error("The '*' operator requires operands of type num.", -2);
			return new Token("Error", TokenType.error);
		});
	
		JavaFunction jf = new JavaFunction("*",2,mc);
		Namespace.addFunction(jf);
		
		// / (Division) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue/bValue);
				
			}
			Shell.error("The '/' operator requires operands of type num.", -2);
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("/",2,mc);
		Namespace.addFunction(jf);
		
		// % (Modulo) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue%bValue);
				
			}
			Shell.error("The '%' operator requires operands of type num.", -2);
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("%",2,mc);
		Namespace.addFunction(jf);
		
		
		//##### ADD CATEGORY OPERATORS #####
		
		// + (Addition) operator
		// This was the first operator I added, so I decided to keep it the less efficient, gross way.
	    mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			boolean aIsInvalid = a.getType()!=TokenType.number;
			boolean bIsInvalid = b.getType()!=TokenType.number;
			
			if(aIsInvalid || bIsInvalid)
			{
				return new Token(a.getData()+b.getData(), TokenType.string);
			}
			// they be numbers!
			double aNum= Double.parseDouble(a.getData());
			double bNum= Double.parseDouble(b.getData());
			double result = aNum+bNum;
			return new Token(result+"", TokenType.number);
		});
		
		jf = new JavaFunction("+",2,mc);
		Namespace.addFunction(jf);
		
		// - (Subtraction) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue-bValue);
				
			}
			Shell.error("The '-' operator requires operands of type num.", -2);
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("-",2,mc);
		Namespace.addFunction(jf);

		
		//##### COMPARE CATEGORY OPERATORS #####
		
		// == (equals) Operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(a.getType()==b.getType())
			{
				// are they numbers?
				// are they numbers?
				if(areBothOfType(a,b,TokenType.number))
				{
					
					// guh
					double aValue = Double.parseDouble(a.getData());
					double bValue = Double.parseDouble(b.getData());
					return toToken(aValue==bValue);
					
				}
				else
				{
					return toToken(a.getData().equals(b.getData()));
					
				}
			}
			return new Token("false", TokenType.bool);
		});
		
		jf = new JavaFunction("==",2,mc);
		Namespace.addFunction(jf);
		
		
		//!= (not equals) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(a.getType()==b.getType())
			{
				// are they numbers?
				if(areBothOfType(a,b,TokenType.number))
				{
					
					// guh
					double aValue = Double.parseDouble(a.getData());
					double bValue = Double.parseDouble(b.getData());
					return toToken(aValue!=bValue);
					
				}
				else
				{
					return toToken(!(a.getData().equals(b.getData())));
					
				}
			}
			return new Token("true", TokenType.bool);
		});
		
		jf = new JavaFunction("!=",2,mc);
		Namespace.addFunction(jf);
		
		// >= (greater than or equal to) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue>=bValue);
				
			}
			Shell.error("The '>=' operator requires operands of type num.", -2);
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction(">=",2,mc);
		Namespace.addFunction(jf);
		
		// <= (Less than or equal to) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue<=bValue);
				
			}
			Shell.error("The '<=' operator requires operands of type num.", -2);
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("<=",2,mc);
		Namespace.addFunction(jf);
		
		// > (greater than) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue>bValue);
				
			}
			Shell.error("The '>' operator requires operands of type num.", -2);
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction(">",2,mc);
		Namespace.addFunction(jf);
		
		//< operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue<bValue);
				
			}
			Shell.error("The '<' operator requires operands of type num.", -2);
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("<",2,mc);
		Namespace.addFunction(jf);
		
		
		//##### LOGIC CATEGORY OPERATORS #####
		
		//|| (Or) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.bool))
			{
				
				// guh
				boolean aValue = Boolean.parseBoolean(a.getData());
				boolean bValue = Boolean.parseBoolean(b.getData());
				return toToken(aValue||bValue);
				
			}
			Shell.error("The '||' operator requires operands of type bool.", -2);
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("||",2,mc);
		Namespace.addFunction(jf);
		
		//& (And) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.bool))
			{
				
				// guh
				boolean aValue = Boolean.parseBoolean(a.getData());
				boolean bValue = Boolean.parseBoolean(b.getData());
				return toToken(aValue&bValue);
				
			}
			Shell.error("The '&' operator requires operands of type bool.", -2);
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("&",2,mc);
		Namespace.addFunction(jf);
		
				
	}
	
	private static boolean areBothOfType(Token a, Token b, TokenType t)
	{
		if(a.getType()==b.getType())
		{
			// are they numbers?
			if(a.getType()==t)
			{
				return true;
			}
		}
		return false;
	}
	
	private static Token toToken(boolean b)
	{
		if(b)
		{
			return new Token("true", TokenType.bool);
		}
		else
		{
			return new Token("false", TokenType.bool);
		}
	}
	
	private static Token toToken(double d)
	{
		return new Token (d+"",TokenType.number);
	}
	
	

}
