package cz.tisnik.cadgfxsync.gfxentity;

public enum GfxEntityAttribute
{
    UNKNOWN,
    DOOR,
    STEP,
    WALL,
    ROOM;

    public static GfxEntityAttribute resolveGfxEntityAttribute(String str)
    {
        if (str == null)
        {
            return UNKNOWN;
        }
        switch (str.trim().toLowerCase().charAt(0))
        {
        case 'd':
            return DOOR;
        case 's':
            return STEP;
        case 'w':
            return WALL;
        case 'r':
            return ROOM;
        default:
            return UNKNOWN;
        }
    }

    public String toString()
    {
        switch (this)
        {
        case DOOR:
            return "d";
        case STEP:
            return "s";
        case WALL:
            return "w";
        case ROOM:
            return "r";
        default:
            return null;
        }
    }
}
