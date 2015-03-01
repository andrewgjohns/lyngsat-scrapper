package org.andrew.scrapper.lyngsat.models;

/**
 * Created by Andrew on 01/03/15.
 */
public class Bouquet
{
    private String name;
    private int startNumber;
    private int endNumber;

    public Bouquet(String propertyValue)
    {
        String[] values = propertyValue.split(",");
        this.name = values[0];
        this.startNumber = Integer.parseInt(values[1]);
        this.endNumber = Integer.parseInt(values[2]);
    }

    public String getName()
    {
        return name;
    }

    public int getStartNumber()
    {
        return startNumber;
    }

    public int getEndNumber()
    {
        return endNumber;
    }
}
