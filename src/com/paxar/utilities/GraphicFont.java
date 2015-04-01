package com.paxar.utilities;
/*
** GraphicFont class 1.0
** 2/22/96 Kevin Hughes, kev@kevcom.com
** 3/26/02 (GNU license added, email address updated)
**
** This program is free software; you can redistribute it and/or modify
** it under the terms of the GNU General Public License as published by
** the Free Software Foundation; either version 2 of the License, or
** at your option) any later version.
**
** This program is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
** GNU General Public License for more details.
**
** You should have received a copy of the GNU General Public License
** along with this program; if not, write to the Free Software
** Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
**
** Should you make any improvements, please contact the original author.
**
** Limitations:
**
**    * There is no way to retrieve the name, size, or style of these
**      fonts. It would be nice if this information could be put in the
**      comment area of the image, (as well as pointers to T1/other versions)
**      but I have no Java code yet to extract GIF comments.
**    * The font format has no magic number; GF crashes if given a bad image.
**    * Kerning is not supported.
**    * Other non-GF font encodings (PC, Mac, Java) have not been fully tested.
**    * Fonts cannot be drawn in multiple colors.
**    * Up/down and right-to-left rendering is not supported.
**    * You cannot combine glyphs to make others.
**    * This file probably has more lines of comments than code. :)
*/

import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * A class that allows the use of a font encoded in the GraphicFont format.
 * It allows much of the same functionality as Sun's Font and FontMetrics
 * classes.
 *
 * For example:
 * <pre>
 *   g = getGraphics();
 *   GraphicFont gf = new GraphicFont(fontImage);
 *   gf.setFgColor(Color.green);
 *   gf.drawString(g, "This is a string drawn with GraphicFont.", 10, 10);
 *   int ascent = gf.getAscent();
 * </pre>
 *
 * @see		awt.Font
 * @see		awt.FontMetrics
 * @version	1.0 February 22, 1996
 * @author	Kevin Hughes, kev@kevcom.com
 */

public class GraphicFont
{
	private int DEFAULT_TAB_WIDTH = 5;
	private int UNKNOWN_CHAR = 42; // an asterix
	private int NO_CHAR = -1;
	private int BLANK_PIXEL = 0x00000000;
	private int OPAQUE_PIXEL = 0xff000000;
	private int BLACK_PIXEL = 0xff000000;
	private int WHITE_PIXEL = 0x00ffffff;
//	private float CONTRAST = (float) 0.2;
	private float CONTRAST = (float) 0.0;

// The CONTRAST value increases the foreground/background contrast by a fixed
// percent, in order to see antialiased text more clearly against colored
// backgrounds.

	private int GF_ORDER = 0;
	private int JAVA_SHORT_ORDER = 1;
	private int JAVA_LONG_ORDER = 2;
	private int MAC_ORDER = 3;
	private int PC_ORDER = 4;
	private int CUSTOM_ORDER = 5;

	private Image fontImage;
	private int imageByteArray[];
	private int imageWidthArray[];
	private int imageByteOffsetArray[];

	private int fontChars;
	private int fontOrder;

	private int totalHeight;
	private int fontHeight;
	private int ascent;
	private int descent;
	private int wordSpace;
	private int charSpace;
	private int leading;
	private int maxAscent;
	private int maxDescent;
	private int tabWidth;

// These font tables describe how the fonts are written in the
// encoded format, with the codes equal to Java font characters.

// The character sequence for the basic GraphicFont encoding format.
// This helps translate the characters into the proper Java character
// numbering scheme.

	private int gfCharMap[] = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
		'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
		'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
		'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(',
		')', '_', '+', '|', '{', '}', ':', '"', '<', '>', '?', '`',
		'-', '=', '\\', '[', ']', ';', '\'', ',', '.', '/'
		};

// Side note: Java fonts are apparently *not* ISO-Latin-1 (ISO8859-1).
// The font set is off by just a few characters.

