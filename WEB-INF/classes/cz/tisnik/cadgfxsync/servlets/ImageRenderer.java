package cz.tisnik.cadgfxsync.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.tisnik.cadgfxsync.Configuration;
import cz.tisnik.cadgfxsync.data.Building;
import cz.tisnik.cadgfxsync.data.DataModel;
import cz.tisnik.cadgfxsync.data.Floor;
import cz.tisnik.cadgfxsync.data.FloorVariant;
import cz.tisnik.cadgfxsync.data.Room;
import cz.tisnik.cadgfxsync.gfxentity.GfxEntity;
import cz.tisnik.cadgfxsync.gfxentity.GfxEntityAttribute;
import cz.tisnik.cadgfxsync.utils.HttpUtils;

public class ImageRenderer extends CustomHttpServlet
{
    public static int imageWidth = 1400;
    public static int imageHeight = 800;
    private static final Color ColorBrown = new Color(200, 200, 90);
    private static final Color ColorRed = Color.RED;
    private static final Color POLYGON_COLOR = Color.BLACK;
    private static final Color ROOM_COLOR_SAP_ATTRIBUTES =       new Color(0xaa, 0xff, 0xaa);
    private static final Color ROOM_COLOR_SAP_NO_ATTRIBUTES =    new Color(0xaa, 0xff, 0xff);
    private static final Color ROOM_COLOR_NO_SAP_ATTRIBUTES =    new Color(0xff, 0xff, 0xaa);
    private static final Color ROOM_COLOR_NO_SAP_NO_ATTRIBUTES = new Color(0xff, 0xaa, 0xff);

    /**
     * Generated serial version ID.
     */
    private static final long serialVersionUID = -7541374361612892100L;

    public void doProcess(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        this.log.logBegin("doProcess()");
        DataModel dataModel = HttpUtils.getDataModel(this);
        Configuration configuration = HttpUtils.getConfiguration(request);

        renderImage(dataModel, configuration, request, response);
        this.log.logEnd("doProcess()");
    }

