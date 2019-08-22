package cz.tisnik.cadgfxsync.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.Floor;
import cz.tisnik.cadgfxsync.data.FloorVariant;
import cz.tisnik.cadgfxsync.data.Room;
import cz.tisnik.cadgfxsync.sap.TSync;

public class SyncStage1 extends CustomHttpServlet
{
    /**
     * Generated serial version ID
     */
    private static final long serialVersionUID = -6412084361306923667L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();

        String aoid = getIAoid(request);
        String valFrom = getIValFrom(this, request);
        String test = getITest(this, request);
        servletContext.setAttribute("i_aoid", aoid);
        servletContext.setAttribute("i_val_from", valFrom);
        servletContext.setAttribute("i_test", test);
        request.setAttribute("i_aoid", aoid);
        request.setAttribute("i_val_from", valFrom);
        request.setAttribute("i_test", test);
        request.setAttribute("table", renderTable(this, request));
        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private String getIAoid(HttpServletRequest request)
    {
        return Building.getBuildingAoid(request);
    }

    private String getIValFrom(HttpServlet httpServlet, HttpServletRequest request)
    {
        FloorVariant floorVariant = Building.getFloorVariant(httpServlet, request);
        String date = floorVariant.getDate();
        String day = date.substring(0, date.indexOf('.'));
        if (day.length() == 1)
        {
            day = "0" + day;
        }
        String month = date.substring(1 + date.indexOf('.'), date.lastIndexOf('.'));
        if (month.length() == 1)
        {
            month = "0" + month;
        }
        String year = date.substring(1 + date.lastIndexOf('.'));
        return year + month + day; 
    }

    private String getITest(HttpServlet httpServlet, HttpServletRequest request)
    {
        return "X";
    }

    private String renderTable(HttpServlet httpServlet, HttpServletRequest request)
    {
        String buildingAoid = Building.getBuildingAoid(request);
        String floorAoid = Building.getFloorAoid(request);
        String floorVariantAoid = Building.getFloorVariantAoid(request);
        Building building = Building.getBuilding(httpServlet, request);
        Floor floor = Building.getFloor(httpServlet, request);
        FloorVariant floorVariant = Building.getFloorVariant(httpServlet, request);
        List<TSync> tSync = fillTSync(buildingAoid, floorAoid, floorVariantAoid, building, floor, floorVariant);
        StringBuffer out = new StringBuffer();
        for (TSync sync : tSync)
        {
            out.append("<tr>");
            out.append("<td>0"+sync.aoLevel+"</td>");
            out.append(item(sync.aoTypeExt));
            out.append(item(sync.aoNr));
            out.append(item(sync.aoId));
            out.append(item(sync.aoFunction));
            out.append(item(sync.xao));
            out.append(item(sync.area));
            out.append(item(sync.chngInd));
            out.append(item(sync.parent));
            out.append("<tr>\n");
        }
        httpServlet.getServletContext().setAttribute("i_table", tSync);
        return out.toString();
    }

    private String item(Object o)
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

    private List<TSync> fillTSync(String buildingAoid, String floorAoid, String floorVariantAoid, Building building, Floor floor, FloorVariant floorVariant)
    {
        List<TSync> list = new ArrayList<TSync>();
        list.add(constructFloorItem(floorAoid, building, floor));
        list.addAll(constructFlats(floor, floorVariant));
        list.addAll(contructRooms(floor, floorVariant));
        return list;
    }

    private TSync constructFloorItem(String floorAoid, Building building, Floor floor)
    {
        return new TSync(01, 'F', constructAoid(floorAoid, false), null, null, floor.getName(), null);
    }

    private List<TSync> constructFlats(Floor floor, FloorVariant floorVariant)
    {
        List<TSync> list = new ArrayList<TSync>();
        for (String flat : floorVariant.getFlats())
        {
            list.add(new TSync(02, 'A', flat, null, null, flat, null));
            for (Entry<String, Room> roomEntry : floorVariant.getRooms().entrySet())
            {
                String key = roomEntry.getKey();
                Room room = roomEntry.getValue();
                if (room.getFlat() != null && room.getFlat().equals(flat))
                {
                    if (!"nemasapid".equals(room.getName().toLowerCase()) && !"nemasapid".equals(key.toLowerCase()))
                    {
                        list.add(new TSync(03, 'R', constructAoid(key), null, null, room.getName(), room.getArea()));
                    }
                }
            }
        }
        return list;
    }

    private List<TSync> contructRooms(Floor floor, FloorVariant floorVariant)
    {
        List<TSync> list = new ArrayList<TSync>();
        for (Entry<String, Room> roomEntry : floorVariant.getRooms().entrySet())
        {
            String key = roomEntry.getKey();
            Room room = roomEntry.getValue();
            if (room.getFlat() == null)
            {
                if (!"nemasapid".equals(room.getName().toLowerCase()) && !"nemasapid".equals(key.toLowerCase()))
                {
                    list.add(new TSync(02, 'R', constructAoid(key), null, null, room.getName(), room.getArea()));
                }
            }
        }
        return list;
    }

    private String constructAoid(String name, boolean check)
    {
        if (check)
        {
            return constructAoid(name);
        }
        else
        {
            return name.substring(name.length() - 2);
        }
    }

    private String constructAoid(String roomName)
    {
        String sub = roomName.substring(roomName.length()-2);
        if (isNumber(sub.charAt(0)) && isNumber(sub.charAt(1)))
        {
            return sub;
        }
        else
        {
            return "";
        }
    }

    private boolean isNumber(char ch)
    {
        return ch>='0' && ch<='9';
    }
}
