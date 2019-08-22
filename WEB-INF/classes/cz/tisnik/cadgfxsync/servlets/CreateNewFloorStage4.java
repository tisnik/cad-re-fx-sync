package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;
import java.util.Map.Entry;

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

public class CreateNewFloorStage4 extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 1646762634862563173L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        Configuration configuration = HttpUtils.getConfiguration(request);
        DataModel dataModel = HttpUtils.getDataModel(this);
        DataModel drawings = HttpUtils.getDrawings(this);
        String floorName = request.getParameter("name");
        String floorId = request.getParameter("id");
        String floorValid = request.getParameter("valid");
        String drawing = request.getParameter("drawing");
        String managementUnitId = configuration.getManagementUnitId();
        String buildingAoid = configuration.getBuildingAoid();
        logSettings(floorName, floorId, floorValid, drawing, managementUnitId, buildingAoid);
        request.setAttribute("log", createNewFloorVariant(dataModel, drawings, buildingAoid, floorName, floorId, floorValid, drawing));
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private void logSettings(String floorName, String floorId, String floorValid, String drawing, String managementUnitId, String buildingId)
    {
        this.log.logSet("management unit id", managementUnitId);
        this.log.logSet("building id", buildingId);
        this.log.logSet("floor name ", floorName);
        this.log.logSet("floor id", floorId);
        this.log.logSet("floor valid", floorValid);
        this.log.logSet("drawing", drawing);
    }

    private void doLog(StringBuffer out, String operation, String result)
    {
        out.append("<tr><td class='group'>" + operation + "</td><td>" + result + "</td></tr>\n");
    }

    private String createNewFloorVariant(DataModel dataModel, DataModel drawings, String buildingAoid, String floorName, String floorId, String floorValid, String drawing)
    {
        this.log.logBegin("createNewFloorVariant()");
        StringBuffer out = new StringBuffer();
        Building srcBuilding = drawings.getDefaultBuildingForDrawing();
        if (srcBuilding == null)
        {
            doLog(out, "Načtení výkresů z datového souboru", "chyba");
            return out.toString();
        }
        doLog(out, "Načtení výkresů z datového souboru", "ok");
        Floor srcFloor = srcBuilding.getFloor(drawing);
        if (srcFloor == null)
        {
            doLog(out, "Načtení výkresu patra " + drawing, "chyba");
            return out.toString();
        }
        doLog(out, "Načtení výkresu patra " + drawing, "ok");
        FloorVariant srcFloorVariant = srcFloor.getDefaultFloorVariantForDrawing();
        if (srcFloorVariant == null)
        {
            doLog(out, "Načtení varianty patra", "chyba");
            return out.toString();
        }
        doLog(out, "Načtení varianty patra", "ok");
        this.log.logEnd("createNewFloorVariant()");
        
        Building building = dataModel.getBuilding(buildingAoid);
        if (building == null)
        {
            doLog(out, "Načtení informací o budově", "budova neexistuje");
            building = new Building(buildingAoid);
            doLog(out, "Vytvoření budovy", "ok");
        }
        doLog(out, "Načtení informací o budově", "ok");
        Floor floor = new Floor(srcFloor);
        floor.setName(floorName);
        building.addFloor(floorId, floor);
        doLog(out, "Založení patra '" + floorName + "' (" + floorId + ")", "ok");
        FloorVariant dstFloorVariant = new FloorVariant();
        String floorVariantName = getFloorVariantName(floorValid);
        String floorVariantDate = getFloorVariantDate(floorValid);
        dstFloorVariant.setDate(floorVariantDate);
        this.log.logSet("floorVariantDate", floorVariantDate);
        this.log.logSet("floorVariantName", floorVariantName);
        floor.addFloorVariant(floorVariantName, dstFloorVariant);
        doLog(out, "Založení varianty '" + floorVariantName + "' (" + floorVariantDate + ")", "ok");
        int roomCount = 0;
        for (Entry<String, Room> room : srcFloorVariant.getRooms().entrySet())
        {
            dstFloorVariant.addRoom(room.getKey(), room.getValue());
            this.log.logSet("room", room.getKey() + "(" + room.getValue().getName()+")");
            roomCount++;
        }
        doLog(out, "Vytvoření místností", roomCount + " ok");
        return out.toString();
    }

    private String getFloorVariantDate(String floorValid)
    {
        String[] dateParts = floorValid.split("-");
        if (dateParts[0].charAt(0)=='0')
        {
            dateParts[0] = dateParts[0].substring(1);
        }
        if (dateParts[1].charAt(0)=='0')
        {
            dateParts[1] = dateParts[1].substring(1);
        }
        String floorVariantName = dateParts[0] + "." + dateParts[1] + "." + dateParts[2];
        return floorVariantName;
    }

    private String getFloorVariantName(String floorValid)
    {
        String[] dateParts = floorValid.split("-");
        if (dateParts[0].charAt(0)=='0')
        {
            dateParts[0] = dateParts[0].substring(1);
        }
        if (dateParts[1].charAt(0)=='0')
        {
            dateParts[1] = dateParts[1].substring(1);
        }
        String floorVariantName = dateParts[2]+dateParts[1]+dateParts[0];
        return floorVariantName;
    }

}
