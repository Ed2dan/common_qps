package com.paxar.qps.ajks;
import java.util.*;

import com.paxar.qps.oracle.*;
import java.io.*;

/**
 * @author Jerome
 *
 * AJKSProcessor provies function to create AJKS files.
 *
 */

public class AJKSProcessor
{
    protected OracleContactInfo info;
    protected Vector batchLines = null;
    protected String retailer = "";
    private String ticketType = "";
    protected String webOrderID = "";
    protected String retailerPONo = "";
    protected String retailerVendorNo = "";

    // Line Break character for AJKS file
    public final static String lineBreak = "\r\n";

    // Batch Item Separator charactor
    public final static String batchItemSeparator = "|";

    public AJKSProcessor()
    {
        batchLines = new Vector();
    }

    /**
     * Function to create AJKS file
     *
     */
    public String getAJKS()
    {
        if ( info == null )
            return "";
        if ( batchLines.size() == 0 )
            return "";
        if ( retailer.equals( "" ) )
            return "";
        if ( ticketType.equals( "" ) )
            return "";

        String output = "";

        // Header
        output += "A" + RPadSpace( info.billCustomerNo, 10 ) +
            RPadSpace( info.contractorPO, 20 ) + "  " + RPadSpace( retailer , 15 ) + RPadSpace( this.retailerVendorNo, 17 ) + info.getOnHoldFlag() + RPadSpace("",23) + RPadSpace(getWebOrderID(),10) + " " + lineBreak;

        // Ship to info
        output += "C01" + RPadSpace( info.shipCompany, 30 ) +
            RPadSpace( info.shipZip, 9 );
        output += RPadSpace( info.shipCountryCode, 4 ) ;
        // Replaced with Blank
        //output += RPadSpace( info.shipCustomerNo, 17 ) + lineBreak;
        output += RPadSpace( "", 17 ) + lineBreak;
        output += "C02" + RPadSpace( info.shipAddress1, 30 ) + lineBreak;
        output += "C03" + RPadSpace( info.shipAddress2, 30 ) + lineBreak;
        output += "C04" + RPadSpace( info.shipAddress3, 30 ) + lineBreak;
        output += "C05" + RPadSpace( info.shipAddress4, 30 ) + lineBreak;
        output += "C06" + RPadSpace( info.shipCity, 27 ) + " " +
            RPadSpace( info.shipState, 2 ) + lineBreak;
        output += "D" + RPadSpace( info.shipVia, 20 ) +
            parseDateCode( info.shipDate ) +
            parseDateCode( info.shipPromiseDate ) +
            RPadSpace( info.shipTerm, 100 ) +
            lineBreak;

        // Instructions
        output += "E01" + RPadSpace( info.vendorNote1, 60 ) + lineBreak;
        output += "E02" + RPadSpace( info.vendorNote2, 60 ) + lineBreak;
        output += "E03(C)" + RPadSpace( info.vendorNote3, 57 ) + lineBreak;
        output += "E04" + RPadSpace( info.printerNote1, 60 ) + lineBreak;
        output += "E05" + RPadSpace( info.printerNote2, 60 ) + lineBreak;
        output += "E06" + RPadSpace( info.printerNote3, 60 ) + lineBreak;
        output += "E07" + RPadSpace( info.shipInstruction1, 60 ) + lineBreak;
        output += "E08" + RPadSpace( info.shipInstruction2, 60 ) + lineBreak;
        output += "E09" + RPadSpace( info.shipInstruction3, 60 ) + lineBreak;
        output += "E10" + RPadSpace( info.orderLineNote1, 60 ) + lineBreak;
        output += "E11" + RPadSpace( info.orderLineNote2, 60 ) + lineBreak;
        output += "E12" + RPadSpace( info.orderLineNote3, 60 ) + lineBreak;

        // Summary
        output += "J001" + PadZero( getTotalPrintQty(), 9 ) +
            RPadSpace( ticketType, 19 ) + RPadSpace( "", 32 ) +
            RPadSpace( info.contractorPO, 20 ) + RPadSpace( this.retailerPONo, 20 ) + lineBreak;

        // Line Items
        int lineNo = 1;
        for ( int i = 0; i < batchLines.size(); i++ )
        {
            AJKSBatchLine batchLine = ( AJKSBatchLine )batchLines.get( i );
            if (batchLine.getPrintQty()>0)
            {
                output += "K001" + PadZero( ( lineNo ), 3 ) +
                    PadZero( batchLine.getPrintQty(), 9 ) + batchItemSeparator +
                    batchLine.getBatchLine() + lineBreak;
                lineNo++;
            }
        }

        // End
        output += "P001   " + RPadSpace( info.specialInstruction1, 60 ) +
            lineBreak;
        output += "P002   " + RPadSpace( info.specialInstruction2, 60 ) +
            lineBreak;
        output += "P003   " + RPadSpace( info.specialInstruction3, 60 ) +
            lineBreak;
        output += "P   001" + RPadSpace( info.billContact, 30) + RPadSpace( info.contractorEmail, 60 ) +
            lineBreak;
        output += "P   002" + RPadSpace( info.billCompany, 30 ) +
            RPadSpace( info.billZip, 9 ) + RPadSpace( info.billCountryCode, 4 ) +
            RPadSpace( "", 17 ) + lineBreak;
        output += "P   003" + RPadSpace( info.billAddress1, 30 ) +
            lineBreak;
        output += "P   004" + RPadSpace( info.billAddress2, 30 ) +
            lineBreak;
        output += "P   005" + RPadSpace( info.billAddress3, 30 ) +
            lineBreak;
        output += "P   006" + RPadSpace( info.billAddress4, 30 ) +
            lineBreak;
        output += "P   007" + RPadSpace( info.billCity, 27 ) + " " +
            RPadSpace( info.billState, 2 ) + lineBreak;

        output += "Z" + lineBreak;
        return output;
    }

