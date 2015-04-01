package com.paxar.utilities.pcMateImage.rule;

import java.util.ArrayList;
import java.util.List;

import com.paxar.utilities.pcMateImage.component.TextSize;
import com.paxar.utilities.pcMateImage.length.Length;

//Default line break condition base on string width
public class StringWidthBreak implements LineBreakRule 
{
	private Length length;
	public StringWidthBreak(Length length){
		this.length = length;
	}
	@Override
	public List<String> split(TextSize text, int dpi) 
	{
		List<String> result = new ArrayList<String>();
		StringBuilder buffer = new StringBuilder();
		int lineWidth = 0;
		for( int i = 0; i < text.getTextChars().size(); i++ )
		{
			for( int k = 0; k < text.getTextChars().get(i).size(); k++ )
			{
				char theChar = text.getTextChars().get(i).get(k).charValue();
				int charWidth = text.getCharsWidth().get(i).get(k);
				if( lineWidth + charWidth > this.length.getPixel(dpi) )
				{	
					result.add( buffer.toString() );
					buffer = new StringBuilder();
					lineWidth = 0;
				}
				buffer.append( theChar );
				lineWidth += charWidth;
			}
		}
		if(buffer.length() > 0)	
			result.add(buffer.toString());		
		return result;
	}
}