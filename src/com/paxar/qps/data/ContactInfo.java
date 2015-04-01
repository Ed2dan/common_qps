/*
 * $RCSfile: ContactInfo.java,v $
 * $Revision: 1.9 $
 * $Date: 2013/08/16 04:04:40 $
 *
 * Copyright 2005 Paxar, Inc. All rights reserved.
 */
package com.paxar.qps.data;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.Serializable;
/**
 * The class <code>ContactInfo</code> stores "bill to" and
 * "ship to" information along with contractor PO, email
 * and service bureau data.
 *
 * @author  Ty Busby
 */
public class ContactInfo implements Serializable
{
    static final long serialVersionUID = -6641426658068552456L;
    /**
     * The public constructor.
     *
     * @param username String
     */
    public ContactInfo( String username )
    {
        this.username = username;
    }

    public String username = "";
    
    public String billToRefNo = "";
    public String billCustomerNo = "";
    public String billContact = "";
    public String billCompany = "";
    public String billAddress1 = "";
    public String billAddress2 = "";
    public String billAddress3 = "";
    public String billAddress4 = "";
    public String billCity = "";
    public String billState = "";
    public String billZip = "";
    public String billCountry = "";
    public String billCountryCode = "";
    public String billPhone = "";
    public String billFax = "";
    
    public String soldToRefNo = "";
    public String soldToCustomerNo = "";
    public String soldCompany = "";
    public String soldAddress1 = "";
    public String soldAddress2 = "";
    public String soldAddress3 = "";
    public String soldCity = "";
    public String soldState = "";
    public String soldZip = "";
    public String soldCountry = "";
    public String soldCountryCode = "";

    public String shipToRefNo = "";
    public String shipCustomerNo = "";
    public String shipContact = "";
    public String shipCompany = "";
    public String shipAddress1 = "";
    public String shipAddress2 = "";
    public String shipAddress3 = "";
    public String shipAddress4 = "";
    public String shipCity = "";
    public String shipState = "";
    public String shipZip = "";
    public String shipCountry = "";
    public String shipCountryCode = "";
    public String shipPhone = "";
    public String shipFax = "";
    public String shipVia = "";
    public String shipTerm = "";

    public String contractorPO = "";
    public String contractorEmail = "";
    public String serviceBureau = "";

    /**
     * Returns an HTML formatted String representation of the
     * ContactInfo object passed to it.
     *
     * @param info ContactInfo
     * @return String
     */
    public final static String getHtml( ContactInfo info )
        {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MMM-dd" );
        String html =
        "<table width='90%' border='1' cellpadding='3' cellspacing='0' align='center'>\n" +
        "<tr><td>\n" +
        "<table width='100%' border='0' cellpadding='1' cellspacing='0' align='center'>\n" +
        "    <tr><td>\n" +
        "        <table width='100%' border='0' cellpadding='0' cellspacing='0' align='left'>\n" +
        "            <tr><td><b>Bill To</b>\n" +
        "            <tr><td>" + info.billContact + "\n" +
        "            <tr><td>" + info.billCompany + "\n" +
        "            <tr><td>" + info.billAddress1 + "\n" +
        "            <tr><td>" + info.billAddress2 + "\n" +
        "            <tr><td>" + info.billAddress3 + "\n" +
        "            <tr><td>" + info.billAddress4 + "\n" +
        "            <tr><td>" + info.billCity + ", " + info.billState + " " + info.billZip + "\n" +
        "            <tr><td>" + info.billCountry + "\n" +
        "            <tr><td>" + info.billPhone + "\n" +
        "            <tr><td>" + info.billFax + " (Fax)\n" +
        "            <tr><td>&nbsp\n" +
        "            <tr><td><b>" + dateFormat.format( new Date( )) + "</b>\n" +
        "            <tr><td>Service Bureau: <b>" + info.serviceBureau + "</b>\n" +
        "        </table>\n" +
        "    <td>\n" +
        "        <table width='100%' border='0' cellpadding='0' cellspacing='0' align='left'>\n" +
        "            <tr><td><b>Ship To</b>\n" +
        "            <tr><td>" + info.shipContact + "\n" +
        "            <tr><td>" + info.shipCompany + "\n" +
        "            <tr><td>" + info.shipAddress1 + "\n" +
        "            <tr><td>" + info.shipAddress2 + "\n" +
        "            <tr><td>" + info.shipAddress3 + "\n" +
        "            <tr><td>" + info.shipAddress4 + "\n" +
        "            <tr><td>" + info.shipCity + ", " + info.shipState + " " + info.shipZip + "\n" +
        "            <tr><td>" + info.shipCountry + "\n" +
        "            <tr><td>" + info.shipPhone + "\n" +
        "            <tr><td>" + info.shipFax + " (Fax)\n" +
        "            <tr><td>Ship Via: <b>" + info.shipVia + "</b>\n" +
        "            <tr><td>Purchase Order: <b>" + info.contractorPO + "</b>\n" +
        "            <tr><td>Email: <a href=mailto:" + info.contractorEmail + ">" + info.contractorEmail + "</a>\n" +
        "         </table>\n" +
        "</table>\n" +
        "</table>\n" +
        "<br>\n";

        return html;
    }

