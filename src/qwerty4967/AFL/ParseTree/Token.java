package qwerty4967.AFL.ParseTree;

import java.util.ArrayList;

import qwerty4967.AFL.Function.AFLFunction;
import qwerty4967.AFL.Interpret.Namespace;
import qwerty4967.AFL.Lang.*;

public class Token extends Element 
{
	// There's not a single comment here, I figure I ought to fix that.
	// tokens are pretty simple. they are elements that have a string, the token, and also contain
	// the type of the token.
	
	private String data;
	private TokenType type;
	
	public Token(String data, TokenType type) 
	{
		super();
		this.data = data;
		this.type = type;
	}
	
	public Token(String data, TokenType type, HasChildren parent) 
	{
		super(parent);
		this.data = data;
		this.type = type;
	}
	
	public Token copy()
	{
		return new Token(this.getData(), this.getType());
	}


	public String getData() {
		return data;
	}

	public void setData(String data) 
	{
		this.data = data;
	}

	public TokenType getType() 
	{
		return type;
	}

	public void setType(TokenType type) 
	{
		this.type = type;
	}

	@Override
	public String toString() 
	{
		return "Token [ ID:"+this.ID+" | \""+data  + "\", " + type + "]";
	}
	
	public String toOutputString()
	{
		if(this.getType()==TokenType.arrayPointer)
		{
			// type out the array pointer
			String toReturn ="{";
			ArrayList<Token> array= Namespace.getArray(this);
			for(Token t: array)
			{
				toReturn+=", ";
				if(t.getType()==TokenType.string)
				{
					toReturn+="\"";
				}
				
				toReturn+=t.toOutputString();
				
				if(t.getType()==TokenType.string)
				{
					toReturn+="\"";
				}
			}
			toReturn +=" }";
			toReturn = toReturn.substring(0, 1) + toReturn.substring(2);
			return toReturn;
		}
		return this.getData();
	}
}