// Damn those Mac fonts; they're all screwed up.
// This table starts at ASCII value 127. Unknown characters with no Java
// equivalent are replaced with asterisks (ASCII value 42).

	private int macCharMap[] = {
		42, 196, 197, 199, 201, 209, 214, 220, 225, 224, 226, 228,
		227, 229, 231, 233, 232, 234, 235, 237, 236, 238, 239, 241,
		243, 242, 244, 246, 245, 250, 249, 251, 252, 134, 176, 162,
		163, 167, 149, 182, 223, 174, 169, 153, 180, 168, 42, 198,
		216, 42, 177, 42, 42, 165, 181, 42, 42, 42, 42, 42, 170, 186,
		42, 230, 248, 191, 161, 172, 42, 131, 42, 42, 171, 187, 133,
		42, 192, 195, 213, 140, 156, 150, 151, 147, 148, 145, 146,
		247, 42, 255, 159, 42, 164, 139, 155, 42, 42, 135, 183, 42,
		132, 137, 194, 202, 193, 203, 200, 205, 206, 207, 204, 211,
		212, 42, 210, 218, 219, 217, 42, 136, 152, 175, 42, 42, 42,
		184, 42, 42, 42
		};

// If a character cannot be found, this table is used to try to make
// a reasonable substitution from the standard ASCII set.

	private int substitutionMap[] = {
		131, 102, 138, 83, 139, 60, 145, 39, 146, 39, 147, 34,
		148, 34, 150, 45, 151, 45, 152, 126, 154, 115, 155, 62,
		159, 89, 166, 124, 169, 99, 173, 45, 174, 114, 181, 117,
		192, 65, 193, 65, 194, 65, 195, 65, 196, 65, 197, 65,
		199, 67, 200, 69, 201, 69, 202, 69, 203, 69, 204, 73,
		205, 73, 206, 73, 207, 73, 209, 78, 210, 79, 211, 79,
		212, 79, 213, 79, 214, 79, 215, 120, 217, 85, 218, 85,
		219, 85, 220, 85, 221, 89, 223, 66, 224, 97, 225, 97,
		226, 97, 227, 97, 228, 97, 229, 97, 231, 99, 232, 101,
		233, 101, 234, 101, 235, 101, 236, 105, 237, 105,
		238, 105, 239, 105, 240, 111, 241, 110, 242, 111,
		243, 111, 244, 111, 245, 111, 246, 111, 247, 47, 249, 117,
		250, 117, 251, 117, 252, 117, 253, 121, 255, 121, -1, -1
		};

	private Hashtable charMap;

	private int fgColorRed;
	private int fgColorGreen;
	private int fgColorBlue;
	private int bgColorRed;
	private int bgColorGreen;
	private int bgColorBlue;

	private boolean hasBackground;

/**
 * Initializes a new GraphicFont with the encoded font passed to it.
 * Font metrics information is initialized with the default values as
 * suggested by the font's author.
 * The font image must be completely loaded beforehand.
 * @param loadedImage	An image encoded in the GraphicFont format
 */

	public GraphicFont(Image loadedImage)
	{
		setFont(loadedImage);
	}

