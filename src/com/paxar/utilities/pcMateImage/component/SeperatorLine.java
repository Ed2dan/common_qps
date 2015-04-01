package com.paxar.utilities.pcMateImage.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.paxar.utilities.pcMateImage.length.Length;
import com.paxar.utilities.pcMateImage.length.LengthPair;

public class SeperatorLine extends LabelBase 
{
	public SeperatorLine(LengthPair position, LengthPair lineDimension)
	{
		this(position, lineDimension, Color.BLACK);
	}
	
	private SeperatorLine(LengthPair position, LengthPair lineDimension, Color baseColor) 
	{
		super(lineDimension, position, baseColor);
	}
}
