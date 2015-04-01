package com.paxar.qps.oracle;
import com.paxar.qps.data.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.io.Serializable;

/**
 * ContactInfo enhanced to include information from Oracle
 *
 * @author jerome
 */

public class OracleContactInfo extends com.paxar.qps.data.ContactInfo implements Serializable
{
    static final long serialVersionUID = 6799641223873370236L;

    /**
     * Constructor
     *
     * @param username String save info for individual username
     */

    public OracleContactInfo( String user )
    {
        super( user );
    }

    public OracleContactInfo()
    {
        super( "" );
    }

    public static final String dateDisplayFormat = "dd-MM-yyyy";
    public static final String dateInputFormat = "dd-MM-yyyy";

    public void setContactInfo( ContactInfo info )
    {
        this.billAddress1 = info.billAddress1;
        this.billAddress2 = info.billAddress2;
        this.billAddress3 = info.billAddress3;
        this.billAddress4 = info.billAddress4;
        this.billAddressID = "";
        this.billCity = info.billCity;
        this.billCompany = info.billCompany;
        this.billContact = info.billContact;
        this.billCountry = info.billCountry;
        this.billCountryCode = info.billCountryCode;
        this.billCustomerNo = info.billCustomerNo;
        this.billFax = info.billFax;
        this.billPhone = info.billPhone;
        this.billState = info.billState;
        this.billZip = info.billZip;

        this.shipAddress1 = info.shipAddress1;
        this.shipAddress2 = info.shipAddress2;
        this.shipAddress3 = info.shipAddress3;
        this.shipAddress4 = info.shipAddress4;
        this.shipAddressID = "";
        this.shipCity = info.shipCity;
        this.shipCompany = info.shipCompany;
        this.shipContact = info.shipContact;
        this.shipCountry = info.shipCountry;
        this.shipCountryCode = info.shipCountryCode;
        this.shipCustomerNo = info.shipCustomerNo;
        this.shipFax = info.shipFax;
        this.shipPhone = info.shipPhone;
        this.shipState = info.shipState;
        this.shipZip = info.shipZip;

        this.shipVia = info.shipVia;
        this.shipDate = getDefaultShipDate();
        this.shipPromiseDate = getDefaultShipDate();

        this.shipInstruction1 = "";
        this.shipInstruction2 = "";
        this.shipInstruction3 = "";
        this.specialInstruction1 = "";
        this.specialInstruction2 = "";
        this.specialInstruction3 = "";
        this.printerNote1 = "";
        this.printerNote2 = "";
        this.printerNote3 = "";
        this.vendorNote1 = "";
        this.vendorNote2 = "";
        this.vendorNote3 = "";
        this.orderLineNote1 = "";
        this.orderLineNote2 = "";
        this.orderLineNote3 = "";
        this.isOffset = false;
        this.isServiceBureau = false;
        this.isOnHold = false;

        if ( this.billCustomerNo == null )
            this.billCustomerNo = "";
        if ( this.shipCustomerNo == null )
            this.shipCustomerNo = "";

        this.contractorEmail = info.contractorEmail;
    }

    public String billAddressID = "";
    public String shipAddressID = "";

    public String shipDate = "";
    public String shipPromiseDate = "";
    public String shipInstruction1 = "";
    public String shipInstruction2 = "";
    public String shipInstruction3 = "";
    public String specialInstruction1 = "";
    public String specialInstruction2 = "";
    public String specialInstruction3 = "";
    public String printerNote1 = "";
    public String printerNote2 = "";
    public String printerNote3 = "";
    public String vendorNote1 = "";
    public String vendorNote2 = "";
    public String vendorNote3 = "";
    public String orderLineNote1 = "";
    public String orderLineNote2 = "";
    public String orderLineNote3 = "";

    public String division = "";

    public boolean isOffset = false;
    public boolean isServiceBureau = false;
    public boolean isOnHold = false;

    public static String getHtml( OracleContactInfo info )
    {
        return getHtml( info, "contactInfo" );
    }