/**
 * Sets a new font. Font metrics information is set to the default
 * values as suggested by the font's author.
 * The font image must be completely loaded beforehand.
 * @param loadedImage	An image encoded in the GraphicFont format
 */

	public void setFont(Image loadedImage)
	{
		int i, j;

// First, the font values and arrays are initialized.
// The foreground is black, the background is white but is not set.

		fontImage = loadedImage;
		fgColorRed = 0;
		fgColorGreen = 0;
		fgColorBlue = 0;
		bgColorRed = 255;
		bgColorGreen = 255;
		bgColorBlue = 255;
		hasBackground = false;

// The font image is converted into a byte array.

		int fontImageWidth = fontImage.getWidth(null);
		int fontImageHeight = fontImage.getHeight(null);
		fontHeight = fontImageHeight - 2;
		imageByteArray = new int[fontImageWidth * fontImageHeight];
		PixelGrabber pg = new PixelGrabber(fontImage, 0, 0,
			fontImageWidth, fontImageHeight, imageByteArray,
			0, fontImageWidth);

		try {
			pg.grabPixels();
			while ((pg.status() & ImageObserver.ALLBITS) == 0)
				;
		} catch (InterruptedException e) {
			;
		}

// The suggested default font metrics are loaded.

		totalHeight = imageByteArray[0] >> 16 & 0xff;
		ascent = imageByteArray[0] >> 8 & 0xff;
		descent = imageByteArray[0] >> 0 & 0xff;
		wordSpace = imageByteArray[1] >> 16 & 0xff;
		charSpace = imageByteArray[1] >> 8 & 0xff;
		leading = imageByteArray[1] >> 0 & 0xff;
		maxAscent = imageByteArray[2] >> 16 & 0xff;
		maxDescent = imageByteArray[2] >> 8 & 0xff;
		fontOrder = imageByteArray[2] >> 0 & 0xff;
		tabWidth = (wordSpace + charSpace) * DEFAULT_TAB_WIDTH;

// How many characters are described in this font?

		fontChars = 0;
		for (i = 0; i < fontImageWidth; i++) {
			if (imageByteArray[i] == BLACK_PIXEL)
				fontChars++;
		}
		fontChars++;

// The character separators are recognized and the widths are recorded.

		imageWidthArray = new int[fontChars];
		imageByteOffsetArray = new int[fontChars];
		imageByteOffsetArray[0] = 1;

		int width = 0;
		for (i = 0, j = 0; i < fontImageWidth; i++) {
			if (imageByteArray[i] == BLACK_PIXEL) {
				imageByteOffsetArray[j + 1] = i + 2;
				imageWidthArray[j++] = width;
				width = 0;
			}
			else
				width++;
		}

		imageWidthArray[j] = width;

// Character mapping is performed according to the particular order.

		charMap = new Hashtable();
		if (fontOrder == GF_ORDER) {
			for (i = 0; i < 94; i++)
				addChar(gfCharMap[i], i);
		}
		else if (fontOrder == JAVA_SHORT_ORDER) {
			for (i = 33, j = 0; i < 127; i++)
				addChar(i, j++);
		}
		else if (fontOrder == JAVA_LONG_ORDER) {
			for (i = 33, j = 0; i < 256; i++)
				addChar(i, j++);
		}
		else if (fontOrder == MAC_ORDER) {
			for (i = 33, j = 0; i < 127; i++)
				addChar(i, j++);
			for (i = j, j = 0; i < 224; i++)
				addChar(macCharMap[j++], i);
		}
		else if (fontOrder == PC_ORDER) {
			for (i = 0; i < 256; i++)
				addChar(i, i);
		}

// Custom character mapping is then found and loaded.

		int testPixel;
		int index = (fontImageHeight - 1) * fontImageWidth;
		for (i = 0; i < fontChars; i++) {
			testPixel = imageByteArray[index + 
				imageByteOffsetArray[i] - 1];
			if (testPixel != WHITE_PIXEL) {
				int value = 0x00ffffff & testPixel;
				addChar(value, i);
			}
		}
	}

/**
 * Gets the total height of the current font, which amounts to the
 * ascent plus the descent plus the leading.
 * @return	The total height of the font in pixels
 */

	public int getHeight()
	{
		return totalHeight;
	}

/**
 * Gets the ascent of the current font, the distance from the baseline
 * to the top of the regular characters (typically A-Z, a-z, 0-9).
 * @return	The typical ascent in pixels
 */

	public int getAscent()
	{
		return ascent;
	}

/**
 * Gets the descent of the current font, the distance from the baseline
 * to the bottom of the regular characters (typically A-Z, a-z, 0-9).
 * @return	The typical descent in pixels
 */

	public int getDescent()
	{
		return descent;
	}

/**
 * Gets the maximum ascent of the current font. No character in the font
 * extends above the baseline further than this.
 * @return	The maximum ascent in pixels
 */

	public int getMaxAscent()
	{
		return maxAscent;
	}

/**
 * Gets the maximum descent of the current font. No character in the font
 * descends below the baseline further than this.
 * @return	The maximum descent in pixels
 */

	public int getMaxDescent()
	{
		return maxDescent;
	}

/**
 * Gets the leading of the current font, the space between the
 * descent of one line and the ascent of the line below it.
 * @return	The leading in pixels
 */

	public int getLeading()
	{
		return leading;
	}

/**
 * Gets the word spacing of the current font. The space character
 * is remapped to this.
 * @return	The typical space between words in pixels
 */

	public int getWordSpace()
	{
		return wordSpace;
	}

