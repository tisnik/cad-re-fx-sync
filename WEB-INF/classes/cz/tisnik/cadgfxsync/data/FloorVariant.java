package cz.tisnik.cadgfxsync.data;

import java.util.*;

public class FloorVariant
{
    private String date;
    private Map<String, Room> rooms;
    private Set<Integer> roomTypes;
    private Set<Integer> roomAreas;
    private Set<Integer> roomCapacities;
    private Set<String>  roomCharterers;
    private Set<Integer> roomAvailabilities;
    private Set<String>  flats;

    public FloorVariant()
    {
        this.rooms = new HashMap<String, Room>();
        this.roomTypes = new TreeSet<Integer>();
        this.roomAreas = new TreeSet<Integer>();
        this.roomCapacities = new TreeSet<Integer>();
        this.roomCharterers = new TreeSet<String>();
        this.roomAvailabilities = new TreeSet<Integer>();
        this.flats = new TreeSet<String>();
    }

    public FloorVariant(String date)
    {
        this();
        this.date = date;
    }

    public void resolveCodebookItems()
    {
        this.roomTypes.clear();
        this.roomAreas.clear();
        this.roomCapacities.clear();
        this.roomCharterers.clear();
        this.roomAvailabilities.clear();
        for (Room room : this.rooms.values())
        {
            if (room.getType() != null)
            {
                this.roomTypes.add(room.getType());
            }
            if (room.getArea() != null)
            {
                this.roomAreas.add(room.getArea());
            }
            if (room.getCapacity() != null)
            {
                this.roomCapacities.add(room.getCapacity());
            }
            if (room.getCharterers() != null)
            {
                this.roomCharterers.addAll(room.getCharterers());
            }
            if (room.getCapacity() != null && room.getCharterers() != null)
            {
                this.roomAvailabilities.add(room.getCapacity() - room.getCharterers().size());
            }
        }
    }

    public void addRoom(String sapId, Room room)
    {
        this.rooms.put(sapId, room);
    }

    public String getDate()
    {
        return this.date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public Map<String, Room> getRooms()
    {
        return this.rooms;
    }

    public Set<Integer> getRoomTypes()
    {
        return roomTypes;
    }

    public Set<Integer> getRoomAreas()
    {
        return roomAreas;
    }

    public Set<Integer> getRoomCapacities()
    {
        return roomCapacities;
    }

    public Set<String> getRoomCharterers()
    {
        return roomCharterers;
    }

    public Set<Integer> getRoomAvailabilities()
    {
        return roomAvailabilities;
    }

    public Room getRoom(String roomId)
    {
        return this.getRooms().get(roomId);
    }

    public void removeRoom(String roomId)
    {
        this.getRooms().remove(roomId);
    }

    public void addFlat(String flatName)
    {
        this.flats.add(flatName);
    }

    public void removeFlat(String flatName)
    {
        this.flats.remove(flatName);
    }

    public Set<String> getFlats()
    {
        return this.flats;
    }

    public boolean hasFlats()
    {
        return this.flats != null && !this.flats.isEmpty();
    }

}
