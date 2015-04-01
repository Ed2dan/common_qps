
package com.paxar.utilities;

import java.util.*;
import java.text.*;
import java.text.SimpleDateFormat;


/**
 * @author DLobacz
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PaxarDate implements java.io.Serializable{

	private DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

	/**
	 *
	 */
	public PaxarDate() {

	}

	/**
	 *
	 * getDate() will get the date in the current Format.
	 *
	 */
	public String getDate(){

	    Date now = new Date();
		return formatter.format(now);
	}

	/**
	 *
	 * getDate() will get the date in the current Format and add addDays to it.
	 *
	 */
	public String getDate(int addDays){

	    Date now = new Date();
        // Change for deprecation
//	    int day = now.getDate();
//	    now.setDate(day + addDays);
                    now.setTime(now.getTime() + (long) addDays * (long) 24 * (long) 60 * (long) 60 * (long) 1000);
		return formatter.format(now);
	}

	/**
	 * getDate(Date date) will output the date parameter and apply the Format, Timezone, and Locale.
	 *
	 * @param formatStr
	 */
	public String getDate(Date date){

		if (date != null){
			return this.formatter.format(date);
		}else{
			return "&nbsp";
		}

	}
	
	/**
	 * getDate(Calendar date) will output the date parameter and apply the Format, Timezone, and Locale.
	 *
	 * @param formatStr
	 */
	public String getDate(Calendar date){

		if (date != null){
			return this.formatter.format(date.getTime());
		}else{
			return "&nbsp";
		}

	}
	/**
	 * getDateOrEmptyString() will output the date parameter and apply the
	 * Format, Timezone, and Locale.  If the date is null, an empty string
	 * is returned.
	 *
	 * @param formatStr
	 */
	public String getDateOrEmptyString(Date date){

		if (date != null){
			return this.formatter.format(date);
		}else{
			return "";
		}

	}
	/**
	 * setFormat will change the date format to the input string.
	 *
	 * @param formatStr
	 */
	public void setFormat(String formatStr){
		this.formatter = new SimpleDateFormat(formatStr);
	}

}