/**
 * Gets the space between non-space characters.
 * @return	The typical space between characters in pixels
 */

	public int getCharSpace()
	{
		return charSpace;
	}

/**
 * Gets the tab width in how many space characters make up a tab
 * character. The default is five - five spaces per tab.
 * @return	The tab width
 */

	public int getTabWidth()
	{
		return tabWidth;
	}

/**
 * Sets the leading in pixels. The setting will only be visible when creating
 * text blocks. The leading can be set to a negative value.
 * @param i		The leading to set the font to
 */

	public void setLeading(int i)
	{
		leading = i;
	}

/**
 * Sets the word spacing in pixels. The space character is
 * remapped to this. Negative values can be used.
 * @param i		The amount of word spacing used in drawing strings.
 */

	public void setWordSpace(int i)
	{
		wordSpace = i;
	}

/**
 * Sets the character spacing in pixels. Negative values can be used.
 * @param i		The amount of space between individual characters.
 */

	public void setCharSpace(int i)
	{
		charSpace = i;
	}

/**
 * Sets the tab width as the number of spaces that make up a tab character.
 * @param i		The number of spaces that comprise a tab character
 */

	public void setTabWidth(int i)
	{
		tabWidth = i;
	}

/**
 * Sets the foreground color, the color the font characters are drawn in.
 * @param c		The color characters will be drawn in
 */

	public void setColor(Color c)
	{
		if (c == null) {
			fgColorRed = 0;
			fgColorGreen = 0;
			fgColorBlue = 0;
		}
		else {
			fgColorRed = c.getRed();
			fgColorGreen = c.getGreen();
			fgColorBlue = c.getBlue();
		}
	}

/**
 * Sets the foreground color. Same as setColor(Color).
 * @param c		The color characters will be drawn in
 */

	public void setFgColor(Color c)
	{
		setColor(c);
	}

/**
 * Sets the background color the font characters are drawn against.
 * This is very useful for displaying different backgrounds for
 * antialiased fonts.
 * @param c		The color characters will be drawn against.
 *			If the background is set to null, the
 *			background will be transparent.
 */

	public void setBgColor(Color c)
	{
		if (c == null) {
			hasBackground = false;
			bgColorRed = 255;
			bgColorGreen = 255;
			bgColorBlue = 255;
		}
		else {
			hasBackground = true;
			bgColorRed = c.getRed();
			bgColorGreen = c.getGreen();
			bgColorBlue = c.getBlue();
		}
	}

/**
 * Returns the width in pixels of a given string. If a character is
 * unsupported in the font, the width of an asterisk character or
 * suitable substitute is used.
 * @param s	The desired string
 * @return	The width of the string after rendering
 */

	public int stringWidth(String s)
	{
		int length = s.length();
		int totalLength = 0;
		int i, c, width;

		for (i = 0; i < length; i++) {
			c = s.charAt(i);
			if (c == ' ')
				totalLength += wordSpace;
			else if (c == '\t')
				totalLength += tabWidth;
			else {
				width = charWidth(s.charAt(i));
				totalLength += width;
				if (i != length - 1)
					totalLength += charSpace;
			}
		}

		return totalLength;
	}

/**
 * Returns the width in pixels of a given character. If the character is
 * unknown, the width of an asterix character or suitable substitue is used.
 * @param c	The desired character
 * @return	The width of the character after rendering
 */

	public int charWidth(int c)
	{
		if (c == ' ')
			return wordSpace;
		else if (c == '\t')
			return tabWidth;

		return imageWidthArray[lookupChar(c)] - 2;
	}

