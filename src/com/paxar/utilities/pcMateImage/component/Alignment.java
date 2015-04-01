package com.paxar.utilities.pcMateImage.component;

public class Alignment {
	/*
	 * align left top means the x y coordinate where you start to print the string is left top of the string printed
	 * align left down means the x y coordinate where you start to print the string is left down of the string printed 
	 * etc...
	 * This means when some image are aligned to the right and in different length, you can simply give them the same x of right top 
	 * and they will aligned to the right. This will NOT make 2 image appear align right if you give them the same x of left top
	 * Default to up and left
	 */
	private int horizontial_alignment = 0, vertical_alignment = 0;
	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_UP = 0;
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGHT = 2;
	public static final int ALIGN_DOWN = 2;
	
	public int getHorizontialAlignment() { return horizontial_alignment; }
	public int getVerticalAlignment() { return this.vertical_alignment; }
	
	public void setHorizonialAlignment(int alignment) { this.horizontial_alignment = alignment; }
	public void setVerticalAlignment(int alignment) { this.vertical_alignment = alignment; }		
}
