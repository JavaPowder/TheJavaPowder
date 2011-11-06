package thejavapowder;

import thejavapowder.Elements.*;

public class Variables {
    int[] surArray = new int[8];
    boolean Drawing = false;
    boolean DebugMode = false;
    boolean antiDouble = false;

    int CurrentX = 100;
    int CurrentY = 100;
    /*Transformated Variables:
      * Variables that represents a long calculation used often
      */
    int CursorX = 100; //Where to draw the cursor
    int CursorY = 100;

    int realZoom = 2;//Zoom * winZoom

    int DrawX = 0;//Where to draw
    int DrawY = 0;
    int LastDrawX = 0;//Where to draw
    int LastDrawY = 0;

    //End of Transformated Variables
    boolean active = true; //For elements menu
    int wait = 30;

    int ScrollX = 0;
    int ScrollY = 0;
    int iconY = 0;
    int iconX = 0;

    byte Zoom = 1;
    byte Size = 10;
    byte Shape = 0;
    byte Equipped = 3;
    byte winZoom = 2;
    float Brightness = 0.0f;

    boolean stopReactions = false;
    boolean Simulating = true;//Paused or not paused
    byte currentMode = 0; // All, Electronic Ect
    int state = 0;
    /* The state it is it
    * 0 = Normal
    * 1 = Menu
    * 2 = Element Menu
    * 3 = Settings
    * 4 = Documentation
    * 5 = Console
    */

    boolean leftClick = true;
    boolean conductive = false;

    int Height = 350;
    int optionsHeight = 14;
    int Width = 600;
    byte[] reaction = new byte[16];
    byte[] reactives = new byte[]{};

    byte element = 0;
    short temp = 0;
    int RandomNum = 0;

    byte[][] Map = new byte[Width][Height];// The particle type map
    short[][] HMap = new short[Width][Height];// The Heat type map
    short[][] VMap = new short[Width][Height];// The Voltage type map
    boolean[][] BMap = new boolean[Width][Height]; // The particle updated map, made for safeguarding.
    byte[][] PMap = new byte[Width][Height];// The Particle Properties Map
    //Elements Initialization
    //Order of creation: new Element(amount-of-burn,weight,conductive(boolean),state("g","p","s","l"), Name, Description, colour(HEX), reactives, default temp)
    Element Elements[] = {
        new Powder((byte) 0, (byte) 50, false, "Coffee", "Crushed Coffee Beans, First Element, Reference to Java", 0x613F37, new byte[]{3}, 20),
        new Solid((byte) 0, (byte) 127, false, "Wall", "Blocks Everything", 0x808080, new byte[]{}, 20),
        new Gas((byte) 10, (byte) 5, false, "Methane", "Highly Flammable Gas", 0xDEDEDE, new byte[]{15}, 20),
        new Liquid((byte) 0, (byte) 30, true, "Water", "Pure H2O", 0x0000FF, new byte[]{15}, 20),
        new Solid((byte) 0, (byte) 127, true, "Iron", "Conductor, Used to Activate Electrical Elements", 0x404040, new byte[]{}, 20),
        new Solid((byte) 4, (byte) 127, true, "Battery", "Infinite Source of Energy", 0x00FF00, new byte[]{}, 20),
        new Solid((byte) 0, (byte) 127, false, "Copper", "Standard Conductor", 0xE8851C, new byte[]{}, 20),
        new Solid((byte) 0, (byte) 127, true, "SemiConductorA", "Conducts only to Semi Conductor B", 0x226315, new byte[]{}, 20),
        new Solid((byte) 0, (byte) 127, true, "SemiConductorB", "Conducts only to Metal", 0x829E1C, new byte[]{}, 20),
        new Solid((byte) 0, (byte) 127, true, "Screen", "Looks different Based on Voltage", 0x000000, new byte[]{}, 20),
        new Solid((byte) 0, (byte) 127, true, "Resistor", "Lowers the voltage", 0xEDED9D, new byte[]{}, 20),
        new Solid((byte) 0, (byte) 10, true, "Rechargable Battery", "Limited source of Power", 0x329E00, new byte[]{}, 20),
        new Solid((byte) 4, (byte) 127, true, "Power Drainer", "Drains the electricity", 0xBABABA, new byte[]{}, 20),
        new Solid((byte) 0, (byte) 127, false, "Crossing", "Makes electricity jump over it", 0xE8851C, new byte[]{}, 20),
        new Solid((byte) 0, (byte) 127, false, "Switch", "Conducts if turned on", 0x00ED00, new byte[]{},20),
        new Gas((byte) 0, (byte) 5, false, "Fire", "Burns stuff", 0xDE2307, new byte[]{}, 200),
        new Solid((byte) 2, (byte) 127, false, "Wood", "Burns slowly", 0xC0A040, new byte[]{}, 20),
        new Liquid((byte) 10, (byte) 80, false, "Petroleum", "Oil, burns", 0x404010, new byte[]{}, 20),
        new Powder((byte) 16, (byte) 15, false, "Sawdust", "Sawdust", 0xFFE0A0, new byte[]{}, 20),
        };
        //Order of creation: new Element(amount-of-burn,weight,conductive(boolean),state("g","p","s","l"), Name, Description, colour(HEX), reactives, default temp)
    public int NUM_ELS = 19;
    public static int NUM_ELS2 = 19;
}
