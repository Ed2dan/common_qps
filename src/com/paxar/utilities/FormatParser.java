package com.paxar.utilities;

import java.io.File;
import org.w3c.dom.Document;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class FormatParser
{
    public FormatParser()
    {
    }

    public long version = 0;
    private boolean xmlInit = false;
    String retailer = "";

    public void parseXml( String strPath )
    {
        version = 0;

        if ( ! xmlInit )
        {
            xmlInit = true;
            DOMParser parser = new DOMParser( );
            try
            {
                parser.parse( strPath );
                Document doc = parser.getDocument( );

                NodeList nodes = doc.getElementsByTagName( "PaxarFormat" );
                if ( nodes.getLength( ) > 0 )
                {
                    Node vNode = nodes.item( 0 ).getAttributes( ).getNamedItem( "Format_Version" );
                    if ( vNode != null )
                    {
                        version = Long.parseLong( vNode.getNodeValue( ));
                    }
                    Node rNode = nodes.item( 0 ).getAttributes( ).getNamedItem( "RBO" );
                    if ( rNode != null )
                    {
                        retailer = rNode.getNodeValue( );
                    }
                }
            } catch ( Exception e )
            {
                e.printStackTrace( );
            }
        }
    }

}
