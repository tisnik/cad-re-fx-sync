package cz.tisnik.cadgfxsync.data;

import java.awt.Polygon;
import java.io.*;
import java.util.*;
import javax.servlet.*;

import cz.tisnik.cadgfxsync.gfxentity.GfxArc;
import cz.tisnik.cadgfxsync.gfxentity.GfxCircle;
import cz.tisnik.cadgfxsync.gfxentity.GfxEntity;
import cz.tisnik.cadgfxsync.gfxentity.GfxLine;
import cz.tisnik.cadgfxsync.gfxentity.GfxText;
import cz.tisnik.cadgfxsync.utils.Log;

public class DataModelInFiles
{
    private static final String DATA_RESOURCE_NAME = "/data/data.txt";
    private static final String DRAWING_RESOURCE_NAME = "/data/drawings.txt";
    public  static final String TEMP_DATA_FILE_NAME = "data.txt";
    public  static final String DRAWING_DATA_FILE_NAME = "drawings.txt";
    public static final String TEMP_BACKUP_DATA_FILE_NAME = "data~.txt";

    private static final boolean LOG_ENTITIES = false;

    enum Operation
    {
        NONE,
        BUILDING,
        FLOOR,
        FLOOR_VARIANT,
        ROOM,
        POLYGON,
    }

    /**
     * Instance objektu použitého pro logování do logovacího souboru či na
     * standardní výstup servlet kontejneru.
     */
    private Log log = new Log( this.getClass().getName() );

    private String lineNo$(int lineNo)
    {
        return String.format("$RED$%04d", lineNo);
    }

