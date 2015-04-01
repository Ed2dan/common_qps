package com.paxar.utilities.pcMateImage.rule;

/*
 * For those who are too lazy to call new OnCharBreak('\n') :(
 */
public class LineFeedBreak extends OnCharBreak
{
	private static final char lineFeedChar = '\n';
	public LineFeedBreak() { super(lineFeedChar); }	
}