/**
 * Returns a rendered image given a string using the current font.
 * If a character is unknown, an asterisk character is substituted.
 * Non-US characters that are not supported in the font may be substituted
 * for simpler ASCII characters.
 * @param s	The desired string
 * @return	The final rendered image. If no background is set,
 *		the background will be transparent.
 */

	public Image stringImage(String s)
	{
		int c, i, x1, x2, width;
		int length = s.length();
		int byteArray[];

		width = stringWidth(s);
		int maxIndex = width * fontHeight;
		byteArray = new int[maxIndex];
                for (i = 0; i < maxIndex; i++)
                        if (hasBackground == true)
                                byteArray[i] = OPAQUE_PIXEL | bgColorRed << 16 |
                                bgColorGreen << 8 | bgColorBlue << 0;
                        else
                                byteArray[i] = BLANK_PIXEL;

		x2 = 0;
		for (i = 0; i < length; i++) {
			c = s.charAt(i);
			if (c == ' ')
				x2 += wordSpace;
			else if (c == '\t')
				x2 += tabWidth;
			else {
				x1 = imageByteOffsetArray[lookupChar(c)];
				copyCharImage(imageByteArray,
					fontImage.getWidth(null), x1,
					byteArray, width, x2,
					charWidth(c), fontHeight, 0);
				if (i != length - 1)
					x2 += charWidth(c) + charSpace;
			}
		}

		return Toolkit.getDefaultToolkit().createImage(new
			MemoryImageSource(width, fontHeight, byteArray,
			0, width));
	}

// Copies the correct characters from the font image in order to
// render text. All the nifty color manipulation and such is done here.

	private void copyCharImage(int sourceArray[], int sourceWidth,
		int sourceX, int destArray[], int destWidth, int destX,
		int cWidth, int cHeight, int startY)
	{
		int x1, x2, y, sourceIndex, destIndex;
		int alpha, red, green, blue;
		boolean doCopy;

		for (y = startY; y < startY + cHeight; y++) {
			for (x1 = sourceX, x2 = destX; x1 < sourceX + cWidth;
			x1++, x2++) {
				doCopy = true;
				sourceIndex = ((y - startY + 1) *
					sourceWidth) + x1;

				alpha = sourceArray[sourceIndex] >> 24 & 0xff;
				red = sourceArray[sourceIndex] >> 16 & 0xff;
				green = sourceArray[sourceIndex] >> 8 & 0xff;
				blue = sourceArray[sourceIndex] >> 0 & 0xff;

				if (red == 0 && green == 0 && blue == 0) {
					red = fgColorRed;
					green = fgColorGreen;
					blue = fgColorBlue;
				}
				else if (hasBackground == true) {
					alpha = 255;
					if (red == 255 && green == 255 &&
					blue == 255)
						doCopy = false;
					else {
						float percent = (float) red /
							(float) 255;
						float ipercent = ((float) 1.0 -
							percent + CONTRAST);
						if (ipercent > 1.0)
							ipercent = (float) 1.0;
						red = (int) ((percent * (float)
							bgColorRed) + (ipercent
							* (float) fgColorRed));
						green = (int) ((percent *
							(float) bgColorGreen) +
							(ipercent * (float)
							fgColorGreen));
						blue = (int) ((percent *
							(float) bgColorBlue) +
							(ipercent * (float)
							fgColorBlue));
					}
				}

				if (doCopy) {
					destIndex = (y * destWidth) + x2;
					destArray[destIndex] = alpha << 24 |
					red << 16 | green << 8 | blue << 0;
				}
			}
		}
	}

