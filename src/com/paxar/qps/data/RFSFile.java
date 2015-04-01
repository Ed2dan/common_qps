package com.paxar.qps.data;
import java.io.*;

/**
 * <p>Title: RFSFile</p>
 *
 * <p>Description: An entry of Remote File Service.  This class is used to send files to Remote File Service</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author jerome
 * @version 1.0
 */
public class RFSFile
{
    private File batchFile=null;
    private String retailer="";
    private String group="";
    private String user=ALL;
    private String site=ALL;
    private String comment="";
    private String type="";
    public static String ALL = "ALL";

    // Type options
    public static int TYPE_BATCH = 0;
    public static int TYPE_BST = 1;

    public RFSFile()
    {
    }


    public RFSFile(File batchFile, String group, String type, String retailer, String site, String user, String comment)
    {
        setBatchFile(batchFile);
        setGroup(group);
        setType(type);
        setRetailer(retailer);
        setSite(site);
        setUser(user);
        setComment(comment);
    }

    public RFSFile(File batchFile, String group, int type, String retailer, String site, String user, String comment)
    {
        this(batchFile, group, ""+type , retailer, site, user, comment);
    }

    public RFSFile(String batchFile, String group, int type, String retailer, String site, String user, String comment)
    {
        this(new File(batchFile), group, ""+type , retailer, site, user, comment);
    }

    public RFSFile(String batchFile, String group, String type, String retailer, String site, String user, String comment)
    {
        this(new File(batchFile), group, ""+type , retailer, site, user, comment);
    }


    public void setBatchFile(String batchFile)
    {
        this.batchFile = new File(batchFile);
    }
    public void setBatchFile( File batchFile )
    {
        this.batchFile = batchFile;
    }

    public void setGroup( String group )
    {
        this.group = group;
    }

    public void setUser( String user )
    {
        if (user.equals(""))
            this.user=ALL;
        else
            this.user = user;
    }

    public void setSite( String site )
    {
        if (site.equals(""))
            this.site=ALL;
        else
            this.site = site;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    public void setType( int type)
    {
        this.type = "" + type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public void setRetailer( String retailer )
    {
        this.retailer = retailer;
    }

    public File getBatchFile()
    {
        return batchFile;
    }

    public String getGroup()
    {
        return group;
    }

    public String getUser()
    {
        return user;
    }

    public String getSite()
    {
        return site;
    }

    public String getComment()
    {
        return comment;
    }

    public String getType()
    {
        return type;
    }

    public String getRetailer()
    {
        return retailer;
    }
}
