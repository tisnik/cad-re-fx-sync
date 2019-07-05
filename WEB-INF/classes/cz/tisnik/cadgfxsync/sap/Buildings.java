package cz.tisnik.cadgfxsync.sap;

import java.util.ArrayList;
import java.util.List;

public class Buildings
{
    private static List<SapObject> buildings = new ArrayList<SapObject>();
    static
    {
        buildings.add(new SapObject("I000100000402;112100252;B;03BU;Budova;100252;1.1.2007;;.;100252;Branná - č. 252;Branná;788 25;;252"));
        buildings.add(new SapObject("I000100000488;112.00001;B;03BU;Budova;1;;;.;1;;;;;"));
        buildings.add(new SapObject("I000100000489;112.00002;B;03BU;Budova;2;1.1.2007;;.;2;;;;;"));
        buildings.add(new SapObject("I000100000490;112.00020;B;03BU;Budova;20;;;.;20;Budova 20;;;;"));
        buildings.add(new SapObject("I000100000211;112100000;B;03BU;Budova;100000;1.1.2007;;.;100000;Přerov 653;Branná 653;788 25;;"));
        buildings.add(new SapObject("I000100000232;112100055;B;03BU;Budova;100055;1.1.2007;;.;100055;Olomouc č.68;Olomouc;772 58;Nerudova ul.; *")); 
    }

    public static List<SapObject> getBuildings()
    {
        return buildings;
    }
}