    public final static String getHtml( ContactInfo info, String className )
        {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MMM-dd" );
        String html =
        "<table width='90%' border='1' align='center'>\n" +
        "<tr><td class='" + className + "'>\n" +
        "<table width='100%' border='0' cellpadding='1' cellspacing='0' align='center'>\n" +
        "    <tr><td class='" + className + "'>\n" +
        "        <table width='100%' border='0' cellpadding='0' cellspacing='0' align='left'>\n" +
        "            <tr><td class='" + className + "'><b>Bill To</b>\n" +
        "            <tr><td class='" + className + "'>" + info.billContact + "\n" +
        "            <tr><td class='" + className + "'>" + info.billCompany + "\n" +
        "            <tr><td class='" + className + "'>" + info.billAddress1 + "\n" +
        "            <tr><td class='" + className + "'>" + info.billAddress2 + "\n" +
        "            <tr><td class='" + className + "'>" + info.billAddress3 + "\n" +
        "            <tr><td class='" + className + "'>" + info.billAddress4 + "\n" +
        "            <tr><td class='" + className + "'>" + info.billCity + ", " + info.billState + " " + info.billZip + "\n" +
        "            <tr><td class='" + className + "'>" + info.billCountry + "\n" +
        "            <tr><td class='" + className + "'>" + info.billPhone + "\n" +
        "            <tr><td class='" + className + "'>" + info.billFax + " (Fax)\n" +
        "            <tr><td class='" + className + "'>&nbsp\n" +
        "            <tr><td class='" + className + "'><b>" + dateFormat.format( new Date( )) + "</b>\n" +
        "            <tr><td class='" + className + "'>Service Bureau: <b>" + info.serviceBureau + "</b>\n" +
        "        </table>\n" +
        "    <td class='" + className + "'>\n" +
        "        <table width='100%' border='0' cellpadding='0' cellspacing='0' align='left'>\n" +
        "            <tr><td class='" + className + "'><b>Ship To</b>\n" +
        "            <tr><td class='" + className + "'>" + info.shipContact + "\n" +
        "            <tr><td class='" + className + "'>" + info.shipCompany + "\n" +
        "            <tr><td class='" + className + "'>" + info.shipAddress1 + "\n" +
        "            <tr><td class='" + className + "'>" + info.shipAddress2 + "\n" +
        "            <tr><td class='" + className + "'>" + info.shipAddress3 + "\n" +
        "            <tr><td class='" + className + "'>" + info.shipAddress4 + "\n" +
        "            <tr><td class='" + className + "'>" + info.shipCity + ", " + info.shipState + " " + info.shipZip + "\n" +
        "            <tr><td class='" + className + "'>" + info.shipCountry + "\n" +
        "            <tr><td class='" + className + "'>" + info.shipPhone + "\n" +
        "            <tr><td class='" + className + "'>" + info.shipFax + " (Fax)\n" +
        "            <tr><td class='" + className + "'>Ship Via: <b>" + info.shipVia + "</b>\n" +
        "            <tr><td class='" + className + "'>Purchase Order: <b>" + info.contractorPO + "</b>\n" +
        "            <tr><td class='" + className + "'>Email: <a href=mailto:" + info.contractorEmail + ">" + info.contractorEmail + "</a>\n" +
        "         </table>\n" +
        "</table>\n" +
        "</table>\n" +
        "<br>\n";

        return html;
    }
}
