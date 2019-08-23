package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.data.DataModel;
import cz.tisnik.cadgfxsync.data.DataModelInFiles;

public class SaveChanges extends CustomHttpServlet
{

    /**
     *  Generated serial version ID.
     */
    private static final long serialVersionUID = 8997393625096581384L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        DataModel dataModel = (DataModel) servletContext.getAttribute("data");
        new DataModelInFiles().writeDataModel(dataModel, DataModelInFiles.TEMP_DATA_FILE_NAME, DataModelInFiles.TEMP_BACKUP_DATA_FILE_NAME);

        String returnTo = request.getParameter("returnto");
        if ( returnTo != null)
        {
            servletContext.getRequestDispatcher("/jsp/" + returnTo + ".jsp").forward(request, response);
        }
        else
        {
            this.redirectTo(servletContext, request, response, "to");
        }
        this.log.logEnd("doProcess()");
    }

}
