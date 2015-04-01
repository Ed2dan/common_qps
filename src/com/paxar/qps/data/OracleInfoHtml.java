package com.paxar.qps.data;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Date;

public class OracleInfoHtml
{	
	public static String getHtml( OracleInfo info )
	{
		StringWriter str = new StringWriter( );
		PrintWriter out = new PrintWriter( str );
	      out.write("    <table width='90%' border='1' align='center'>\n");
	      out.write("\t<tr><td class='data'>\r\n");
	      out.write("\t\t<table width='100%' border='0' align='center' cellpadding='0' cellspacing='0'>\r\n");
	      out.write("\t\t<tr><td class='data' width='33%' valign='top'>\r\n");
	      out.write("\t\t\t<table width='100%' border='0' align='center'>\r\n");
	      out.write("\t\t\t<tr><td class='data'><font size='3' color=\"#808080\"><b>BILL TO</b></font>\r\n");
	      out.write("\t\t\t<tr><td class='data'>Customer No: ");
	      out.print( info.getValue( OracleInfo.bill_to_cust_number ) );
	      out.write("\r\n");	
		String bill[] = info.getAddress( OracleInfo.bill_to_cust_name, OracleInfo.bill_to_address3 );
		for ( int i = 0; i < bill.length; i++ )
		{

	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( bill[i] );
	      out.write('\r');
	      out.write('\n');
		
		}

	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( info.getValue( OracleInfo.bill_to_city ) + ", " + info.getValue( OracleInfo.bill_to_state ) + " " + info.getValue( OracleInfo.bill_to_zip ) );
	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( info.getValue( OracleInfo.bill_to_country ) );
	      out.write("\r\n");      
	      out.write("\t\t\t</table>\r\n");
	      out.write("\t\t\t<td class='data' width='33%' valign='top'>\r\n");
	      out.write("\t\t\t<table width='100%' border='0' align='center'>\r\n");
	      out.write("\t\t\t<tr><td class='data'><font size='3' color=\"#808080\"><b>SOLD TO</b></font>\r\n");
	      out.write("\t\t\t<tr><td class='data'>Customer No: ");
	      out.print( info.getValue( OracleInfo.sold_to_cust_number ) );
	      out.write("\r\n");
		String sold[] = info.getAddress( OracleInfo.sold_to_cust_name, OracleInfo.sold_to_address3 );
		for ( int i = 0; i < sold.length; i++ )
		{

	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( sold[i] );
	      out.write('\r');
	      out.write('\n');
		
		}

	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( info.getValue( OracleInfo.sold_to_city ) + ", " + info.getValue( OracleInfo.sold_to_state ) + " " + info.getValue( OracleInfo.sold_to_zip ) );
	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( info.getValue( OracleInfo.sold_to_country ) );
	      out.write("\r\n");	      	      
	      out.write("\t\t\t</table>\r\n");
	      out.write("\t\t\t<td class='data' width='33%' valign='top'>\r\n");
	      out.write("\t\t\t<table width='100%' border='0' align='center'>\r\n");
	      out.write("\t\t\t<tr><td class='data'><font size='3' color=\"#808080\"><b>SHIP TO</b></font>\r\n");
	      out.write("\t\t\t<tr><td class='data'>Customer No: ");
	      out.print( info.getValue( OracleInfo.ship_to_cust_number ));
	      out.write("\r\n");	
		String ship[] = info.getAddress( OracleInfo.ship_to_name, OracleInfo.ship_to_address3 );
		for ( int i = 0; i < ship.length; i++ )
		{

	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( ship[i] );
	      out.write('\r');
	      out.write('\n');
		
		}

	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( info.getValue( OracleInfo.ship_to_city ) + ", " + info.getValue( OracleInfo.ship_to_state ) + " " + info.getValue( OracleInfo.ship_to_zip ) );
	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( info.getValue( OracleInfo.ship_to_country ) );
	      out.write("\r\n");      	      
	      out.write("\t\t\t</table>\r\n");
	      out.write("\t\t</table>\r\n");
	      out.write("\t<tr><td class='data'>\r\n");
	      out.write("\t\t<table width='100%' border='0' align='center' cellpadding='0' cellspacing='0'>\r\n");
	      out.write("\t\t<tr><td class='data' width='33%' valign='top'>\r\n");
	      out.write("\t\t\t<table width='100%' border='0' align='center'>\r\n");
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>EMAIL:</b></font> <a class='plain' href=\"mailto:");
	      out.print( info.getValue( OracleInfo.contractor_email ) );
	      out.write('"');
	      out.write('>');
	      out.print( info.getValue( OracleInfo.contractor_email ) );
	      out.write("</a>\r\n");
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>CONTRACTOR PO:</b></font> ");
	      out.print( info.getValue( OracleInfo.contractor_po ) );
	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>SERVICE BUREAU:</b></font> ");
	      out.print( info.getValue( OracleInfo.service_bureau ) );
	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>ORDER DATE:</b></font> ");
	      out.print( OracleInfo.dateFormat.format( new Date( )) );
	      out.write("\r\n");
	      out.write("\t\t\t</table>\r\n");
	      out.write("\t\t\t<td class='data' width='33%' valign='top'>\r\n");
	      out.write("\t\t\t<table width='100%' border='0' align='center'>\r\n");
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>SHIP TO ATTN:</b></font> ");
	      out.print( info.getValue( OracleInfo.contact_name ));
	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>SHIP VIA:</b></font> ");
	      out.print( info.getShipVia( ).getValue( ShipVia.ship_via ) );
	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>FREIGHT TERMS:</b></font> ");
	      out.print( info.getFreightTerms( ).getValue( FreightTerms.freight_terms ) );
	      out.write("\r\n");
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>ACCOUNT NUMBER:</b></font> ");
	      out.print( info.getValue( OracleInfo.account_number ) );
	      out.write("\r\n");	      	      
	      out.write("\t\t\t</table>\r\n");
	      out.write("\t\t\t<td class='data' width='33%' valign='top'>\r\n");
	      out.write("\t\t\t<table width='100%' border='0' align='center'>\r\n");
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>REQUESTED DATE:</b></font> ");
	      out.print( info.getValue( OracleInfo.request_date ));
	      out.write("\r\n");	      
	      out.write("\t\t\t<tr><td class='data'><font color=\"#808080\"><b>SPECIAL INSTRUCTIONS:</b></font>\r\n");
	      out.write("\t\t\t<tr><td class='data'>");
	      out.print( info.getValue( OracleInfo.special_instructions ) );
	      out.write("\r\n");
	      out.write("\t\t\t</table>\t\r\n");
	      out.write("\t\t</table>\t\r\n");
	      out.write("\t</table>\n");		
		
		return str.toString( );
	}
}