    public DataModel readData(ServletContext servletContext, String resourceName, String tempFileName, boolean debugInitDataModel, boolean debugFetchValueObject)
    {
        this.log.logBegin("readData");
        DataModel dataModel = new DataModel();

        Building building = null;
        String buildingSapId = null;
        Floor floor = null;
        String floorSapId = null;
        FloorVariant floorVariant = null;
        String floorVariantName = null;
        Room room = null;
        String roomSapId = null;
        Polygon polygon = null;

        BufferedReader in = null;
        Operation operation = Operation.NONE;
        int lineNo = 0;
        try
        {
            File fin = new File(System.getProperty("java.io.tmpdir")+ "/" + tempFileName);
            if (!fin.exists() || !fin.canRead() || !fin.isFile())
            {
                copySourceFileToWorkFile(fin, resourceName, servletContext.getResourceAsStream(resourceName));
            }
            this.log.logBegin("opening input data stream");
            //in = new BufferedReader(new FileReader(fin));
            in = new BufferedReader(new InputStreamReader(new FileInputStream(fin), "UTF-8"));
            String line;
            while ((line=in.readLine()) != null)
            {
                lineNo++;
                line = line.trim();
                int space = line.indexOf(' ');
                String key = null, value = null;
                if (space > 0)
                {
                    key = line.substring(0, space);
                    value = line.substring(space + 1);
                }
                else
                {
                    key = line;
                }
                if ("end".equals(key))
                {
                    switch (operation)
                    {
                        case BUILDING:
                            operation = Operation.NONE;
                            dataModel.addBuilding(buildingSapId, building);
                            this.log.logEnd(lineNo$(lineNo)+"  $VIOLET$building $GRAY$" + buildingSapId + ", " + building.getName());
                            break;
                        case FLOOR:
                            operation = Operation.BUILDING;
                            building.addFloor(floorSapId, floor);
                            this.log.logEnd(lineNo$(lineNo)+"  $VIOLET$floor $GRAY$" + floorSapId + ", " + floor.getName() + ", " + floor.getArea());
                            break;
                        case FLOOR_VARIANT:
                            operation = Operation.FLOOR;
                            floorVariant.resolveCodebookItems();
                            floor.addFloorVariant(floorVariantName, floorVariant);
                            this.log.logEnd(lineNo$(lineNo)+"  $VIOLET$variant $GRAY$" + floorVariantName + ", " + floorVariant.getDate());
                            break;
                        case ROOM:
                            operation = Operation.FLOOR_VARIANT;
                            floorVariant.addRoom(roomSapId, room);
                            this.log.logEnd(lineNo$(lineNo)+"  $VIOLET$room $GRAY$" + roomSapId );
                            break;
                        case POLYGON:
                            operation = Operation.ROOM;
                            room.addPolygon(polygon);
                            if (LOG_ENTITIES)
                            {
                                this.log.logEnd(lineNo$(lineNo) + "  $VIOLET$polygon $GRAY$" + polygon.npoints);
                            }
                            break;
                    }
                }
                if ("name".equals(key))
                {
                    switch (operation)
                    {
                        case BUILDING:
                            building.setName(value);
                            break;
                        case FLOOR:
                            floor.setName(value);
                            break;
                        case ROOM:
                            room.setName(value);
                            break;
                    }
                }
                if ("pozadavek".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        room.setPozadavek(value);
                    }
                }
                else if ("area".equals(key))
                {
                    switch (operation)
                    {
                        case FLOOR:
                            floor.setArea(Integer.parseInt(value));
                            break;
                        case ROOM:
                            room.setArea(Integer.parseInt(value));
                            break;
                    }
                }
                else if ("type".equals(key))
                {
                    if (operation==Operation.ROOM)
                    {
                        room.setType(Integer.parseInt(value));
                    }
                }
                else if ("capacity".equals(key))
                {
                    if (operation==Operation.ROOM)
                    {
                        room.setCapacity(Integer.parseInt(value));
                    }
                }
                else if ("free".equals(key))
                {
                    if (operation==Operation.ROOM)
                    {
                        room.setFree(Integer.parseInt(value));
                    }
                }
                else if ("date".equals(key))
                {
                    switch (operation)
                    {
                        case FLOOR_VARIANT:
                            floorVariant.setDate(value);
                            break;
                    }
                }
                else if ("building".equals(key))
                {
                    if (operation == Operation.NONE)
                    {
                        operation = Operation.BUILDING;
                        building = new Building();
                        buildingSapId = value;
                        this.log.logBegin(lineNo$(lineNo)+" $GREEN$building $GRAY$" + buildingSapId);
                    }
                }
                else if ("floor".equals(key))
                {
                    if (operation == Operation.BUILDING)
                    {
                        operation = Operation.FLOOR;
                        floor = new Floor();
                        floorSapId = value;
                        this.log.logBegin(lineNo$(lineNo)+"  $GREEN$floor $GRAY$" + floorSapId);
                    }
                }
                else if ("variant".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        operation = Operation.FLOOR_VARIANT;
                        floorVariant = new FloorVariant();
                        floorVariantName = value;
                        this.log.logBegin(lineNo$(lineNo)+"  $GREEN$variant $GRAY$" + floorVariantName);
                    }
                }
                else if ("room".equals(key))
                {
                    if (operation == Operation.FLOOR_VARIANT)
                    {
                        operation = Operation.ROOM;
                        room = new Room();
                        roomSapId = value;
                        this.log.logBegin(lineNo$(lineNo)+"  $GREEN$room $GRAY$" + roomSapId);
                    }
                }
                else if ("line".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        if (LOG_ENTITIES)
                        {
                            this.log.log(lineNo$(lineNo) + "    $GREEN$line $GRAY$" + value);
                        }
                        room.addGfxEntity(new GfxLine(value));
                    }
                }
                else if ("circle".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        if (LOG_ENTITIES)
                        {
                            this.log.log(lineNo$(lineNo) + "    $GREEN$circle $GRAY$" + value);
                        }
                        room.addGfxEntity(new GfxCircle(value));
                    }
                }
                else if ("arc".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        if (LOG_ENTITIES)
                        {
                            this.log.log(lineNo$(lineNo) + "    $GREEN$arc $GRAY$" + value);
                        }
                        room.addGfxEntity(new GfxArc(value));
                    }
                }
                else if ("text".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        if (LOG_ENTITIES)
                        {
                            this.log.log(lineNo$(lineNo) + "    $GREEN$text $GRAY$" + value);
                        }
                        room.addGfxEntity(new GfxText(value, floor.getDefaultFontSize()));
                    }
                }
                else if ("charterer".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        if (LOG_ENTITIES)
                        {
                            this.log.log(lineNo$(lineNo) + "    $GREEN$charterer $GRAY$" + value);
                        }
                        room.addCharterer(value);
                    }
                }
                else if ("polygon".equals(key))
                {
                    if (operation == Operation.ROOM)
                    {
                        operation = Operation.POLYGON;
                        polygon = new Polygon();
                        if (LOG_ENTITIES)
                        {
                            this.log.logBegin(lineNo$(lineNo)+"  $GREEN$polygon$GRAY$");
                        }
                    }
                }
                else if ("point".equals(key))
                {
                    if (operation == Operation.POLYGON)
                    {
                        String[] values = value.trim().split("\\s+");
                        double x = Double.parseDouble(values[0].trim());
                        double y = Double.parseDouble(values[1].trim());
                        polygon.addPoint((int)Math.round(x), (int)Math.round(y));
                    }
                }
                else if ("scale".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        this.log.logSet("scale", value);
                        floor.setDefaultScale(Double.parseDouble(value));
                    }
                }
                else if ("xpos".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        this.log.logSet("x-pos", value);
                        floor.setDefaultXpos(Double.parseDouble(value));
                    }
                }
                else if ("ypos".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        this.log.logSet("y-pos", value);
                        floor.setDefaultYpos(Double.parseDouble(value));
                    }
                }
                else if ("font_size".equals(key))
                {
                    if (operation == Operation.FLOOR)
                    {
                        this.log.logSet("font_size", value);
                        floor.setDefaultFontSize(Double.parseDouble(value));
                    }
                }
                else if ("flat".equals(key))
                {
                    switch (operation)
                    {
                    case ROOM:
                        room.setFlat(value);
                        break;
                    case FLOOR_VARIANT:
                        floorVariant.addFlat(value);
                        break;
                    }
                }
            }
            this.log.log("done reading " + fin.getAbsolutePath());
        }
        catch (IOException e)
        {
            this.log.log("$RED$read error $GRAY$: " + e.getMessage());
            e.printStackTrace();
        }
        try
        {
            if (in != null)
            {
                this.log.logEnd("closing input data stream ");
                in.close();
            }
        }
        catch (IOException e)
        {
            this.log.log("$RED$stream close error $GRAY$: " + e.getMessage());
            e.printStackTrace();
        }
        this.log.logEnd("readData");
        return dataModel;
    }

    public DataModel readDrawings(ServletContext servletContext, boolean debugInitDataModel, boolean debugFetchValueObject)
    {
        this.log.logBegin("readDrawings");
        DataModel dataModel = readData(servletContext, DRAWING_RESOURCE_NAME, DRAWING_DATA_FILE_NAME, debugInitDataModel, debugFetchValueObject);
        this.log.logEnd("readDrawings");
        return dataModel;
    }

    public DataModel readDataModel(ServletContext servletContext, boolean debugInitDataModel, boolean debugFetchValueObject)
    {
        this.log.logBegin("readDataModel");
        DataModel dataModel = readData(servletContext, DATA_RESOURCE_NAME, TEMP_DATA_FILE_NAME, debugInitDataModel, debugFetchValueObject);
        dataModel.setRoomTypes(new RoomTypes(servletContext));
        this.log.logEnd("readDataModel");
        return dataModel;
    }

    private void copySourceFileToWorkFile(File fout, String dataResourceName, InputStream inputStream)
    {
        this.log.logBegin("copySourceFileToWorkFile");
        this.log.logSet("input file ", dataResourceName);
        this.log.logSet("output file", fout.getAbsolutePath());
        BufferedReader in = null;
        BufferedWriter out = null;
        try
        {
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            out = new BufferedWriter(new FileWriter(fout));
            String line;
            int lineCount = 0;
            int charCount = 0;
            while (null != (line=in.readLine()))
            {
                lineCount ++;
                charCount += line.length();
                out.write(line);
                out.write("\n");
            }
            this.log.logSet("copied lines", lineCount);
            this.log.logSet("copied chars", charCount);
        }
        catch (UnsupportedEncodingException e)
        {
            this.log.logError(e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            this.log.logError(e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException e)
            {
                this.log.logError(e.getMessage());
                e.printStackTrace();
            }
            try
            {
                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException e)
            {
                this.log.logError(e.getMessage());
                e.printStackTrace();
            }
        }
        this.log.logEnd("copySourceFileToWorkFile");
    }

    private void copyDestFileToTemp(String finName, String foutName)
    {
        this.log.logBegin("copyDestFileToTemp");
        this.log.logSet("input file ", finName);
        this.log.logSet("output file", foutName);
        BufferedReader in = null;
        BufferedWriter out = null;
        try
        {
            in = new BufferedReader(new FileReader(finName));
            out = new BufferedWriter(new FileWriter(foutName));
            String line;
            int lineCount = 0;
            int charCount = 0;
            while (null != (line=in.readLine()))
            {
                lineCount ++;
                charCount += line.length();
                out.write(line);
                out.write("\n");
            }
            this.log.logSet("copied lines", lineCount);
            this.log.logSet("copied chars", charCount);
        }
        catch (UnsupportedEncodingException e)
        {
            this.log.logError(e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            this.log.logError(e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException e)
            {
                this.log.logError(e.getMessage());
                e.printStackTrace();
            }
            try
            {
                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException e)
            {
                this.log.logError(e.getMessage());
                e.printStackTrace();
            }
        }
        this.log.logEnd("copyDestFileToTemp");
    }

    public void writeDataModel(DataModel dataModel, String fileName, String tempFileName)
    {
        this.log.logBegin("writeDataModel");
        copyDestFileToTemp(System.getProperty("java.io.tmpdir")+"/" + fileName, System.getProperty("java.io.tmpdir")+"/" + tempFileName);
        try
        {
            File fout = new File(System.getProperty("java.io.tmpdir")+"/" + fileName);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout), "UTF-8"));
            try
            {
                writeBuildings(out, dataModel);
            }
            finally
            {
                out.close();
            }
        }
        catch (IOException e)
        {
            this.log.logError( e.getMessage() );
        }
        this.log.logEnd("writeDataModel");
    }

    private void writeBuildings(Writer fout, DataModel dataModel)
        throws IOException
    {
        for (Map.Entry<String, Building> buildingPair : dataModel.getBuildings().entrySet())
        {
            Building building = buildingPair.getValue();
            fout.write("building " + buildingPair.getKey() + "\n");
            fout.write("    name " + building.getName() + "\n");
            writeFloors(fout, building);
            fout.write("end\n");
        }
    }

    private void writeFloors(Writer fout, Building building)
        throws IOException
    {
        for (Map.Entry<String, Floor> floorPair : building.getFloors().entrySet())
        {
            Floor floor = floorPair.getValue();
            fout.write("    floor " + floorPair.getKey() + "\n");
            fout.write("        name " + floor.getName() + "\n");
            fout.write("        scale " + floor.getDefaultScale() + "\n");
            String xpos = String.valueOf(floor.getDefaultXpos());
            String ypos = String.valueOf(floor.getDefaultYpos());
            String fontSize = String.valueOf(floor.getDefaultFontSize());
            if (xpos.endsWith(".0"))
            {
                xpos = xpos.substring(0, xpos.length()-2);
            }
            if (ypos.endsWith(".0"))
            {
                ypos = ypos.substring(0, ypos.length()-2);
            }
            if (fontSize.endsWith(".0"))
            {
                fontSize = fontSize.substring(0, fontSize.length()-2);
            }
            fout.write("        xpos " + xpos + "\n");
            fout.write("        ypos " + ypos + "\n");
            fout.write("        font_size " + fontSize + "\n");
            if (floor.getArea() != null)
            {
                fout.write("        area " + floor.getArea() + "\n");
            }
            writeFloorVariant(fout, floor);
            fout.write("    end\n");
        }
    }

    private void writeFloorVariant(Writer fout, Floor floor)
        throws IOException
    {
        List<String> keys = new ArrayList<String>(floor.getFloorVariants().keySet());
        Collections.sort(keys);
        for (String key : keys)
        {
            FloorVariant floorVariant = floor.getFloorVariants().get(key);
            fout.write("        variant " + key + "\n");
            fout.write("            date " + floorVariant.getDate() + "\n");
            if (floorVariant.hasFlats())
            {
                for (String flatName : floorVariant.getFlats())
                {
                    fout.write("            flat " + flatName + "\n");
                }
            }
            writeRooms(fout, floorVariant);
            fout.write("        end\n");
        }
    }

    private void writeRooms(Writer fout, FloorVariant floorVariant)
        throws IOException
    {
        List<String> keys = new ArrayList<String>(floorVariant.getRooms().keySet());
        Collections.sort(keys);
        for (String key : keys)
        {
            Room room = floorVariant.getRooms().get(key);
            fout.write("            room " + key + "\n");
            if (room.getName() != null && !"".equals(room.getName()))
            {
                fout.write("                name " + room.getName() + "\n");
            }
            if (room.hasTypeSet())
            {
                fout.write("                type " + room.getType() + "\n");
            }
            if (room.getArea() != null)
            {
                fout.write("                area " + room.getArea() + "\n");
            }
            if (room.hasCapacitySet())
            {
                fout.write("                capacity " + room.getCapacity() + "\n");
            }
            if (room.isFreeSet())
            {
                fout.write("                free " + room.getFree() + "\n");
            }
            if (room.getPozadavek() != null)
            {
                fout.write("                pozadavek " + room.getPozadavek() + "\n");
            }
            if (room.getFlat() != null)
            {
                fout.write("                flat " + room.getFlat() + "\n");
            }
            writeCharterers(fout, room);
            writeEntitiesOfType(fout, room, GfxText.class.getSimpleName());
            writeEntitiesOfType(fout, room, GfxLine.class.getSimpleName());
            writeEntitiesOfType(fout, room, GfxCircle.class.getSimpleName());
            writeEntitiesOfType(fout, room, GfxArc.class.getSimpleName());
            writePolygons(fout, room);
            fout.write("            end\n");
        }
    }

    private void writePolygons(Writer fout, Room room) throws IOException
    {
        for (Polygon polygon : room.getPolygons())
        {
            fout.write("                polygon\n");
            for (int i = 0; i < polygon.npoints; i++)
            {
                int x = polygon.xpoints[i];
                int y = polygon.ypoints[i];
                fout.write(String.format("                    point %3d %3d\n", x, y));
            }
            fout.write("                end\n");
        }
    }

    private void writeEntitiesOfType(Writer fout, Room room, String className) throws IOException
    {
        for (GfxEntity entity : room.getEntities())
        {
            if (entity.getClass().getSimpleName().equals(className))
            {
                fout.write("                " + entity.toString() + "\n");
            }
        }
    }

    private void writeCharterers(Writer fout, Room room) throws IOException
    {
        for (String charterer : room.getCharterers())
        {
            fout.write("                charterer " + charterer + "\n");
        }
    }

}

