package com.paxar.qps.data;

/**
 * <p>Title: Unicode String</p>
 *
 * <p>Description: A class for common unicode operations</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class UnicodeString
{
    public UnicodeString()
    {
    }

    /**
     * This function is to decode &#9999 into real unicode presentation
     * @param src String
     * @return String
     */
    public static String getUnicodeFromHTML( String src )
    {
        if ( src == null )
            return null;
        String str = src;
        while ( str.indexOf( "&#" ) > -1 )
        {
            int start = str.indexOf( "&#" );
            int end = str.indexOf( ";", start );
            if ( end == -1 )
                return str;

            String unicodechar = "";
            try
            {
                char c = ( char )Integer.parseInt( str.substring( start + 2, end ) );
                unicodechar = new Character( c ).toString();
            }
            catch ( Exception e )
            {
                return str;
            }
            String newstr = str.substring( 0, start ) + unicodechar + str.substring( end + 1 );
            str = newstr;
        }
        return str;
    }

    /**
     * This function is to encode unicode characters into HTML presentation
     * @param src String
     * @return String
     */
    public static String getHTMLFromUnicode( String src )
    {
        String result = "";
        for (int i=0; i< src.length(); i++)
        {
            int nv = (int) new Character(src.charAt(i)).charValue();
            if (nv > 128)
                result += "&#" + nv + ";";
            else
                result += src.charAt(i);
        }
        return result;
    }

    public static void main( String args[] )
    {
        System.out.println( getUnicodeFromHTML( "Testing &#49;&#50;&#51;456" ) );
    }
}



