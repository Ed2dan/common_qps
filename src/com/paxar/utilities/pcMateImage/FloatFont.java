package com.paxar.utilities.pcMateImage;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/*
 * ==UGLY HACK== 
 * Wrapper class around java.awt.Font. 
 * If you could make java graphics render font in any dpi AND support floating point font size,
 * feel free to replace it :)
 *  
 *  
 * java.awt.Font.getSize() is SUPPOSED to be in point size ( 1/72 inch ), but unable to support floating point
 * this is why setting GraphicsConfiguration when creating Graphics2D does not work, so live with it...
 * Also, Java2D assume font to pixel convention DPI is 72dpi (hence new Font("blah",Font.PLAIN,72" will
 * rasterize to (sort of)300 pixel high character)
 * So we calculate and normalize it as close as the given dpi...size should be closer to actual when in higher resolution
 */
public class FloatFont 
{	
	private final static double JAVA_DEFAULT_DPI = 72.0;
	
	/*
	 * Load Font from font file directly
	 */
	private static Map<String, FloatFont> FONT_CACHE = new HashMap<String, FloatFont>();
	private static FloatFont loadFont(File fontFile, String name, double fontSize, double fontWeight, int fontType) 
	{
		FloatFont font = null;
	    if (FONT_CACHE != null && (font = FONT_CACHE.get(name)) != null)
	    	return font;
	    try 
	    {
	    	Font loadedFont = Font.createFont(fontType, fontFile).deriveFont( new Hashtable<TextAttribute, Object>() );	    	
	    	Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
	    		map.put(TextAttribute.SIZE, fontSize);
	    		map.put(TextAttribute.WEIGHT, fontWeight);
	    	Font modifiedFont = loadedFont.deriveFont( map );
	    	font = new FloatFont( modifiedFont, fontSize, true );
	    	
	    } 
	    catch (Exception ex) 
	    {
	    	ex.printStackTrace();
	    	System.err.println(fontFile.getName() + " not loaded from " + fontFile.getAbsolutePath() + ".  Using serif font.");
	    	font = new FloatFont( new Font("serif", Font.PLAIN, (int)Math.round(fontSize)), fontSize );
	    }
	    return font;
	}
	
	private Font javaFont;
	private double size;
	private boolean isLoaded;

	public FloatFont(Font javaFont, double size, boolean isLoaded)
	{
		this.javaFont = javaFont.deriveFont((float)size);
		this.size = size;
		this.isLoaded = isLoaded;
	}	
	public FloatFont(Font javaFont, double size)
	{
		this(javaFont, size, false);
	}
	public FloatFont(FloatFont copy)
	{
		this(copy.javaFont, copy.size, copy.isLoaded);
	}
	public FloatFont(String name, int style, double size)
	{
		this( new Font( name, style, (int)Math.round(size) ), size );
	}
	public FloatFont(File fontFile, String name, double fontSize, double fontWeight, int fontType) 
	{
		this( loadFont(fontFile, name, fontSize, fontWeight, fontType) );
	}
	public FloatFont(File fontFile, String name, double fontSize, double fontWeight)
	{
		this( loadFont(fontFile, name, fontSize, fontWeight, Font.TRUETYPE_FONT) );
	}
	public FloatFont(File fontFile, String name, double fontSize)
	{
		this( loadFont(fontFile, name, fontSize, TextAttribute.WEIGHT_REGULAR, Font.TRUETYPE_FONT) );
	}
	
	public Font getOriginalJavaFont()
	{
		return this.javaFont;
	}
	public Font getNormalizedJavaFont( int dpi )
	{
		Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();			
			map.put(TextAttribute.SIZE, normalizeFontSize( this.size, dpi ));
		return this.javaFont.deriveFont( map );
	}
 
	//Get the closest font size in 72 dpi relative to the given dpi 
	private int normalizeFontSize( double fontSizeInPoint, int dpi )
	{
		double shouldBeInch = fontSizeInPoint / JAVA_DEFAULT_DPI;
		int shouldHavePixel = (int)Math.round(shouldBeInch * dpi);
		return shouldHavePixel;
	}	
}