    public void setContactInfo( OracleContactInfo info )
    {
        this.info = info;
    }

    public OracleContactInfo getContactInfo()
    {
        return info;
    }

    public void setRetailer( String retailer )
    {
        this.retailer = retailer;
    }

    public String getRetailer()
    {
        return retailer;
    }

    public String getTicketType()
    {
        return ticketType;
    }

    public void setWebOrderID( String orderID )
    {
        this.webOrderID = orderID;
    }

    public String getWebOrderID()
    {
        return webOrderID;
    }

    public void setRetailerPONo( String retailerPONo )
    {
        this.retailerPONo = retailerPONo;
    }

    public String getRetailerPONo()
    {
        return this.retailerPONo;
    }

    public void setRetailerVendorNo( String retailerVendorNo )
    {
        this.retailerVendorNo = retailerVendorNo;
    }

    public String getRetailerVendorNo()
    {
        return this.retailerVendorNo;
    }




    /**
     * Get Ship Date in form of MMDDYY
     *
     * Input: DD-MM-YYYY or DD/MM/YYYY
     *
     * @return String
     */
    public String parseDateCode( String datecode )
    {
        try {
            String shipDateCode = "";
            String dateSeparator = "-";
            if ( datecode.indexOf( "/" ) > -1 )
                dateSeparator = "/";

            if ( ( datecode.indexOf( dateSeparator ) > -1 ) &&
                 ( datecode.lastIndexOf( dateSeparator ) !=
                   datecode.indexOf( dateSeparator ) ) )
            {
                String day = datecode.substring( 0,
                                                 datecode.indexOf( dateSeparator ) );
                String month = datecode.substring( datecode.indexOf(
                    dateSeparator ) + 1,
                    datecode.lastIndexOf(
                        dateSeparator ) + 1 );
                String year = datecode.substring( datecode.lastIndexOf(
                    dateSeparator ) + 1 );
                shipDateCode = PadZero( month, 2 ) + PadZero( day, 2 ) +
                               PadZero( year.substring( 2 ), 2 );
                return PadZero( shipDateCode, 6 );
            }
        } catch (Exception e) {}
        return "      ";
    }

    /**
     * Add padding space to the right
     *
     * @param src String
     * @param length int length of string required
     * @return String
     */
    protected String RPadSpace( String src, int length )
    {
        if ( src.length() > length )
            return src.substring( 0, length );
        int pad = length - src.length();
        for ( int i = 0; i < pad; i++ )
            src += " ";
        return src;
    }

    /**
     * Add leading zeros
     *
     * @param src long
     * @param length int length of string required
     * @return String
     */
    protected String PadZero( long src, int length )
    {
        return PadZero( "" + src, length );
    }

    protected String PadZero( String src, int length )
    {
        if ( src.length() > length )
            return src.substring( 0, length );
        int pad = length - src.length();
        for ( int i = 0; i < pad; i++ )
            src = "0" + src;
        return src;
    }

    public void setBatchLines( Vector batchLines )
    {
        this.batchLines = batchLines;
    }

    public void setTicketType( String ticketType )
    {
        this.ticketType = ticketType;
    }

    public void addBatchLine( AJKSBatchLine batchLine )
    {
        if ( batchLine.getPrintQty() > 0 )
            this.batchLines.add( batchLine );
    }

    public void clearBatchLines()
    {
        this.batchLines.clear();
    }

    public AJKSBatchLine getBatchLine( int i )
    {
        return ( AJKSBatchLine )batchLines.get( i );
    }

    public long getTotalPrintQty()
    {
        long printQty = 0;
        for ( int i = 0; i < batchLines.size(); i++ )
        {
            AJKSBatchLine line = ( AJKSBatchLine )batchLines.get( i );
            printQty += line.getPrintQty();
        }
        return printQty;
    }
}





