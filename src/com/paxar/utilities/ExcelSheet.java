package com.paxar.utilities;

import java.util.*;

import jxl.*;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author jerome
 * @version 1.0
 */
public class ExcelSheet {
    private ExcelFile file;
    private Sheet sheet;
    private int header_row;
    private int start_row;
    private int end_row;
    private HashMap map;

    public ExcelSheet() {
    }

    public ExcelSheet(Sheet sheet, ExcelFile file){
        this.sheet = sheet;
        this.file = file;
        this.header_row = -1;
        this.start_row = -1;
        this.end_row = -1;
    }

    public void setHeader_row( int header_row ) {
        this.header_row = header_row;
    }

    public void setStart_row( int start_row ) {
        this.start_row = start_row;
    }

    public void setHeaders( HashMap headers ) {
        this.map = headers;
    }

    public void setWorksheet( Sheet worksheet ) {
        this.sheet = worksheet;
    }

    protected void setFile( ExcelFile file ) {
        this.file = file;
    }

    public void setEnd_row() {
        this.end_row = sheet.getColumns();
    }

    public int getHeader_row() {
        return header_row;
    }

    public int getStart_row() {
        return start_row;
    }

    public HashMap getHeaders() {
        return map;
    }

    public Sheet getWorksheet() {
        return sheet;
    }

    public int getEnd_row() {
        return end_row;
    }

    protected ExcelFile getFile() {
        return file;
    }

    public String getSheetName()
    {
        if (!sheet.equals(null))
            return sheet.getName();
        return "";
    }

    public void setHeaderList(String [] list) throws Exception
    {
        map = new HashMap();
        boolean done = false;
        // get the header_row number
        for (int i=0; i<sheet.getRows()-1; i++)
        {
            for (int j=0; j<sheet.getColumns()-1;j++)
            {
                if (done)
                    continue;
                if ( sheet.getCell( j, i ).getContents().toUpperCase().
                     replace( '\n', ' ' ).replace( '\r', ' ' ).
                     replaceAll( "  ",
                     " " ).startsWith( list[0].toUpperCase() ) ) {
                    header_row = i;
                    done = true;
                }
            }
        }
        //get each header's corresponding column if they exist
        if (header_row > -1)
        {
            for ( int i = 0; i < sheet.getColumns(); i++ ) {
                for ( int j = 0; j < list.length; j++ ) {
                    if ( sheet.getCell( i, header_row ).getContents().
                         toUpperCase().
                         replace( '\n', ' ' ).replace( '\r',
                         ' ' ).replaceAll( "  ", " " ).
                         startsWith( list[j].toUpperCase() ) )
                        map.put( list[j], new Integer( i ) );
                }
            }
        }
    }

    protected int getHeaderColumn(String str) throws Exception
    {
        if (map.size() == 0)
            return -1;
        else
            return ((Integer)map.get(str)).intValue();
    }

    public ExcelRow getRow(int i) throws Exception
    {
        return new ExcelRow(sheet.getRow(i), this);
    }

    public int size() throws Exception
    {
        if (start_row == -1)
            return -1;
        return sheet.getRows() - start_row;
    }
}
