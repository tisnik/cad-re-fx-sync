package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.FloorVariant;

public class RemoveFlat extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -8891411246352312420L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();

        removeFlat(request);
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private void removeFlat(HttpServletRequest request)
    {
        FloorVariant floorVariant = Building.getFloorVariant(this, request);
        String flatName = request.getParameter("flat");
        if (floorVariant != null && flatName != null)
        {
            this.log.logSet("flat", flatName);
            floorVariant.removeFlat(flatName);
        }
    }
}
