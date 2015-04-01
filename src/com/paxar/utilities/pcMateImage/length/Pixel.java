package com.paxar.utilities.pcMateImage.length;

public class Pixel implements Length
{
	private final static String UNIT = "px";
	private final int pixel;
	
	public static Pixel getFromPixel(int pixel, int dpi)
	{
		return new Pixel(pixel);
	}
	
	public Pixel(int pixel)
	{
		this.pixel = pixel;
	}
	@Override
	public double getRawValue()
	{
		return this.pixel;
	}
	@Override
	public int getPixel(int dpi)
	{
		return this.pixel;
	}
	@Override
	public String getUnit()
	{
		return UNIT;
	}
	
	public Pixel inc(Pixel amount)
	{
		return new Pixel( this.pixel + amount.pixel );
	}
	public Pixel dec(Pixel amount)
	{
		return new Pixel( this.pixel - amount.pixel );
	}
}
