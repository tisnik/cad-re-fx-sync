package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.Configuration;
import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.DataModel;
import cz.tisnik.cadgfxsync.data.Floor;
import cz.tisnik.cadgfxsync.data.FloorVariant;
import cz.tisnik.cadgfxsync.sap.SapInterface;
import cz.tisnik.cadgfxsync.sap.SapObject;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class CreateNewFloorStage3 extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -8625146314575311411L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        String buildingId = request.getParameter("id");
        String buildingAoid = request.getParameter("aoid");
        this.log.logSet("buildingId", buildingId);
        this.log.logSet("buildingAoid", buildingAoid);
        Configuration configuration = HttpUtils.getConfiguration(request);
        configuration.setBuildingId(buildingId);
        configuration.setBuildingAoid(buildingAoid);
        String managementUnitId = configuration.getManagementUnitId();
        this.log.logSet("managementUnitId", managementUnitId);
        HttpUtils.storeConfiguration(request, configuration);
        DataModel dataModel = HttpUtils.getDataModel(this);
        DataModel drawings = HttpUtils.getDrawings(this);

        request.setAttribute("selectedManagementUnit", renderSelectedManagementUnit(managementUnitId));
        request.setAttribute("selectedBuilding", renderSelectedBuilding(buildingId));
        request.setAttribute("existingFloors", renderExistingFloors(dataModel, buildingId));
        request.setAttribute("drawingList", renderDrawings(drawings));
        request.setAttribute("existingVariants", renderExistingVariants(dataModel, buildingId));
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private String renderDrawings(DataModel drawings)
    {
        this.log.logBegin("renderDrawings");
        StringBuffer out = new StringBuffer();
        Building building = drawings.getDefaultBuildingForDrawing();
        if (building == null)
        {
            return "žádné výkresy nebyly nalezeny";
        }
        Map<String, Floor> floors = building.getFloors();
        Set<String> floorNames = new TreeSet<String>(floors.keySet());
        for (String floorName : floorNames)
        {
            this.log.logSet("floor", floorName);
            out.append("<option value='" + floorName + "'>" + floorName + "</option>\n");
        }
        this.log.logSet("out", out.toString());
        this.log.logEnd("renderDrawings");
        return out.toString();
    }

    private String renderExistingVariants(DataModel dataModel, String intreno)
    {
        this.log.logBegin("renderExistingVariants " + intreno);
        SapInterface sapInterface = new SapInterface();
        List<SapObject> buildings = sapInterface.readBuildings();
        String buildingId = null;
        for (SapObject building : buildings)
        {
            if (intreno.equals(building.getIntreno()))
            {
                buildingId = building.getAoid();
                break;
            }
        }
        Building building = dataModel.getBuildings().get(buildingId);
        if (building == null)
        {
            this.log.logEnd("renderExistingVariants no data");
            return "";
        }
        StringBuffer out = new StringBuffer();
        List<String> floorIds = new ArrayList<String>(building.getFloors().keySet());
        boolean first = true;
        for (String floorId : floorIds)
        {
            Floor floor = building.getFloor(floorId);
            List<String> floorVariantIds = new ArrayList<String>(floor.getFloorVariants().keySet());
            this.log.logBegin("floor: " + floorId);
            for (String floorVariantId : floorVariantIds)
            {
                this.log.logSet("variant", floorVariantId);
                if (!first)
                {
                    out.append(",\n");
                }
                FloorVariant floorVariant = floor.getFloorVariant(floorVariantId);
                String date = floorVariant.getDate();
                date = date.replace('.', '-');
                String[] parts = date.split("-");
                if (parts[0].length() == 1)
                {
                    parts[0] = "0" + parts[0];
                }
                if (parts[1].length() == 1)
                {
                    parts[1] = "0" + parts[1];
                }
                date = String.format("%s-%s-%s", parts[0], parts[1], parts[2]);
                out.append(String.format("    ['%s', '%s']", floorId, date));
                first = false;
            }
            this.log.logEnd("floor");
        }
        this.log.logSet("out", out.toString());
        this.log.logEnd("renderExistingVariants");
        return out.toString();
    }

    private String renderExistingFloors(DataModel dataModel, String intreno)
    {
        SapInterface sapInterface = new SapInterface();
        List<SapObject> buildings = sapInterface.readBuildings();
        String buildingId = null;
        for (SapObject building : buildings)
        {
            if (intreno.equals(building.getIntreno()))
            {
                buildingId = building.getAoid();
                break;
            }
        }
        Building building = dataModel.getBuildings().get(buildingId);
        if (building == null)
        {
            return "<tr><td colspan='4'>žádná patra prozatím nebyla v této budově vytvořena</td></tr>";
        }
        StringBuffer out = new StringBuffer();
        List<String> floorIds = new ArrayList<String>(building.getFloors().keySet());
        Collections.sort(floorIds);
        for (String floorId : floorIds)
        {
            Floor floor = building.getFloor(floorId);
            List<String> floorVariantIds = new ArrayList<String>(floor.getFloorVariants().keySet());
            Collections.sort(floorVariantIds);
            for (String floorVariantId : floorVariantIds)
            {
                FloorVariant floorVariant = floor.getFloorVariant(floorVariantId);
                out.append(String.format("<tr><td><a href='javascript:void(0)' onClick='floorInfoClick(\"%s\", \"%s\")'>%s (informace)</a></td><td class='objectid'>%s</td><td>%s</td><td class='objectid'><a href='javascript:void(0)' onClick='floorVariantInfoClick(\"%s\", \"%s\", \"%s\")'>informace o variantě</a></td></tr>\n", buildingId, floorId, floor.getName(), floorId, floorVariant.getDate(), buildingId, floorId, floorVariantId));
            }
        }
        return out.toString();
    }

    private String renderSelectedManagementUnit(String managementUnitId)
    {
        SapInterface sapInterface = new SapInterface();
        SapObject managementUnit = sapInterface.getManagementUnit(managementUnitId);
        if (managementUnit == null)
        {
            return "nelze načíst informace o hospodářské jednotce " + managementUnitId;
        }
        else
        {
            return managementUnit.getAoid() + "  " + managementUnit.getAoTypeText() + "  " + managementUnit.getXao();
        }
    }

    private String renderSelectedBuilding(String buildingId)
    {
        SapInterface sapInterface = new SapInterface();
        SapObject building = sapInterface.getBuilding(buildingId);
        if (building == null)
        {
            return "nelze načíst informace o budově " + buildingId;
        }
        else
        {
            StringBuffer out = new StringBuffer();
            if (building.getAoid() != null)
            {
                out.append(building.getAoid());
            }
            if (building.getAoTypeText() != null)
            {
                out.append("  ");
                out.append(building.getAoTypeText());
            }
            if (building.getCity() != null)
            {
                out.append("  ");
                out.append(building.getCity());
            }
            if (building.getCity() != null)
            {
                out.append("  ");
                out.append(building.getCity());
            }
            if (building.getStreet() != null)
            {
                out.append(", ");
                out.append(building.getStreet());
            }
            if (building.getHouseNum() != null)
            {
                out.append(", ");
                out.append(building.getHouseNum());
            }
            if (building.getPostCode() != null)
            {
                out.append(", ");
                out.append(building.getPostCode());
            }
            return out.toString();
        }
    }

}
