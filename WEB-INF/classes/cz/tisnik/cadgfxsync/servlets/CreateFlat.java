package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.FloorVariant;

public class CreateFlat extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 6989051969112901581L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();

        createFlat(request);
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private void createFlat(HttpServletRequest request)
    {
        FloorVariant floorVariant = Building.getFloorVariant(this, request);
        String flatName = request.getParameter("flatName");
        if (floorVariant != null && flatName != null && !flatName.trim().isEmpty())
        {
            this.log.logSet("flat", flatName);
            floorVariant.addFlat(flatName);
        }
    }

}
