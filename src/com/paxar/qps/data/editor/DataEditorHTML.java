package com.paxar.qps.data.editor;
import java.util.*;
import java.io.Serializable;
/**
 * <p>Title: DataEditorHTML</p>
 *
 * <p>Description: DataEditor HTML output module</p>
 *
 * @author jerome
 * @version 1.0
 */
public class DataEditorHTML implements Serializable
{
    static final long serialVersionUID = 6951665452542484817L;
    final public static int RECORDS_PER_PAGE = 50;

    String fields[] = null;
    String headers[] = null;
    String editable[] = null;
    String controlURL = null;
    int records_per_page = RECORDS_PER_PAGE;

    public DataEditorHTML()
    {
    }

    /**
     * Set database fields
     * @param fields String[]
     */
    public void setFields( String fields[] )
    {
        this.fields = fields;
    }

    public String getField( int i )
    {
        return fields[ i ];
    }

    public void setRecordsPerPage( int per )
    {
        records_per_page = per;
    }

    /**
     * Set editable fields
     * @param editable String[]
     */
    public void setEditable( String editable[] )
    {
        this.editable = editable;
    }

    /**
     * Set display table description
     * @param headers String[]
     */
    public void setHeaders( String headers[] )
    {
        this.headers = headers;
    }

    /**
     * Get the header display for supplied column #
     * @param column int
     * @return String
     */
    public String getHeader( int column, int currentSorted, int currentOrder )
    {
        String html = "";
        html += "<a class=\"delink\" ";
        if ( column != currentSorted )
            html += " href=\"#\" onclick=\"sort_page(" + column + "," + DataEditorSortKey.ASC + ");\"> " + headers[ column ] +
                " </a>";
        else if ( column == currentSorted && currentOrder==DataEditorSortKey.ASC)
            html += " href=\"#\" onclick=\"sort_page(" + column + "," + DataEditorSortKey.DESC+ ");\"> <span class=\"desorth\">" +
                headers[ column ] + "</span> </a>";
        else
            html += " href=\"#\" onclick=\"sort_page(" + column + "," + DataEditorSortKey.ASC +");\"> <span class=\"desorth\">" +
                headers[ column ] + "</span> </a>";
        return html;
    }

    /**
     * Get display based on editable list
     * @param column int
     * @param record DataEditorRecord
     * @return String
     */
    public String getFieldDisplay( int column, DataEditorRecord record )
    {
        if ( editable == null )
            return getDisplay( column, record );
        if ( Arrays.binarySearch( editable, getField( column ) ) >= 0 )
            return getEdit( column, record );
        return getDisplay( column, record );
    }

    /**
     * get normal display of data
     * @param column int
     * @param record DataEditorRecord
     * @return String
     */
    public String getDisplay( int column, DataEditorRecord record )
    {
        if ( record == null )
            return null;
        if ( record.get( fields[ column ] ) == null )
            return null;

        String html = "";
        html += record.getString( fields[ column ] ).replaceAll( "<", "&lt;" ).
            replaceAll( ">", "&gt;" );
        return html;
    }

    /**
     * get edit prompt of data
     * @param column int
     * @param record DataEditorRecord
     * @return String
     */
    public String getEdit( int column, DataEditorRecord record )
    {
        if ( record == null )
            return null;
        if ( record.get( fields[ column ] ) == null )
            return null;

        String html = "";
        html += "<input type=\"text\" name=\"" + fields[ column ] + "\" size=\"30\" ";
        html += " value=\"" +
            record.getString( fields[ column ] ).replaceAll( "\"", "\\\"" ) +
            "\" ";
        if ( record.isInt( fields[ column ] ) )
            html += " onkeyup=\"this.value = this.value.replace(/[^0-9]/g,'')\" ";
        html += "/>";
        return html;
    }

    /**
     * returns the size of fields
     * @return int
     */
    public int size()
    {
        if ( fields == null )
            return 0;
        return fields.length;
    }

    public static String getStyleSheet()
    {
        String css = "<style>";

        // List Table
        css += "table.de { border: 2px solid #BBB; width: 90%; text-align: center; } \n";
        css += "table.de th { border: 1px solid #DDD; background: #EEE; font-weight: normal; padding: 5px; } \n";
        css += "span.desorth { color: #F30 ; text-decoration: none;  } \n";
        css += "a.delink:link, a.delink:visited { color: blue; text-decoration: underline; font-weight: normal; } \n";
        css += "a.delink:active, a.delink:hover { color: red; text-decoration: none; font-weight: normal; } \n";
        css += "td.detb { background: transparent; padding: 5px; vertical-align: top; } \n";
        css += "td.deta { background: #EEE; padding: 5px; vertical-align: top; } \n";

        // Page Control
        css += "table.depg { border: 5px solid white; text-align: center; background: #EEE; padding: 5px; } ";
        css += "a.depg:link, a.depg:visited  { color: blue; text-decoration: underline; font-weight: normal; } \n";
        css += "a.depg:active, a.depg:hover { color: red; text-decoration: none; font-weight: normal; } \n";

        css += "table.deedit { border: 2px solid #BBB; background: #EEE; } \n";
        css += "table.deedit th { border-bottom: 1px solid #BBB; border-right: 1px solid #BBB; text-align: right; font-weight: normal; padding: 5px; } ";
        css += "table.deedit td { border-bottom: 1px solid white; padding: 5px; } ";
        css += "</style>\n";
        return css;
    }

