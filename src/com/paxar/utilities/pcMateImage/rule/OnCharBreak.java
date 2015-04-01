package com.paxar.utilities.pcMateImage.rule;

import java.util.Arrays;
import java.util.List;

import com.paxar.utilities.pcMateImage.component.TextSize;

//Default line break condition base on character
public class OnCharBreak implements LineBreakRule 
{
	private char breakChar;
	public OnCharBreak(char breakChar){
		this.breakChar = breakChar;
	}
	@Override
	public List<String> split(TextSize text, int dpi) {
		StringBuilder buffer = new StringBuilder();
		for( String line : text.getTexts() )		
			buffer.append( line );
		return Arrays.asList( buffer.toString().split(String.valueOf(this.breakChar)) );
	}
}