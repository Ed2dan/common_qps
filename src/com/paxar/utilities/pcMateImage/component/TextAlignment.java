package com.paxar.utilities.pcMateImage.component;

import com.paxar.utilities.pcMateImage.length.Length;
import com.paxar.utilities.pcMateImage.rule.LineBreakRule;


public class TextAlignment extends Alignment
{	
	/*
	 * char space is space between characters.
	 * It is NOT the actual white space between 2 line of text, but distance between 2 x coordinate.
	 * If you want to do that you should implement in a higher-level container
	 * So setting max_char_space smaller then the actual character width will make 2 characters overlap
	 * Default to the character width given by font. 
	 */
	private Length min_char_space = null, max_char_space = null;
	private LineBreakRule multiLineRule;
	
	/*
	 * line space is space between line. Only effective when mulitline is true.
	 * It is NOT the actual white space between 2 line of text, it's the distance between 2 y coordinate.
	 * If you want to do that you should implement in a higher-level container 
	 * So setting max_line_space smaller then the actual character height will make 2 lines overlap
	 * Default to the character height given by font.
	 */
	private Length min_line_height = null, max_line_height = null;
	
	/*
	 * Reverse the string, abcdefg become gfedcba
	 */
	private boolean reversePrint = false;
	
	public LineBreakRule getMultiLineRule() { return multiLineRule; }
	public Length getMinCharSpace() { return min_char_space; }
	public Length getMaxCharSpace() { return max_char_space; }
	public Length getMinLineHeight() { return min_line_height; }
	public Length getMaxLineHeight() { return max_line_height; }
	public boolean isReversePrint() { return reversePrint;}

	public void setMinCharSpace(Length min_char_space) { this.min_char_space = min_char_space; }
	public void setMaxCharSpace(Length max_char_space) { this.max_char_space = max_char_space;  }
	public void setCharSpace(Length char_space){ this.setMinCharSpace(char_space); this.setMaxCharSpace(char_space); }
	public void setMinLineHeight(Length min_line_space) { this.min_line_height = min_line_space; }
	public void setMaxLineHeight(Length max_line_space) { this.max_line_height = max_line_space; }
	public void setLineHeight(Length line_space) { this.setMinLineHeight(line_space); this.setMaxLineHeight(line_space); }
	public void setMultiLineRule(LineBreakRule rule) { this.multiLineRule = rule; }
	public void setReversePrint(boolean reversePrint) { this.reversePrint = reversePrint; }
}