    private void renderImage(DataModel dataModel, Configuration configuration, HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        this.log.logBegin("renderImage()");
        String buildingId = configuration.getSelectedBuilding();
        String floorId = configuration.getSelectedFloor();
        String floorVariantId = configuration.getSelectedFloorVariant();
        String roomId = configuration.getSelectedRoom();
        this.log.logSet("buildingId", buildingId);
        this.log.logSet("floorId", floorId);
        this.log.logSet("floorVariantId", floorVariantId);
        this.log.logSet("roomId", roomId);

        Building building = dataModel.getBuilding(buildingId);
        Floor floor = building == null ? null : building.getFloor(floorId);
        FloorVariant floorVariant = floor == null ? null : floor.getFloorVariant(floorVariantId);

        if (request.getParameter("coordsx") != null && request.getParameter("coordsy") != null)
        {
            int coordsx = Integer.parseInt(request.getParameter("coordsx"));
            int coordsy = Integer.parseInt(request.getParameter("coordsy"));
            this.log.log("set coords to $BLUE$"+coordsx+","+coordsy);
            double xorg = imageWidth/2.0;
            double yorg = imageHeight/2.0;
            double x = coordsx;
            double y = coordsy;
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
                        roomId = key;
                    }
                }
            }
        }

        BufferedImage bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D gc = bi.createGraphics();

        gc.setBackground(Color.WHITE);
        gc.clearRect(0, 0, imageWidth, imageHeight);
        gc.setColor(ColorBrown);
        gc.drawRect(0, 0, imageWidth - 1, imageHeight - 1);

        setTransformation(gc, floor);

        Font font = new Font("Helvetica", Font.PLAIN, (int) Math.abs(Math.floor(floor.getDefaultFontSize())));
        gc.setFont(font);

        if (floorVariant != null)
        {
            drawFilledPolygons(configuration, dataModel, gc, floor, floorVariant, roomId);
            drawGfxEntities(configuration, dataModel, gc, floor, floorVariant, roomId);
            drawPolygons(configuration, dataModel, gc, floor, floorVariant, roomId);
        }

        writeImage(response, bi);
        this.log.log("image written");
        this.log.logEnd("renderImage()");
    }

    private void writeImage(HttpServletResponse response, BufferedImage bi) throws IOException
    {
        response.setHeader( "Pragma", "no-cache" );
        response.addHeader( "Cache-Control", "must-revalidate" );
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png;charset=utf-8");
        OutputStream o = response.getOutputStream();
        try
        {
            ImageIO.write(bi, "png", o);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        o.flush();
        o.close();
    }

    private void drawFilledPolygons(Configuration configuration, DataModel dataModel, Graphics2D gc, Floor floor, FloorVariant floorVariant, String roomId)
    {
        for (Map.Entry<String, Room> roomPair : floorVariant.getRooms().entrySet())
        {
            String key = roomPair.getKey();
            Room room = roomPair.getValue();
            if ("nemaSapID".equals(key))
            {
                continue;
            }
            boolean hasCorrectAoid = !key.startsWith("*");
            boolean hasAttributes = room.hasAttributes();
            Color color = null;
            if (hasCorrectAoid)
            {
                if (hasAttributes)
                {
                    color = ROOM_COLOR_SAP_ATTRIBUTES;
                }
                else
                {
                    color = ROOM_COLOR_SAP_NO_ATTRIBUTES;
                }
            }
            else
            {
                if (hasAttributes)
                {
                    color = ROOM_COLOR_NO_SAP_ATTRIBUTES;
                }
                else
                {
                    color = ROOM_COLOR_NO_SAP_NO_ATTRIBUTES;
                }
            }
            gc.setColor(color);
            for (Polygon polygon : room.getPolygons())
            {
                gc.fillPolygon(polygon);
            }
        }
    }

    private void drawPolygons(Configuration configuration, DataModel dataModel, Graphics2D gc, Floor floor, FloorVariant floorVariant, String roomId)
    {
        for (Map.Entry<String, Room> roomPair : floorVariant.getRooms().entrySet())
        {
            Room room = roomPair.getValue();
            if (roomId != null && roomPair.getKey().equals(roomId))
            {
                for (Polygon polygon : room.getPolygons())
                {
                    gc.setColor(ColorRed);
                    Stroke oldStroke = gc.getStroke();
                    gc.setStroke(new BasicStroke((float)(4.0/floor.getDefaultScale())));
                    gc.drawPolygon(polygon);
                    gc.setStroke(oldStroke);
                }
            }
            else
            {
                gc.setColor(POLYGON_COLOR);
                for (Polygon polygon : room.getPolygons())
                {
                    gc.drawPolygon(polygon);
                }
            }
        }
    }

    private void drawGfxEntities(Configuration configuration, DataModel dataModel, Graphics2D gc,
            Floor floor, FloorVariant floorVariant, String roomId)
    {
        for (Map.Entry<String, Room> roomPair : floorVariant.getRooms().entrySet())
        {
            Room room = roomPair.getValue();
            if (roomId != null && roomPair.getKey().equals(roomId))
            {
                gc.setColor(Color.RED);
                for (GfxEntity entity : room.getEntities())
                {
                    entity.draw(gc);
                }
            }
            else
            {
                for (GfxEntity entity : room.getEntities())
                {
                    Color entityColor = resolveEntityColor(entity);
                    gc.setColor(entityColor);
                    if (isEntityWall(entity))
                    {
                        Stroke oldStroke = gc.getStroke();
                        gc.setStroke(new BasicStroke((float)(2.0/floor.getDefaultScale())));
                        entity.draw(gc);
                        gc.setStroke(oldStroke);
                    }
                    else
                    {
                        entity.draw(gc);
                    }
                }
            }
        }
    }

    private boolean isEntityWall(GfxEntity entity)
    {
        return entity.getAttribute() == GfxEntityAttribute.WALL;
    }

    private Color resolveEntityColor(GfxEntity entity)
    {
        if (entity == null)
        {
            return Color.ORANGE;
        }
        if (entity.getAttribute() == null)
        {
            return Color.YELLOW.darker();
        }
        switch (entity.getAttribute())
        {
        case UNKNOWN:
            return Color.LIGHT_GRAY;
        case DOOR:
            return Color.GREEN;
        case WALL:
            return Color.CYAN.darker();
        case STEP:
            return Color.BLUE;
        case ROOM:
            return Color.BLACK;
        default:
            return Color.yellow.darker();
        }
    }

    private void setTransformation(Graphics2D gc, Floor floor)
    {
        final double xorg = imageWidth/2.0;
        final double yorg = imageHeight/2.0;

        AffineTransform transform = gc.getTransform();
        transform.translate(xorg, yorg);
        transform.scale(floor.getDefaultScale(), floor.getDefaultScale());
        transform.translate((int)floor.getDefaultXpos(), (int)floor.getDefaultYpos());
        transform.translate(-xorg, -yorg);
        gc.setTransform(transform);
    }

}
