package qwerty4967.AFL.Interpret;

import java.util.ArrayList;
import qwerty4967.AFL.*;
import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.Lang.*;
import qwerty4967.AFL.ParseTree.Token;


public class Namespace 
{
	// MAN, I'm properly starting on the code that runs the code...
	// well, only kind of
	// but hey, it's in the Interpret package, that's got to count for something
	
	// The namespace class contains 3 seperate namespaces of things that AFL uses.
	// Functions, variables, and arrays-
	private static ArrayList<Function> functions = new ArrayList<Function>();
	private static ArrayList<Variable>variables = new ArrayList<Variable>();
	// never made a two dimensional arraylist I don't think...
	private static ArrayList<ArrayList<Token>>arrays = new ArrayList<ArrayList<Token>>();
	
	public static boolean addFunction(Function toAdd)
	{
		String name = toAdd.getName();
		
		// first check that the function's name isn't restricted.
		for( String s : Lang.CONTROL_FUNCTIONS)
		{
			
			if(name.equals(s))
			{
				// hopefully whoever has to actually deal with it shouts an error message.
				return false;
			}
		}
		
		// then check that it doesn't match any other functions.
		for(Function f: functions)
		{
			if(f.equals(toAdd))
			{
				return false;
			}
		}
		
		
		functions.add(toAdd);
		return true;
	}
	
	public static Function getFunction(String name, int parameters)
	{
		for(Function f: functions)
		{
			if(f.getName().equals(name))
			{
				if(f.getParameters()==parameters)
				{
					return f;
				}
			}
			
		}
		
		return null;
	}
	
	public static boolean removeFunction(Function toRemove)
	{
		String name = toRemove.getName();
		int parameters = toRemove.getParameters();
		return removeFunction(name, parameters);
	}
	
	public static boolean removeFunction(String name, int parameters)
	{
		Function f = getFunction(name, parameters);
		if( f==null)
		{
			return false;
		}
		
		functions.remove(f);
		return true;
	}
	
	public static void clearFunctions()
	{
		functions.clear();
	}
	
	public static boolean setVariable(Token variable, Token value, AFLFunction scope)
	{
		// so, we set a variable.
		// first, we've got to see wheter it already exists.
		if(scope.getName().equals("_main"))
		{
			scope=null;
		}
		String name = variable.getData();
		int lineNumber = variable.getStatementNumber();
		
		Variable var = getVariable(name, scope);
		if(var==null)
		{
			// huh, either we've got a problem, or we just need to make the variable.
			if(variableHasInvalidScope(name, scope))
			{
				String file = scope==null?"":scope.getFile();
				Shell.error("Variable '"+name+"' has already been defined in a local scope. It cannot be used in a global scope.",lineNumber,file);
				return false;
			}
			
			// okay then, create the variable, since it doesn't exist, at least in this scope.
			// probably
			var=new Variable(name, value, scope);
			variables.add(var);
			
			
		}
		else
		{
			var.setValue(value);
		}
		return true;
		
	}
	
	public static Token getVariableValue(Token name, AFLFunction scope)
	{
		// We are assuming that name is in fact a variable.
		// if it's not, then that's Interpreter's problem.
		
		String varName = name.getData();
		int lineNumber = name.getStatementNumber();
		Variable var = getVariable(varName, scope);
		if(var == null)
		{
			
			if(variableHasInvalidScope(varName, scope))
			{
				// this never runs?
				Shell.error("Variable '"+varName+"' has already been defined in a local scope. It cannot be used in a global scope.",lineNumber,scope.getFile());
				return null;
			}
			if(!variableExists(varName,scope))
			{
				Shell.error("Variable '"+varName+"' is not defined.",lineNumber,scope.getFile());
				return null;
			}

			
			
		}
		
		return var.getValue();
	}

	private static boolean variableExists(String name, AFLFunction Scope)
	{
		// okay, here's the thing
		// if Scope is null, only find matching.
		// if scope is not null, find matching, or null;
		for(Variable v: variables)
		{
			if(v.getName().equals(name))
			{
				// okay, pedal to the metal
				// really eclipse? pedal isn't a word to you?
				// eclipse is a silly dingbat.
				
				if(v.getScope()==null)
				{
					return true;
				}
				
				if(Scope!=null)
				{
					if(Scope==v.getScope())
					{
						return true;
					}
				}
			}
		}
	
		return false;
	}
	
