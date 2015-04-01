package com.paxar.utilities.pcMateImage.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.paxar.utilities.pcMateImage.length.*;


public class LabelBase extends Image
{
	private Color baseColor;
	public LabelBase(LengthPair canvas) 
	{
		this(canvas, LengthPair.Pixel(0, 0), Color.WHITE );
	}
	public LabelBase(LengthPair canvas, Color baseColor) 
	{
		this(canvas, LengthPair.Pixel(0, 0), baseColor );
	}	
	public LabelBase(LengthPair canvas, LengthPair position, Color baseColor) 
	{
		super(BufferedImage.TYPE_INT_ARGB, canvas, position);
		this.baseColor = baseColor;		
	}
	
	@Override
	public void draw(Graphics2D g, int dpi) throws Exception
	{
		int width = canvas.getWidth().getPixel(dpi);
		int height = canvas.getHeight().getPixel(dpi);
		if( this.baseColor != null )
		{
			g.setColor(this.baseColor);
			g.fillRect(0, 0, width, height);
		}
	}
}
