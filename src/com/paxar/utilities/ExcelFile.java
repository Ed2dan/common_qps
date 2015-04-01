package com.paxar.utilities;

import jxl.*;
import java.io.*;
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
public class ExcelFile {
    private Workbook workbook;
    public ExcelFile() {
    }

    public ExcelFile(Workbook workbook)
    {
        this.workbook = workbook;
    }

    public ExcelFile(InputStream is)
    {
        try{
            this.workbook = Workbook.getWorkbook( is );
        }
        catch (Exception e){}
    }

    public void setWorkbook( Workbook workbook ) {
        this.workbook = workbook;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public ExcelSheet getSheet(int sheetNum) throws Exception
    {
        return new ExcelSheet(workbook.getSheet(sheetNum), this);
    }

    public Sheet getSheetByName(String sheetname) throws Exception
    {
        String [] sheetnames = workbook.getSheetNames();
        for (int i=0; i<sheetnames.length; i++)
        {
            if (sheetnames[i].equals(sheetname))
                return workbook.getSheet(i);
        }
        return null;
    }

    public int size() throws Exception
    {
        return workbook.getNumberOfSheets();
    }
}
