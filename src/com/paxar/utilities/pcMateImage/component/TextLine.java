package com.paxar.utilities.pcMateImage.component;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.paxar.utilities.pcMateImage.length.LengthPair;


/*
 * Simple container to align text within, added Text are expected to be in relative position of text line
 * Texts are auto center aligned vertically and it's relative position are calculated
 * Original vertical alignment are ignored
 */
public class TextLine extends Image
{
	private final List<Text> texts = new ArrayList<Text>();
	public TextLine(LengthPair position) 
	{
		super(BufferedImage.TYPE_INT_ARGB, position);		
	}

	//Some times we scale the text, the text will drop back to Image
	@Override
	public void add(Image mayBeText)
	{
		if( mayBeText instanceof Text )
			this.add( (Text)mayBeText );
		else
			super.add( mayBeText );
	}
	//Text are expected to be in relative position to text line's position
	public void add(Text text)
	{	
		this.texts.add(text);		
	}
	public List<Text> getTexts()
	{
		return this.texts;
	}	
	
	@Override
	protected int getActualPositionX(Image image, int dpi)
	{
		return (int)Math.round( image.getPosition().getWidth().getPixel(dpi) * this.scaleX );
	}
	
	@Override
	protected int getActualPositionY(Image image, int dpi)
	{
		return (int)Math.round( image.getPosition().getHeight().getPixel(dpi) * this.scaleY );
	}	
	
	//Translate the texts's relative position to position relative to the canvas		
	private LengthPair toActualPosition(Text text, Graphics2D g, int dpi, int maxStringHeight)
	{		
		int translatedWidth = text.getPosition().getWidth().getPixel(dpi);
		int translatedHeight = text.getPosition().getHeight().getPixel(dpi);
		
		int textMaxStringWidth = text.getTextSize(g, dpi).getMaxStringWidth();
		int textTotalStringHeight = text.getTextSize(g, dpi).getTotalStringHeight();
		int textStringHeightDiff = maxStringHeight - textTotalStringHeight; 
		
		//Convert position information due to alignment
		if(text.getTextSetting().getHorizontialAlignment() == TextAlignment.ALIGN_RIGHT)
			translatedWidth -= textMaxStringWidth;
		else if(text.getTextSetting().getHorizontialAlignment() == TextAlignment.ALIGN_CENTER)
			translatedWidth -= textMaxStringWidth / 2;

		if(text.getTextSetting().getVerticalAlignment() == TextAlignment.ALIGN_UP)
			translatedHeight += (textStringHeightDiff / 2);
		else if(text.getTextSetting().getVerticalAlignment() == TextAlignment.ALIGN_DOWN)
			translatedHeight -= (textStringHeightDiff / 2);
	
		return LengthPair.Pixel( translatedWidth, translatedHeight );
	}
	
	@Override
	protected void draw(Graphics2D g, int dpi) throws Exception
	{			
		int maxStringHeight = 0;
		for( Text text : this.texts )	
		{
			maxStringHeight = Math.max(text.getTextSize(g, dpi).getTotalStringHeight(), maxStringHeight);			
		}
		int maxWidth = 0, maxHeight = 0;
		for( Text text : this.texts )
		{
			//Set new position
			//Textline's text are output as left vertically-center aligned in order to match different font size
			Text newSubText = text;
			newSubText.setPosition( this.toActualPosition( text, g, dpi, maxStringHeight ) );
			newSubText.setVerticalAlignment(TextAlignment.ALIGN_CENTER);
			newSubText.setHorizonialAlignment(TextAlignment.ALIGN_LEFT);
			int newPosX = newSubText.getPosition().getWidth().getPixel(dpi);
			int newPosY = newSubText.getPosition().getHeight().getPixel(dpi);
			BufferedImage newTextImage = newSubText.render(dpi);

			g.drawImage( newTextImage, newPosX, newPosY, null );
			
			int newPosWidth = newSubText.getCanvas().getWidth().getPixel(dpi);
			int newPosHeight = newSubText.getCanvas().getHeight().getPixel(dpi);			
			maxWidth = Math.max( newPosX + newPosWidth, maxWidth );
			maxHeight = Math.max( newPosY + newPosHeight, maxHeight );			
			this.canvas = LengthPair.Pixel(maxWidth, maxHeight);
		}
	}
}
