package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.conn.jco.JCoException;

import cz.tisnik.cadgfxsync.sap.Helpers;
import cz.tisnik.cadgfxsync.sap.TSync;

public class SyncStage3 extends CustomHttpServlet
{

    /**
     * Generated serial version ID
     */
    private static final long serialVersionUID = -3611390629351119362L;

    @Override
    public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();

        String aoid = (String) servletContext.getAttribute("i_aoid");
        String valFrom = (String) servletContext.getAttribute("i_val_from");
        String test = " ";
        request.setAttribute("i_aoid", aoid);
        request.setAttribute("i_val_from", valFrom);
        request.setAttribute("i_test", test);
        List<TSync> tSync = Helpers.readTSync(servletContext);
        Helpers.logReadAttributes(this.log, aoid, valFrom, test, tSync);
        try
        {
            Helpers.sendDataToSap(this.log, aoid, valFrom, test, tSync, request);
            this.redirectTo(servletContext, request, response, "ok");
        }
        catch (JCoException e)
        {
            e.printStackTrace();
            request.setAttribute("errorString", e.getMessage());
            this.redirectTo(servletContext, request, response, "sapError");
        }
        this.log.logEnd("doProcess()");
    }
    
}
