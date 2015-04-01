package com.paxar.utilities.pcMateImage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.paxar.utilities.pcMateImage.component.Image;
import com.paxar.utilities.pcMateImage.length.Length;


/*
 * Wrapper class to handle save/display Image and draw overlay ruler on top of it.
 */
public class ImageBuilder 
{	
	protected int dpi;
	protected List<Image> textImages;	
	protected Length ruler_tick;
	
	private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
	private static Font rulerFont = new Font("Arial", Font.PLAIN, 9);		
	public ImageBuilder(int dpi, List<Image> textImages, Length ruler_tick)
	{
		this.dpi = dpi;
		this.ruler_tick = ruler_tick;
		this.textImages = textImages;
	}
	public ImageBuilder(int dpi, List<Image> textImages) throws Exception
	{
		this(dpi,textImages,null);
	}
	public ImageBuilder(int dpi, Image textImage, Length ruler_tick)
	{
		this(dpi, Arrays.asList( new Image[]{ textImage } ), ruler_tick);
	}	
	public ImageBuilder(int dpi, Image textImage) throws Exception
	{
		this(dpi, textImage, null);
	}	
	
	private List<BufferedImage> renderImages() throws Exception
	{
		List<BufferedImage> result = new ArrayList<BufferedImage>();
		for(Image textImage : this.textImages)
		{
			BufferedImage tempImg = textImage.render( this.dpi );
			BufferedImage overlay;
			if( this.ruler_tick != null )
			{
				overlay = new BufferedImage(tempImg.getWidth(), tempImg.getHeight(), BufferedImage.TYPE_INT_RGB);
				overlay.getGraphics().drawImage(tempImg, 0, 0, null);						
				drawRuler( overlay.getGraphics(), overlay.getWidth(), overlay.getHeight(), this.ruler_tick, this.dpi );
			}
			else
			{
				overlay = new BufferedImage(tempImg.getWidth(), tempImg.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
				overlay.getGraphics().drawImage(tempImg, 0, 0, null);
			}
			result.add( overlay );
		}
		return result;
	}
	
	//Add ruler	
	private String getRulerMark( Length length, int tick )
	{
		String numericString = decimalFormat.format(length.getRawValue() * tick);
		return numericString + length.getUnit();
	}
	private void drawRuler(Graphics g, int width, int height, Length tick_every, int dpi)
	{	
		g.setColor(Color.GRAY);
		g.setFont( rulerFont );
		int tick;
	    // draw vertical rule at left side (top to bottom)
	    tick = 0;
	    for (int y=0; y < height; y++)
	    {
	        if (y % (tick_every.getPixel(dpi)) == 0)
	        {	        	
	        	g.drawString( getRulerMark( tick_every, tick++ ) , 0, y);	        	
	            g.drawLine(0, y, 20, y);
	        }	        	
	    }
	    
	    // draw horizontal rule at top (left to right)
	    tick = 0;
	    for (int x=0; x < width; x++)
	    {
	        if (x % (tick_every.getPixel(dpi)) == 0)  
	        {	        
	        	g.drawString( getRulerMark( tick_every, tick++ ), x, 0 + 10);
	            g.drawLine(x, 0, x, 20);
	        }
	    }
	}
	
	//Turn the image for PCMate
	private List<BufferedImage> renderPCMateImages() throws Exception
	{		
		List<BufferedImage> rawImages = renderImages();
		List<BufferedImage> result = new ArrayList<BufferedImage>();
		for(BufferedImage rawImage : rawImages)
		{		
			//Turn the final image to suit PCMate
			BufferedImage displayImage = new BufferedImage(rawImage.getHeight(), rawImage.getWidth(), BufferedImage.TYPE_BYTE_BINARY); 
			Graphics2D renderer = displayImage.createGraphics();
			AffineTransform old_transform = renderer.getTransform();			
			AffineTransform transform = new AffineTransform();
			transform.translate(rawImage.getHeight() / 2,rawImage.getWidth() / 2);
			transform.rotate( Math.toRadians( 90 ));			
			transform.translate(-rawImage.getWidth() / 2,-rawImage.getHeight() / 2);
			renderer.setTransform( transform );	
			renderer.drawImage(rawImage, 0, 0, null);		
			renderer.setTransform(old_transform);
			renderer.dispose();
			result.add( displayImage );
		}
		return result;
	}
	
	//Save Image to file
	public List<File> saveImage( String fileFormat, String outputFile ) throws Exception
	{
		return saveImage( fileFormat, new File( outputFile ) );
	}		
	public List<File> saveImage( String fileFormat, File outputFile ) throws Exception
	{		
		return saveImage( renderImages(), fileFormat, outputFile );
	}
	//Save PCMate Image to file
	public List<File> savePCMateImage( File outputFile ) throws Exception
	{			
		return saveImage( renderPCMateImages(), "bmp", outputFile );
	}	
	public List<File> savePCMateImage( String outputFilePath ) throws Exception
	{			
		return saveImage( renderPCMateImages(), "bmp", new File( outputFilePath ) );
	}	
	
	public List<File> saveImage( List<BufferedImage> images, String fileFormat, File outputFile ) throws Exception
	{	
		List<File> result = new ArrayList<File>();
		int count = 1;
		for(BufferedImage image : images)
		{
			String fileName = outputFile.getPath() + (count++) + "." + fileFormat;
			File output = new File( fileName );
			ImageIO.write( image, fileFormat, output );
			result.add( output );
		}		
		return result;
	}
	
	//Display image in JOptionPane
	public void displayImage( ) throws Exception
	{
		displayImage( renderImages() );
	}
	//Display PCMate Image
	public void displayPCMateImage( ) throws Exception
	{
		displayImage( renderPCMateImages() );
	}
	
	public void displayImage( List<BufferedImage> images ) throws Exception
	{
		try
		{			
			for( BufferedImage image : images )
				JOptionPane.showMessageDialog(null, new JLabel( new ImageIcon( image ) ), "icon", -1);
		}
		catch(java.awt.HeadlessException ex)
		{
			System.err.println( "Your system do not have a display, skipping image preview" );
		}		
	}
}
