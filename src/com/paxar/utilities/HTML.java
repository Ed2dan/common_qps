
package com.paxar.utilities;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * @author DLobacz
 * 
 * Utilities for HTML web pages.
 *
 */
public class HTML implements java.io.Serializable{
	
	/**
	 * NullToNbsp will return the passed in String if it's not null,
	 * &nbsp if it is null.  This is handy for populating a table
	 * with database colums.
	 * 
	 * @param s
	 * @return &nbsp s
	 * 
	 */
	
	public String NullToNbsp(String s){
		if ((s == null) || s.equals("") || s.equals(" ")){
			return "&nbsp";
		}else{
			return s;
		}
	}
	
	public String fixNull(String s){
		if (s == null){
			return "";
		}else{
			return s;
		}
	}
	
	/**
	 * nullStrToNull will convert a value of "null" to an empty string.
	 * It is useful in converting Postgres blank columns that return
	 * the string "null".
	 * 
	 * @param s
	 * @return
	 */
	public String nullStrToNull(String s){
		if (s.equals("null")){
			return "";
		}else{
			return s;
		}
	}
}
