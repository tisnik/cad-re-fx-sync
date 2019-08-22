package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.sap.SapInterface;
import cz.tisnik.cadgfxsync.sap.SapObject;

public class CreateNewFloorStage1 extends CustomHttpServlet
{
    /**
     *  Generated serial version ID.
     */
    private static final long serialVersionUID = -2004505729004482291L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();

        request.setAttribute("out", renderAllObjects());
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private String renderAllObjects()
    {
        StringBuffer out = new StringBuffer();
        SapInterface sapInterface = new SapInterface();
        List<SapObject> managementUnits = sapInterface.readManagementUnits();
        for (SapObject object : managementUnits)
        {
            out.append("<tr>\n");
            out.append("<td><a href='CreateNewFloorStage2?id="+object.getIntreno()+"'>"+object.getAoid()+"</a></td>");
            out.append("<td>"+object.getAoType()+"</td>");
            out.append("<td>"+object.getAoTypeText()+"</td>");
            out.append("<td>"+object.getValidFrom()+"</td>");
            out.append("<td>"+object.getValidTo()+"</td>");
            out.append("<td>"+object.getXao()+"</td>");
            out.append("<td><a href='javascript:void(0)' onClick='managementUnitInfoClick(\""+object.getIntreno()+"\")'>informace</td>");
            out.append("</tr>\n");
        }
        return out.toString();
    }

}
