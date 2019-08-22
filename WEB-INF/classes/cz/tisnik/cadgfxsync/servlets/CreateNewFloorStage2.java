package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.Configuration;
import cz.tisnik.cadgfxsync.sap.SapInterface;
import cz.tisnik.cadgfxsync.sap.SapObject;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class CreateNewFloorStage2 extends CustomHttpServlet
{

    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -1616830881100157292L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        String managementUnitId = request.getParameter("id");
        this.log.logSet("managementUnitId", managementUnitId);
        Configuration configuration = HttpUtils.getConfiguration(request);
        configuration.setManagementUnitId(managementUnitId);
        HttpUtils.storeConfiguration(request, configuration);

        request.setAttribute("selectedManagementUnit", renderSelectedManagementUnit(managementUnitId));
        request.setAttribute("out", renderAllObjects());
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
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

    private String renderAllObjects()
    {
        StringBuffer out = new StringBuffer();
        SapInterface sapInterface = new SapInterface();
        List<SapObject> buildings = sapInterface.readBuildings();
        for (SapObject object : buildings)
        {
            out.append("<tr>\n");
            out.append("<td><a href='CreateNewFloorStage3?id="+object.getIntreno()+"&aoid="+object.getAoid()+"'>"+object.getAoid()+"</a></td>");
            out.append("<td>"+_(object.getAoType())+"</td>");
            out.append("<td>"+_(object.getAoTypeText())+"</td>");
            out.append("<td>"+_(object.getValidFrom())+"</td>");
            out.append("<td>"+_(object.getValidTo())+"</td>");
            out.append("<td>"+_(object.getXao())+"</td>");
            out.append("<td>"+_(object.getCity())+"</td>");
            out.append("<td>"+_(object.getStreet())+"</td>");
            out.append("<td>"+_(object.getHouseNum())+"</td>");
            out.append("<td>"+_(object.getPostCode())+"</td>");
            out.append("<td><a href='javascript:void(0)' onClick='buildingInfoClick(\""+object.getIntreno()+"\")'>informace</td>");
            out.append("</tr>\n");
        }
        return out.toString();
    }

    private String _(String str)
    {
        return str == null ? "" : str;
    }
}
