/*
 * Created on Apr 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.paxar.utilities;

import java.text.*;
import java.util.*;

import org.apache.commons.lang.StringUtils;

/**
 * @author DLobacz
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConvertCurrency implements java.io.Serializable{
	private static final long serialVersionUID = 8852591475246368110L;
	
	public String ConvertToCurrency(String price, String currencyType) {
		return Currency.getInstance(currencyType).getSymbol() + formatPrice(price);
	}
	
	public String formatPrice(String price) {
		if (price.length( ) < 3) {
			return "0.00";
		} 
		
		if (StringUtils.contains(price, ".")) {
			return price;
		}
		
		String cents = price.substring(price.length() - 2, price.length());
		return price.substring(0, price.length()- 2) + '.' + cents;
	}
	
	public String ConvertToCurrency(float s, String currencyType) {
		Locale locale = Locale.CANADA;
		return NumberFormat.getCurrencyInstance(locale).format(s);
	}
}