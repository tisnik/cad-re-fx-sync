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
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class FloorInfo extends CustomHttpServlet
{

    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -7647989915203748095L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        String buildingId = request.getParameter("building");
        String floorId = request.getParameter("floor");
        this.log.logSet("buildingId", buildingId);
        this.log.logSet("floorId", floorId);

        setFloorInfo(request, buildingId, floorId);
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private void setFloorInfo(HttpServletRequest request, String buildingId, String floorId)
    {
        DataModel dataModel = HttpUtils.getDataModel(this);
        Building building = dataModel.getBuilding(buildingId);
        if (building == null)
        {
            request.setAttribute("building", "nenalezeno");
            request.setAttribute("buldingName", "nenalezeno");
            request.setAttribute("floor", "nenalezeno");
            request.setAttribute("floorName", "nenalezeno");
            request.setAttribute("variants", "0");
        }
        request.setAttribute("building", buildingId);
        request.setAttribute("buildingName", building.getName());
        Floor floor = building.getFloor(floorId);
        if (floor == null)
        {
            request.setAttribute("floor", "nenalezeno");
            request.setAttribute("floorName", "nenalezeno");
            request.setAttribute("variants", "0");
        }
        request.setAttribute("floor", floorId);
        request.setAttribute("floorName", floor.getName());
        Map<String, FloorVariant> variants = floor.getFloorVariants();
        StringBuffer out = new StringBuffer();
        for (Map.Entry<String, FloorVariant> variant : variants.entrySet())
        {
            out.append(String.format("<tr><td>%s</td><td>%s</td></tr>\n", variant.getKey(), variant.getValue()
                    .getDate()));
        }
        request.setAttribute("variants", out);
    }

}
