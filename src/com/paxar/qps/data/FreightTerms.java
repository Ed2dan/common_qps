package com.paxar.qps.data;

import java.sql.ResultSet;

public class FreightTerms extends DataProvider
{
	private static final long serialVersionUID = 2333900505959692744L;
	
	public FreightTerms( ResultSet rs ) throws Exception
	{
		super( rs );
	}
	
	public FreightTerms( )
	{
		super( record_size );
		for ( int i = 0; i < record_size; i++ )
		{
			setValue( i, "???" );
		}
	}
	
	public final static int retailer = 0;
	public final static int freight_code = 1;
	public final static int freight_terms = 2;
	public final static int record_size = 3;	
}
