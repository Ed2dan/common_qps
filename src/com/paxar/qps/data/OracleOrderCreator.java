package com.paxar.qps.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OracleOrderCreator
{
	public final static SimpleDateFormat oracleFormat = new SimpleDateFormat( "M/d/yyyy" );
	public final static SimpleDateFormat fileStampFormat = new SimpleDateFormat( "yyyyMMdd_HHmmssSSS" );
	
	protected File sourceDir = null;
	protected OracleInfo info = null;
	public OracleOrderCreator( String base, OracleInfo info, String retailer )
	{
		this.info = info;
		this.sourceDir = new File( base + File.separator + "oracle" + File.separator + retailer + 
								   File.separator + info.getValue( OracleInfo.service_bureau ));
		sourceDir.mkdirs( );
	}
	
	public File create( OracleOrderProvider order ) throws Exception
	{
		File oracleFile = getOutputFile( order );
		PrintWriter writer = null; 
		boolean hasQty = false;
		try
		{
			int line = 1;
			writer = new PrintWriter( new FileOutputStream( oracleFile ));
			for ( int i = 0; i < order.size( ); i++ )
			{
				OracleOrderLineProvider orderLine = order.getOracleLineItem( i );
				if ( Integer.parseInt( orderLine.getQuantity( )) > 0 )
				{
					hasQty = true;
					writer.println( getLine( orderLine, line ));
					line++;
				}
			}
		} finally
		{
			if ( writer != null )
			{
				writer.close( );
			}
		}
		if ( ! hasQty )
		{
			oracleFile.delete( );
			throw new RuntimeException( "Order did not contain any quantities." );
		}
		return oracleFile;
	}
	
	protected String getLine( OracleOrderLineProvider orderLine, int idx ) throws Exception
	{
		String line = "";
		line += "D2comm Import|";
		line += info.getValue( OracleInfo.order_type ) + "|";
		line += info.getValue( OracleInfo.price_list ) + "|";
		line += info.getValue( OracleInfo.d2comm_order_number ) + "|";
		line += idx + "|";		
		line += info.getValue( OracleInfo.sold_to_ref_number ) + "|";
		line += info.getValue( OracleInfo.bill_to_ref_number ) + "|";
		line += info.getValue( OracleInfo.ship_to_ref_number ) + "|";
		line += info.getValue( OracleInfo.contact_name ) + " TEL: " + info.getValue( OracleInfo.contact_phone ) + 
				" EMAIL: " + info.getValue( OracleInfo.contractor_email ) + "|";
		line += info.getValue( OracleInfo.contractor_po ) + "|";
		line += oracleFormat.format( new Date( )) + "|";
		try
		{
			line += oracleFormat.format( OracleInfo.dateFormat.parse( info.getValue( OracleInfo.request_date ))) + "|";
		} catch ( Exception e )
		{
			line += "|";
		}
		line += "|"; // info.getValue( OracleInfo.sales_rep_number ) + "|";
		line += info.getShipVia( ).getValue( ShipVia.ship_code ) + "|";		
		line += info.getFreightTerms( ).getValue( FreightTerms.freight_code ) + "|";		
		line += info.getValue( OracleInfo.special_instructions ).trim( ).replaceAll( "\\r\\n", " " );
		if ( info.getValue( OracleInfo.account_number ) != null && info.getValue( OracleInfo.account_number ).trim( ).length( ) > 0 )
		{
			line += " Account# " + info.getValue( OracleInfo.account_number );
		}
		line += "|";
		line += orderLine.getPartNo( ) + "|";
		line += "EA|";
		line += orderLine.getQuantity( ) + "|";		
		line += info.getOperatingUnit( ).getValue( OracleOperatingUnit.ship_from_org ) + "|";
		line += orderLine.getPurchaseOrder( ) + "|";
		line += orderLine.getCustomerItemDesc( ) + "|";
		line += info.getValue( OracleInfo.customer_service_rep ) + "|";
		return line;
	}
	
	public synchronized File getOutputFile( OracleOrderProvider order )
	{
		File outputFile = new File( sourceDir, "d2comm_" + order.getRBO( ) + "_" + fileStampFormat.format( new Date( )) + ".qps" );
		while ( outputFile.exists( ))
		{
			try
			{
				Thread.sleep( 100 );
			} catch ( Exception e ) {}
			outputFile = new File( sourceDir, "d2comm_" + order.getRBO( ) + "_" + fileStampFormat.format( new Date( )) + ".qps" );
		}
		return outputFile;
	}
}
