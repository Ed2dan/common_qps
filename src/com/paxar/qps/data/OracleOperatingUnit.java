package com.paxar.qps.data;

import java.sql.ResultSet;

public class OracleOperatingUnit extends DataProvider
{
	private static final long serialVersionUID = -4058905980134383366L;
	
	public OracleOperatingUnit( ResultSet rs ) throws Exception
	{
		super( rs );
	}
	
	public OracleOperatingUnit( )
	{
		super( record_size );
	}
	
	public final static int service_bureau = 0;
	public final static int operating_unit_id = 1;
	public final static int ship_from_org = 2;
	public final static int record_size = 3;
}
