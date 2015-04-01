package com.paxar.qps.data;

public class HtmlInclude
{
    protected static String style =
        "<style>\n" +
        "<!---\n" +
        "      tr.tableheader {text-align: center; background-color: LightGrey; font-family: Verdana; font-size: 9pt; font-weight: bold; align: center}\n" +
        "      tr.data  {text-align: center; font-family: Verdana; font-size: 9pt; align: center}\n" +
        "      tr.title  {text-align: center; font-family: Verdana; font-size: 11pt; align: center; font-weight: bold}\n" +
        "      tr.listlabel {font-weight: bold; text-align: right; font-family: Verdana; font-size: 9pt; align: right}\n" +
        "      tr.listdata {text-align: left; font-family: Verdana; font-size: 9pt; font-weight: normal; align: left}\n" +
        "      td.tableheader {text-align: center; background-color: LightGrey; font-family: Verdana; font-size: 9pt; font-weight: bold; align: center}\n" +
        "      td.data {text-align: left; font-family: Verdana; font-size: 9pt;}\n" +
        "      td.header {text-align: center; font-family: Verdana; font-size: 14pt;}\n" +
        "      td.dataright {text-align: right; font-family: Verdana; font-size: 9pt;}\n" +
        "      td.datacenter {text-align: center; font-family: Verdana; font-size: 9pt;}\n" +
        "      td.redalert {text-align: center; font-family: Verdana; font-size: 9pt; color: red;}\n" +
        "      td.datacaption {text-align: center; font-family: Verdana; font-size: 11pt; font-weight: bold;}\n" +
        "      input.data {text-align: left; font-family: Verdana; font-size: 9pt;}\n" +
        "      input.buttoncaption {text-align: center; font-family: Verdana; font-size: 10pt;}\n" +
        "      select.data {text-align: left; font-family: Verdana; font-size: 9pt;}\n" +
        "      textarea {text-align: left; font-family: Verdana; font-size: 9pt;}\n" +
        "      td.OtherTabsBg {text-align: left; background-color: Black; font-family: Verdana; font-size: 8pt; font-weight: bold; color: white}\n" +
        "      td.TabRetailer {text-align: right; background-color: Black; font-family: Verdana; font-size: 11pt; font-weight: bold; color: white}\n" +
        "        :link { color: blue }\n" +
        "        :visited { color: blue }\n" +
        "      a:active { color: blue }\n" +
        "      a:hover { color: red }\n" +
        "      a.plain { text-decoration: none }\n" +
        "--->\n" +
        "</style>\n";

    public static String top( )
    {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                      "<html>\n" +
                      "<head>\n" +
                      "<meta content=\"text/html; charset=ISO-8859-1\" http-equiv=\"content-type\">\n" +
                      style +
                      "<body>\n";
        return html;
    }

    public static String bottom( )
    {
        String html = "";
        html += 	"</body></html>";
        return html;
    }
}
