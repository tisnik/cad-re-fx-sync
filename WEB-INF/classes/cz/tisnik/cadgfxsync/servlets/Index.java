package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import cz.tisnik.cadgfx.Configuration;
import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.DataModel;
import cz.tisnik.cadgfxsync.data.Floor;
import cz.tisnik.cadgfxsync.data.FloorVariant;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class Index extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 197073842801485910L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        DataModel dataModel = HttpUtils.getDataModel(this);

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
            return "V datech nejsou ulozeny zadne informace o budovach";
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
                    String href=String.format("<a href='ShowFloorVariant?building=%s&floor=%s&variant=%s'>%s</a>\n", building.getKey(), floorId, floorVariantId, floorVariant.getDate());
                    out.append("            <li>" + href + " <span class='objectid'>(" + floorVariantId + ")</span></li>\n");
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

    /*
    private void renderSelectedObject(DataModel dataModel, Configuration configuration, StringBuffer out)
    {
        Building building = dataModel.getBuilding(configuration.building);
        Floor floor = building == null ? null : building.getFloors().get(configuration.floor);
        FloorVariant floorVariant = floor == null ? null :floor.getFloorVariants().get(configuration.floorVariant);
        //Room room = floorVariant == null ? null : floorVariant.getRooms().get(configuration.room);

        out.append("<span style='color:#800'>Budova: </span>");
        if (building != null)
        {
            out.append("<span style='color:#000'>"+building.getName()+"&nbsp;</span>\n");
        }
        else
        {
            out.append("<span style='color:#f00'>nenalezeno</span>\n");
        }

        out.append("<span style='color:#800'>Podlaží: </span>");
        if (floor != null)
        {
            out.append("<span style='color:#000'>"+floor.getName()+"&nbsp;</span>\n");
        }
        else
        {
            out.append("<span style='color:#f00'>nenalezeno</span>\n");
        }

        out.append("<span style='color:#800'>Varianta: </span>");
        if (floorVariant != null)
        {
            out.append("<span style='color:#000'>"+configuration.floorVariant+" ("+floorVariant.getDate()+")&nbsp;</span>\n");
        }
        else
        {
            out.append("<span style='color:#f00'>nenalezeno</span>\n");
        }
    }
*/
}