    public static String getHtml( OracleContactInfo info, String className )
    {

        String freightTerm = info.shipTerm;
        try { freightTerm = FreightTermsDAO.getFreightDesc(info.shipTerm); }
        catch (Exception e) {}

        SimpleDateFormat dateFormat = new SimpleDateFormat( dateDisplayFormat );
        String html =
            "<table width='90%' border='1' cellpadding='3' cellspacing='0' align='center'>\n" +
            "<tr><td class='" + className + "'>\n" +
            "<table width='100%' border='0' cellpadding='1' cellspacing='0' align='center'>\n" +
            "    <tr><td class='" + className + "' valign='top' width='50%'>\n" +
            "        <table border='0' cellpadding='0' cellspacing='0' align='left'>\n" +
            "            <tr><td class='" + className + "'><b>Bill To</b>\n" +
            "            <tr><td class='" + className + "'>(" +
            info.billCustomerNo + ")\n" +
            "            <tr><td class='" + className + "'>" + info.billContact +
            "\n" +
            "            <tr><td class='" + className + "'>" + info.billCompany +
            "\n" +
            "            <tr><td class='" + className + "'>" +
            info.billAddress1 +
            "\n" +
            "            <tr><td class='" + className + "'>" +
            info.billAddress2 +
            "\n" +
            "            <tr><td class='" + className + "'>" +
            info.billAddress3 +
            "\n" +
            "            <tr><td class='" + className + "'>" +
            info.billAddress4 +
            "\n" +
            "            <tr><td class='" + className + "'>" + info.billCity +
            ", " +
            info.billState + " " + info.billZip + "\n" +
            "            <tr><td class='" + className + "'>" +
            info.billCountryCode +
            "\n" +
            "            <tr><td class='" + className + "'>" + info.billPhone +
            "\n" +
            "            <tr><td class='" + className + "'>" + info.billFax +
            " (Fax)\n" +
            "            <tr><td class='" + className + "'>&nbsp\n" +
            "            <tr><td class='" + className + "'>Username: <b>" +
            info.username + "</b>\n" +
            "            <tr><td class='" + className + "'>Order Date: <b>" +
            dateFormat.format( new Date() ) + "</b>\n" +
            "            <tr><td class='" + className + "'>Service Bureau: <b>" +
            info.serviceBureau + "</b>\n" +
//            "            <tr><td class='" + className + "'>Printer Type: <b>" +
//            (info.isOffset ? "Offset Printer" : "Paxar Thermal Printer") + "</b>\n" +
            "            <tr><td class='" + className + "'>Submit Status: <b>" +
            (info.isOnHold ? "<font color=\"red\">On Hold</font>" : "Normal") + "</b>\n" +
            "        </table>\n" +
            "    <td class='" + className + "' valign='top' width='50%'>\n" +
            "        <table border='0' cellpadding='0' cellspacing='0' align='left'>\n" +
            "            <tr><td class='" + className + "'><b>Ship To</b>\n" +
            "            <tr><td class='" + className + "'>(" +
            info.shipCustomerNo + ")\n" +
            "            <tr><td class='" + className + "'>" + info.shipContact +
            "\n" +
            "            <tr><td class='" + className + "'>" + info.shipCompany +
            "\n" +
            "            <tr><td class='" + className + "'>" +
            info.shipAddress1 +
            "\n" +
            "            <tr><td class='" + className + "'>" +
            info.shipAddress2 +
            "\n" +
            "            <tr><td class='" + className + "'>" +
            info.shipAddress3 +
            "\n" +
            "            <tr><td class='" + className + "'>" +
            info.shipAddress4 +
            "\n" +
            "            <tr><td class='" + className + "'>" + info.shipCity +
            ", " +
            info.shipState + " " + info.shipZip + "\n" +
            "            <tr><td class='" + className + "'>" +
            info.shipCountryCode +
            "\n" +
            "            <tr><td class='" + className + "'>" + info.shipPhone +
            "\n" +
            "            <tr><td class='" + className + "'>" + info.shipFax +
            " (Fax)\n" +
            "            <tr><td class='" + className +
            "'>Shipping Method: <b>" +
            info.shipVia + "</b>\n" +
            "            <tr><td class='" + className +
            "'>Requested Ship Date: <b>" +
            convertDate( info.shipDate ) + "</b>\n" +
            "            <tr><td class='" + className +
            "'>Promise Ship Date: <b>" +
            convertDate( info.shipPromiseDate ) + "</b>\n" +
            "            <tr><td class='" + className + "'>Freight Term: <b>" +
            freightTerm + "</b>\n" +
            "            <tr><td class='" + className + "'>Purchase Order: <b>" +
            info.contractorPO + "</b>\n" +
            "            <tr><td class='" + className +
            "'>Email: <a href=mailto:" +
            info.contractorEmail + ">" + info.contractorEmail + "</a>\n" +
            "         </table>\n" +
            "</table>\n" +
            "</table>\n" +
            "<br>\n";
        return html;
    }

    public static String getDefaultShipDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat( dateInputFormat );
        Date today = new Date();
        today.setTime( today.getTime() + 1000 * 60 * 60 * 24 * 5 );
        return sdf.format( today );
    }

    public static String convertDate( String src )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( dateInputFormat );
        SimpleDateFormat ddf = new SimpleDateFormat( dateDisplayFormat );
        return ddf.format( sdf.parse( src, new ParsePosition( 0 ) ) );
    }

    public static String getDefaultShipTerm()
    {
        return "PREPAY AND ADD";
    }

    public String getOnHoldFlag()
    {
        if (isOnHold)
            return "V";
        return " ";
    }
}



