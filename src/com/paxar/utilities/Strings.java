package com.paxar.utilities;

public class Strings implements java.io.Serializable{
	
	/*
	 * countOccurences will return the number of times the first string contains the 
	 * second string.
	 */
    public static int countOccurences(String string, String tosearchfor){
        int count = 0;
        for(int index = 0; (index = string.indexOf(tosearchfor, index)) != -1; count++, index += tosearchfor.length());
        return count;
    }
    
}
