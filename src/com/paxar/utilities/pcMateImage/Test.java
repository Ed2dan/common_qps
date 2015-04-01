package com.paxar.utilities.pcMateImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.paxar.utilities.pcMateImage.component.*;
import com.paxar.utilities.pcMateImage.length.*;
import com.paxar.utilities.pcMateImage.rule.*;

public class Test 
{
	private static final boolean featureTest = false;
	private static final boolean printRuler = false;
	private static final boolean saveImage = true;

	//Entry point
	public static void main(String[] args) throws Exception
	{		
		//testPrint();		
		//testCanvas();		
		
		if(featureTest)
		{
			int dpi = 300;
			Image images = doTest( "C:\\test", "testFont_2.ttf" );		
			
			ImageBuilder builder;
			if(printRuler)
				builder = new ImageBuilder( dpi, images, new Millimeter(2.5) );
			else
				builder = new ImageBuilder( dpi, images );
			builder.displayImage( );
			if(saveImage)
				builder.saveImage( "bmp", "gapTest_chars.bmp" );
		}
		else
		{
			int dpi = 600;
			List<Image> images = gapPageTestNew();
			ImageBuilder builder;
			if(printRuler)
				builder = new ImageBuilder( dpi, images, new Millimeter(1) );
			else
				builder = new ImageBuilder( dpi, images );
			builder.displayImage();
			if(saveImage)
				builder.saveImage("bmp", "gap_page_");
		}
		System.out.println( "Done" );
	}	
	
