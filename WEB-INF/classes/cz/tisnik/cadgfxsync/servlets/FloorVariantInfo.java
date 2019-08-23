package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.DataModel;
import cz.tisnik.cadgfxsync.data.Floor;
import cz.tisnik.cadgfxsync.data.FloorVariant;
import cz.tisnik.cadgfxsync.data.Room;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class FloorVariantInfo extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -5166774551213922657L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        String buildingId = request.getParameter("building");
        String floorId = request.getParameter("floor");
        String floorVariantId = request.getParameter("variant");
        this.log.logSet("buildingId", buildingId);
        this.log.logSet("floorId", floorId);
        this.log.logSet("floorVariantId", floorVariantId);

        setFloorVariantInfo(request, buildingId, floorId, floorVariantId);
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private void setFloorVariantInfo(HttpServletRequest request, String buildingId, String floorId,
            String floorVariantId)
    {
        DataModel dataModel = HttpUtils.getDataModel(this);
        Building building = dataModel.getBuilding(buildingId);
        if (building == null)
        {
            request.setAttribute("building", "nenalezeno");
            request.setAttribute("buldingName", "nenalezeno");
            request.setAttribute("floor", "nenalezeno");
            request.setAttribute("floorName", "nenalezeno");
            request.setAttribute("variant", "nenalezeno");
            request.setAttribute("variantDate", "nenalezeno");
        }
        request.setAttribute("building", buildingId);
        request.setAttribute("buildingName", building.getName());
        Floor floor = building.getFloor(floorId);
        if (floor == null)
        {
            request.setAttribute("floor", "nenalezeno");
            request.setAttribute("floorName", "nenalezeno");
            request.setAttribute("variant", "nenalezeno");
            request.setAttribute("variantDate", "nenalezeno");
        }
        request.setAttribute("floor", floorId);
        request.setAttribute("floorName", floor.getName());
        FloorVariant floorVariant = floor.getFloorVariant(floorVariantId);
        request.setAttribute("variant", floorVariantId);
        request.setAttribute("variantDate", floorVariant.getDate());
        Map<String, Room> rooms = floorVariant.getRooms();
        StringBuffer out = new StringBuffer();
        for (Map.Entry<String, Room> room : rooms.entrySet())
        {
            out.append(String.format("<tr><td>%s</td><td>%s</td></tr>\n", room.getKey(), room.getValue().getName()));
        }
        request.setAttribute("rooms", out);
    }

}
