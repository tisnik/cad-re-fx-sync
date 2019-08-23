package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.Configuration;
import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.DataModel;
import cz.tisnik.cadgfxsync.data.Floor;
import cz.tisnik.cadgfxsync.data.FloorVariant;
import cz.tisnik.cadgfxsync.data.Room;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class SaveRoomInfo extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 1L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        DataModel dataModel = HttpUtils.getDataModel(this);
        Configuration configuration = HttpUtils.getConfiguration(request);
        changeRoomInfo(request, dataModel, configuration);
        request.setAttribute("roomId", request.getParameter("roomId"));
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private void changeRoomInfo(HttpServletRequest request, DataModel dataModel, Configuration configuration)
    {
        String buildingId = configuration.getSelectedBuilding();
        String floorId = configuration.getSelectedFloor();
        String floorVariantId = configuration.getSelectedFloorVariant();
        String roomId = request.getParameter("roomId");
        String name = request.getParameter("name");
        String area = request.getParameter("area");
        String capacity = request.getParameter("capacity");
        String free = request.getParameter("free");
        String type = request.getParameter("roomType");
        String flat = request.getParameter("flat");
        this.log.logSet("building", buildingId);
        this.log.logSet("floorId", floorId);
        this.log.logSet("floorVariantId", floorVariantId);
        this.log.logSet("roomId", roomId);
        this.log.logSet("name", name);
        this.log.logSet("area", area);
        this.log.logSet("capacity", capacity);
        this.log.logSet("free", free);
        this.log.logSet("type", type);
        this.log.logSet("flat", flat);

        Building building = dataModel.getBuilding(buildingId);

        if (building == null)
        {
            return;
        }
        Floor floor = building.getFloor(floorId);
        if (floor == null)
        {
            return;
        }
        FloorVariant floorVariant = floor.getFloorVariant(floorVariantId);
        if (floorVariant == null)
        {
            return;
        }
        Room room = floorVariant.getRoom(roomId);
        if (room != null)
        {
            room.setName(name);
            Float roomArea = toFloat(area);
            room.setArea(roomArea == null ? null : Math.round(roomArea));
            room.setCapacity(toInt(capacity));
            room.setFree(toInt(free));
            room.setType(toInt(type));
            if (flat != null && !flat.trim().isEmpty())
            {
                room.setFlat(flat);
            }
        }
        /*
        if (!oldRoomId.equals(newRoomId))
        {
            Room room = floorVariant.getRoom(oldRoomId);
            floorVariant.addRoom(newRoomId, room);
            floorVariant.removeRoom(oldRoomId);
        }*/
    }

    private Float toFloat(String string)
    {
        try
        {
            return Float.parseFloat(string);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    private Integer toInt(String string)
    {
        try
        {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

}
