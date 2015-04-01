package com.paxar.utilities.pcMateImage.component;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.paxar.utilities.pcMateImage.length.LengthPair;


public class Glyph extends Image
{
	private final File imageFile;
	private final BufferedImage loadedImg;
	private final LengthPair targetSize;
	
	public Glyph(String imageFilePath, LengthPair position) throws IOException {
		this( new File( imageFilePath ), position, null );		
	}
	public Glyph(File imageFile, LengthPair position) throws IOException {
		this( imageFile, position, null );
	}
	public Glyph(String imageFilePath, LengthPair position, LengthPair targetSize) throws IOException {
		this( new File( imageFilePath ), position, targetSize );		
	}
	public Glyph(File imageFile, LengthPair position, LengthPair targetSize) throws IOException {
		super(BufferedImage.TYPE_INT_ARGB, position);
		this.imageFile = imageFile;
		this.loadedImg = ImageIO.read(this.imageFile);
		this.targetSize = targetSize;		
		this.canvas = LengthPair.Pixel( this.loadedImg.getWidth(), this.loadedImg.getHeight() );
	}
	
	//If target size is specificed, it means it need to scale
	private void targetSizeMeansScale(Graphics2D g, int dpi)
	{
		int width = this.loadedImg.getWidth();
		int height = this.loadedImg.getHeight();
		
		if( this.targetSize != null && 
			(this.targetSize.getWidth().getPixel(dpi) != width || 
			this.targetSize.getHeight().getPixel(dpi) != height) )
		{
			this.scale(
				(double)this.targetSize.getWidth().getPixel(dpi) / (double)width, 
				(double)this.targetSize.getHeight().getPixel(dpi) / (double)height
			);
		}		
	}
		
	@Override
	protected void doTransform(Graphics2D g, int dpi) throws Exception
	{	
		this.targetSizeMeansScale(g, dpi);
		int width = this.loadedImg.getWidth();
		int height = this.loadedImg.getHeight();
		if( this.needDoTransform() )
		{
			AffineTransform transform = new AffineTransform();			
			if( this.doScale )
			{
				transform.scale( this.scaleX , this.scaleY );
			}
			if( this.doRotation )				
			{
				transform.translate(width/2, height/2);
				transform.rotate( Math.toRadians( this.rotation )  );		
				transform.translate(-width/2, -height/2);	
			}		
			g.setTransform( transform );
		}
	}
	
	@Override
	public void draw(Graphics2D g, int dpi) throws Exception
	{
		g.drawImage( loadedImg, 0, 0, null );
		this.canvas = LengthPair.Pixel( 
				(int)Math.round( this.loadedImg.getWidth() * this.scaleX ), 
				(int)Math.round( this.loadedImg.getHeight() * this.scaleY ) );		
	}	
}
