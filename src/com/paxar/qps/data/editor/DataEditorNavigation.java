package com.paxar.qps.data.editor;
import java.util.*;
import java.io.Serializable;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DataEditorNavigation implements Serializable
{
    static final long serialVersionUID = -4928093414695718952L;
    private int sortBy=0;
    private int sortOrder=DataEditorSortKey.ASC;
    private int page=1;
    private int totalRecords=0;

    public DataEditorNavigation(Map map)
    {
        if (map.get("desort")!=null)
            sortBy = Integer.parseInt(((String[])map.get("desort"))[0]);
        if (map.get("deorder")!=null)
            sortOrder = Integer.parseInt(((String[])map.get("deorder"))[0]);
        if (map.get("depage")!=null)
            page = Integer.parseInt(((String[])map.get("depage"))[0]);
    }

    public void setSortBy( int sortBy )
    {
        this.sortBy = sortBy;
    }

    public void setSortOrder( int sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    public void setPage( int page )
    {
        this.page = page;
    }

    public void setTotalRecords( int totalRecords )
    {
        this.totalRecords = totalRecords;
    }

    public int getSortBy()
    {
        return sortBy;
    }

    public int getSortOrder()
    {
        return sortOrder;
    }

    public int getPage()
    {
        return page;
    }

    public int getTotalRecords()
    {
        return totalRecords;
    }
}
