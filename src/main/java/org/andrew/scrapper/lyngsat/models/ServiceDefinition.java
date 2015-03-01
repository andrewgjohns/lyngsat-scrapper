package org.andrew.scrapper.lyngsat.models;

/**
 * Created by Andrew on 01/03/15.
 */
public class ServiceDefinition
{
    private String number;
    private String name;
    private String logoUrl;
    private String frequency;
    private String polarisation;
    private String deliverySystem;
    private String constellation;
    private String symbolRate;
    private String fec;
    private String sid;
    private String tags;
    private boolean isHd;
    private boolean isVideoGuard;
    private String multiplexUrl;

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLogoUrl()
    {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl)
    {
        this.logoUrl = logoUrl;
    }

    public String getFrequency()
    {
        return frequency;
    }

    public void setFrequency(String frequency)
    {
        this.frequency = frequency;
    }

    public String getPolarisation()
    {
        return polarisation;
    }

    public void setPolarisation(String polarisation)
    {
        this.polarisation = polarisation;
    }

    public String getDeliverySystem()
    {
        return deliverySystem;
    }

    public void setDeliverySystem(String deliverySystem)
    {
        this.deliverySystem = deliverySystem;
    }

    public String getConstellation()
    {
        return constellation;
    }

    public void setConstellation(String constellation)
    {
        this.constellation = constellation;
    }

    public String getSymbolRate()
    {
        return symbolRate;
    }

    public void setSymbolRate(String symbolRate)
    {
        this.symbolRate = symbolRate;
    }

    public String getFec()
    {
        return fec;
    }

    public void setFec(String fec)
    {
        this.fec = fec;
    }

    public String getSid()
    {
        return sid;
    }

    public void setSid(String sid)
    {
        this.sid = sid;
    }

    public String getTags()
    {
        return tags;
    }

    public boolean isHd()
    {
        return isHd;
    }

    public boolean isVideoGuard()
    {
        return isVideoGuard;
    }

    public String getMultiplexUrl()
    {
        return multiplexUrl;
    }
}
