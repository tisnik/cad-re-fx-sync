package cz.tisnik.cadgfxsync.gfxentity;

import java.awt.Graphics2D;

import cz.tisnik.cadgfxsync.utils.MathCommon;

public class GfxText implements GfxEntity
{
    private int x;
    private int y;
    private int original_y;
    private String text;
    private GfxEntityAttribute attribute;

    public GfxText()
    {
        this(0, 0, "", GfxEntityAttribute.UNKNOWN);
    }

    public GfxText(int x, int y, String text, GfxEntityAttribute attribute)
    {
        this.x = x;
        this.y = y;
        this.text = text;
        this.attribute = attribute;
    }

    public GfxText(String value, double defaultFontSize)
    {
        String[] values = value.trim().split("\\s+");
        try
        {
            this.x = MathCommon.doubleStr2int(values[0]);
            this.y = MathCommon.doubleStr2int(values[1]);
            this.original_y = this.y;
            if (defaultFontSize>0.0)
            {
                this.y += (int)Math.round(defaultFontSize);
            }
            this.text = value.substring(1 + value.indexOf('"'), value.lastIndexOf('"'));
            if (values.length > 3)
            {
                this.attribute = GfxEntityAttribute.resolveGfxEntityAttribute(values[3]);
            }
        }
        catch (Exception e)
        {
            System.out.println("***"+value+"***");
            e.printStackTrace();
        }
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getOriginalY()
    {
        return this.original_y;
    }

    public String getText()
    {
        return this.text;
    }

    public GfxEntityAttribute getAttribute()
    {
        return attribute;
    }

    public String toString()
    {
        if (this.attribute != null && this.attribute.toString() != null)
        {
            return String.format("text %3d %3d \"%s\" %s", this.getX(), this.getOriginalY(), this.getText(), this.attribute.toString());
        }
        else
        {
            return String.format("text %3d %3d \"%s\"", this.getX(), this.getOriginalY(), this.getText());
        }
    }

    public void draw(Graphics2D gc)
    {
        gc.drawString(this.getText(), this.getX(), this.getY());
    }
}
