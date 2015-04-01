package com.paxar.utilities;

public class Esc
{
    /**
     * Usage:</br>
     *
     * Esc.escape( "Jim's Steak House \ Grill" );
     * returns the SQL compliant: "Jim''s Steak House \\ Grill"
     *
     * @param s String
     * @return String
     */
    public static final String esc( String s )
    {
        char find[] =      { '\'', '\\'   };
        String replace[] = { "''", "\\\\" };

        if (s==null)
            return "";

        String returnString;

        for ( int i = 0; i < find.length; i++ )
        {
            returnString = "";
            for ( int j = 0; j < s.length( ); j++ )
            {
                if ( s.charAt( j ) == find[i] )
                {
                    returnString += replace[i];
                } else
                {
                    returnString += s.charAt( j );
                }
            }
            s = returnString;
        }
        return s;
    }

    /**
     * Esc for <Input>
     * returns compliant to HTML quotes
     * @param s String
     * @return String
     */
    public static final String input(String s)
    {
        char find[] =
                      {'&',
                      '\\',  '\"', '\''};
        String replace[] =
                           {
                           "&amp;", "&#92;", "&quot;", "&#39;"};

        if ( s == null )
            return "";

        String returnString;

        for ( int i = 0; i < find.length; i++ )
        {
            returnString = "";
            for ( int j = 0; j < s.length(); j++ )
            {
                if ( s.charAt( j ) == find[ i ] )
                {
                    returnString += replace[ i ];
                }
                else
                {
                    returnString += s.charAt( j );
                }
            }
            s = returnString;
        }
        return s;
    }

}
