package com.paxar.qps.oracle;
import java.util.HashMap;
import java.util.Iterator;
/**
 * <p>Title: OracleContactPersons</p>
 *
 * <p>Description: A set of OracleContactPerson</p>
 *
 * @author jerome
 * @version 1.0
 */
public class OracleContactPersons
{
    HashMap map = null;

    public OracleContactPersons()
    {
        map = new HashMap();
    }

    public void addContact(String fullName, String phone, String phoneType)
    {
        OracleContactPerson person = null;
        if (map.containsKey(fullName))
        {
            person = (OracleContactPerson) map.get(fullName);
        }
        else
        {
            person = new OracleContactPerson(fullName);
            map.put(fullName, person);
        }

        if (isFax(phoneType))
            person.addFax(phone);
        else
            person.addPhone(phone);

        map.remove(fullName);
        map.put(fullName, person);
    }

    public int size()
    {
        return map.keySet().size();
    }

    public OracleContactPerson get(int i)
    {
        Iterator it = map.keySet().iterator();
        int k=0;
        while (it.hasNext())
        {
            OracleContactPerson person = (OracleContactPerson)map.get(it.next());
            if (i==k)
                return person;
            k++;
        }
        return null;
    }

    public OracleContactPerson getByName(String fullName)
    {
        return (OracleContactPerson) map.get(fullName);
    }

    public static boolean isFax(String phoneType)
    {
        phoneType = phoneType.toUpperCase();
        if (phoneType.equals( "FACSIMILE" ))
            phoneType = "FAX";
        if (phoneType.equals("FAX"))
            return true;
        return false;
    }
}
