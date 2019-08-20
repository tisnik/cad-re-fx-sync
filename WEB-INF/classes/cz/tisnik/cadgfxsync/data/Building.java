package cz.tisnik.cadgfxsync.data;

import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import cz.tisnik.cadgfxsync.Configuration;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class Building
{
    private String name;
    private Map<String, Floor> floors;

    public Building()
    {
        this.floors = new HashMap<String, Floor>();
    }

    public Building(String name)
    {
        this();
        this.name = name;
    }

    public void addFloor(String sapId, Floor floor)
    {
        this.floors.put(sapId, floor);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public Floor getFloor(String id)
    {
        return this.floors.get(id);
    }

    public Map<String, Floor> getFloors()
    {
        return this.floors;
    }

    public static String getBuildingAoid(HttpServletRequest request)
    {
        Configuration configuration = HttpUtils.getConfiguration(request);
        return configuration.getSelectedBuilding();
    }

    public static String getFloorAoid(HttpServletRequest request)
    {
        Configuration configuration = HttpUtils.getConfiguration(request);
        return configuration.getSelectedFloor();
    }

    public static String getFloorVariantAoid(HttpServletRequest request)
    {
        Configuration configuration = HttpUtils.getConfiguration(request);
        return configuration.getSelectedFloorVariant();
    }

    public static Building getBuilding(HttpServlet servlet, HttpServletRequest request)
    {
        DataModel dataModel = HttpUtils.getDataModel(servlet);
        Configuration configuration = HttpUtils.getConfiguration(request);
        String buildingId = configuration.getSelectedBuilding();
        return dataModel.getBuilding(buildingId);
    }

    public static Floor getFloor(HttpServlet servlet, HttpServletRequest request)
    {
        DataModel dataModel = HttpUtils.getDataModel(servlet);
        Configuration configuration = HttpUtils.getConfiguration(request);
        String buildingId = configuration.getSelectedBuilding();
        String floorId = configuration.getSelectedFloor();
        Building building = dataModel.getBuilding(buildingId);
        if (building == null)
        {
            return null;
        }
        return building.getFloor(floorId);
    }

    public static FloorVariant getFloorVariant(HttpServlet servlet, HttpServletRequest request)
    {
        DataModel dataModel = HttpUtils.getDataModel(servlet);
        Configuration configuration = HttpUtils.getConfiguration(request);
        String buildingId = configuration.getSelectedBuilding();
        String floorId = configuration.getSelectedFloor();
        String floorVariantId = configuration.getSelectedFloorVariant();
        Building building = dataModel.getBuilding(buildingId);
        if (building == null)
        {
            return null;
        }
        Floor floor = building.getFloor(floorId);
        if (floor == null)
        {
            return null;
        }
        return floor.getFloorVariant(floorVariantId);
    }

}

