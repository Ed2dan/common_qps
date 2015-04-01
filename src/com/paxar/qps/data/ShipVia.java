package com.paxar.qps.data;

import java.sql.ResultSet;

public class ShipVia extends DataProvider
{
	private static final long serialVersionUID = -6667344241826711182L;
	
	public ShipVia( ResultSet rs ) throws Exception
	{
		super( rs );
	}
	
	public ShipVia( )
	{
		super( record_size );
		for ( int i = 0; i < record_size; i++ )
		{
			setValue( i, "" );
		}
	}
	
	public final static int retailer = 0;
	public final static int ship_code = 1;
	public final static int ship_via = 2;
	public final static int record_size = 3;
}
