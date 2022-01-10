package qwerty4967.AFL.Interpret;
import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.Lang.*;
import qwerty4967.AFL.ParseTree.*;
public class Resolver 
{
	protected static Token resolve( Element e )
	{
		
		
		if(e instanceof Token)
		{
			return useToken((Token)e);
		}
		
		// it's a function.
		//Shell.error("AFL does not yet support function calls or operators. Sorry!", c.getStatementNumber());
		//return new Token("Error", TokenType.error);
		FunctionCall f = (FunctionCall)e;
		
		// check that f has a valid name.
		// basically anything that exists, unless it's a CONTROL FUNCTION!
		if(!functionIsValid(f))
		{
			return new Token("Error", TokenType.error);
		}
		
		// okay, on to, you know, actually resolving it!
		Token toReturn = resolveFunction(f);
		return toReturn;
		
		
	}
	
	// this is primarily to grab variables.
	private static Token useToken (Token t)
	{
		if(t.getType()== TokenType.variable)
		{
			// uh oh...
			// gotta look for variables.
			Token toReturn=Namespace.getVariableValue(t, t.getFunction());
			if(toReturn==null)
			{
				//Shell.error("Variable '"+t.getData()+"' is not defined in this context.", t.getStatementNumber());
				return new Token("Error",TokenType.error);
			}
			return toReturn;
		}
		return t;
	}
	
	private static boolean functionIsValid(FunctionCall f)
	{
		// first check that the function doesn't have a resereved name, then check that the function exists.
		String functionName = f.getFunctionName();
		for(ControlFunction cf : Lang.controlFunctions)
		{
			String s = cf.getName();
			if(functionName.equals(s))
			{
				// we've got ourselves a problemo, kids!
				// *cough* that must've been uncle Ross's ghost
				Shell.error("Cannot call function '"+s+"' in this context.", f.getStatementNumber());
				return false;
			}
		}
		
		// the name is valid.
		// does the function exist?
		int size = f.getSize();
		if(Namespace.getFunction(functionName, size)==null)
		{
			// check for help.
			if(functionName.equals("help"))
			{
				if(size<2)
				{
					Shell.error("Uh-Oh! AFL couldn't find the help function!"
							+ "\nThere might be a few reasons for this, including asking AFL to run code from a file."
							+ "\nIn any case, documentation for AFL is available at qwerty4967.github.io/AFL/docs. ", f.getStatementNumber());
					return false;
				}
			}
			Shell.error("No overload for function '"+functionName+"' exists with "+size+" parameters. ", f.getStatementNumber());
			return false;
		}
		
		return true;
			
	}
	
	private static Token resolveFunction(FunctionCall f)
	{
		// we know that f exists. which is nice.
		// we do need to, of course, grab it.
		String functionName = f.getFunctionName();
		int functionParameters = f.getSize();
		Function toCall = Namespace.getFunction(functionName, functionParameters);
		
		Token[] parameters = new Token[functionParameters];
		
		for(int i = 0; i<functionParameters; i++)
		{
			// resolving tokens.
			Element e = f.getChild(i);
			Token param = resolve(e);
			
			if(param == null)
			{
				Shell.error("The partial result of a statement cannot be void.", f.getStatementNumber());
				return new Token ("Error", TokenType.error);
			}
			
			if(param.getType() == TokenType.error)
			{
				return param;
			}
			parameters[i]=param;
		}
		
		// now we should have are parameter array all filled up.
		// that's good.
		// call the function and ship off it's result!
		return toCall.call(parameters);
	}
}
