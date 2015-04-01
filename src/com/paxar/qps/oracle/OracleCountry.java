package com.paxar.qps.oracle;
import java.io.Serializable;
/**
 * @author Jerome
 *
 * OracleCountry defines a territory of Oracle.  Code and description
 *
 */

public class OracleCountry implements Serializable
{
     static final long serialVersionUID = -1441646516517428444L;

    private String countryDesc;
    private String countryCode;

    public OracleCountry(String countryCode, String countryDesc)
    {
        this.countryCode = countryCode;
        this.countryDesc = countryDesc;
    }

    public void setCountryDesc( String countryDesc )
    {
        this.countryDesc = countryDesc;
    }

    public void setCountryCode( String countryCode )
    {
        this.countryCode = countryCode;
    }

    public String getCountryDesc()
    {
        return countryDesc;
    }

    public String getCountryCode()
    {
        return countryCode;
    }
}