	private static Font loadFont(File fontFile, double fontSize) throws FontFormatException, IOException
	{		
		Font loadedFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);	    	
    	Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
    		map.put(TextAttribute.SIZE, fontSize);
    		map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
    	return loadedFont.deriveFont( map );		
	}
	private static int testLineHeight = 30;
	private static int testLine = 1;
	private static void testFont(Font font, Graphics2D g)
	{		
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		String fontFamily = fm.getFont().getFamily();
		String fontName = fm.getFont().getFontName();
		String fontInfo = fontFamily + "'s " + fontName;
		String testString = "ABCDE123456,./;':\"\\!@#$%^&*()-=+_";
		//String testString = "プリント";
		
		int infoStartX = 0, testStartX = 600, lineLength = 100;				
		g.drawString( fontInfo, 0, (testLineHeight * testLine) );
		g.drawLine(infoStartX, (testLineHeight * testLine), infoStartX + lineLength, (testLineHeight * testLine));
		g.drawString( testString, testStartX, (testLineHeight * testLine) );
		g.drawLine(testStartX, (testLineHeight * testLine), testStartX + lineLength, (testLineHeight * testLine));	
		testLine++;
	}
	private static void dumpCareSymbols(Font font, Graphics2D g)
	{		
		g.setFont(font);
		String testString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";	
		int infoStartX = 0, testStartX = 600, lineLength = 100;				
		g.drawString( testString, 0, (testLineHeight * testLine) );
		g.drawLine(infoStartX, (testLineHeight * testLine), infoStartX + lineLength, (testLineHeight * testLine));	
		testLine++;
	}
	private static void testPrint() throws Exception
	{
		String customFontFolder = "C:\\store\\doc\\GAP\\Old Navy\\Collected Fonts\\Collected Fonts\\";
		String KozGoProFolder = customFontFolder + "Kozuka Gothic Pro" + File.separator;
		String MyriadProFolder = customFontFolder + "Myriad Pro" + File.separator;
		
		int fontSize = 30;		
		List<Font> fonts = new ArrayList<Font>();		

		fonts.add( loadFont(new File( KozGoProFolder + "KozGoPro-Bold.otf"),fontSize) );
		fonts.add( loadFont(new File( KozGoProFolder + "KozGoPro-ExtraLight.otf"),fontSize) );
		fonts.add( loadFont(new File( KozGoProFolder + "KozGoPro-Heavy.otf"), fontSize) );
		fonts.add( loadFont(new File( KozGoProFolder + "KozGoPro-Light.otf"), fontSize) );
		fonts.add( loadFont(new File( KozGoProFolder + "KozGoPro-Medium.otf"),fontSize) );
		fonts.add( loadFont(new File( KozGoProFolder + "KozGoPro-Regular.otf"), fontSize) );

		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-SemiboldIt.otf"), fontSize) );
		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-Semibold.otf"), fontSize) );
		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-Regular.otf"), fontSize) );
		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-It.otf"), fontSize) );
		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-CondIt.otf"), fontSize) );
		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-Cond.otf"), fontSize) );
		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-BoldIt.otf"), fontSize) );
		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-BoldCondIt.otf"), fontSize) );
		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-BoldCond.otf"), fontSize) );
		fonts.add( loadFont(new File( MyriadProFolder + "MyriadPro-Bold.otf"), fontSize) );

		fonts.add( loadFont(new File( customFontFolder + "ARIALUNI.TTF"), fontSize) );		
		fonts.add( loadFont(new File ( customFontFolder + "Robosans-Regular.ttf"), fontSize) );
		
		fonts.add( loadFont(new File( "C:\\Windows\\Fonts\\" + "KozGoPro-Regular.otf"), fontSize) );
		fonts.add( new Font( "Kozuka Gothic Pro R", Font.PLAIN, fontSize) );
		

		fonts.add( loadFont(new File( customFontFolder + "JapaneseNext.ttf"), fontSize) );		
		fonts.add( loadFont(new File( customFontFolder + "SARTEXCARESYM GAP.TTF"), fontSize) );
		
		
		
		BufferedImage omg = new BufferedImage( 1024, 768, BufferedImage.TYPE_BYTE_BINARY );

		Graphics2D g = omg.createGraphics();				
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1024, 768);
		g.setColor(Color.BLACK);
		/*
		for(Font font : fonts)
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);		
		g.setFont( new Font("KozGoPro-Bold", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 50 );
		g.drawLine(0, 50, 100, 50);
		g.setFont( new Font("KozGoPro-Bold", Font.BOLD, fontSize) ); g.drawString( "Hello World", 0, 100 );
		g.drawLine(0, 100, 100, 100);
		g.setFont( new Font("KozGoPro-Bold", Font.ITALIC, fontSize) ); g.drawString( "Hello World", 0, 150 );
	  	Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
		map.put(TextAttribute.SIZE, fontSize);
		map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRA_LIGHT);		
		g.setFont( new Font("KozGoPro-Bold", Font.PLAIN, fontSize).deriveFont(map) ); g.drawString( "Hello World", 0, 200 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 250 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 300 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 350 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 400 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 450 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 500 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 550 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 600 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 650 );
		g.setFont( new Font("KozGoPro", Font.PLAIN, fontSize) ); g.drawString( "Hello World", 0, 700 );
		*/	
		
		for(Font font : fonts)
			testFont(font, g);
			//dumpCareSymbols(font, g);
		
		JOptionPane.showMessageDialog(null, new JLabel( new ImageIcon( omg ) ), "icon", -1);		
		
		class OMGLabel extends JLabel
		{
			private Font font;
			public OMGLabel(Font font){
				this.font = font;				 
			}
			@Override
			public void paintComponent(Graphics g){
				super.paintChildren(g);
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, 1024, 768);
				g.setColor(Color.BLACK);
				g.setFont(font);
				FontMetrics fm = g.getFontMetrics();
				String fontFamily = fm.getFont().getFamily();
				String fontName = fm.getFont().getFontName();
				String fontInfo = fontFamily + "'s " + fontName;
				String testString = "プリントABCDE123456,./;':\"\\!@#$%^&*()-=+_";
						
				g.drawString( fontInfo, 0, 30 );				
				g.drawString( testString, 20,30 );					
			}
		}
		JLabel label1 = new OMGLabel( new Font( "Kozuka Gothic Pro R", Font.PLAIN, fontSize) );
		JLabel label2 = new OMGLabel( new Font( "Arial Unicode MS", Font.PLAIN, fontSize) );
		JFrame frame = new JFrame("LabelDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1024, 768);
		frame.add(label1);
		frame.add(label2);
		frame.setVisible(true);		
		//JOptionPane.showMessageDialog(null, label, "icon", -1);
		
		g.dispose();
		
	}
	private static void testCanvas() throws Exception
	{
		int dpi = 600;
		FloatFont materialChKrJpFont = new FloatFont("Arial Unicode MS", Font.PLAIN, 4.5 );
		List<Image> images = new ArrayList<Image>();		
		
		TextLine aLine = new TextLine( LengthPair.Millimeter(0, line(0)) );
			aLine.add( new Text( materialChKrJpFont, "棉", LengthPair.Millimeter(0, 0)) );
			aLine.render(dpi);
		Image labelBase = new LabelBase( aLine.getCanvas() );
		labelBase.add(aLine);		
		images.add( labelBase );
		
		Image text = new Text( materialChKrJpFont, "棉", LengthPair.Millimeter(0, 0));
			text.render(dpi);
		Image labelBase2 = new LabelBase( text.getCanvas() );
		labelBase2.add( text );
		images.add( labelBase2 );
		
		ImageBuilder builder = new ImageBuilder( dpi, images, new Millimeter(2.5) );
		builder.displayImage();
	}
	
	//Evil hax for positioning
	private static double line_based = 0;
	private static double lineheight = 1.5;
	private static void relocate_lineBase(double mm){ line_based = mm; }
	private static void reset_lineheight(double mm){ lineheight = mm; }
	private static double line(int line){ return line_based + lineheight * line; }
	
	public static List<Image> gapPageTestNew() throws Exception
	{	
		int dpi = 600;
		Length materialLine = new Millimeter(1.7);
		Length careLine = new Millimeter(1.9);
		Length multiLine = new Millimeter(1.9);
		
		double dot = 1.5;
		double cou = 3.2;
		double far = 39;
		
		//String MyriadFontPath = "font\\Myriad Pro\\MyriadPro-Regular.otf";		
		//String MyriadFontPath = "font\\Myriad Pro\\MyriadPro-Semibold.otf";
		String MyriadFontPath = "font\\Myriad Pro\\MyriadPro-Bold.otf";
		double fontWeight = TextAttribute.WEIGHT_REGULAR;
		//double fontWeight = TextAttribute.WEIGHT_ULTRABOLD;
		
		FloatFont careSymbolJP = new FloatFont(new File("font\\JapaneseNext.ttf"), "JapaneseNext", 17);
		FloatFont careSymbolCN = new FloatFont(new File("font\\SARTEXCARESYM GAP.TTF"), "SARTEXCARESYMGAP", 12);
		
		FloatFont dotFont = new FloatFont("Arial Unicode MS", Font.PLAIN, 6 );
		FloatFont countryFontMaterial = new FloatFont("Arial", Font.BOLD, 4.5 );
		FloatFont countryFont = new FloatFont("Arial", Font.BOLD, 4 );
		FloatFont materialFont = new FloatFont("Arial Unicode MS", Font.PLAIN, 4.5 );
		
		FloatFont careFont = new FloatFont( new File( MyriadFontPath ), "Myriad Pro", 4.5, fontWeight );
		FloatFont materialChKrJpFont = new FloatFont("Arial Unicode MS", Font.PLAIN, 4.5 );
		
		FloatFont sizeFont = new FloatFont("Arial Unicode MS", Font.PLAIN, 9 );
		FloatFont madeInFont = new FloatFont( new File( MyriadFontPath ), "Myriad Pro", 4.5, fontWeight );
		FloatFont extraInfoFont = new FloatFont( new File( MyriadFontPath ), "Myriad Pro", 4.75, fontWeight );
		
		List<Image> result = new ArrayList<Image>();	
	
		Length labelbaseWidth = new Millimeter( 40 );
		Length labelbaseHeight = new Millimeter( 94 );
		Length labelCenter = new Millimeter( labelbaseWidth.getRawValue() / 2 );
		LengthPair labelbaseDimension = new LengthPair( labelbaseWidth, labelbaseHeight );
		LineBreakRule multiLineRule = new StringWidthWordBreak( labelbaseWidth );
		LineBreakRule jpCnKrMultiLineRule = new StringWidthBreak( labelbaseWidth );
				
		//Page 1
		Image labelBase = new LabelBase( labelbaseDimension );
		
		TextLine aLine;
		Text partName, country;
		relocate_lineBase(0);
		
			aLine = new TextLine( LengthPair.Millimeter(0, 2.5) );
			partName = new Text( materialFont, "SHELL/EXTÉRIEUR/", LengthPair.Millimeter(dot, 0));
			aLine.add( partName );			
			aLine.add( new Text( materialChKrJpFont, "面料/表地", partName.addRelativeWidth(dpi, new Millimeter(0)) ) );	
		labelBase.add(aLine);
						
			aLine = new TextLine( aLine.addBaseRelativeHeight(dpi, new Millimeter(3.7)) );
			aLine.add( new Text(dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			aLine.add( new Text(materialFont, "70% COTTON, 30% POLYESTER", LengthPair.Millimeter(cou, 0)) );
		labelBase.add(aLine);
		
			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, materialLine ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFontMaterial, "CA :", LengthPair.Millimeter(cou, 0));			
			aLine.add( country );
			aLine.add( new Text( materialFont, "70% COTON, 30% POLYESTER", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
		labelBase.add(aLine);
	
			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, materialLine ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFontMaterial, "JP :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialChKrJpFont, "綿", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
			aLine.add( new Text( materialFont, "70%", LengthPair.Millimeter(far, 0)).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		labelBase.add(aLine);
			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, new Millimeter(2) ) );
			aLine.add( new Text( materialChKrJpFont, "ポリエステル", LengthPair.Millimeter(dot, 0)) );
			aLine.add( new Text( materialFont, "30%", LengthPair.Millimeter(far, 0)).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		labelBase.add(aLine);
		
			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, materialLine ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFontMaterial, "CN :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialFont, "70%", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
			aLine.add( new Text( materialChKrJpFont, "棉", LengthPair.Millimeter(far, 0)).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		labelBase.add(aLine);
			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, new Millimeter(2.0) ) );
			aLine.add( new Text( materialFont, "30%", LengthPair.Millimeter(dot, 0)) );
			aLine.add( new Text( materialChKrJpFont, "聚酯纤维", LengthPair.Millimeter(far, 0)).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		labelBase.add(aLine);
		
			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, new Millimeter( 3.4 ) ) );
			Text materialDetail = new Text( materialFont, "LINING/DOUBLURE/", LengthPair.Millimeter(dot, 0));
			aLine.add( materialDetail );			
			aLine.add( new Text( materialChKrJpFont, "里料/裏地", materialDetail.addRelativeWidth(dpi, new Millimeter(0)) ) );	
		labelBase.add(aLine);
		
			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, new Millimeter( 3.3 ) ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			aLine.add( new Text( materialFont, "100% COTTON", LengthPair.Millimeter(cou, 0)) );
		labelBase.add(aLine);
		
			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, materialLine ) );		
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFontMaterial, "CA :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialFont, "100% COTON", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
		labelBase.add(aLine);
		
			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, materialLine ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFontMaterial, "JP :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialChKrJpFont, "綿", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
			aLine.add( new Text( materialFont, "100%", LengthPair.Millimeter(far, 0)).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		labelBase.add(aLine);

			aLine = new TextLine( aLine.addBaseRelativeHeight( dpi, materialLine ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFontMaterial, "CN :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialFont, "100%", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
			aLine.add( new Text( materialChKrJpFont, "棉", LengthPair.Millimeter(far, 0)).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		labelBase.add(aLine);
		
		labelBase.add( new SeperatorLine( LengthPair.Millimeter(20, 49), LengthPair.Millimeter(6, 0.5) ).setHorizonialAlignment(Alignment.ALIGN_CENTER) );
		Image preCareSepLine = new SeperatorLine( LengthPair.Millimeter(19, 57.25), LengthPair.Millimeter(35, 0.2) ).setHorizonialAlignment(Alignment.ALIGN_CENTER);
		labelBase.add( preCareSepLine );
		Text careSymbolJPText = new Text( careSymbolJP, "EKNU", preCareSepLine.addRelativeHeight(dpi, new Millimeter(19), new Millimeter(0)) ).setHorizonialAlignment(Alignment.ALIGN_CENTER);
		labelBase.add( careSymbolJPText );
		Text careSymbolCNText = new Text( careSymbolCN, "H1S7x", careSymbolJPText.addRelativeHeight(dpi, new Millimeter(19), new Millimeter(0) ) ).setHorizonialAlignment(Alignment.ALIGN_CENTER);		
		labelBase.add( careSymbolCNText );
		Image postCareSepLine = new SeperatorLine( careSymbolCNText.addRelativeHeight(dpi, new Millimeter(19), new Millimeter(0.5) ), LengthPair.Millimeter(35, 0.2) ).setHorizonialAlignment(Alignment.ALIGN_CENTER);
		labelBase.add( postCareSepLine );
		
			aLine = new TextLine( postCareSepLine.addRelativeHeight( dpi, new Millimeter( 0 ), new Millimeter( 0 ) ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			aLine.add( new Text( careFont, "MACHINE WASH WARM. WASH INSIDE OUT WITH LIKE", LengthPair.Millimeter(cou, 0)).scale(0.96, 1.0) );
		labelBase.add(aLine);		
		Image materialMLine1 = new Text( careFont, 
					"COLORS. ONLY NON-CHLORINE BLEACH WHEN NEEDED." +
					"TUMBLE DRY LOW. WARM IRON. DO NOT IRON ON " + 
					"PRINT. DO NOT DRY CLEAN.", 
					aLine.addRelativeHeight( dpi, new Millimeter(dot), new Millimeter(-1.0) ))
					.setMultiLineRule(multiLineRule).setLineHeight( multiLine )
					.scale(0.96, 1.0);
		labelBase.add( materialMLine1 );
		
			aLine = new TextLine( materialMLine1.addRelativeHeight(dpi, new Millimeter( 0 ), new Millimeter(-1.0) ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFont, "CA : ", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( careFont, "LAVER À LA MACHINE À L’EAU TIÈDE. LAVER À", country.addRelativeWidth(dpi, new Millimeter(0.5)) ).scale(0.96, 1.0) );
		labelBase.add(aLine);
		labelBase.add( new Text( careFont, 
				"L’ENVERS AVEC COULEURS SEMBLABLES. UTILISER AU " +
				"BESOIN UN AGENT DE BLANCHIMENT SANS CHLORE " +
				"UNIQUEMENT. SÉCHER À LA MACHINE À BASSE " +
				"TEMPÉRATURE. NE PAS REPASSER LE MOTIF IMPRIMÉ." +
				"NE PAS NETTOYER À SEC.", 
				aLine.addRelativeHeight(dpi, new Millimeter(dot), new Millimeter( -1.0 )) )
				.setMultiLineRule(multiLineRule).setLineHeight( multiLine ).scale(0.96, 1.0) );
		
		result.add( labelBase );		
		
		//Page 2
		labelBase = new LabelBase( labelbaseDimension );
		
		relocate_lineBase(0);	
		
			aLine = new TextLine( LengthPair.Millimeter(0, line(2)) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFont, "ID :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( careFont, "CUCI DAN KERINGKAN DENGAN LAPISAN DALAM", country.addRelativeWidth(dpi, new Millimeter(0.5)) ).scale(0.96, 1.0) );
		labelBase.add(aLine);
		Image materialText2 = new Text( careFont, 
			"DILUAR, SATUKAN DENGAN YANG SEWARNA / CUCI " + 
			"DENGAN MESIN DAN AIR HANGAT/ HANYA GUNAKAN " + 
			"PEMUTIH NON KLORIN JIKA DIPERLUKAN/ KERINGKAN " + 
			"DALAM MESIN PENGERING BERSUHU RENDAH/SETRIKA " + 
			"DENGAN SUHU SEDANG/ JANGAN SETRIKA PADA " + 
			"GAMBAR / JANGAN DICUCI KERING.", 
			aLine.addBaseRelativeHeight( dpi, new Millimeter(dot), new Millimeter( 2.5 ) ))		
			.setMultiLineRule(multiLineRule).setLineHeight( multiLine )
			.scale(0.96, 1.0);
		labelBase.add( materialText2 );
		
			aLine = new TextLine( materialText2.addRelativeHeight( dpi, new Millimeter( 0 ), new Millimeter( -1.0 ) ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFont, "JP :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialChKrJpFont, "プリント部分にはアイロンをかけないでくださ", country.addRelativeWidth(dpi, new Millimeter(0.5)) ).setMaxCharSpace(new Point(4.2)) );
		labelBase.add(aLine);
		Text materialText3 = new Text( materialChKrJpFont, 
				"い。/ 洗濯の際は裏返しにして、同系色のものと一緒に洗濯/乾燥してください。", 
				aLine.addBaseRelativeHeight(dpi, new Millimeter(dot), new Millimeter( 2.5 )))
			.setMaxCharSpace(new Point(4.2))
			.setMultiLineRule(jpCnKrMultiLineRule).setLineHeight( new Millimeter( 2.2 ) );
		labelBase.add( materialText3 );
		
			aLine = new TextLine( materialText3.addRelativeHeight(dpi, new Millimeter( 0 ), new Millimeter(-1) ) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFont, "CN :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialChKrJpFont, "不可熨烫印花 请与同类色衣物反面洗涤，干燥", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
		labelBase.add(aLine);
		
		Text address = new Text( materialChKrJpFont,"東京都渋谷区  千駄ヶ谷 5-32-10",aLine.addBaseRelativeHeight(dpi, new Millimeter(dot), new Millimeter( 4.2 ) ));
		labelBase.add( address );
		address = new Text( materialChKrJpFont,"ギャップジャパン  株式会社",address.addBaseRelativeHeight(dpi, new Millimeter(dot), materialLine ));
		labelBase.add( address ); 
				
		Text etcLine1 = new Text( extraInfoFont, "S/123456-00   STYLEDESCRIPTI", address.addBaseRelativeHeight(dpi, new Millimeter(dot), new Millimeter( 4.0 ) ) );
		labelBase.add( etcLine1 );
		Text etcLine2 = new Text( extraInfoFont, "COLORDESLORDES   V/12345678   SP14   02/12", etcLine1.addBaseRelativeHeight(dpi, new Millimeter(dot), materialLine ) );
		labelBase.add( etcLine2 );		
		
		labelBase.add( new SeperatorLine( LengthPair.Millimeter(20, 45), LengthPair.Millimeter(6, 0.5) ).setHorizonialAlignment(Alignment.ALIGN_CENTER) );
	
		Image logo = new Glyph( new File("GAP_ON Logo.bmp"), new LengthPair( labelCenter, new Millimeter(58) ),  
				LengthPair.Millimeter(17, 4.1)).setHorizonialAlignment(TextAlignment.ALIGN_CENTER);
		labelBase.add( logo );
	
		Image sizeText;
		sizeText = new Text( sizeFont, "XS/TP", logo.addRelativeHeight(dpi, labelCenter, new Millimeter(-0.7) ) ).setHorizonialAlignment(TextAlignment.ALIGN_CENTER);
		labelBase.add( sizeText );
		sizeText = new Text( sizeFont, "TALL/GRAND", sizeText.addBaseRelativeHeight(dpi, labelCenter, new Millimeter(3))).setHorizonialAlignment(TextAlignment.ALIGN_CENTER);
		labelBase.add( sizeText );
		sizeText = new Text( sizeFont, "165/88A", sizeText.addBaseRelativeHeight(dpi, labelCenter, new Millimeter(3))).setHorizonialAlignment(TextAlignment.ALIGN_CENTER);
		labelBase.add( sizeText ); 
		sizeText = new Text( madeInFont, "MADE IN CHINA", sizeText.addBaseRelativeHeight(dpi, labelCenter, new Millimeter(6))).setHorizonialAlignment(TextAlignment.ALIGN_CENTER);
		labelBase.add( sizeText );
		sizeText = new Text( materialFont, "RN 54023 CA17897", sizeText.addBaseRelativeHeight(dpi, labelCenter, new Millimeter(2.0))).setHorizonialAlignment(TextAlignment.ALIGN_CENTER);
		labelBase.add( sizeText );

			aLine = new TextLine( sizeText.addBaseRelativeHeight(dpi, new Millimeter( 0 ), new Millimeter(2.5)) );
			Text materialDetail2 = new Text( materialFont, "SHELL/EXTÉRIEUR/", LengthPair.Millimeter(dot, 0));
			aLine.add( materialDetail2 );
			aLine.add( new Text( materialChKrJpFont, "面料/表地", materialDetail2.addRelativeWidth(dpi, new Millimeter(0))) );
		labelBase.add(aLine);
		
			aLine = new TextLine( aLine.addBaseRelativeHeight(dpi, new Millimeter(4.0)) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			aLine.add( new Text( materialFont, "100% COTTON", LengthPair.Millimeter(cou, 0)) );
		labelBase.add(aLine);
			
			aLine = new TextLine( aLine.addBaseRelativeHeight(dpi, materialLine) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFontMaterial, "CA :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialFont, "100% COTON", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
		labelBase.add(aLine);
		
			aLine = new TextLine( aLine.addBaseRelativeHeight(dpi, materialLine) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFontMaterial, "JP :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialChKrJpFont, "綿", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
			aLine.add( new Text( materialFont, "100%", LengthPair.Millimeter(far, 0)).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		labelBase.add(aLine);
		
			aLine = new TextLine( aLine.addBaseRelativeHeight(dpi, materialLine) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(dot, 0)) );
			country = new Text( countryFontMaterial, "CN :", LengthPair.Millimeter(cou, 0));
			aLine.add( country );
			aLine.add( new Text( materialFont, "100%", country.addRelativeWidth(dpi, new Millimeter(0.5)) ) );
			aLine.add( new Text( materialChKrJpFont, "棉", LengthPair.Millimeter(far, 0)).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		labelBase.add(aLine);
		
		result.add( labelBase );
		
		return result;
	}

	public static Image doTest(String customFontPath, String customFontName) throws Exception
	{	

		//Font size are in point ( 1 point = 1/72 inch )
		FloatFont headerFont = new FloatFont("Arial", Font.PLAIN, 9);
		FloatFont bodyFont = new FloatFont("Arial Unicode MS", Font.BOLD, 4);

		//Font Test		
		FloatFont uniqloFont = new FloatFont("UniqloRegular", Font.PLAIN, 4);
		FloatFont wslEngl = new FloatFont("WST_Engl", Font.PLAIN, 4);
		FloatFont dotFont = new FloatFont("Arial Unicode MS", Font.PLAIN, 8 );
		FloatFont materialFont = new FloatFont("Arial Unicode MS", Font.PLAIN, 4 );
		FloatFont countryFont = new FloatFont("Arial", Font.BOLD, 4 );		
		
		Image textImage;

		/*
		//Plain Text Test
		textImage.add( new Text( headerFont, "Hello World", LengthPair.Millimeter(2, 20)) );
		textImage.add( new Text( bodyFont, "Yeaa", LengthPair.Millimeter(2, 22.75)) );
		textImage.add( new Text( bodyFont, "End", LengthPair.Millimeter(2, 24)) );
		
		//Unicode Test
		textImage.add( new Text( bodyFont, "測試", LengthPair.Millimeter(2, 26)) );
		textImage.add( new Text( bodyFont, "テスト試", LengthPair.Millimeter(2, 28)) );
		textImage.add( new Text( bodyFont, "테스트", LengthPair.Millimeter(2, 30)) );
		textImage.add( new Text( bodyFont, "δοκιμή", LengthPair.Millimeter(2, 32)) );
		textImage.add( new Text( bodyFont, "מבחן", LengthPair.Millimeter(2, 34)) );
		textImage.add( new Text( bodyFont, "algodão", LengthPair.Millimeter(2, 36)) );

		//Rotation Test
		LengthPair block4Offset = LengthPair.Millimeter(18, 35);
		textImage.add( new Text( bodyFont, "Test0Degree", block4Offset) );	
		textImage.add( new Text( bodyFont, "Test90Degree", block4Offset).rotate(90) );
		textImage.add( new Text( bodyFont, "Test180Degree", block4Offset).rotate(180) );
		textImage.add( new Text( bodyFont, "Test270Degree", block4Offset).rotate(270) );
		textImage.add( new Text( bodyFont, "Test360Degree", block4Offset).rotate(360) );			
		
		//Font Test
		textImage.add( new Text( bodyFont, "Font 0", LengthPair.Millimeter(2, 47)) );		
		textImage.add( new Text( uniqloFont, "Font 1", LengthPair.Millimeter(8, 47)) );
		textImage.add( new Text( wslEngl, "Font 2", LengthPair.Millimeter(14, 47)) );
		textImage.add( new Text( bodyFont, "Font 3", LengthPair.Millimeter(20, 47)) );		
		textImage.add( new Text( localFont, "Loaded local font", LengthPair.Millimeter(2, 45)) );
		
		//Glyph Test
		textImage.add( new Glyph( "testSymb1.bmp", LengthPair.Millimeter(5, 50)).scale(0.5) );
		textImage.add( new Glyph( "testSymb2.bmp", LengthPair.Millimeter(15, 50)).rotate(90).scale(1.2) );
		textImage.add( new Glyph( "testSymb3.bmp", LengthPair.Millimeter(25, 50)).rotate(180).scale(0.7) );
		textImage.add( new Glyph( "testSymb4.bmp", LengthPair.Millimeter(35, 50)).rotate(270).scale(1.1) );
		//Glphy target scale test
		textImage.add( new Glyph( new File("GAP_ON Logo.bmp"), LengthPair.Millimeter(0, 0), LengthPair.Millimeter(25, 5)) );

		//Char Width ,line break and custom line break rule test
		relocate_lineBase(55);
		reset_lineheight( 2 );

		textImage.add( new Text( bodyFont, "100% COTTON", LengthPair.Millimeter(7, line(0))).setCharSpace(new Millimeter(3)) );
		textImage.add( new Text( bodyFont, "100% COTTON", LengthPair.Millimeter(7, line(1))).setReversePrint(true) );

		textImage.add( new Text( bodyFont, "100% COTTON\n100%PLASTIC", LengthPair.Millimeter(7, line(2))) );
		textImage.add( new Text( bodyFont, "100% COTTON", LengthPair.Millimeter(7, line(3))).setMultiLineRule(new CharCountBreak(3)).setMaxLineHeight(new Point(3.5)) );		
		textImage.add( new Text( bodyFont, "100% COTTON", LengthPair.Millimeter(7, line(6))).setMultiLineRule(new StringWidthBreak(new Millimeter(5))).setMaxLineHeight(new Point(5.5)) );
		
		textImage.add( new Text( bodyFont, "100% COTTONS\n200% COTTONS", LengthPair.Millimeter(7, line(8))).setMultiLineRule(new OnCharBreak('\n')).setMaxLineHeight(new Millimeter(1.3)) );		

		//Align test
		LengthPair referencePoint = LengthPair.Millimeter(14, line(10));
		textImage.add( new Text( bodyFont, "Left Up align", referencePoint).setHorizonialAlignment(TextAlignment.ALIGN_LEFT).setVerticalAlignment(TextAlignment.ALIGN_UP) );
		textImage.add( new Text( bodyFont, "Center Up align", referencePoint).setHorizonialAlignment(TextAlignment.ALIGN_CENTER).setVerticalAlignment(TextAlignment.ALIGN_UP) );
		textImage.add( new Text( bodyFont, "Right Up align", referencePoint).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT).setVerticalAlignment(TextAlignment.ALIGN_UP) );
		
		textImage.add( new Text( bodyFont, "Left Mid align", referencePoint).setHorizonialAlignment(TextAlignment.ALIGN_LEFT).setVerticalAlignment(TextAlignment.ALIGN_CENTER) );
		textImage.add( new Text( bodyFont, "Mid Mid align", referencePoint).setHorizonialAlignment(TextAlignment.ALIGN_CENTER).setVerticalAlignment(TextAlignment.ALIGN_CENTER) );		
		textImage.add( new Text( bodyFont, "Right Mid align", referencePoint).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT).setVerticalAlignment(TextAlignment.ALIGN_CENTER) );		
		
		textImage.add( new Text( bodyFont, "Left Down align", referencePoint).setHorizonialAlignment(TextAlignment.ALIGN_LEFT).setVerticalAlignment(TextAlignment.ALIGN_DOWN) );
		textImage.add( new Text( bodyFont, "Mid Down align", referencePoint).setHorizonialAlignment(TextAlignment.ALIGN_CENTER).setVerticalAlignment(TextAlignment.ALIGN_DOWN) );
		textImage.add( new Text( bodyFont, "Right Down align", referencePoint).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT).setVerticalAlignment(TextAlignment.ALIGN_DOWN) );

		//Align with differnt font size/style test	

		textImage.add( new Text( dotFont, "■", LengthPair.Millimeter(1, 5)) );
		textImage.add( new Text( countryFont, "JP1: ", LengthPair.Millimeter(3, 5)) );
		textImage.add( new Text( materialFont, "10% COTTON\n10% POLYESTER", LengthPair.Millimeter(30, 5)).setMultiLineRule( new OnCharBreak('\n') ) );
		
		textImage.add( new Text( dotFont, "■", LengthPair.Millimeter(1, 5)).setVerticalAlignment(TextAlignment.ALIGN_CENTER) );
		textImage.add( new Text( countryFont, "JP2: ", LengthPair.Millimeter(3, 5)).setVerticalAlignment(TextAlignment.ALIGN_CENTER) );
		textImage.add( new Text( materialFont, "20% COTTON\n20% POLYESTER", LengthPair.Millimeter(30, 5)).setMultiLineRule( new OnCharBreak('\n') ).setVerticalAlignment(TextAlignment.ALIGN_CENTER).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		
		textImage.add( new Text( dotFont, "■", LengthPair.Millimeter(5, 12.5)) );

		//Text Line Test, added text's position are in relative position to TextLine
		TextLine jpLine = new TextLine(LengthPair.Millimeter(0, 12.5));
		jpLine.add( new Text( dotFont, "■", LengthPair.Millimeter(1, 0)) );
		jpLine.add( new Text( countryFont, "JP: ", LengthPair.Millimeter(3, 0)) );
		jpLine.add( new Text( materialFont, "20% COTTON\n20% POLYESTER", LengthPair.Millimeter(30, 0)).setMultiLineRule( new OnCharBreak('\n') ).setMaxLineHeight( new Millimeter( 1.2 ) ) );
		jpLine.add( new Text( materialFont, "30% COTTON\n30% POLYESTER", LengthPair.Millimeter(30, 3)).setMultiLineRule( new OnCharBreak('\n') ).setMaxLineHeight( new Millimeter( 1.2 ) ).setHorizonialAlignment(TextAlignment.ALIGN_RIGHT) );
		jpLine.add( new Text( materialFont, "40% COTTON\n40% POLYESTER", LengthPair.Millimeter(30, 6)).setMultiLineRule( new OnCharBreak('\n') ).setHorizonialAlignment(TextAlignment.ALIGN_CENTER) );
		textImage.add( jpLine );

		FloatFont sizeFont = new FloatFont("Arial Unicode MS", Font.PLAIN, 9 );
		FloatFont madeInFont = new FloatFont("Arial Unicode MS", Font.PLAIN, 4.5 );			

		textImage.add( new Glyph( new File("GAP_ON Logo.bmp"), LengthPair.Millimeter(19, 57), LengthPair.Millimeter(15, 3)).setHorizonialAlignment(TextAlignment.ALIGN_CENTER) );
		textImage.add( new Text( sizeFont, "XS", LengthPair.Millimeter(19, 60)).setHorizonialAlignment(TextAlignment.ALIGN_CENTER) );
		textImage.add( new Text( sizeFont, "TP", LengthPair.Millimeter(19, 63.5)).setHorizonialAlignment(TextAlignment.ALIGN_CENTER) );
		textImage.add( new Text( sizeFont, "165/88A", LengthPair.Millimeter(19, 67)).setHorizonialAlignment(TextAlignment.ALIGN_CENTER) );
		textImage.add( new Text( madeInFont, "MADE IN CHINA", LengthPair.Millimeter(19, 70.5)).setHorizonialAlignment(TextAlignment.ALIGN_CENTER) );
		textImage.add( new Text( madeInFont, "RN 54023 CA17897", LengthPair.Millimeter(19, 72.5)).setHorizonialAlignment(TextAlignment.ALIGN_CENTER) );
		Image r = new LabelBase ( LengthPair.Millimeter(40, 94) );
	
		TextLine aLine = new TextLine( LengthPair.Millimeter(0, 0) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(0, 0)) );
			aLine.add( new Text( countryFont, "CA : ", LengthPair.Millimeter(1.5, 0)) );
			aLine.add( new Text( materialFont, "LAVER À LA MACHINE À L’EAU TIÈDE. LAVER À", LengthPair.Millimeter(5, 0)).scale(0.94,1.0) );
		*/		
		/*
		TextLine aLine = new TextLine( LengthPair.Millimeter(0, 0) );
			aLine.add( new Text( dotFont, "■", LengthPair.Millimeter(1.5, 0)) );
			Image country = new Text( countryFont, "ID :", LengthPair.Millimeter(3.2, 0));
			aLine.add( country );
			aLine.add( new Text( materialFont, "CUCI DAN KERINGKAN DENGAN LAPISAN DALAM", country.addRelativeWidth(300, new Millimeter(0.5)) ).scale(0.97, 1.0) );
		aLine.render(300);
		*/
		Image materialMLine1 = new Text( materialFont, 
				"DILUAR, SATUKAN DENGAN YANG SEWARNA / CUCI " + 
				"DENGAN MESIN DAN AIR HANGAT/ HANYA GUNAKAN " + 
				"PEMUTIH NON KLORIN JIKA DIPERLUKAN/ KERINGKAN " + 
				"DALAM MESIN PENGERING BERSUHU RENDAH/SETRIKA " + 
				"DENGAN SUHU SEDANG/ JANGAN SETRIKA PADA " + 
				"GAMBAR / JANGAN DICUCI KERING. DILUAR, SATUKAN D. DILUAR, SATUKAN D", 
				//aLine.addRelativeHeight( 300, new Millimeter(1.5), new Millimeter( -1.5 ) ))
				LengthPair.Millimeter(0,0) ) 
				.setMultiLineRule(new StringWidthWordBreak( new Millimeter( 40 ) )).setLineHeight( new Millimeter( 1.5 ) )
				.scale(0.97, 1.0);
		materialMLine1.render(300);		

		textImage = new LabelBase( materialMLine1.getCanvas(), Color.WHITE );
		//textImage.add( aLine );
		textImage.add( materialMLine1 );
		

		return textImage;
	}
}