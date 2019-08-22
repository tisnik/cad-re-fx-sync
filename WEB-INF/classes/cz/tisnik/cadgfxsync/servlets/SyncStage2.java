package cz.tisnik.cadgfxsync.servlets;

//import com.sap.conn.jco.JCo;
//import com.sap.conn.jco.JCoContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;

import cz.tisnik.cadgfxsync.sap.Helpers;
import cz.tisnik.cadgfxsync.sap.TSync;

public class SyncStage2 extends CustomHttpServlet
{

    static
    {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "10.4.66.60");
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "001");
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "ROZHRANI_T1");
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "ROP1PPL2");
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "cs");
        createDestinationDataFile(Helpers.ABAP_AS, "jcoDestination", connectProperties);

        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3");
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,    "10");
        createDestinationDataFile(Helpers.ABAP_AS_POOLED, "jcoDestination", connectProperties);

        //connectProperties.clear();
        //connectProperties.setProperty(DestinationDataProvider.JCO_MSHOST, "binmain");
        //connectProperties.setProperty(DestinationDataProvider.JCO_R3NAME,  "BIN");
        //connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "000");
        //connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "JCOTEST");
        //connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "JCOTEST");
        //connectProperties.setProperty(DestinationDataProvider.JCO_GROUP, "PUBLIC");
        //connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");
        //createDataFile(ABAP_MS, "jcoDestination", connectProperties);
    }

    static void createDestinationDataFile(String destinationName, String suffix, Properties connectProperties)
    {
        File destCfg = new File(destinationName + "." + suffix);
        try
        {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            connectProperties.store(fos, "for tests only !");
            fos.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }

    /**
     * Generated serial version ID
     */
    private static final long serialVersionUID = -6302426122899426170L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();

        String aoid = (String)servletContext.getAttribute("i_aoid");
        String valFrom = (String)servletContext.getAttribute("i_val_from");
        String test = (String)servletContext.getAttribute("i_test");
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