	private static boolean variableHasInvalidScope(String name, AFLFunction Scope)
	{
		// okay, here's the thing	
		// if Scope is null, only find matching.
		// if scope is not null, find matching, or null;
		for(Variable v: variables)
		{
			if(v.getName().equals(name))
			{
				// okay, pedal to the metal
				// really eclipse? pedal isn't a word to you?
				// eclipse is a silly dingbat.
				
				if(v.getScope()!=null)
				{
					if(Scope==null)
					{
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	
	
	private static Variable getVariable(String name, AFLFunction Scope)
	{
		// okay, here's the thing
		// if Scope is null, only find matching.
		// if scope is not null, find matching, or null;
		for(Variable v: variables)
		{
			if(v.getName().equals(name))
			{
				// okay, pedal to the metal
				// really eclipse? pedal isn't a word to you?
				// eclipse is a silly dingbat.
				
				if(v.getScope()==null)
				{
					return v;
				}
				
				if(Scope!=null)
				{
					if(Scope==v.getScope())
					{
						return v;
					}
					continue;
				}
				
				// is this ever valid?
				// it's not valid!
				return null;
			}
		}
		
	
	
		
		return null;
	}
	
	public static Token newArray()
	{
		// create the array.
		ArrayList<Token> newArray = new ArrayList<Token>();
		arrays.add(newArray);
		
		// prepare the pointer
		int index = arrays.size()-1;
		return new Token(index+"", TokenType.arrayPointer);
	}
	
	public static ArrayList<Token> getArray(Token pointer)
	{
		if(pointer.getType()!=TokenType.arrayPointer)
		{
			Shell.error("Invalid array pointer... This seems like an internal error? That seems bad.", -2,"");
			return null;
		}
		int index;
		index = Integer.parseInt(pointer.getData());
		
		return getArray(index);
	}
	
	public static ArrayList<Token> getArray(int index)
	{
		if(index>=arrays.size())
		{
			Shell.error("Invalid array refrence... This seems like an internal error? That seems bad.", -2,"");
			return null;		
		}
		
		return arrays.get(index);
		
	}
	
	public static boolean setArray(Token pointer, ArrayList<Token> array)
	{
		if(pointer.getType()!=TokenType.arrayPointer)
		{
			Shell.error("Invalid array pointer... This seems like an internal error? That seems bad.", -2,"");
			return false;
		}
		int index;
		index = Integer.parseInt(pointer.getData());
		return setArray(index, array);
	}
	
	public static boolean setArray(int index, ArrayList<Token> array)
	{
		if(index>=arrays.size())
		{
			Shell.error("Invalid array refrence... This seems like an internal error? That seems bad.", -2,"");
			return false;
		}
		
		arrays.set(index, array);
		return true;
	}
	// more for testing purposes than anything else.
	// I wonder if it will be used at all when all of this is done?
	public static void clearVariables()
	{
		// as an aside, I really like all of the abstractions I've built up
		// now that parse is all finished, I can just talk about function parameters and stuff
		// I scarcely have to think about tokens, definitely not figuring out what type they are
		// it's nice
		// nothing to do with this method.
		// as of writing this comment, it's the thirteenth of october, 2021
		// this project has been an ongoing concern for like 6 months
		// hopefully that won't be 7...
		// I plan to get finished by next week, gonna take
		// full advantage of fall break.
		// I'm writing this method just before I plan out the largest, like, structural hurdle left, which is Interpreter itself.
		// after that's done and working all that will be left is to define operations and functions.
		// may as well be finished at that point
		// I'm excited
		// though admittedly all we've got right now is 3,500 lines of code that can't add 2+2.
		// welp.
		// soon!
		variables.clear();
	}
	
	

	public static String debug() 
	{
		String toReturn = "Functions:";
		//toReturn+="\n    Functions:";
		for(Function f: functions)
		{
			toReturn+="\n        "+f.getFile()+"."+f.getName()+"("+f.getParameters()+")";		
		}
		//toReturn+="\n    Variables:";
		/*for(Variable v: variables)
		{
			toReturn+="\n        "+v;
		}
		toReturn+="\n    Arrays:";
		for(ArrayList<Token> a: arrays)
		{
			toReturn+="\n        "+a;
		}*/
		return toReturn;
	}
	
	
	
	
}
