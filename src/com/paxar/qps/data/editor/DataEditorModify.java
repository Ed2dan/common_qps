package com.paxar.qps.data.editor;
import java.io.Serializable;
import java.util.HashMap;
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
public class DataEditorModify extends HashMap implements Serializable
{
    static final long serialVersionUID = 432691481458028994L;
    final static public int MODE_EDIT = 0;
    final static public int MODE_ADD = 1;
    final static public String TYPE_READONLY = "readonly";
    final static public String TYPE_NUMERIC = "numeric";

    protected DataEditorRecord record = null;
    int mode = MODE_EDIT;

    public DataEditorModify( DataEditorRecord record )
    {
        this.record = record;
    }

    public void setMode( int mode )
    {
        this.mode = mode;
    }

    public void setReadonly( String field )
    {
        this.put( field, TYPE_READONLY );
    }

    public void setNumeric( String field )
    {
        this.put( field, TYPE_NUMERIC );
    }

    public void setReadonly( String fields[] )
    {
        if (fields==null)
            return;
        for ( int i = 0; i < fields.length; i++ )
            setReadonly( fields[ i ] );
    }

    public void setNumeric( String fields[] )
    {
        if (fields==null)
            return;
        for ( int i = 0; i < fields.length; i++ )
            setNumeric( fields[ i ] );
    }

    public boolean isReadonly( String field )
    {
        String ftype = ( String )this.get( field );
        if ( ftype == null )
            return false;
        if ( ftype.equals( TYPE_READONLY ) )
            return true;
        return false;
    }

    public boolean isNumeric( String field )
    {
        String ftype = ( String )this.get( field );
        if ( ftype == null )
            return false;
        if ( ftype.equals( TYPE_NUMERIC ) )
            return true;
        return false;
    }

    public String getFieldDisplay( String field, int size )
    {
        if ( isReadonly( field ) )
            return getFieldValue( field );
        return getFieldInput( field, size );
    }

    public String getFieldValue( String field )
    {
        if (record.getString( field )==null)
            return "<input name=\"" + field + "\" type=\"hidden\" value=\"\" />";
        String html = "";
        html += record.getString( field ).replaceAll( "<", "&lt;" ).replaceAll( ">", "&gt;" );
        html += "<input name=\"" + field + "\" type=\"hidden\" value=\"" + record.getString( field ).replaceAll( "\"", "\\\"" ) + "\" />";
        return html;
    }

    public String getFieldInput( String field , int size)
    {
        String field_value = "";
        if (record.getString( field )!=null)
            field_value = record.getString( field ).replaceAll( "\"", "\\\"" );
        int display_size = size;
        if (display_size >= 30)
            display_size = 30;
        String html = "";
        html += "<input name=\"" + field + "\" value=\"" + field_value + "\" ";
        html += " size=\"" + display_size + "\" maxlength=\"" + size + "\" ";
        if (isNumeric(field))
            html += " onkeyup=\"this.value = this.value.replace(/[^0-9]/g,'')\" ";
        html += "/>";
        return html;
    }

}
