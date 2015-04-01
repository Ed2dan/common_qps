package com.paxar.qps.data;

public interface OracleOrderProvider
{
	public int size( );
	public OracleOrderLineProvider getOracleLineItem( int index );
	public String getRBO( );
}
