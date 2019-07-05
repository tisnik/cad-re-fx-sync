package cz.tisnik.cadgfxsync.sap;

public class SapObject
{
    private String intreno;
    private String aoid;
    private String aoTypeExt;
    private String aoType;
    private String aoTypeText;
    private String aoNr;
    private String validFrom;
    private String validTo;
    private String partSeparator;
    private String partAoid;
    private String xao;
    private String city;
    private String postCode;
    private String street;
    private String houseNum;

    public SapObject()
    {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public SapObject(String str)
    {
        String[] parts = str.split(";");
        this.setIntreno(getPart(parts, 0));
        this.setAoid(getPart(parts, 1));
        this.setAoTypeExt(getPart(parts, 2));
        this.setAoType(getPart(parts, 3));
        this.setAoTypeText(getPart(parts, 4));
        this.setAoNr(getPart(parts, 5));
        this.setValidFrom(getPart(parts, 6));
        this.setValidTo(getPart(parts, 7));
        this.setPartSeparator(getPart(parts, 8));
        this.setPartAoid(getPart(parts, 9));
        this.setXao(getPart(parts, 10));
        this.setCity(getPart(parts, 11));
        this.setPostCode(getPart(parts, 12));
        this.setStreet(getPart(parts, 13));
        this.setHouseNum(getPart(parts, 14));
    }

    public SapObject(String aoTypeExt, String aoType, String aoTypeText, String aoNr, String validFrom, String validTo,
            String partSeparator, String partAoid, String xao, String city, String postCode, String street,
            String houseNum)
    {
        super();
        this.aoTypeExt = aoTypeExt;
        this.aoType = aoType;
        this.aoTypeText = aoTypeText;
        this.aoNr = aoNr;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.partSeparator = partSeparator;
        this.partAoid = partAoid;
        this.xao = xao;
        this.city = city;
        this.postCode = postCode;
        this.street = street;
        this.houseNum = houseNum;
    }

    private String getPart(String[] parts, int index)
    {
        return index < parts.length ? parts[index] : null;
    }

    public String getIntreno()
    {
        return this.intreno;
    }

    public void setIntreno(String intreno)
    {
        this.intreno = intreno;
    }

    public String getAoid()
    {
        return this.aoid;
    }

    public void setAoid(String aoid)
    {
        this.aoid = aoid;
    }

    public String getAoTypeExt()
    {
        return this.aoTypeExt;
    }

    public void setAoTypeExt(String aoTypeExt)
    {
        this.aoTypeExt = aoTypeExt;
    }

    public String getAoType()
    {
        return this.aoType;
    }

    public void setAoType(String aoType)
    {
        this.aoType = aoType;
    }

    public String getAoTypeText()
    {
        return this.aoTypeText;
    }

    public void setAoTypeText(String aoTypeText)
    {
        this.aoTypeText = aoTypeText;
    }

    public String getAoNr()
    {
        return this.aoNr;
    }

    public void setAoNr(String aoNr)
    {
        this.aoNr = aoNr;
    }

    public String getValidFrom()
    {
        return this.validFrom;
    }

    public void setValidFrom(String validFrom)
    {
        this.validFrom = validFrom;
    }

    public String getValidTo()
    {
        return this.validTo;
    }

    public void setValidTo(String validTo)
    {
        this.validTo = validTo;
    }

    public String getPartSeparator()
    {
        return partSeparator;
    }

    public void setPartSeparator(String partSeparator)
    {
        this.partSeparator = partSeparator;
    }

    public String getPartAoid()
    {
        return this.partAoid;
    }

    public void setPartAoid(String partAoid)
    {
        this.partAoid = partAoid;
    }

    public String getXao()
    {
        return this.xao;
    }

    public void setXao(String xao)
    {
        this.xao = xao;
    }

    public String getCity()
    {
        return this.city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getPostCode()
    {
        return this.postCode;
    }

    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    public String getStreet()
    {
        return this.street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getHouseNum()
    {
        return this.houseNum;
    }

    public void setHouseNum(String houseNum)
    {
        this.houseNum = houseNum;
    }
}
