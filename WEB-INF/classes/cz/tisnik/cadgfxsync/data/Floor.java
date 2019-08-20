package cz.tisnik.cadgfxsync.data;

import java.util.*;

public class Floor
{
    private Map<String, FloorVariant> floorVariants;
    private String name;
    private Integer area;
    private double defaultXpos;
    private double defaultYpos;
    private double defaultScale;
    private double defaultFontSize;

    public Floor()
    {
        this.defaultXpos = 0.0;
        this.defaultYpos = 0.0;
        this.defaultScale = 1.0;
        this.defaultFontSize = 1.0;
        this.floorVariants = new HashMap<String, FloorVariant>();
    }

    public Floor(String name, Integer area)
    {
        this();
        this.name = name;
        this.area = area;
    }

    public Floor(Floor srcFloor)
    {
        this.name = srcFloor.name;
        this.area = srcFloor.area;
        this.defaultScale = srcFloor.defaultScale;
        this.defaultXpos = srcFloor.defaultXpos;
        this.defaultYpos = srcFloor.defaultYpos;
        this.defaultFontSize = srcFloor.defaultFontSize;
        this.floorVariants = new HashMap<String, FloorVariant>();
    }

    public void addFloorVariant(String name, FloorVariant floorVariant)
    {
        this.floorVariants.put(name, floorVariant);
    }

    public String getName()
    {
        return this.name;
    }

    public Integer getArea()
    {
        return this.area;
    }

    public Map<String, FloorVariant> getFloorVariants()
    {
        return this.floorVariants;
    }

    public FloorVariant getDefaultFloorVariantForDrawing()
    {
        return this.getFloorVariant("default");
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setArea(Integer area)
    {
        this.area = area;
    }

    public FloorVariant getFloorVariant(String floorVariantId)
    {
        return this.floorVariants.get(floorVariantId);
    }

    public double getDefaultXpos()
    {
        return this.defaultXpos;
    }

    public void setDefaultXpos(double defaultXpos)
    {
        this.defaultXpos = defaultXpos;
    }

    public double getDefaultYpos()
    {
        return this.defaultYpos;
    }

    public void setDefaultYpos(double defaultYpos)
    {
        this.defaultYpos = defaultYpos;
    }

    public double getDefaultScale()
    {
        return this.defaultScale;
    }

    public void setDefaultScale(double defaultScale)
    {
        this.defaultScale = defaultScale;
    }

    public double getDefaultFontSize()
    {
        return this.defaultFontSize;
    }

    public void setDefaultFontSize(double defaultFontSize)
    {
        this.defaultFontSize = defaultFontSize;
    }
}
