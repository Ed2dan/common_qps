package com.paxar.qps.ajax;
import java.util.*;
import javax.servlet.ServletRequest;
import java.lang.reflect.*;
/**
 * <p>Title: AjaxProcessor</p>
 *
 * <p>Description:
   This is a Ajax Java library to be used with AjaxAction Javascript to ease Ajax development.

   To use this class, see below example.

   AjaxProcessor ajax = new AjaxProcessor(request);
   ajax.run(this);    // Or any class that you want to invoke the functions stated from Javascript.
   out.println(ajax.getAjaxResponse());

   public void hello1(AjaxProcessor ajax)
   {
      String result = ajax.getParameter("field1"); // Get the value field1 provided from Javascript
      ajax.addResult("result", result);            // Send result with field name "result"
   }

 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author jerome
 * @version 1.0
 */
public class AjaxProcessor
{
    final static public String STATUS_OK = "OK";
    final static public String STATUS_ERROR = "ERROR";
    final static public String AjaxVersion = "1.0";

    String respondFunction = "";
    String actionFunction = "";
    String status = "";
    String message = "";
    HashMap resultValues = null;
    HashMap parameters = null;

    public AjaxProcessor( ServletRequest request )
    {
        respondFunction = request.getParameter( "AjaxResponse" );
        actionFunction = request.getParameter( "AjaxAction" );
        resultValues = new HashMap();
        parameters = new HashMap();
        parameters.putAll( request.getParameterMap() );
    }

    /**
     * Execute the server application process
     * @param runTimeObj Object
     */
    public void run( Object runTimeObj )
    {
        status = STATUS_OK;
        java.lang.reflect.Method actionMethod = null;
        try
        {
            actionMethod = runTimeObj.getClass().getDeclaredMethod( actionFunction, new Class[]
                {this.getClass()} );
        }
        catch ( Exception e )
        {
            status = STATUS_ERROR;
            message = "Unable to load action function: " + actionFunction;
            return;
        }

        try
        {
            actionMethod.invoke( runTimeObj, new Object[]
                                 {this} );
        }
        catch ( InvocationTargetException e )
        {
            status = STATUS_ERROR;
            message = "Error running " + actionFunction + ": " + e.getTargetException().toString() + " " +
                      getExceptionTrace( e.getTargetException() );
        }
        catch ( IllegalAccessException e )
        {
            status = STATUS_ERROR;
            message = "Error running " + actionFunction + ": Illegal Access Exception";
        }
    }

    /**
     * Get parameters obtained from clients
     * @param key String
     * @return String[]
     */
    public String[] getParameters( String key )
    {
        return ( String[] )parameters.get( key );
    }

    public String getParameter( String key )
    {
        String result[] = getParameters( key );
        if ( result != null )
            return result[ 0 ];
        return null;
    }

    /**
     * Add result to client
     * @param key String
     * @param value String
     */
    public void addResult( String key, String value )
    {
        resultValues.put( key, value );
    }

    /**
     * Get the Ajax response XML page
     * @return String
     */
    public String getAjaxResponse()
    {
        String result = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
        result += "<Ajax>\n";
        result += "  <AjaxSystem>\n";
        result += "    <AjaxVersion>" + AjaxVersion + "</AjaxVersion>\n";
        result += "    <AjaxResponse>" + respondFunction + "</AjaxResponse>\n";
        result += "    <AjaxAction>" + actionFunction + "</AjaxAction>\n";
        result += "    <AjaxStatus>" + status + "</AjaxStatus>\n";
        result += "    <AjaxMessage>" + message + "</AjaxMessage>\n";
        result += "  </AjaxSystem>\n";
        result += "  <AjaxResult>\n";
        Iterator i = resultValues.keySet().iterator();
        while ( i.hasNext() )
        {
            String key = ( String )i.next();
            result += "    <" + key + ">";
            result += escape( ( String )resultValues.get( key ) );
            result += "</" + key + ">\n";
        }
        result += "  </AjaxResult>\n";
        result += "</Ajax>\n";
        return result;
    }

    /**
     * Escape <> characters
     * @param src String
     * @return String
     */
    public String escape( String src )
    {
        if ( src == null )
            return "";
        return src.replaceAll( "&", "&#38;" ).replaceAll( "<", "&#60;" ).replaceAll( ">", "&#62;" );
    }

    /**
     *
     * @param e Throwable
     * @return String
     */
    public String getExceptionTrace( Throwable e )
    {
        String str = "";
        StackTraceElement ste[] = e.getStackTrace();
        for ( int i = 0; i < ste.length; i++ )
        {
            str = str + "  " + ste[ i ].toString() + "\n";
            // Ignore JSP stack trace
            if ( ste[ i ].toString().substring( 0, 14 ).equals( "org.apache.jsp" ) )
                i = 999;
        }
        return str;
    }

    /**
     * Force error message
     * @param message String
     */
    public void setError( String message )
    {
        this.status = AjaxProcessor.STATUS_ERROR;
        this.message = message;
    }
}


