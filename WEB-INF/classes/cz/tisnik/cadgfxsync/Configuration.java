package cz.tisnik.cadgfxsync;

import java.io.Serializable;

public class Configuration implements Serializable
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 4226157359581679713L;

    private String managementUnitId;
    private String buildingId;
    private String buildingAoid;
    private String selectedBuilding;
    private String selectedFloor;
    private String selectedFloorVariant;
    private String selectedRoom;

    public String getManagementUnitId()
    {
        return this.managementUnitId;
    }

    public void setManagementUnitId(String managementUnitId)
    {
        this.managementUnitId = managementUnitId;
    }

    public String getBuildingId()
    {
        return this.buildingId;
    }

    public void setBuildingId(String buildingId)
    {
        this.buildingId = buildingId;
    }

    public String getBuildingAoid()
    {
        return this.buildingAoid;
    }

    public void setBuildingAoid(String buildingAoid)
    {
        this.buildingAoid = buildingAoid;
    }

    public String getSelectedBuilding()
    {
        return selectedBuilding;
    }

    public void setSelectedBuilding(String selectedBuilding)
    {
        this.selectedBuilding = selectedBuilding;
    }

    public String getSelectedFloor()
    {
        return selectedFloor;
    }

    public void setSelectedFloor(String selectedFloor)
    {
        this.selectedFloor = selectedFloor;
    }

    public String getSelectedFloorVariant()
    {
        return selectedFloorVariant;
    }

    public void setSelectedFloorVariant(String selectedFloorVariant)
    {
        this.selectedFloorVariant = selectedFloorVariant;
    }

    public void setSelectedRoom(String room)
    {
        this.selectedRoom = room;
    }

    public String getSelectedRoom()
    {
        return selectedRoom;
    }

}
