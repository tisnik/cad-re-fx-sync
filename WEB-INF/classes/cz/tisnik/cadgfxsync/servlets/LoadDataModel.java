package cz.tisnik.cadgfxsync.servlets;

import java.awt.Polygon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.DataModel;
import cz.tisnik.cadgfxsync.data.DataModelInFiles;
import cz.tisnik.cadgfxsync.data.Floor;
import cz.tisnik.cadgfxsync.data.FloorVariant;
import cz.tisnik.cadgfxsync.data.Room;
import cz.tisnik.cadgfxsync.gfxentity.GfxArc;
import cz.tisnik.cadgfxsync.gfxentity.GfxCircle;
import cz.tisnik.cadgfxsync.gfxentity.GfxEntity;
import cz.tisnik.cadgfxsync.gfxentity.GfxLine;
import cz.tisnik.cadgfxsync.gfxentity.GfxText;

public class LoadDataModel extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -4590857196286556794L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        DataModelInFiles dmif = new DataModelInFiles();
        DataModel dataModel = dmif.readDataModel(servletContext, true, true);
        DataModel drawings = dmif.readDrawings(servletContext, true, true);
        servletContext.removeAttribute("drawings");
        servletContext.removeAttribute("data");
        servletContext.removeAttribute("initialized");
        servletContext.setAttribute("data", dataModel);
        servletContext.setAttribute("drawings", drawings);
        servletContext.setAttribute("initialized", true);
        request.setAttribute("aoid_list", renderAllObjects(dataModel));

        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private String renderAllObjects(DataModel dataModel)
    {
        StringBuffer out = new StringBuffer("");
        if (dataModel == null)
        {
            return "Nebyla nactena zadna data!";
        }
        if (dataModel.getBuildings() == null || dataModel.getBuildings().isEmpty())
        {
            return "V externím souboru nejsou uloženy žádné informace o budovách";
        }
        out.append("\n<ul class='mktree'>\n");
        for (Map.Entry<String, Building> building : dataModel.getBuildings().entrySet())
        {
            out.append("    <li class='liOpen'>"+building.getValue().getName()+ " <span class='objectid'>(" + building.getKey()+ ")</span>\n");
            out.append("    <ul>\n");
            List<String> floorIds = new ArrayList<String>(building.getValue().getFloors().keySet());
            Collections.sort(floorIds);
            for (String floorId : floorIds)
            {
                Floor floor = building.getValue().getFloor(floorId);
                out.append("        <li class='liOpen'>" + floor.getName() + " <span class='objectid'>(" + floorId + ")</span>\n");
                out.append("        <ul>\n");
                List<String> floorVariantIds = new ArrayList<String>(floor.getFloorVariants().keySet());
                Collections.sort(floorVariantIds);
                for (String floorVariantId : floorVariantIds)
                {
                    FloorVariant floorVariant = floor.getFloorVariant(floorVariantId);
                    String href=String.format("%s\n", floorVariant.getDate());
                    out.append("            <li class='liClosed'>" + href + " <span class='objectid'>(" + floorVariantId + ")</span>\n");
                    out.append("            <ul>\n");
                    int rooms = 0;
                    int lines = 0;
                    int circles = 0;
                    int arcs = 0;
                    int polygons = 0;
                    int points = 0;
                    int texts = 0;
                    for (Room selectedRoom : floorVariant.getRooms().values())
                    {
                        rooms++;
                        for (GfxEntity entity : selectedRoom.getEntities())
                        {
                            if (entity instanceof GfxLine)
                            {
                                lines++;
                            }
                            if (entity instanceof GfxCircle)
                            {
                                circles++;
                            }
                            if (entity instanceof GfxArc)
                            {
                                arcs++;
                            }
                            if (entity instanceof GfxText)
                            {
                                texts++;
                            }
                        }
                        polygons += selectedRoom.getPolygons().size();
                        for (Polygon polygon : selectedRoom.getPolygons())
                        {
                            points += polygon.npoints;
                        }
                    }
                    out.append("<li><span class='key'>Místností:</span> " + rooms);
                    out.append("<li><span class='key'>Úseček:</span> " + lines);
                    out.append("<li><span class='key'>Kružnic:</span> " + circles);
                    out.append("<li><span class='key'>Oblouků:</span> " + arcs);
                    out.append("<li><span class='key'>Textů:</span> " + texts);
                    out.append("<li><span class='key'>Polygonů:</span> " + polygons);
                    out.append("<li><span class='key'>Vrcholů polygonů:</span> " + points);
                    out.append("<li><span class='key'>Vrcholů/polygon:</span> " + ((float) points / polygons));
                    out.append("            </ul>\n");
                    out.append("            </li>\n");
                }
                out.append("        </ul>\n");
                out.append("        </li>\n");
            }
            out.append("    </ul>\n");
            out.append("    </li>\n");
        }
        out.append("</ul>\n");
        return out.toString();
    }

}
