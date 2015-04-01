package com.paxar.utilities;

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
public class ExcelRow {
    private Cell [] row;
    private ExcelSheet sheet;
    public ExcelRow() {
    }

    public ExcelRow(Cell [] row, ExcelSheet sheet){
        this.sheet = sheet;
        this.row = row;
    }

    public void setRow( Cell[] row ) {
        this.row = row;
    }

    protected void setSheet( ExcelSheet sheet ) {
        this.sheet = sheet;
    }

    public Cell[] getRow() {
        return row;
    }

    protected ExcelSheet getSheet() {
        return sheet;
    }

    public String getValue(int column) throws Exception
    {
        return row[column].getContents();
    }

    public String getValue(String str) throws Exception
    {
        return row[sheet.getHeaderColumn(str)].getContents().trim();
    }
}
