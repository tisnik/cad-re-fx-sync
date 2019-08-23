package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.Configuration;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class ShowFloorVariant extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 5638153875105958382L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        String building = request.getParameter("building");
        String floor = request.getParameter("floor");
        String variant = request.getParameter("variant");
        this.log.logSet("building", building);
        this.log.logSet("floor", floor);
        this.log.logSet("variant", variant);
        Configuration configuration = HttpUtils.getConfiguration(request);
        configuration.setSelectedBuilding(building);
        configuration.setSelectedFloor(floor);
        configuration.setSelectedFloorVariant(variant);
        HttpUtils.storeConfiguration(request, configuration);

        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

}
