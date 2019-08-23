package cz.tisnik.cadgfxsync.servlets;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.Configuration;
import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.DataModel;
import cz.tisnik.cadgfxsync.data.Floor;
import cz.tisnik.cadgfxsync.data.FloorVariant;
import cz.tisnik.cadgfxsync.data.Room;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class RoomList extends CustomHttpServlet
{
    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = 1254980L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        ServletContext servletContext = this.getServletContext();
        Configuration configuration = HttpUtils.getConfiguration(request);
        String buildingId = configuration.getSelectedBuilding();
        String floorId = configuration.getSelectedFloor();
        String floorVariantId = configuration.getSelectedFloorVariant();
        String selectedRoomId = request.getParameter("id");
        if (request.getAttribute("roomId") != null)
        {
            selectedRoomId = (String)request.getAttribute("roomId");
        }
        String coordsx = request.getParameter("coordsx");
        String coordsy = request.getParameter("coordsy");

        request.setAttribute("flatList", setFlatList(request, configuration, buildingId, floorId, floorVariantId));
        request.setAttribute("roomList", setRoomList(request, configuration, buildingId, floorId, floorVariantId, selectedRoomId, coordsx, coordsy));

        this.redirectTo(servletContext, request, response, "ok");
        this.log.logEnd("doProcess()");
    }

    private String setFlatList(HttpServletRequest request, Configuration configuration, String buildingId,
            String floorId, String floorVariantId)
    {
        StringBuffer out = new StringBuffer();
        DataModel dataModel = HttpUtils.getDataModel(this);
        Building building = dataModel.getBuilding(buildingId);
        if (building == null)
        {
            out.append("budova nenalezena");
            return out.toString();
        }
        Floor floor = building.getFloor(floorId);
        if (floor == null)
        {
            out.append("patro nenalezeno");
            return out.toString();
        }
        FloorVariant floorVariant = floor.getFloorVariant(floorVariantId);
        if (floorVariant == null)
        {
            out.append("varianta nenalezena");
            return out.toString();
        }
        if (floorVariant.hasFlats())
        {
            for (String flatName : floorVariant.getFlats())
            {
                out.append("<tr><td>" + flatName + "</td><td><a href='RemoveFlat?flat=" + flatName
                        + "'>smazat</a></td></tr>\n");
            }
        }
        return out.toString();
    }

    private String setRoomList(HttpServletRequest request, Configuration configuration, String buildingId, String floorId, String floorVariantId, String selectedRoomId, String coordsx, String coordsy)
    {
        StringBuffer out = new StringBuffer();
        DataModel dataModel = HttpUtils.getDataModel(this);
        Building building = dataModel.getBuilding(buildingId);
        if (building == null)
        {
            out.append("budova nenalezena");
            return out.toString();
        }
        Floor floor = building.getFloor(floorId);
        if (floor == null)
        {
            out.append("patro nenalezeno");
            return out.toString();
        }
        FloorVariant floorVariant = floor.getFloorVariant(floorVariantId);
        if (floorVariant == null)
        {
            out.append("varianta nenalezena");
            return out.toString();
        }
        if (coordsx != null && coordsy != null)
        {
            int icoordsx = Integer.parseInt(coordsx);
            int icoordsy = Integer.parseInt(coordsy);
            this.log.log("set coords to $BLUE$"+coordsx+","+coordsy);
            double xorg = ImageRenderer.imageWidth/2.0;
            double yorg = ImageRenderer.imageHeight/2.0;
            double x = icoordsx;
            double y = icoordsy;
            x-=xorg;
            y-=yorg;
            x/=floor.getDefaultScale();
            y/=floor.getDefaultScale();
            x-=floor.getDefaultXpos();
            y-=floor.getDefaultYpos();
            x+=xorg;
            y+=yorg;
            for (Map.Entry<String, Room> roomPair : floorVariant.getRooms().entrySet())
            {
                String key = roomPair.getKey();
                if ("nemaSapID".equals(key)) continue;
                Room room = roomPair.getValue();
                for (Polygon polygon : room.getPolygons())
                {
                    if (polygon.contains(x, y))
                    {
                        selectedRoomId = key;
                    }
                }
            }
        }
        Map<String, Room> rooms = floorVariant.getRooms();
        List<String> keys = new ArrayList<String>(floorVariant.getRooms().keySet());
        Collections.sort(keys);
        for (String key : keys)
        {
            if ("nemaSapID".equals(key))
            {
                continue;
            }
            Room room = rooms.get(key);
            boolean selected = selectedRoomId != null && selectedRoomId.equals(key);
            boolean hasCorrectAoid = !key.startsWith("*");
            boolean hasAttributes = room.hasAttributes();
            String color = null;
            if (hasCorrectAoid)
            {
                if (hasAttributes)
                {
                    color = "#00cc00";
                }
                else
                {
                    color = "#00cccc";
                }
            }
            else
            {
                if (hasAttributes)
                {
                    color = "#cccc00";
                }
                else
                {
                    color = "#cc00cc";
                }
            }
            String background = selected ? " style='background:#ffaaaa'" : "";
            String roomName = room.getName();
            if (roomName == null || "".equals(roomName))
            {
                roomName = "* nezadáno *";
            }
            out.append(String.format("<tr%s><td><a href='RoomList?id=%s' onclick='onRoomClick(\"%s\")'>%s</a></td><td style='color:%s'>%s</td></tr>\n", background, key, key, key, color, roomName));
        }
        this.log.logSet("selectedRoomId", selectedRoomId);
        if (selectedRoomId != null)
        {
            setRoomInfo(request, dataModel, configuration, selectedRoomId);
        }
        return out.toString();
    }

    private void setRoomInfo(HttpServletRequest request, DataModel dataModel, Configuration configuration, String roomId)
    {
        Floor floor = Building.getFloor(this, request);
        FloorVariant floorVariant = Building.getFloorVariant(this, request);

        if (floorVariant == null)
        {
            request.setAttribute("message", "varianta nenalezena");
            return;
        }
        Room room = floorVariant.getRoom(roomId);
        if (room == null || roomId == null)
        {
            request.setAttribute("message", "místnost nenalezena");
            return;
        }
        request.setAttribute("aoid", _(roomId));
        request.setAttribute("name", _(room.getName()));
        request.setAttribute("area", _(room.getArea()));
        request.setAttribute("computedArea", computeArea(floor, room));
        request.setAttribute("capacity", _(room.getCapacity()));
        request.setAttribute("free", _(room.getFree()));
        request.setAttribute("type", renderRoomType(dataModel, room.getType()));
        request.setAttribute("flats", renderFlatList(floorVariant, room));
    }

    private String computeArea(Floor floor, Room room)
    {
        if (floor == null || room == null || room.getPolygons() == null || room.getPolygons().size()<1)
        {
            return "";
        }
        log.logSet("floor", floor == null);
        log.logSet("room", room == null);
        log.logSet("room.getPolygons()", room.getPolygons() == null);
        log.logSet("room.getPolygons().size()", room.getPolygons().size());
        Polygon polygon = room.getPolygons().get(0);
        Rectangle rectangle = polygon.getBounds();
        double area = rectangle.height * rectangle.height * floor.getDefaultScale() / 1000.0;
        return String.format("%5.0f", area);
    }

    private String _(Object obj)
    {
        return obj == null ? "" : obj.toString();
    }

    private String renderRoomType(DataModel dataModel, Integer type)
    {
        StringBuffer out = new StringBuffer();
        for (Entry<Integer, String> roomType : dataModel.getRoomTypes().getRoomTypes().entrySet())
        {
            String selected = (type == roomType.getKey()) ? " selected='selected'" : "";
            out.append("<option " + selected + " value='" + roomType.getKey() + "'>" + roomType.getValue() + "</option>\n");
        }
        return out.toString();
    }

    private String renderFlatList(FloorVariant floorVariant, Room room)
    {
        StringBuffer out = new StringBuffer();
        out.append("<option value=''>---</option>\n");
        if (floorVariant == null || room == null || !floorVariant.hasFlats())
        {
            return out.toString();
        }
        for (String flatName : floorVariant.getFlats())
        {
            String selected = flatName.equals(room.getFlat()) ? " selected='selected'" : "";
            out.append("<option " + selected + " value='" + flatName + "'>" + flatName + "</option>\n");
        }
        return out.toString();
    }
}
