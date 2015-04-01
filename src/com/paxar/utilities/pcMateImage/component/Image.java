package com.paxar.utilities.pcMateImage.component;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.paxar.utilities.pcMateImage.length.*;


public abstract class Image
{	
	protected static final boolean DEBUG = false;
	
	protected static final LengthPair DEFAULT_POSITION = LengthPair.Pixel(0, 0);
	protected static final LengthPair DEFAULT_CANVAS_SIZE = LengthPair.Pixel(640, 480);
	
	protected final int imageType;	
	protected final List<Image> subImages;
	protected LengthPair canvas, position;
	protected Alignment setting;
	
	protected boolean doRotation = false;
	protected double rotation;	
	protected boolean doScale = false;
	protected double scaleX = 1.0, scaleY = 1.0;
	
	public Image(int imageType, LengthPair canvas, LengthPair position)
	{
		this.imageType = imageType;
		this.canvas = canvas;
		this.position = position;
		this.subImages = new ArrayList<Image>();
		this.setting = new Alignment();
	}
	public Image(int imageType)
	{
		this(imageType,DEFAULT_CANVAS_SIZE, DEFAULT_POSITION);
	}
	public Image(int imageType, LengthPair position)
	{
		this(imageType,DEFAULT_CANVAS_SIZE, position);
	}
	
	public Alignment getAlignment(){ return this.setting; }
	
	//Drawing space size
	public LengthPair getCanvas() { return canvas; }
	
	//Position is the relative position to its parent(Provider of Graphis2D instance)
	public LengthPair getPosition() { return this.position; }
	public Image setPosition(LengthPair position){ this.position = position; return this; }
	
	//Add child images
	public void addAll( List<? extends Image> subImages )
	{
		this.subImages.addAll(subImages);
	}
	public void add( Image subImage )
	{
		this.subImages.add( subImage );
	}
	
	/*
	 * Transformation methods
	 */
	protected boolean needDoTransform()
	{
		return this.doRotation || this.doScale;
	}	
	public void clearTransform()
	{
		this.doRotation = false;
		this.doScale = false;
	}
	
	//Rotation are in clock wise
	public Image rotate( double degree ) 
	{	
		doRotation = true;
		rotation = degree;
		return this;
	}
	
	//0.5 = 50% of the original, 1.3 = 130% of the original, etc
	public Image scale( double percent )
	{		
		return scale( percent, percent );
	}	
	public Image scale( double percentX, double percentY )
	{
		doScale = true;
		scaleX = percentX;
		scaleY = percentY;
		return this;
	}
	/*
	 * Transform methods End
	 */
	
	protected int getActualPositionX(Image image, int dpi)
	{
		int translatedWidth = image.getPosition().getWidth().getPixel(dpi);
		if(image.getAlignment().getHorizontialAlignment() == Alignment.ALIGN_RIGHT)
			translatedWidth -= image.getCanvas().getWidth().getPixel(dpi);
		else if(image.getAlignment().getHorizontialAlignment() == Alignment.ALIGN_CENTER)
			translatedWidth -= image.getCanvas().getWidth().getPixel(dpi) / 2;
		return translatedWidth;
	}
	protected int getActualPositionY(Image image, int dpi)
	{
		int translatedHeight = image.getPosition().getHeight().getPixel(dpi);
		if(image.getAlignment().getVerticalAlignment() == Alignment.ALIGN_DOWN)
			translatedHeight += image.getCanvas().getHeight().getPixel(dpi);
		else if(image.getAlignment().getVerticalAlignment() == Alignment.ALIGN_CENTER)
			translatedHeight += image.getCanvas().getHeight().getPixel(dpi) / 2 ;
		return translatedHeight;
	}
	protected LengthPair getActualPosition(Image image, int dpi)
	{	
		return LengthPair.Pixel( getActualPositionX(image, dpi), getActualPositionY(image, dpi) );
	}
		
