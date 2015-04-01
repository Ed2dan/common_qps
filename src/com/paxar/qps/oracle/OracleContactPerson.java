package com.paxar.qps.oracle;

import java.util.Vector;
/**
 * <p>Title: OracleContactPerson</p>
 *
 * <p>Description: Oracle contact person info</p>
 *
 * @author jerome
 * @version 1.0
 */
public class OracleContactPerson
{
    private String name = "";
    private Vector phone=null;
    private Vector fax=null;

    public OracleContactPerson(String fullName)
    {
        name = fullName;
        phone = new Vector();
        fax = new Vector();
    }

    public String getName()
    {
        return name;
    }

    public void addPhone(String phone)
    {
        if (!contains(this.phone,phone))
            this.phone.add(phone);
    }

    public void addFax(String phone)
    {
        if (!contains(this.fax,phone))
            this.fax.add(phone);
    }

    public String[] getPhones()
    {
        String dest[] = new String[phone.size()];
        phone.copyInto(dest);
        return dest;
    }


    public String[] getFaxes()
    {
        String dest[] = new String[fax.size()];
        fax.copyInto(dest);
        return dest;
    }

    private static String fixNumber(String src)
    {
        return src.replaceAll(" ","").replaceAll("+","").replaceAll("-","").replaceAll("ext","").replaceAll("(","").replaceAll(")","");
    }

    private static boolean contains(Vector vc, String src)
    {
        for (int i=0; i< vc.size(); i++)
        {
            String element = (String) vc.get(i);
            if (fixNumber(src).equals(fixNumber(element)))
                return true;
        }
        return false;
    }
}
