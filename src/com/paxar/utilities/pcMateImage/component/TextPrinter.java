package com.paxar.utilities.pcMateImage.component;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

//Handle the logic of printing string of Text
class TextPrinter
{
	private final int originalX, originalY;
	private int x, y;
	private int printPointerLine, printPointerChar;
	private TextAlignment setting;
	public TextPrinter(int x, int y, TextAlignment setting)
	{
		this.originalX = x;
		this.originalY = y;
		this.setting = setting;
		this.x = x;
		this.y = y;	
		this.printPointerLine = 0;
		this.printPointerChar = 0;
	}
	public int getX(){ return this.x; }
	public int getY(){ return this.y; }		
	
	private int capWidth(int charWidth, int dpi)
	{
		if( setting.getMinCharSpace() != null && charWidth < setting.getMinCharSpace().getPixel(dpi) )
			return setting.getMinCharSpace().getPixel(dpi);
		else if( setting.getMaxCharSpace() != null && charWidth > setting.getMaxCharSpace().getPixel(dpi) )
			return setting.getMaxCharSpace().getPixel(dpi);
		else
			return charWidth;
	}
	private int capHeight(int lineHeight, int dpi)
	{
		if( setting.getMinLineHeight() != null && lineHeight < setting.getMinLineHeight().getPixel(dpi) )
			return setting.getMinLineHeight().getPixel(dpi);
		else if( setting.getMaxLineHeight() != null && lineHeight > setting.getMaxLineHeight().getPixel(dpi) )
			return setting.getMaxLineHeight().getPixel(dpi);
		else
			return lineHeight;			
	}
	private String reverse(String text)
	{
		StringBuilder reverse = new StringBuilder();
		for( int i = text.length() - 1; i >= 0; i-- )
			reverse.append( text.charAt(i) );
		return reverse.toString();
	}
	private TextSize initTextInfo(Graphics2D g, int dpi, String text)
	{			
		if( setting.isReversePrint() )
			return TextSize.getFromGText(g, dpi, this.reverse( text ), setting );			
		else
			return TextSize.getFromGText(g, dpi, text, setting);
	}
			
	//Print the string
	public void print(Graphics2D g, int dpi, String text)
	{	
		this.printPointerLine = 0;
		this.printPointerChar = 0;
		TextSize info = this.initTextInfo(g, dpi, text);
		for(int i = 0; i < info.getTextChars().size(); i++)
		{
			for(int j = 0; j < info.getTextChars().get(i).size(); j++)
			{
				int heightMod = info.getStringHeight();
				g.drawString( Character.toString(info.getTextChars().get(i).get(j)), this.getX(), this.getY() + heightMod );				
				this.x += capWidth( info.getCharsWidth().get(this.printPointerLine).get(this.printPointerChar), dpi );
				this.printPointerChar++;
			}
			this.printPointerChar = 0;
			this.y += capHeight( info.getStringHeight(), dpi );
	
			this.x = this.originalX;
			this.printPointerLine++;
		}
		this.printPointerLine = 0;
	}		
}