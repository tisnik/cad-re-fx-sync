package cz.tisnik.cadgfxsync;

import java.util.HashMap;
import java.util.Map;

import cz.tisnik.cadgfxsync.utils.Log;

public class Controller
{
    /**
     * Instance of class used for output log information either to the standard
     * output or to log file whose name is specified in web.xml.
     */
    private static final Log log = new Log( Controller.class.getName() );

    private static final char SEPARATION_CHAR = '#';

    /**
     * This map contains redirection URLs. There could exists more redirections
     * for each servlet because class name of the servlet is concatenated with
     * it's status code(s). Servlet name must be separated by "#" from status code.
     */
    private static final Map<String, String> redirectionUrls = new HashMap<String, String>();

    static
    {
        redirectionUrls.put("Index#ok", "/jsp/Index.jsp");
        redirectionUrls.put("BuildingInfo#ok", "/jsp/BuildingInfo.jsp");
        redirectionUrls.put("Commands#ok", "/jsp/Commands.jsp");
        redirectionUrls.put("SyncStage1#ok", "/jsp/SyncStage1.jsp" );
        redirectionUrls.put("SyncStage2#ok", "/jsp/SyncStage2.jsp" );
        redirectionUrls.put("SyncStage2#saperror", "/jsp/SyncStage2sapError.jsp" );
        redirectionUrls.put("SyncStage3#ok", "/jsp/SyncStage3.jsp" );
        redirectionUrls.put("SyncStage3#saperror", "/jsp/SyncStage3sapError.jsp" );
        redirectionUrls.put("ShowFloorVariant#ok", "/jsp/EditFloorVariant.jsp");
        redirectionUrls.put("SaveRoomInfo#ok", "/RoomList");
        redirectionUrls.put("RoomList#ok", "/jsp/RoomList.jsp");
        redirectionUrls.put("RemoveFlat#ok", "/RoomList");
        redirectionUrls.put("ManagementUnitInfo#ok", "/jsp/ManagementUnitInfo.jsp");
        redirectionUrls.put("CreateNewFloorStage1#ok", "/jsp/CreateNewFloorStage1.jsp");
        redirectionUrls.put("CreateNewFloorStage2#ok", "/jsp/CreateNewFloorStage2.jsp");
        redirectionUrls.put("CreateNewFloorStage3#ok", "/jsp/CreateNewFloorStage3.jsp");
        redirectionUrls.put("CreateNewFloorStage4#ok", "/jsp/CreateNewFloorStage4.jsp");
        redirectionUrls.put("CreateFlat#ok", "/RoomList");
        redirectionUrls.put("FloorInfo#ok", "/jsp/FloorInfo.jsp");
        redirectionUrls.put("Drawing#ok", "/jsp/Drawing.jsp");
        redirectionUrls.put("FloorVariantInfo#ok", "/jsp/FloorVariantInfo.jsp");
        redirectionUrls.put("LoadDataModel#ok", "/jsp/LoadDataModel.jsp");
        redirectionUrls.put("SaveChanges#ok", "/jsp/SaveChanges.jsp");
    }

    @SuppressWarnings("unchecked")
    public static String getRedirectUrl(Class clazz, String status)
    {
        String className = clazz.getName();
        log.logSet("class name:", className);
        className = clazz.getName().substring(1 + clazz.getName().lastIndexOf('.'));
        log.logSet("simple class name:", className);
        log.logSet("status: ", status);
        String redirectionString = redirectionUrls.get(className + SEPARATION_CHAR + status.toLowerCase());
        if (redirectionString != null)
        {
            log.logSet("redirected to: ", redirectionString);
        }
        else
        {
            log.logError("redirection not found");
        }
        return redirectionString;
    }
}
