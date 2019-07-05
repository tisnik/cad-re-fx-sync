package cz.tisnik.cadgfxsync.sap;

import java.util.ArrayList;
import java.util.List;

public class ManagementUnits
{
    private static List<SapObject> managementUnits = new ArrayList<SapObject>();
    static
    {
        managementUnits.add(new SapObject("I000100000097;112;;01HJ;Hospodářská jednotka;112;1.1.2007;;.;112;Olomoucký kraj;;;;"));
    }

    public static List<SapObject> getManementUnits()
    {
        return managementUnits;
    }
}
