package thejavapowder;

import thejavapowder.Elements.*;

public class Variables {
    static byte elementNum = 15;
    static int[] surArray = new int[8];
    static boolean Drawing = false;
    static boolean DebugMode = false;
    static boolean antiDouble = false;

    static int CurrentX = 100;
    static int CurrentY = 100;
    /*Transformated Variables:
      * Variables that represents a long calculation used often
      */
    static int CursorX = 100; //Where to draw the cursor
    static int CursorY = 100;

    static int realZoom = 2;//Zoom * winZoom

    static int DrawX = 0;//Where to draw
    static int DrawY = 0;
    static int LastDrawX = 0;//Where to draw
    static int LastDrawY = 0;

    //End of Transformated Variables
    static boolean active = true; //For elements menu
    static int wait = 30;

    static int ScrollX = 0;
    static int ScrollY = 0;
    static int iconY = 0;
    static int iconX = 0;

    static byte Zoom = 1;
    static byte Size = 10;
    static byte Shape = 0;
    static byte Equipped = 3;
    static byte winZoom = 2;
    static float Brightness = 0.0f;

    static boolean stopReactions = false;
    static boolean Simulating = true;//Paused or not paused
    static byte currentMode = 0; // All, Electronic Ect
    static int state = 0;
    /* The state it is it
    * 0 = Normal
    * 1 = Menu
    * 2 = Element Menu
    * 3 = Settings
    * 4 = Documentation
    * 5 = Console
    */

    static boolean leftClick = true;
    static boolean conductive = false;

    static int Height = 350;
    static int Width = 600;
    static byte[] reaction = new byte[16];
    static byte[] reactives = new byte[]{};

    static byte element = 0;
    static short temp = 0;
    static int RandomNum = 0;

    static byte[][] Map = new byte[Width][Height];// The particle type map
    static short[][] HMap = new short[Width][Height];// The Heat type map
    static short[][] VMap = new short[Width][Height];// The Voltage type map
    static boolean[][] BMap = new boolean[Width][Height]; // The particle updated map, made for safeguarding.
    static byte[][] PMap = new byte[Width][Height];// The Particle Properties Map
    //Elements Initialization
    //Order of creation: new Element(amount-of-burn,weight,conductive(boolean),state("g","p","s","l"), Name, Description, colour(HEX), reactives, default temp)
    static Element Elements[] = {
        new Coffee((byte) 0, (byte) 50, false, 'p', "Coffee", "Crushed Coffee Beans, First Element, Reference to Java", 0x613F37, new byte[]{3}, 20),
        new Wall((byte) 0, (byte) 127, false, 's', "Wall", "Blocks Everything", 0x808080, new byte[]{}, 20),
        new Methane((byte) 10, (byte) 5, false, 'g', "Methane", "Highly Flammable Gas", 0xDEDEDE, new byte[]{15}, 20),
        new Water((byte) 0, (byte) 30, true, 'l', "Water", "Pure H2O", 0x0000FF, new byte[]{15}, 20),
        new Iron((byte) 0, (byte) 127, true, 's', "Iron", "Conductor, Used to Activate Electrical Elements", 0x404040, new byte[]{}, 20),
        new Battery((byte) 4, (byte) 127, true, 's', "Battery", "Infinite Source of Energy", 0x00FF00, new byte[]{}, 20),
        new Copper((byte) 0, (byte) 127, false, 's', "Copper", "Standard Conductor", 0xE8851C, new byte[]{}, 20),
        new SemiConductorA((byte) 0, (byte) 127, true, 's', "SemiConductorA", "Conducts only to Semi Conductor B", 0x226315, new byte[]{}, 20),
        new SemiConductorB((byte) 0, (byte) 127, true, 's', "SemiConductorB", "Conducts only to Metal", 0x829E1C, new byte[]{}, 20),
        new Screen((byte) 0, (byte) 127, true, 's', "Screen", "Looks different Based on Voltage", 0x000000, new byte[]{}, 20),
        new Resistor((byte) 0, (byte) 127, true, 's', "Resistor", "Lowers the voltage", 0xEDED9D, new byte[]{}, 20),
        new RechargableBattery((byte) 0, (byte) 10, true, 's', "Rechargable Battery", "Limited source of Power", 0x329E00, new byte[]{}, 20),
        new PowerDrainer((byte) 4, (byte) 127, true, 's', "Power Drainer", "Drains the electricity", 0xBABABA, new byte[]{}, 20),
        new Crossing((byte) 0, (byte) 127, false, 's', "Crossing", "Makes electricity jump over it", 0xE8851C, new byte[]{}, 20),
        new Switch((byte) 0, (byte) 127, false, 's', "Switch", "Conducts if turned on", 0x00ED00, new byte[]{},20),
        new Fire((byte) 0, (byte) 5, false, 'g', "Fire", "Burns stuff", 0xDE2307, new byte[]{}, 200),
        new Element((byte) 2, (byte) 127, false, 's', "Wood", "Burns slowly", 0xC0A040, new byte[]{}, 20),
        new Element((byte) 10, (byte) 80, false, 'l', "Petroleum", "Oil, burns", 0x404010, new byte[]{}, 20),
        new Element((byte) 16, (byte) 15, false, 'p', "Sawdust", "Sawdust", 0xFFE0A0, new byte[]{}, 20),
        };
        //Order of creation: new Element(amount-of-burn,weight,conductive(boolean),state("g","p","s","l"), Name, Description, colour(HEX), reactives, default temp)
        int NUM_ELS = 19;
}
