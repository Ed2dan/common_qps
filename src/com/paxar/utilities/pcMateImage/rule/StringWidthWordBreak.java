package com.paxar.utilities.pcMateImage.rule;

import java.util.ArrayList;
import java.util.List;

import com.paxar.utilities.pcMateImage.component.TextSize;
import com.paxar.utilities.pcMateImage.length.Length;

//Default line break condition base on string width, but only if the remaining char are able to form a word.
public class StringWidthWordBreak implements LineBreakRule 
{	
	private static final List<Character> DEFAULT_SEPERATOR;
	static
	{
		DEFAULT_SEPERATOR = new ArrayList<Character>();
		DEFAULT_SEPERATOR.add( ' ' );
		DEFAULT_SEPERATOR.add( '\t' );
		DEFAULT_SEPERATOR.add( '/' );
		DEFAULT_SEPERATOR.add( '\\' );
		DEFAULT_SEPERATOR.add( ',' );
		DEFAULT_SEPERATOR.add( '.' );
	}
	
	private class Token
	{
		private int width;
		private String text;
		public Token( int width, String text )
		{
			this.width = width;
			this.text = text;
		}
		public int getWidth(){ return this.width; }
		public String getText(){ return this.text; }
	}
	
	private Length lineLength;
	private List<Character> seperators;
	public StringWidthWordBreak( Length lineLength )
	{
		this( lineLength, DEFAULT_SEPERATOR );
	}
	public StringWidthWordBreak( Length lineLength, List<Character> seperators )
	{
		this.lineLength = lineLength;
		this.seperators = seperators;
	}	
	private List<Token> splits( TextSize text, List<Character> seperators )
	{
		List<Token> result = new ArrayList<Token>();
		StringBuilder buffer = new StringBuilder();
		int tokenWidth = 0;
		for( int i = 0 ;i < text.getTextChars().size(); i++ )		
		{
			for( int k = 0; k < text.getTextChars().get(i).size(); k++ )
			{				
				char temp = text.getTextChars().get(i).get(k).charValue();
				int charWidth = text.getCharsWidth().get(i).get(k);
				
				buffer.append( temp );
				tokenWidth += charWidth;
				if( seperators.contains( temp ) )
				{
					result.add( new Token( tokenWidth, buffer.toString() ) );					
					buffer = new StringBuilder();
					tokenWidth = 0;
				}
			}
		}
		if( buffer.length() > 0 )		
			result.add( new Token( tokenWidth, buffer.toString() ) );		
		return result;
	}
	
	@Override
	public List<String> split(TextSize text, int dpi)
	{
		List<String> result = new ArrayList<String>();
		List<Token> tokens = this.splits( text, this.seperators );		
		StringBuilder buffer = new StringBuilder();
		int lineWidth = 0;
		for( Token token : tokens )
		{			
			if( lineWidth + token.getWidth() > this.lineLength.getPixel(dpi) )
			{
				result.add( buffer.toString() );
				buffer = new StringBuilder();
				lineWidth = 0;				
			}			
			buffer.append( token.getText() );
			lineWidth += token.getWidth();							
		}
		if(buffer.length() > 0)
			result.add(buffer.toString());
		return result;
	}
	
}
