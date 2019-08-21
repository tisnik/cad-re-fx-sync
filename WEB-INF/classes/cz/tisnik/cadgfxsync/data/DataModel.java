package cz.tisnik.cadgfxsync.data;

import java.util.*;

public class DataModel
{
    private Map<String, Building> buildings;
    private RoomTypes roomTypes;
    private Colors colors;

    public DataModel()
    {
        this.buildings = new HashMap<String, Building>();
    }

    public Colors getColors()
    {
        return colors;
    }

    public void setColors(Colors colors)
    {
        this.colors = colors;
    }

    public RoomTypes getRoomTypes()
    {
        return roomTypes;
    }

    public void setRoomTypes(RoomTypes roomTypes)
    {
        this.roomTypes = roomTypes;
    }

    public void addBuilding(String sapId, Building building)
    {
        this.buildings.put(sapId, building);
    }

    public Building getBuilding(String buildingId)
    {
        return this.buildings.get(buildingId);
    }

    public Map<String, Building> getBuildings()
    {
        return this.buildings;
    }

    public Building getDefaultBuildingForDrawing()
    {
        return this.buildings.get("Drawings");
    }
}