	//Rendering methods
	public BufferedImage render(int dpi) throws Exception
	{
		return this.render( this.canvas, dpi );
	}
	public BufferedImage render(LengthPair dimension, int dpi) throws Exception
	{
		return this.render( dimension.getWidth(), dimension.getHeight(), dpi );
	}
	public BufferedImage render(Length width, Length height, int dpi) throws Exception
	{
		return this.render( width.getPixel(dpi), height.getPixel(dpi), dpi );
	}
	public BufferedImage render(int width, int height, int dpi) throws Exception
	{
		if( width == 0 || height == 0 )
		{
			width = DEFAULT_CANVAS_SIZE.getWidth().getPixel(dpi);
			height = DEFAULT_CANVAS_SIZE.getHeight().getPixel(dpi);
		}
		BufferedImage image = new BufferedImage( width, height, this.imageType );
		Graphics2D g = image.createGraphics();
		
		//Do self's transform
		AffineTransform old_transform = g.getTransform();
		try
		{
			this.doTransform(g, dpi);
			this.draw(g, dpi);
		}
		finally
		{
			g.setTransform(old_transform);
		}
		for( Image item : this.subImages )		
		{
			//Render all sub-image first so their actual screen size can be determined.
			BufferedImage img = item.render(dpi);
			
			LengthPair actualPosition = this.getActualPosition(item, dpi);
			g.drawImage( img,
					actualPosition.getWidth().getPixel(dpi),
					actualPosition.getHeight().getPixel(dpi), null );
		}
		g.dispose();
		
		if(DEBUG)
			JOptionPane.showMessageDialog(null, new JLabel( new ImageIcon( image ) ), "icon", -1);
		
		return image;
	}
	
	protected abstract void draw(Graphics2D g, int dpi) throws Exception;
	protected void doTransform(Graphics2D g, int dpi) throws Exception { /*Do nothing*/ }	
	public Image setHorizonialAlignment(int alignment) { this.setting.setHorizonialAlignment(alignment); return this; }
	public Image setVerticalAlignment(int alignment) { this.setting.setVerticalAlignment(alignment); return this; }
	
	//Relative Pos
	public LengthPair addBaseRelativeHeight(int dpi, Length height) throws Exception 
	{ 
		return addBaseRelativePos( dpi, null, height ); 
	}
	public LengthPair addBaseRelativeHeight(int dpi, Length width, Length height) throws Exception 
	{ 
		LengthPair next = addBaseRelativePos( dpi, null, height ); 
		return LengthPair.Pixel(width.getPixel(dpi), next.getHeight().getPixel(dpi));
	}
	public LengthPair addBaseRelativeWidth(int dpi, Length width) throws Exception 
	{ 
		return addBaseRelativePos( dpi, width, null ); 
	}
	public LengthPair addBaseRelativeWidth(int dpi, Length width, Length height) throws Exception 
	{ 
		LengthPair next = addBaseRelativePos( dpi, width, null ); 
		return LengthPair.Pixel(next.getWidth().getPixel(dpi), height.getPixel(dpi));
	}	
	
	public LengthPair addRelativeHeight(int dpi, Length height) throws Exception 
	{ 
		return addRelativePos( dpi, null, height ); 
	}
	public LengthPair addRelativeHeight(int dpi, Length width, Length height) throws Exception 
	{ 
		LengthPair next = addRelativePos( dpi, null, height ); 
		return LengthPair.Pixel(width.getPixel(dpi), next.getHeight().getPixel(dpi));
	}	
	public LengthPair addRelativeWidth(int dpi, Length width) throws Exception 
	{ 
		return addRelativePos( dpi, width, null ); 
	}
	public LengthPair addRelativeWidth(int dpi, Length width, Length height) throws Exception 
	{ 
		LengthPair next = addRelativePos( dpi, width, null ); 
		return LengthPair.Pixel(next.getWidth().getPixel(dpi), height.getPixel(dpi));
	}
	
	public LengthPair addBaseRelativePos(int dpi, Length width, Length height) throws Exception { return addRelativePos( false, dpi, width, height ); }	
	public LengthPair addRelativePos(int dpi, Length width, Length height) throws Exception { return addRelativePos( true, dpi, width, height ); }
	public LengthPair addRelativePos(boolean addCanvasSize, int dpi, Length width, Length height) throws Exception
	{
		//Get screen size		
		this.render(dpi);
		int lastPx = this.getPosition().getWidth().getPixel(dpi);
		int lastPy = this.getPosition().getHeight().getPixel(dpi);
		if(addCanvasSize)
		{
			lastPx += this.getCanvas().getWidth().getPixel(dpi);		
			lastPy += this.getCanvas().getHeight().getPixel(dpi);
		}		
		return LengthPair.Pixel( 
				(width == null ? this.getPosition().getWidth().getPixel(dpi) : lastPx + width.getPixel(dpi)), 
				(height == null ? this.getPosition().getHeight().getPixel(dpi) : lastPy + height.getPixel(dpi)) 
		);		
	}
	
}
