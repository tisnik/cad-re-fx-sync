package cz.tisnik.cadgfxsync.sap;

import java.util.List;

public class SapInterface
{

    public List<SapObject> readManagementUnits()
    {
        return ManagementUnits.getManementUnits();
    }

    public List<SapObject> readBuildings()
    {
        return Buildings.getBuildings();
    }

    public SapObject getSapObject(String intreno, List<SapObject> list)
    {
        if (intreno == null)
        {
            return null;
        }
        for (SapObject sapObject : list)
        {
            if (intreno.equals(sapObject.getIntreno()))
            {
                return sapObject;
            }
        }
        return null;
    }

    public SapObject getManagementUnit(String intreno)
    {
        return getSapObject(intreno, readManagementUnits());
    }

    public SapObject getBuilding(String intreno)
    {
        return getSapObject(intreno, readBuildings());
    }
}
