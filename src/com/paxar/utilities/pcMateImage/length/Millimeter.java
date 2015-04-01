package com.paxar.utilities.pcMateImage.length;

public class Millimeter implements Length 
{
	private final static String UNIT = "mm";
	private static final double MM_TO_INCH = 0.03937007874015748031496062992126;
	private final double mm;
	
	public static Millimeter getFromPixel(int pixel, int dpi)
	{
		return new Millimeter( (double)pixel / (double)dpi / MM_TO_INCH );
	}
	
	public Millimeter(double mm)
	{
		this.mm = mm;
	}
	public double getMm() {
		return mm;
	}
	@Override
	public double getRawValue()
	{
		return this.getMm();
	}
	@Override
	public int getPixel(int dpi)
	{
		return (int)Math.round( mm * MM_TO_INCH * dpi );
	}
	@Override
	public String getUnit()
	{
		return UNIT;
	}
	
	public Inch inc(Millimeter amount)
	{
		return new Inch( this.mm + amount.mm );
	}
	public Inch dec(Millimeter amount)
	{
		return new Inch( this.mm - amount.mm );
	}
}
