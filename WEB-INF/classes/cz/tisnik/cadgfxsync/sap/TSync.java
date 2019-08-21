package cz.tisnik.cadgfxsync.sap;

public class TSync
{
    public int     aoLevel;
    public char    aoTypeExt;
    public String  aoNr;
    public String  aoId;
    public String  aoFunction;
    public String  xao;
    public Integer area;
    public char    chngInd;
    public String  parent;

    public TSync(int aoLevel, char aoTypeExt, String aoNr, String aoId, String aoFunction, String xao, Integer area,
            char chngInd, String parent)
    {
        super();
        this.aoLevel = aoLevel;
        this.aoTypeExt = aoTypeExt;
        this.aoNr = aoNr;
        this.aoId = aoId;
        this.aoFunction = aoFunction;
        this.xao = xao;
        this.area = area;
        this.chngInd = chngInd;
        this.parent = parent;
    }

    public TSync(int aoLevel, char aoTypeExt, String aoNr, String aoId, String aoFunction, String xao, Integer area)
    {
        super();
        this.aoLevel = aoLevel;
        this.aoTypeExt = aoTypeExt;
        this.aoNr = aoNr;
        this.aoId = aoId;
        this.aoFunction = aoFunction;
        this.xao = xao;
        this.area = area;
        this.chngInd = ' ';
        this.parent = null;
    }

    public String toString()
    {
        return String.format("%d %c %s %s %s", this.aoLevel, this.aoTypeExt, this.aoNr, this.aoId, this.xao);
    }
}
