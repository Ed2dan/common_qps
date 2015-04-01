package com.paxar.utilities.pcMateImage.component;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.paxar.utilities.pcMateImage.FloatFont;
import com.paxar.utilities.pcMateImage.length.*;
import com.paxar.utilities.pcMateImage.rule.LineBreakRule;


public class Text extends Image
{	
	protected final FloatFont font;
	protected final String text;
	
	public Text(Text copy)
	{
		this( copy.font, copy.text, copy.position );
		this.doRotation = copy.doRotation;
		this.doScale = copy.doScale;
		this.setting = copy.setting;
	}	
	public Text(FloatFont font, String text)
	{
		this(font, text, DEFAULT_POSITION);
	} 		
	public Text(FloatFont font, String text, LengthPair position)
	{
		super(BufferedImage.TYPE_INT_ARGB, position);
		this.font = font;
		this.text = text;
		this.setting = new TextAlignment();
	} 	
	
	@Override
	protected void doTransform(Graphics2D g, int dpi) throws Exception
	{
		TextSize info = this.getTextSize(g, dpi);
		if( this.needDoTransform() )
		{
			AffineTransform transform = new AffineTransform();			
			if( this.doScale )
			{	
				transform.scale( this.scaleX , this.scaleY );
			}
			if( this.doRotation )
			{
				transform.translate(info.getMaxStringWidth()/2, info.getStringHeight()/2);
				transform.rotate( Math.toRadians( this.rotation ));
				transform.translate(-info.getMaxStringWidth()/2, -info.getStringHeight()/2);
			}
			g.setTransform( transform );
		}
	}	
			
	@Override
	public void draw(Graphics2D g, int dpi) throws Exception
	{
		g.setFont( this.font.getNormalizedJavaFont(dpi) );
		g.setColor(Color.BLACK);
		FontMetrics fm = g.getFontMetrics();		
		new TextPrinter( 0, 0, this.getTextSetting() ).print(g, dpi, this.text);
		
		TextSize size = getTextSize( g, dpi );
		int newWidth = size.getMaxStringWidth(); 
		int newHeight = size.getTotalStringHeight();
		this.canvas = LengthPair.Pixel(newWidth, newHeight);		
	}
	
	public FloatFont getFont() { return font; }
	public String getText() { return text; }	
	
	public TextSize getTextSize(int dpi)
	{
		BufferedImage dummy = new BufferedImage( 1, 1, this.imageType );
		return getTextSize( dummy.createGraphics(), dpi );
	}
	public TextSize getTextSize(Graphics2D g, int dpi)
	{
		return TextSize.getFromGFont(g, dpi, font, text, this.getTextSetting());
	}
	
	//String manipulation flags		
	public TextAlignment getTextSetting(){ return (TextAlignment)setting; }
	public List<String> split(Graphics2D g, int dpi)
	{
		List<String> result = new ArrayList<String>();
		if( this.getTextSetting().getMultiLineRule() != null )
		{
			result.addAll( this.getTextSetting().getMultiLineRule().split(this.getTextSize(g, dpi), dpi) );
		}
		else			
			result.add( this.text );		
		return result;
	}
	
	//Short hand setting methods
	@Override
	public Text setHorizonialAlignment(int alignment) { this.getTextSetting().setHorizonialAlignment(alignment); return this; }
	
	@Override
	public Text setVerticalAlignment(int alignment) { this.getTextSetting().setVerticalAlignment(alignment); return this; }
	
	public Text setMinCharSpace(Length min_char_space) { this.getTextSetting().setMinCharSpace(min_char_space); return this; }
	public Text setMaxCharSpace(Length max_char_space) { this.getTextSetting().setMaxCharSpace(max_char_space); return this; }
	public Text setCharSpace(Length char_space){ this.getTextSetting().setCharSpace(char_space); return this; }
	public Text setMinLineHeight(Length min_line_space) { this.getTextSetting().setMinLineHeight(min_line_space); return this; }
	public Text setMaxLineHeight(Length max_line_space) { this.getTextSetting().setMaxLineHeight(max_line_space); return this; }
	public Text setLineHeight(Length line_space) { this.getTextSetting().setLineHeight(line_space); return this; }
	public Text setMultiLineRule(LineBreakRule rule) { this.getTextSetting().setMultiLineRule(rule); return this; }
	public Text setReversePrint(boolean reversePrint) { this.getTextSetting().setReversePrint(reversePrint); return this; }
}
