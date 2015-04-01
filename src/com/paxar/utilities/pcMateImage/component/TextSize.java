package com.paxar.utilities.pcMateImage.component;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.paxar.utilities.pcMateImage.FloatFont;
import com.paxar.utilities.pcMateImage.length.LengthPair;
import com.paxar.utilities.pcMateImage.rule.LineBreakRule;


public class TextSize
{
	public static TextSize getTextWidth(Text text, int dpi)
	{
		BufferedImage image = new BufferedImage( 1, 1, BufferedImage.TYPE_BYTE_BINARY );
		Graphics2D g = image.createGraphics();
		return text.getTextSize(g, dpi);
	}
	public static TextSize getTextWidth(FloatFont font, String text, int dpi)
	{
		return getTextWidth( font, text, dpi, null );
	}	
	public static TextSize getTextWidth(FloatFont font, String text, int dpi, LineBreakRule rule)
	{
		Text tryText = new Text( font, text, LengthPair.Pixel(0, 0) ).setMultiLineRule(rule);
		return getTextWidth( tryText, dpi );
	}
	
	public static TextSize getFromGFont(Graphics2D g, int dpi, FloatFont font, String text, TextAlignment setting )
	{		
		Font oldFont = g.getFont();
		try
		{
			g.setFont( font.getNormalizedJavaFont(dpi) );
			return getFromGText( g, dpi, text, setting );						
		}
		finally
		{
			g.setFont(oldFont);
		}
	}
	public static TextSize getFromGText(Graphics2D g, int dpi, String text, TextAlignment setting)
	{
		TextSize unsplited = new TextSize( g, text, setting, dpi );
		if( setting.getMultiLineRule() != null )
		{				
			List<String> lines = setting.getMultiLineRule().split(unsplited, dpi);
			TextSize splited = new TextSize( g, lines, setting, dpi ); 
			return splited;
		}
		else
			return unsplited;			
	}
	
	private final TextAlignment setting;
	private final int dpi;
	
	private List<String> texts;
	private int stringHeight;
	private List<Integer> stringWidth;
	private List<ArrayList<Character>> textChars;
	private List<ArrayList<Integer>> charsWidth;
	private int maxCharWidth = Integer.MIN_VALUE, minCharWidth = Integer.MAX_VALUE;
	private int maxStringWidth = Integer.MIN_VALUE, minStringWidth = Integer.MAX_VALUE;
	private int totalStringHeight = 0;
	private TextSize(Graphics2D g, String text, TextAlignment setting, int dpi)
	{
		this( g, Arrays.asList( text ), setting, dpi );
	}
	private TextSize(Graphics2D g, List<String> texts, TextAlignment setting, int dpi)
	{
		FontMetrics fm = g.getFontMetrics();
		
		this.setting = setting;
		this.dpi = dpi;
		this.texts = texts;

		if( setting != null && setting.getMultiLineRule() != null )
		{
			if( setting.getMinLineHeight() != null )
				this.stringHeight = Math.max( fm.getHeight(), setting.getMinLineHeight().getPixel(dpi) );			
			else if( setting.getMaxLineHeight() != null )
				this.stringHeight = Math.min( fm.getHeight(), setting.getMaxLineHeight().getPixel(dpi) );
		}
		else
			this.stringHeight = fm.getHeight();
		this.stringWidth = new ArrayList<Integer>();
		this.textChars = new ArrayList<ArrayList<Character>>();
		this.charsWidth = new ArrayList<ArrayList<Integer>>();
		for( int i = 0; i < texts.size(); i++ )
		{
			int stringWidth = fm.stringWidth(texts.get(i));
			this.stringWidth.add( stringWidth );	
			this.textChars.add( new ArrayList<Character>() );
			this.charsWidth.add( new ArrayList<Integer>() );
			this.maxStringWidth = Math.max(this.maxStringWidth, stringWidth);
			this.minStringWidth = Math.min(this.minStringWidth, stringWidth);
			this.totalStringHeight += this.stringHeight;
			char[] tempChars = texts.get(i).toCharArray();
			for( char tempChar : tempChars )
			{
				int charWidth = fm.charWidth( tempChar );
				this.textChars.get(i).add( tempChar );				
				this.charsWidth.get(i).add( charWidth );
				this.maxCharWidth = Math.max(this.maxCharWidth, charWidth);
				this.minCharWidth = Math.min(this.minCharWidth, charWidth);					
			}			
		}
		this.totalStringHeight += fm.getDescent();
	}
	public int getStringHeight(){ return this.stringHeight; }
	public int getTotalStringHeight(){ return this.totalStringHeight; }
		
	public List<String> getTexts() { return texts; }
	public List<Integer> getStringWidth() { return stringWidth; }	
	public int getMaxStringWidth() { return maxStringWidth; }
	public int getMinStringWidth() { return minStringWidth; }
	
	public List<ArrayList<Character>> getTextChars() { return textChars; }
	public List<ArrayList<Integer>> getCharsWidth() { return charsWidth; }
	public int getMaxCharWidth(){ return this.maxCharWidth; }
	public int getMinCharWidth(){ return this.minCharWidth; }
}