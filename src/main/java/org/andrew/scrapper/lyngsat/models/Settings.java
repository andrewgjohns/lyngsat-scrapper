package org.andrew.scrapper.lyngsat.models;

import java.util.*;

/**
 * Created by Andrew on 01/03/15.
 */
public class Settings
{
    private String channelUrl;
    private String sidUrl;
    private String position;
    private String region;
    private String excludedBouquet;
    private boolean useHd;
    private boolean useHdForVideoGuard;
    private List<Bouquet> bouquets;


    public Settings(Properties props)
    {
        channelUrl = props.getProperty("CHANNEL-URL");
        sidUrl = props.getProperty("SID-URL");
        position = props.getProperty("POSITION");
        region = props.getProperty("REGION");
        excludedBouquet = props.getProperty("BOUQUET-EXCLUDE");
        useHd = (boolean) props.get("USE-HD");
        useHdForVideoGuard = (boolean) props.get("USE-HD-VIDEO-GUARD");

        // assign the property names in a enumaration
        final List propertyNames = Collections.list(props.propertyNames());
        this.bouquets = new ArrayList<Bouquet>();

        for (Object prop : propertyNames)
        {
            if (prop.toString().contains("BOUQUET."))
            {
                this.bouquets.add(new Bouquet(props.getProperty(prop.toString())));
            }
        }
    }

    public String getChannelUrl()
    {
        return channelUrl;
    }

    public String getSidUrl()
    {
        return sidUrl;
    }

    public String getPosition()
    {
        return position;
    }

    public String getRegion()
    {
        return region;
    }

    public boolean isUseHd()
    {
        return useHd;
    }

    public boolean isUseHdForVideoGuard()
    {
        return useHdForVideoGuard;
    }

    public List<Bouquet> getBouquets()
    {
        return bouquets;
    }

    public String getExcludedBouquet()
    {
        return excludedBouquet;
    }
}
