package com.paxar.utilities.pcMateImage.rule;

import java.util.ArrayList;
import java.util.List;

import com.paxar.utilities.pcMateImage.component.TextSize;

//Default line break condition base on char count
public class CharCountBreak implements LineBreakRule 
{
	private int charCount;
	public CharCountBreak(int charCount){
		this.charCount = charCount;
	}
	@Override
	public List<String> split(TextSize text, int dpi) 
	{
		List<String> result = new ArrayList<String>();
		StringBuilder buffer = new StringBuilder();
		int lineCharCount = 0;
		for( int i = 0; i < text.getTextChars().size(); i++ )
		{
			for( int k = 0; k < text.getTextChars().get(i).size(); k++ )
			{
				char temp = text.getTextChars().get(i).get(k).charValue();
				if( lineCharCount + 1 > this.charCount )
				{
					result.add( buffer.toString() );
					buffer = new StringBuilder();
					lineCharCount = 0;
				}
				buffer.append( temp );
				lineCharCount++;
			}	
		}
		if(buffer.length() > 0)
			result.add(buffer.toString());
		return result;
	}
}