/**
 * Returns a rendered image of a left-justified text block given a string
 * and a width in pixels. Only spaces are wrapped. Newline characters (\n)
 * create hard breaks.
 * @param s		The desired string
 * @param width		The maximum width of the text block
 * @return		The final rendered image. If no background is set,
 *			the background will be transparent.
 */

	public Image stringImage(String s, int width)
	{
		int c, i, x1, x2, y, cLen = 0;
		int currentWidth = 0, maxWidth = 0, lines = 1;
		int lastSpace = 0;
		int byteArray[];
		char charArray[];

		int length = s.length();
		charArray = new char [length];
		charArray = s.toCharArray();
		for (i = 0; i < length; i++) {
			c = (int) charArray[i];
			if (c == '\n')
				currentWidth = 0;
			else if (c == ' ' || c == '\t')
				cLen = charWidth(c);
			else
				cLen = charWidth(c) + charSpace;

			if (currentWidth + cLen > width) {
				if (c == ' ')
					charArray[i] = '\n';
				else
					charArray[lastSpace] = '\n';
				currentWidth = 0;
				i = lastSpace;
			}
			else {
				if (c == ' ')
					lastSpace = i;
				currentWidth += cLen;
			}
		}

// This loop goes through the string again to calculate the
// correct maximum image width.

		currentWidth = 0;
		for (i = 0; i < length; i++) {
			c = (int) charArray[i];
			if (c == '\n') {
				currentWidth -= charSpace;
				if (currentWidth > maxWidth)
					maxWidth = currentWidth;
				currentWidth = 0;
				lines++;
			}
			else if (c == ' ' || c == '\t')
				currentWidth += charWidth(c);
			else
				currentWidth += (charWidth(c) + charSpace);
		}
		c = (int) charArray[i - 1];
		if (c != ' ' && c != '\t')
			currentWidth -= charSpace;
		if (currentWidth > maxWidth)
			maxWidth = currentWidth;

		int height = ((fontHeight + leading) * lines) - leading;
		int maxIndex = maxWidth * height;
		byteArray = new int[maxIndex];
                for (i = 0; i < maxIndex; i++)
                        if (hasBackground == true)
                                byteArray[i] = OPAQUE_PIXEL | bgColorRed << 16 |
                                bgColorGreen << 8 | bgColorBlue << 0;
                        else
                                byteArray[i] = BLANK_PIXEL;

		x2 = 0;
		y = 0;
		for (i = 0; i < length; i++) {
			c = (int) charArray[i];
			if (c == ' ')
				x2 += wordSpace;
			else if (c == '\t')
				x2 += tabWidth;
			else if (c == '\n') {
				x2 = 0;
				y += fontHeight + leading;
			}
			else {
				x1 = imageByteOffsetArray[lookupChar(c)];
				copyCharImage(imageByteArray,
					fontImage.getWidth(null), x1,
					byteArray, maxWidth, x2,
					charWidth(c), fontHeight, y);
				if (i != length - 1)
					x2 += charWidth(c) + charSpace;
			}
		}

		return Toolkit.getDefaultToolkit().createImage(new
			MemoryImageSource(maxWidth, height, byteArray,
			0, maxWidth));
	}

/**
 * Draws a rendered image of a string given a graphics context, a string,
 * and the coordinates to draw at. Similar to Font.drawString(), the
 * Y coordinate starts at the baseline of the font.
 * @param g		The graphic context to draw into
 * @param s		The desired string to render
 * @param x		The X coordinate
 * @param y		The Y coordinate
 */

	public void drawString(Graphics g, String s, int x, int y)
	{
		Image stringImage = stringImage(s);
		g.drawImage(stringImage, x, y - ascent, null);
	}

/**
 * Draws a rendered image of a left-justified text block given a graphics
 * context, a string, the coordinates to draw at, and a width in pixels.
 * The Y coordinate starts at the baseline of the first line.
 * @param g		The graphic context to draw into
 * @param s		The desired string to render
 * @param x		The X coordinate
 * @param y		The Y coordinate
 * @param width		The maximum width of the text block
 */

	public void drawString(Graphics g, String s, int x, int y, int width)
	{
		Image stringImage = stringImage(s, width);
		g.drawImage(stringImage, x, y - ascent, null);
	}

/**
 * Returns a rendered image given a character.
 * If the character is unknown, an asterix or appropriate substitute
 * is returned.
 * @param c		The desired character
 * @return		The rendered image of the character.
 *			If no background is set, the background will
 *			be transparent.
 */

	public Image charImage(int c)
	{
		return stringImage(String.valueOf(c));
	}

// Character map hash table functions.
// The key: The integer code that represents a glyph
// The value: The index at which the glyph is stored in the image

// If an index cannot be found at first, a substitution is attempted.
// If that fails, the index to the default unknown character is returned.

	private void addChar(int key, int value)
	{
		charMap.put(new Integer(key), new Integer(value));
	}

	private int getChar(int key)
	{
		Integer result;
		result = (Integer) charMap.get(new Integer(key));
		if (result == null)
			return NO_CHAR;
		else
			return result.intValue();
	}

	private int lookupChar(int c)
	{
		int i, value, newValue;

		value = getChar(c);
		if (value != NO_CHAR)
			return value;
		for (i = 0; substitutionMap[i] != -1; i += 2) {
			if (substitutionMap[i] == c) {
				newValue = getChar(substitutionMap[i + 1]);
				if (newValue == NO_CHAR)
					return getChar(UNKNOWN_CHAR);
				else
					return newValue;
			}
		}
		return getChar(UNKNOWN_CHAR);
	}
}
