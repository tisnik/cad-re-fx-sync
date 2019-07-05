package cz.tisnik.cadgfxsync.sap;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

import cz.tisnik.cadgfxsync.utils.Log;

public class Helpers
{
    public static String ABAP_AS = "ABAP_AS_WITHOUT_POOL";
    public static String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";
    public static String ABAP_MS = "ABAP_MS_WITHOUT_POOL";

    @SuppressWarnings("unchecked")
    public static List<TSync> readTSync(ServletContext servletContext)
    {
        return (List<TSync>)servletContext.getAttribute("i_table");
    }

    public static void logReadAttributes(Log log, String aoid, String valFrom, String test, List<TSync> tSync)
    {
        log.logSet("aoid", aoid);
        log.logSet("valFrom", valFrom);
        log.logSet("test", test);
        if (tSync != null)
        {
            for (TSync sync : tSync)
            {
                log.logSet("tSync", sync.toString());
            }
        }
        else
        {
            log.logSet("tSync", null);
        }
    }

    public static void sendDataToSap(Log log, String aoid, String valFrom, String test, List<TSync> tSync,
            HttpServletRequest request) throws JCoException
    {
        log.log("getting destination for SAP call");
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        log.log("getting function Z_CAD_SYNC");
        JCoFunction function = destination.getRepository().getFunction("Z_CAD_SYNC");
        if (function == null)
        {
            throw new RuntimeException("Z_CAD_SYNC not found in SAP.");
        }

        // JCoTable table = new JCo JCoTable();

        JCoParameterList parameterList = function.getImportParameterList();
        parameterList.setValue("I_AOID", aoid);
        parameterList.setValue("I_VAL_FROM", valFrom);
        parameterList.setValue("I_TEST", test);
        // parameterList.setValue("T_SYNC", table);
        JCoTable saptsync = function.getTableParameterList().getTable("T_SYNC");
        JCoMetaData metadata = saptsync.getMetaData();
        int cnt = metadata.getFieldCount();
        for (int i = 0; i < cnt; i++)
        {
            log.logSet("metadata: " + i, metadata.getName(i));
        }
        for (TSync sync : tSync)
        {
            saptsync.appendRow();
            saptsync.setValue(0, sync.aoLevel);
            saptsync.setValue(1, sync.aoTypeExt);
            saptsync.setValue(2, sync.aoNr);
            saptsync.setValue(3, sync.aoId);
            saptsync.setValue(4, sync.aoFunction);
            saptsync.setValue(5, sync.xao);
            saptsync.setValue(6, sync.area);
            saptsync.setValue(7, sync.chngInd);
        }

        function.execute(destination);

        StringBuffer message = new StringBuffer();
        JCoTable ret = function.getTableParameterList().getTable("T_RET");
        for (int i = 0; i < ret.getNumRows(); i++)
        {
            ret.setRow(i);
            String type = ret.getString("TYPE");
            String color = null;
            switch (type.length() > 0 ? type.charAt(0) : ' ')
            {
            case 'S':
            case 's':
                color = "00a000";
                break;
            case 'E':
            case 'e':
                color = "a00000";
                break;
            case 'W':
            case 'w':
                color = "a000a0";
                break;
            case 'I':
            case 'i':
                color = "0000b0";
                break;
            case 'A':
            case 'a':
                color = "a00000";
                break;
            }

            if (color == null)
            {
                message.append("<p>" + ret.getString("MESSAGE") + "</p>");
            }
            else
            {
                message.append("<p style='color:#" + color + "'>" + ret.getString("MESSAGE") + "</p>");
            }
            System.out.println(ret.getString("MESSAGE"));
        }
        request.setAttribute("message", message.toString());

        // JCoStructure returnStructure =
        // function.getExportParameterList().getStructure("T_RET");
        // if (!
        // (returnStructure.getString("TYPE").equals("")||returnStructure.getString("TYPE").equals("S"))
        // )
        // {
        // throw new RuntimeException(returnStructure.getString("MESSAGE"));
        // }

        StringBuffer out = new StringBuffer();
        JCoTable retsync = function.getTableParameterList().getTable("T_SYNC");
        for (int i = 0; i < retsync.getNumRows(); i++)
        {
            retsync.setRow(i);
            log.log(retsync.getString("AOTYPE_EXT") + '\t' + retsync.getString("XAO"));
            out.append("<tr>");
            String aolevel = retsync.getString("AOLEVEL");
            if (aolevel.length() < 2)
            {
                aolevel = "0" + aolevel;
            }
            log.log(aolevel);
            out.append("<td>" + aolevel + "</td>");
            out.append(item(retsync.getString("AOTYPE_EXT")));
            out.append(item(retsync.getString("AONR")));
            out.append(item(retsync.getString("AOID")));
            out.append(item(retsync.getString("AOFUNCTION")));
            out.append(item(retsync.getString("XAO")));
            out.append(item(retsync.getString("FL_AREA")));
            out.append(item(retsync.getString("CHNGIND")));
            out.append(item(retsync.getString("PARENT")));
            out.append("<tr>\n");
        }
        request.setAttribute("table", out.toString());
    }

    private static String item(Object o)
    {
        if (o == null)
        {
            return "<td>&nbsp;</td>";
        }
        else
        {
            return "<td>" + o.toString() + "</td>";
        }
    }

}