    public void setShowControl( String url )
    {
        controlURL = url;
    }

    public String getDisplayTable( DataEditorRecord src_records[], DataEditorNavigation denav )
    {
        return getDisplayTable(src_records, denav.getPage(), denav.getSortBy(), denav.getSortOrder());
    }

    public String getDisplayTable( DataEditorRecord src_records[], int page, int sort_by, int sort_order )
    {

        DataEditorRecord records[] = getThisPageRecords(src_records, page);

        String html = "<table class=\"de\" align=\"center\"><tr>";
        if ( controlURL != null )
            html += "<th>Edit</th>";
        for ( int i = 0; i < headers.length; i++ )
        {
            html += "<th>" + getHeader( i, sort_by, sort_order ) + "</th>";
        }
        html += "</tr>\n";
        for ( int i = 0; i < records.length; i++ )
        {
            DataEditorRecord record = records[ i ];
            html += "<tr>";
            if ( controlURL != null )
            {
                html += "<td><a href=\"" + controlURL + "?index=" + getThisPageIndex(i, page) + "\">";
                html += "<img src=\"/qps/images/edit.gif\" border=\"0\"/></a></td>\n";
            }
            for ( int j = 0; j < size(); j++ )
            {
                if ( i % 2 == 1 )
                    html += "<td class=\"deta\">";
                else
                    html += "<td class=\"detb\">";
                html += "" + getFieldDisplay( j, record ) + "</td>";
            }
            html += "</tr>\n";
        }
        if (controlURL != null)
        {
            html += "<tr><td colspan=\"2\" style=\"text-align: left\"> &nbsp; &nbsp; <a class=\"delink\" href=\"" + controlURL + "?index=-1\">";
            html += "<img src=\"/qps/images/plus.gif\" border=\"-\" /> Add New</a> &nbsp; </td></tr>\n";
        }
        html += "</table>";
        return html;
    }

    public String getPageControl(DataEditorNavigation denav)
    {
        return getPageControl(denav.getPage(), denav.getTotalRecords());
    }

    public String getPageControl(int page, int total)
    {
        int total_pages = ((total - 1) / records_per_page) + 1;

        String html = "";

        // Page Navigation bar
        html += "<table width=\"360\" align=\"center\" class=\"depg\"><tr align=\"center\">";
        if (page==1)
            html += "<td width=\"100\">&nbsp;</td>";
        else
            html += "<td width=\"100\"> <a class=\"depg\" href=\"#\" onclick=\"go_page(" + (page-1) + ")\">&lt; Prev</a> </td>";
        html += "<td width=\"160\">Page: <select onchange=\"go_page(this.value)\">";
        for (int i=1; i<= total_pages; i++)
            html += "<option value=\"" + i + "\" " + (i==page ? "selected=\"selected\"":"") + " >" + i + "</option>\n";
        html += "</select></td>";
        if (page==total_pages)
            html += "<td width=\"100\">&nbsp;</td>";
        else
            html += "<td width=\"100\"> <a class=\"depg\" href=\"#\" onclick=\"go_page(" + (page+1) + ")\"> Next &gt; </a></td>";
        html += "</tr></table>\n";
        return html;
    }

    public String getControl(DataEditorNavigation denav)
    {
        String html = "";
        // Page Form
        html += "<form name=\"depage_nav\" action=\"\" method=\"post\">\n";
        html += "<input type=\"hidden\" id=\"depage\" name=\"depage\" value=\"" + denav.getPage() + "\" />\n";
        html += "<input type=\"hidden\" id=\"desort\" name=\"desort\" value=\"" + denav.getSortBy() + "\" />\n";
        html += "<input type=\"hidden\" id=\"deorder\" name=\"deorder\" value=\"" + denav.getSortOrder() + "\" />\n";
        html += "</form>\n";

        // Page Scripts
        html += "<script type=\"text/javascript\">\n";
        html += "function go_page(page) {\n";
        html += "document.depage_nav.depage.value=page;\n";
        html += "document.depage_nav.action=location.href.split('?')[0];\n";
        html += "document.depage_nav.submit();\n";
        html += "}\n";
        html += "\n";

        html += "function sort_page(index, order) {\n";
        html += "document.depage_nav.desort.value=index;\n";
        html += "document.depage_nav.deorder.value=order;\n";
        html += "document.depage_nav.action=location.href.split('?')[0];\n";
        html += "document.depage_nav.submit();\n";
        html += "}\n";

        html += "document.depage_nav.reset();\n";
        html += "</script>\n";

        return html;
    }

    public DataEditorRecord[] getThisPageRecords( DataEditorRecord src[], int page )
    {
        int total = src.length;
        int from = ( page - 1 ) * records_per_page;
        int to = from + records_per_page;
        if ( to > total )
            to = total;
        int cursor = 0;
        DataEditorRecord dest[] = new DataEditorRecord[to - from];
        for ( int i = from; i < to; i++ )
        {
            dest[ cursor ] = src[ i ];
            cursor++;
        }

        return dest;
    }

    public int getThisPageIndex(int subindex, int page)
    {
        return ( page - 1 ) * records_per_page + subindex;
    }

    public static String getMessage(String message)
    {
        if (message == null)
            return "";
        if (message.equals(""))
            return "";
        String html = "";
        html += "<script type=\"text/javascript\">";
        html += "alert('" + message.replaceAll("'","")+ "')";
        html += "</script>\n";

        return html;
    }

}

