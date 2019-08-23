package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.Configuration;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class Drawing extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 1L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        String rnd = request.getParameter("rnd");
        String id = request.getParameter("id");
        this.log.logSet("rnd", rnd);
        this.log.logSet("id", id);
        Configuration configuration = HttpUtils.getConfiguration(request);
        configuration.setSelectedRoom(id);
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

}
