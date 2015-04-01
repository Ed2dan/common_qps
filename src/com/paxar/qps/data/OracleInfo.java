package com.paxar.qps.data;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class OracleInfo extends DataProvider
{
	private static final long serialVersionUID = -4277164964550845301L;
	public final static SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

	public OracleInfo( ResultSet rs ) throws Exception
	{
		super( rs );
	}

	protected OracleOperatingUnit unit = new OracleOperatingUnit( );
	public OracleOperatingUnit getOperatingUnit( )
	{
		return unit;
	}
	public void setOperatingUnit( OracleOperatingUnit unit )
	{
		this.unit = unit;
	}

	protected ShipVia shipVia = new ShipVia( );
	public ShipVia getShipVia( )
	{
		return shipVia;
	}
	public void setShipVia( ShipVia via )
	{
		this.shipVia = via;
	}

	protected FreightTerms freightTerms = new FreightTerms( );
	public FreightTerms getFreightTerms( )
	{
		return freightTerms;
	}
	public void setFreightTerms( FreightTerms terms )
	{
		this.freightTerms = terms;
	}

	public String[] getAddress( int from, int to )
	{
		Vector address = new Vector( );
		for ( int i = from; i <= to; i++ )
		{
			address.add( getValue( i ));
		}
		for ( int i = address.size( ) - 1; i >= 0; i-- )
		{
			if ( address.get( i ).toString( ).trim( ).length( ) == 0 )
			{
				address.remove( i );
			}
		}
		String array[] = new String[address.size( )];
		address.copyInto( array );
		return array;
	}

    public final static int id = 0;
    public final static int user_name = 1;
    public final static int retailer = 2;
    public final static int sales_rep_number = 3;
    public final static int bill_to_ref_number = 4;
    public final static int bill_to_cust_number = 5;
    public final static int bill_to_cust_name = 6;
    public final static int bill_to_address1 = 7;
    public final static int bill_to_address2 = 8;
    public final static int bill_to_address3 = 9;
    public final static int bill_to_city = 10;
    public final static int bill_to_state = 11;
    public final static int bill_to_zip = 12;
    public final static int bill_to_country = 13;
    public final static int bill_to_country_code = 14;
    public final static int sold_to_ref_number = 15;
    public final static int sold_to_cust_number = 16;
    public final static int sold_to_cust_name = 17;
    public final static int sold_to_address1 = 18;
    public final static int sold_to_address2 = 19;
    public final static int sold_to_address3 = 20;
    public final static int sold_to_city = 21;
    public final static int sold_to_state = 22;
    public final static int sold_to_zip = 23;
    public final static int sold_to_country = 24;
    public final static int sold_to_country_code = 25;
    public final static int ship_to_ref_number = 26;
    public final static int ship_to_cust_number = 27;
    public final static int ship_to_name = 28;
    public final static int ship_to_address1 = 29;
    public final static int ship_to_address2 = 30;
    public final static int ship_to_address3 = 31;
    public final static int ship_to_city = 32;
    public final static int ship_to_state = 33;
    public final static int ship_to_zip = 34;
    public final static int ship_to_country = 35;
    public final static int ship_to_country_code = 36;
    public final static int order_type = 37;
    public final static int price_list = 38;
    public final static int customer_service_rep = 39;
    public final static int system = 40;
    public final static int contact_name = 41;
    public final static int special_instructions = 42;
    public final static int request_date = 43;
    public final static int account_number = 44;
    public final static int contact_phone = 45;
    public final static int contractor_po = 46;
    public final static int contractor_email = 47;
    public final static int service_bureau = 48;
    public final static int d2comm_order_number = 49;
    public final static int shipping_instructions = 50;
    public final static int payment_terms = 51;
    public final static int ship_mark = 52;
    public final static int cs_lot = 53;
    public final static int customer_req_date = 54;
    public final static int order_date = 55;
    public final static int order_time = 56;
    public final static int printer_note = 57;
    public final static int record_size = 58;
}
