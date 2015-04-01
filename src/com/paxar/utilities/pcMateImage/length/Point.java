package com.paxar.utilities.pcMateImage.length;

public class Point implements Length  
{
	private final static String UNIT = "pt";
	private final static double PT_TO_INCH = 72;	
	private final double pt;
	
	public static Point getFromPixel(int pixel, int dpi)
	{
		return new Point( ((double)pixel / (double)dpi) * PT_TO_INCH );
	}
	
	public Point(double pt)
	{
		this.pt = pt;
	}
	@Override
	public int getPixel(int dpi) {
		return (int)Math.round(this.pt / PT_TO_INCH * dpi);
	}
	@Override
	public double getRawValue() {
		return this.pt;
	}
	@Override
	public String getUnit() {
		return UNIT;
	}
	
	public Point inc(Point amount)
	{
		return new Point( this.pt + amount.pt );
	}
	public Point dec(Point amount)
	{
		return new Point( this.pt - amount.pt );
	}
	
}
