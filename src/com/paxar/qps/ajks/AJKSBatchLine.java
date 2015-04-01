package com.paxar.qps.ajks;

public class AJKSBatchLine
{
    private long printQty=0;
    private String batchLine="";

    public AJKSBatchLine()
    {
    }


    public void setBatchLineWithQty(String batchLine, String delimiter)
    {
        this.batchLine = batchLine.substring(batchLine.indexOf(delimiter));
        this.printQty = Long.parseLong(batchLine.substring(0, batchLine.indexOf(delimiter)));
    }

    public void setBatchLine( String batchLine )
    {
        this.batchLine = batchLine;
    }

    public void setPrintQty( long printQty )
    {
        this.printQty = printQty;
    }

    public String getBatchLine()
    {
        return batchLine;
    }

    public long getPrintQty()
    {
        return printQty;
    }

}
