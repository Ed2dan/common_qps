package com.paxar.utilities.pcMateImage.length;

public class Inch implements Length 
{
	private final static String UNIT = "inch";
	private final double inch;
	
	public static Inch getFromPixel(int pixel, int dpi)
	{
		return new Inch( ((double)pixel / (double)dpi) );
	}
	
	public Inch(double inch)
	{		
		this.inch = inch;
	}
	public double getInch() {
		return inch;
	}
	@Override
	public double getRawValue()
	{
		return this.getInch();
	}
	@Override
	public int getPixel(int dpi) {
		return (int)Math.round( this.inch * dpi );
	}
	@Override
	public String getUnit()
	{
		return UNIT;
	}

	public Inch inc(Inch amount)
	{
		return new Inch( this.inch + amount.inch );
	}
	public Inch dec(Inch amount)
	{
		return new Inch( this.inch - amount.inch );
	}
}
