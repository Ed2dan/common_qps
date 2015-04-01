package com.paxar.utilities.pcMateImage.length;

public interface Length 
{
	//Return the pixel given the dpi
	public int getPixel(int dpi);
	//Return raw numerical value of their own respective type
	public double getRawValue();
	//Return measurment unit
	public String getUnit();
}
