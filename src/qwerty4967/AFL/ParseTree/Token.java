package qwerty4967.AFL.ParseTree;

import qwerty4967.AFL.Lang;
import qwerty4967.AFL.Function.AFLFunction;

public class Token extends Element 
{
	// There's not a single comment here, I figure I ought to fix that.
	// tokens are pretty simple. they are elements that have a string, the token, and also contain
	// the type of the token.
	
	private String data;
	private Lang.tokenTypes type;
	
	public Token(String data, Lang.tokenTypes type, AFLFunction function, Container parent) 
	{
		super(function, parent);
		this.data = data;
		this.type = type;
	}

	public Token(String data, Lang.tokenTypes type, AFLFunction function) 
	{
		super(function);
		this.data = data;
		this.type = type;
	}


	public String getData() {
		return data;
	}

	public void setData(String data) 
	{
		this.data = data;
	}

	public Lang.tokenTypes getType() 
	{
		return type;
	}

	public void setType(Lang.tokenTypes type) 
	{
		this.type = type;
	}

	@Override
	public String toString() 
	{
		return "Token [ ID:"+this.ID+"| "+data  + ", " + type + "]";
	}

}