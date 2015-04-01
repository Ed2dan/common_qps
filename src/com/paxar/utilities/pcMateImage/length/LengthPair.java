package com.paxar.utilities.pcMateImage.length;

public class LengthPair 
{	
	public static LengthPair Millimeter(double width, double height)
	{
		return new LengthPair( new Millimeter(width),new Millimeter(height) );
	}
	public static LengthPair Inch(double width, double height)
	{
		return new LengthPair( new Inch(width),new Inch(height) );
	}
	public static LengthPair Point(int width, int height)
	{
		return new LengthPair( new Point(width),new Point(height) );
	}	
	public static LengthPair Pixel(int width, int height)
	{
		return new LengthPair( new Pixel(width),new Pixel(height) );
	}
	
	protected final Length width, height;
	public LengthPair( Length width, Length height )
	{
		this.width = width;
		this.height = height;
	}
	public Length getWidth() {
		return width;
	}
	public Length getHeight() {
		return height;
	}
}
