package thejavapowder;
/* */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TheJavaPowder extends JFrame implements Runnable, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {
    /*Main class
       * Paints, Listener for Events and loops through all the things that need to be done
       *
       * Known Bugs to fix:
       *
       * When on pause, it doesn't draw smoothly
       * Fix: Unknown
       *
       * There's no cursor when you scroll down to 1 pixel
       * Fix: Draw an cursor when at lowest brush
       *
       * Screen doesn't work properly
       * Fix: Unknown
       *
       * The Setting Menu's Button shows 2 popups instead of 1
       *
       * You can't set the background color
       *
       */


    int Height          = 400;
    int Width           = 600;
    byte winZoom        = 2;


    byte[][]  Map         = new byte  [Width][Height];// The Particle type map
    float[][] HMap        = new float [Width][Height];// The Heat type map
    int[][]   VMap        = new int   [Width][Height];  // The Voltage type map
    byte[][]  PMap        = new byte  [Width][Height];// The Particle Properties Map
    byte[][]  LMap        = new byte  [Width][Height];// The Particle Life Map
    float[][] PrMap       = new float [Width/4][Height/4];// The Pressure Map
    float[][] VxMap       = new float [Width/4][Height/4];// The X Velocity Map
    float[][] VyMap       = new float [Width/4][Height/4];// The Y Velocity Map
    float[][] OldPrMap    = new float [Width/4][Height/4];// The Old Pressure Map
    float[][] OldVxMap    = new float [Width/4][Height/4];// The Old X Velocity Map
    float[][] OldVyMap    = new float [Width/4][Height/4];// The Old Y Velocity Map



    final Image scaPng                = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/a-semiconductor.png"));
    final Image scbPng                = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/b-semiconductor.png"));
    final Image batteryPng            = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/battery.png"));
    final Image coffeePng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/coffee.png"));
    final Image copperPng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/copper.png"));
    final Image ironPng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/iron.png"));
    //final Image logicGatePng        = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logicgate.png"));
    final Image methanePng            = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/methane.png"));
    final Image nonePng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/none.png"));
    final Image powerDrainerPng       = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/power-drainer.png"));
    final Image rechargableBatteryPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/rechargable-battery.png"));
    final Image resistorPng           = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/resistor.png"));
    final Image screenPng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/screen.png"));
    final Image switchPng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/switch.png"));
    final Image wallPng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/wall.png"));
    final Image waterPng              = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/water.png"));
    final Image crossingPng           = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/wire-crossing.png"));
    final Image firePng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/fire.png"));
    final Image woodPng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/wood.png"));
    final Image petrolPng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/petrol.png"));

    //Image creditPng           = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/credit.png"));
    //Image playPng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/play.png"));
    final Image settingsPng           = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/settings.png"));
    final Image javaPowderPng         = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/javapowder.png"));
    final Image consolePng            = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elements/console.png"));
    final Image edit_heightPng        = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/edit_height.png"));
    final Image edit_widthPng         = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/edit_width.png"));
    final Image elem_electricPng      = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elem_electric.png"));
    final Image elem_setpropPng       = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/elem_setprop.png"));
    final Image file_loadPng          = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/file_load.png"));
    final Image file_savePng          = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/file_save.png"));
    final Image braincleptPng         = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/Brainclept.png"));


    ImageTemplate[] images = new ImageTemplate[]{
            new ImageTemplate(elem_electricPng, 103 * winZoom, (Height)  * winZoom , 27 * 2, 30 * winZoom ),
            new ImageTemplate(elem_setpropPng, 134 * winZoom, (Height) * winZoom, 54 * 2, 30 * winZoom ),
            new ImageTemplate(file_loadPng, 192 * winZoom, (Height) * winZoom, 34 * 2, 30 * winZoom ),
            new ImageTemplate(file_savePng, 230 * winZoom, (Height) * winZoom, 34 * 2, 30 * winZoom ),
    };
    ImageTemplate[] imagesMenu = new ImageTemplate[]{
            new ImageTemplate(javaPowderPng, 202 * winZoom, 21 * winZoom, 202 * winZoom, 171 * winZoom ),
            new ImageTemplate(settingsPng, 252 * winZoom, 211 * winZoom, 102 * winZoom, 50 * winZoom ),
            new ImageTemplate(braincleptPng, 252 * winZoom, 301 * winZoom, 101 * winZoom, 85 * winZoom ),
    };

    public static void main(String []args) {
        new TheJavaPowder();
    }

    public TheJavaPowder() {
        run();
    }




    public static final  Variables var = new Variables();
    public static final Update update = new Update();
    public static final Console console = new Console();
	public static final Methods meth = new Methods();
	private static final FileSaver saver = new FileSaver();
	private final draw draw = new draw();
    public void run() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                saver.savePref(Width, Height, winZoom);
                System.exit(0);
            }
        });
	    long before;
	    long after;
	    long start;
        init();
	    before = System.nanoTime();
        while (!quit) {
	        start = System.nanoTime();
	        update.update(Width, Height, winZoom, Map, HMap, VMap, PMap, LMap, PrMap, VxMap, VyMap, OldPrMap, OldVxMap, OldVyMap);
	        after = System.nanoTime();
	        if(after - before > 40000000)
	        {
		        repaint();

		        before = System.nanoTime();
	        }
	        if(System.nanoTime()-start != 0)
	            var.PaintFPS = (int)(1000000000/(System.nanoTime()-start));
	        else
		        var.PaintFPS = 1337;     /* */

        }
    }

	private final Image[] thumbnails = new Image[]{
            coffeePng,
		    wallPng,
		    methanePng,
		    waterPng,
		    ironPng,
		    batteryPng,
		    copperPng,
		    scaPng,
		    scbPng,
		    screenPng,
		    resistorPng,
		    rechargableBatteryPng,
		    powerDrainerPng,
		    crossingPng,
		    switchPng,
		    firePng,
		    woodPng,
		    petrolPng,
		    nonePng,
		    nonePng,
		    nonePng,
		    nonePng,
		    nonePng};





	private Graphics bufferGraphics;
	private Image offscreen;
	private Dimension dim;

	private int TotalFrame  = 0;
	private long TotalFPS   = 0;
	private boolean quit    = false;


    // The console area.
    static final JTextArea consolearea = new JTextArea("", 20, 40);
    //JScrollPane scrollPane = new JScrollPane(consolearea);

    //FileSaver fileSaver = new FileSaver();

    // Endline constant
    private final String end = System.getProperty("line.separator");


    // a type value reminder, not all are gonna be variables you know
    /*Types:
       * 0 = Powder
       * 1 = Solid
       * 2 = Gas
       * 3 = Liquid
       * 4 = Electric
       *
       *
       */
	

    private void init() {
        if(saver.hasPref())
        {
            Width = saver.getPref()[0];
            Height = saver.getPref()[1];
            winZoom = (byte)saver.getPref()[2];
        }
        else
        {
            Width = 600;
            Height = 400;
            winZoom = 2;
        }

	    resetItems();
	    this.setIconImage(javaPowderPng);
        

        this.setVisible(true);
        this.setResizable(false);
        this.setBackground(Color.black);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);
        this.setSize(Width * winZoom, (Height + var.optionsHeight) * winZoom); //manually set your Frame's size

        dim = getSize();

        var.realZoom = var.Zoom * winZoom;
        var.DrawX = (var.CurrentX + (var.ScrollX / 2)) / var.Zoom;
        var.DrawY = (var.CurrentY + (var.ScrollY / 2)) / var.Zoom;
        var.LastDrawX = (var.CurrentX + (var.ScrollX / 2)) / var.Zoom;
        var.LastDrawY = (var.CurrentY + (var.ScrollY / 2)) / var.Zoom;
        offscreen = createImage(dim.width, dim.height);

        bufferGraphics = offscreen.getGraphics();

        consolearea.setVisible(false);
        consolearea.setFont(new Font("Courier New", Font.BOLD, 10));

        this.addMouseListener(this);
        this.addKeyListener(this);
        addMouseMotionListener(this);

        consolearea.append("Java Powder initialised." + end); // Added endline constant appending

        /*
           * Reactives Fields Meanings:
           *  0: Element it turns in
           *  1. Is the Reactive Destroyed ( 0 or 1 )
           *  2. Voltage it makes
           *  3. Properties changes
           *  4. Heat it makes
           *  5. Pressure it makes
           *  6. Elements it creates
           *  7. Amount of element created ( Defined in 6. )
           *  Others are free
           */
        var.Elements[0].react[3] = new byte[]{2, 1, 0, 0, 0, 0, 2, 1}; // Coffee+Water = Methane
        var.Elements[3].react[15] = new byte[]{3, 1, 0, 0, 50, 0, 3, 0}; //Water+Fire = Destroyed
    }

    public void resetItems()
    {
        int iconNum;
        int borders;
        images = new ImageTemplate[]{
                new ImageTemplate(elem_electricPng, 206, (Height)  * winZoom , 27 * winZoom, 30 * winZoom ),
                new ImageTemplate(elem_setpropPng, 206 + 31 * winZoom, (Height) * winZoom, 54 * winZoom, 30 * winZoom ),
                new ImageTemplate(file_loadPng, 206 + 89 * winZoom, (Height) * winZoom, 34 * winZoom, 30 * winZoom ),
                new ImageTemplate(file_savePng, 206 + 127 * winZoom, (Height) * winZoom, 34 * winZoom, 30 * winZoom ),
        };
        imagesMenu = new ImageTemplate[]{
                new ImageTemplate(javaPowderPng, 202 * winZoom, 21 * winZoom, 202 * winZoom, 171 * winZoom ),
                new ImageTemplate(settingsPng, 252 * winZoom, 211 * winZoom, 102 * winZoom, 50 * winZoom ),
                new ImageTemplate(braincleptPng, 252 * winZoom, 301 * winZoom, 101 * winZoom, 85 * winZoom ),
        };

        for(int i = 0; i < 50; i++)//For an good amount of times
        {
            if( i * (40 * winZoom) - 40 > Width)//If the total length is over the screen
            {
                iconNum = i - 1;//Set the maximum amount of icons
                borders = (Width - 40 * iconNum) / 2;//Calculate the free borders
            }
        }
        Map         = new byte  [Width][Height];// The Particle type map
        HMap        = new float [Width][Height];// The Heat type map
        VMap        = new int   [Width][Height];  // The Voltage type map
        PMap        = new byte  [Width][Height];// The Particle Properties Map
        LMap        = new byte  [Width][Height];// The Particle Life Map
        PrMap       = new float [Width/4][Height/4];// The Pressure Map
        VxMap       = new float [Width/4][Height/4];// The X Velocity Map
        VyMap       = new float [Width/4][Height/4];// The Y Velocity Map
        OldPrMap    = new float [Width/4][Height/4];// The Old Pressure Map
        OldVxMap    = new float [Width/4][Height/4];// The Old X Velocity Map
        OldVyMap    = new float [Width/4][Height/4];// The Old Y Velocity Map

        for (int x = 0; x < Width; x++) {
            for (int y = 0; y < Height; y++) {
                Map[x][y] = -127;
                PMap[x][y] = 0;
                VMap[x][y] = 0;
                HMap[x][y] = 0;
                LMap[x][y] = 0;
            }
        }
        for (int x = 0; x < Width/4; x++)
            for (int y = 0; y < Height/4; y++) {
                PrMap[x][y] = 0;
                VxMap[x][y] = 0;
                VyMap[x][y] = 0;
                OldPrMap[x][y] = 0;
                OldVxMap[x][y] = 0;
                OldVyMap[x][y] = 0;
            }

    }

    @Override
    public void paint(Graphics g) {

        var.antiDouble = false;
        var.Brightness = 0;


        var.CurrentX = (var.MouseX + var.ScrollX) / winZoom;
        var.DrawX = (var.MouseX + (var.ScrollX * winZoom)) / var.realZoom;

        var.CurrentY = (var.MouseY + var.ScrollY) / winZoom;
        var.DrawY = (var.MouseY + (var.ScrollY * winZoom)) / var.realZoom;

        if (bufferGraphics == null) return;
        bufferGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());



        // The Colouring loop
        if (var.state == 0 || var.state == 2 || var.state == 5) {//The game, the element menu or the console

	        bufferGraphics.setColor(new Color(0x00007F));
	        bufferGraphics.fillRect(4 * 2, (Height + 1) * winZoom, 37 * 2, 10 * winZoom);
	        bufferGraphics.fillRect(45 * 2, (Height + 1) * winZoom, 54 * 2, 10 * winZoom);

	        for(int i = 0; i < images.length; i++)
	        {
		        bufferGraphics.drawImage(images[i].image, images[i].x, images[i].y, images[i].Width, images[i].Height, this);
	        }
			bufferGraphics.setColor(Color.WHITE);
	        bufferGraphics.drawString("Reset Scene", 5*2, (Height+8) * winZoom);
	        bufferGraphics.drawString("Reset Average FPS", 46*2, (Height+8) * winZoom);


            for (int x = 0; x < Width; x++) {
                for (int y = 0; y < Height; y++) {
                    if (Map[x][y] >= 0) {
                        int size = var.realZoom-1;//The - 1 makes the pixes looked zoomed in
                        if (var.Zoom == 1) size++;
                        bufferGraphics.setColor(new Color(var.Elements[Map[x][y]].colour));
                        bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * winZoom, (y * var.Zoom - var.ScrollY) * winZoom, size, size);
                    }
                    if (VMap[x][y] > 1 && Map[x][y] != 9 && Map[x][y] != 5 && Map[x][y] != 11 && Map[x][y] != 10 && Map[x][y] != 13 && Map[x][y] != 14)//If there is Voltage but it's not the Screen, Battery or R-Battery
                    {
                        int size = var.realZoom-1;//The - 1 makes the pixes looked zoomed in
                        if (var.Zoom == 1) size++;
                        bufferGraphics.setColor(new Color(1f, 0f, 0f, VMap[x][y] / 1500f));//Make it Red depending on the Voltage
                        bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * winZoom, (y * var.Zoom - var.ScrollY) * winZoom, size, size);//Draw
                    }
                }
            }



            bufferGraphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
			draw.drawCursor(var.DrawX, var.DrawY, var.Size, bufferGraphics, Width, Height, winZoom);

            if (var.state == 0 && (var.MouseX / winZoom) < Width && var.MouseX > 0 && (var.MouseY / winZoom) < Height && var.MouseY > 0) {
	            bufferGraphics.setColor(Color.WHITE);

                if (Map[var.DrawX][var.DrawY] != -127)
                    bufferGraphics.drawString("ID:" + var.Elements[Map[var.DrawX][var.DrawY]].name, 10, 30 + 5 * winZoom);//Draw the Hovered Element Name
                else
                    bufferGraphics.drawString("ID: None", 10, 30 + 5 * winZoom);//Draw the Hovered Element Name

                bufferGraphics.drawString("Voltage:" + VMap[var.DrawX][var.DrawY], 10, 30 + 15 * winZoom);//Draw the Hovered Voltage
                bufferGraphics.drawString("Property:" + PMap[var.DrawX ][var.DrawY], 10, 30 + 25 * winZoom);//Draw the Property Level
                bufferGraphics.drawString("Temperature:" + HMap[var.DrawX][var.DrawY] + " C", 10, 30 + 35 * winZoom);//Draw the Temperature
				if (var.DrawX < Width-4 && var.DrawX >= 0 && var.DrawY < Height-4 && var.DrawY >= 0)
					bufferGraphics.drawString("Pressure:" + PrMap[var.DrawX/4][var.DrawY/4], 10, 30 + 45 * winZoom);//Draw the Pressure
				if (var.DrawX < Width-4 && var.DrawX >= 0 && var.DrawY < Height-4 && var.DrawY >= 0)
					bufferGraphics.drawString("X Velocity:" + VxMap[var.DrawX/4][var.DrawY/4], 10, 30 + 55 * winZoom);//Draw the Pressure
				if (var.DrawX < Width-4 && var.DrawX >= 0 && var.DrawY < Height-4 && var.DrawY >= 0)
					bufferGraphics.drawString("Y Velocity:" + VyMap[var.DrawX/4][var.DrawY/4], 10, 30 + 65 * winZoom);//Draw the Pressure
				bufferGraphics.drawString("FPS:" + var.PaintFPS, 10, 30 + 75 * winZoom);//Draw the FPS
				bufferGraphics.drawString("Mouse-x:" + var.DrawX, 10, 30 + 85 * winZoom);//Draw the Mouse X Coordinate
				bufferGraphics.drawString("Mouse-y:" + var.DrawY, 10, 30 + 95 * winZoom);//Draw the Mouse Y Coordinate
            }

            if (var.state == 2)//If we are choosing an element
            {
                bufferGraphics.setColor(new Color(0x777777));
                for (int i = 0; i < var.Elements.length; i++) {
	                if ( i >= thumbnails.length || thumbnails[i] == nonePng ||
		                !bufferGraphics.drawImage(
				         thumbnails[i],//The next icon
				        (50 * winZoom + i * 40 * winZoom) - var.iconX,
				         25 * winZoom + var.iconY,
				         40 * winZoom,
				         40 * winZoom, this)) {


	                    bufferGraphics.drawImage(nonePng, (50 * winZoom + i * 40 * winZoom) - var.iconX, 25 * winZoom + var.iconY, 40 * winZoom, 40 * winZoom, this);
                        bufferGraphics.drawString(var.Elements[i].name, (52 * winZoom + i * 40 * winZoom) - var.iconX, 35 * winZoom + var.iconY);//Draw the Element's name before the picture appears
                    }
                    if (var.overEl == i)
                    {
                        bufferGraphics.drawString(var.Elements[i].description, 52 * winZoom, 125 * winZoom);//Draw the Element's description
                    }

                    if (i % 13 == 12) {
                        var.iconY += 40 * winZoom;
                        var.iconX += 40 * winZoom + i * 40 * winZoom;
                    }
                }
                var.iconY = 0;
                var.iconX = 0;
                bufferGraphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
            } else if (var.state == 5) {
                if (!bufferGraphics.drawImage(consolePng, 0, 25, Width * winZoom, Height / 3 * winZoom, this)) {
                    bufferGraphics.drawString("Derp", 300, 300);
                }
                bufferGraphics.setColor(new Color(0x00ED00));
                bufferGraphics.drawString("JavaPowder Console *Alpha*", 20, Height / 3 * winZoom + 5);
            }
        } else if (var.state == 1) {

	        for(int i = 0; i < imagesMenu.length; i++)
	        {
		        bufferGraphics.drawImage(imagesMenu[i].image, imagesMenu[i].x, imagesMenu[i].y, imagesMenu[i].Width, imagesMenu[i].Height, this);
	        }

        } else if (var.state == 3) {//The settings menu

            bufferGraphics.setColor(new Color(0x00AA00));
            bufferGraphics.fillRect((Width/2-80)*winZoom, (Height/2-40)*winZoom, 160, 20);
            bufferGraphics.setColor(new Color(0x000000));
            bufferGraphics.drawString("Change the Window's Width", (Width/2-78)*winZoom, 12+(Height/2-40)*winZoom);

            bufferGraphics.setColor(new Color(0x00AA00));
            bufferGraphics.fillRect((Width/2-80)*winZoom, (Height/2)*winZoom, 160, 20);
            bufferGraphics.setColor(new Color(0x000000));
            bufferGraphics.drawString("Change the Window's Height", (Width/2-78)*winZoom, 12+(Height/2)*winZoom);

            bufferGraphics.setColor(new Color(0x00AA00));
            bufferGraphics.fillRect((Width/2-80)*winZoom, (Height/2+40)*winZoom, 160, 20);
            bufferGraphics.setColor(new Color(0x000000));
            bufferGraphics.drawString("Change the Window's Zoom", (Width/2-78)*winZoom, 12+(Height/2+40)*winZoom);

            bufferGraphics.setColor(new Color(0x00AA00));
            bufferGraphics.fillRect((Width/2-80)*winZoom, (Height/2+80)*winZoom, 160, 20);
            bufferGraphics.setColor(new Color(0x000000));
            bufferGraphics.drawString("Switch the background colour", (Width/2-78)*winZoom, 12+(Height/2+80)*winZoom);




        }

        g.drawImage(offscreen, 0, 0, this);
    }

    public void mouseDragged(MouseEvent e) {
        var.MouseX = e.getX();
        var.MouseY = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        var.MouseX = e.getX();
        var.MouseY = e.getY();
        if (var.state == 2)
        {
            int x = 50 * winZoom, y = 25 * winZoom, num = 0;
            while (num < var.NUM_ELS) {
                if (var.MouseX > x && var.MouseY > y && var.MouseX < x + (40*winZoom) && var.MouseY < y + (40*winZoom)) {
                    var.overEl = (byte)(num);
                }
                num++;
                x += 40 * winZoom;
                if (num % 13 == 0)
                {
                    x = 50 * winZoom;
                    y += 40 * winZoom;
                }
            }
        }
    }


    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {

        if (var.MouseY >= Height*winZoom)
        {
            int xc = var.MouseX;
			int yc = var.MouseY;
            if (xc >= 8 && xc <= 82 && yc <= (Height+11)*winZoom)
            {
                for (int x = Width - 1; x > 1; x--) {
                    for (int y = Height - 1; y > 1; y--)//For each Space
                    {
                        Map[x][y] = -127;
                        VMap[x][y] = 0;
                        PMap[x][y] = 0;
                        LMap[x][y] = 0;
                    }
                }
                for (int x = (Width - 1)/4; x >= 0; x--) {
                    for (int y = (Height - 1)/4; y >= 0; y--)//For each Space
                    {
	                    PrMap[x][y] = 0;
	                    VxMap[x][y] = 0;
	                    VyMap[x][y] = 0;
	                    OldPrMap[x][y] = 0;
	                    OldVxMap[x][y] = 0;
	                    OldVyMap[x][y] = 0;
                    }
                }
                console.printtxt("Scene Reset.");
                var.Drawing = false; var.active = false;
            }
            else if (xc >= 90 && xc <= 198 && yc <= (Height+11)*winZoom)
            {
                TotalFPS = 0;
                TotalFrame = 0;
                var.Drawing = false; var.active = false;
            }
            else if (xc >= images[0].x && xc <= images[0].x + images[0].Width && yc >= images[0].y && yc <= images[0].y + images[0].Height)
            {
                var.Equipped = -126;
                var.Drawing = false; var.active = false;
            }
            else if (xc >= images[1].x && xc <= images[1].x + images[1].Width && yc >= images[1].y && yc <= images[1].y + images[1].Height)
            {
                var.Equipped = -125;
                var.Drawing = false; var.active = false;
            }
            else if (xc >= images[2].x && xc <= images[2].x + images[2].Width && yc >= images[2].y && yc <= images[2].y + images[2].Height)
            {
	            FileSaver.LoadFile(JOptionPane.showInputDialog(null,"Enter the Name of a Save to Open"));
	            var.Drawing = false; var.active = false;
            }
            else if (xc >= images[3].x && xc <= images[3].x + images[3].Width)
            {
                FileSaver.SaveFile(JOptionPane.showInputDialog(null,"Enter a Save Name"), Map);
                var.Drawing = false; var.active = false;
            }

        }
        else if (var.state == 0) {
	        var.Drawing = true;
            if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
                var.leftClick = true;
            } else if (e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK) {
                var.leftClick = false;
            }

        }

        if (var.state == 1) {
            if (var.MouseX > imagesMenu[0].x && var.MouseY > imagesMenu[0].y && var.MouseX < imagesMenu[0].x + imagesMenu[0].Width && var.MouseY < imagesMenu[0].y + imagesMenu[0].Height) {
                var.state = 0;
                var.Drawing = false; var.active = false;
            }
            if (var.MouseX > imagesMenu[1].x && var.MouseY > imagesMenu[1].y && var.MouseX < imagesMenu[1].x + imagesMenu[1].Width && var.MouseY < imagesMenu[1].y + imagesMenu[1].Height) {
                var.state = 3;
                var.Drawing = false; var.active = false;
            }
        } else if (var.state == 2) {
            int x = 50 * winZoom, y = 25 * winZoom, num = 0;
            while (num < var.NUM_ELS) {
                if (var.MouseX > x && var.MouseY > y && var.MouseX < x + (40*winZoom) && var.MouseY < y + (40*winZoom)) {
                    var.Equipped = (byte)(num);
                    var.state = 0;
                    var.Drawing = false; var.active = false;
                }
                num++;
                x += 40 * winZoom;
                if (num % 13 == 0)
                {
                    x = 50 * winZoom;
                    y += 40 * winZoom;
                }
            }
        }
        else if(var.state == 3)
        {
            if (var.MouseX >= (Width/2-80)*winZoom && var.MouseY >=  (Height/2-40)*winZoom && var.MouseX < (Width/2+100)*winZoom && var.MouseY < (Height/2-20)*winZoom && !var.antiDouble)
            {
                var.antiDouble = true;
                String NW = JOptionPane.showInputDialog(null,"Enter the new Screen's Width ( In pixels )");
                try
                {
                    int newWidth = Integer.parseInt(NW);
                    if(newWidth >= 100 && newWidth <= 1500)
                    {
                        Width = newWidth;
                        this.setSize(Width * winZoom, (Height + var.optionsHeight) * winZoom); //update the frame's size
						dim = getSize();
						offscreen = createImage(dim.width, dim.height);
						bufferGraphics = offscreen.getGraphics();
                    }
                    else
                    {
                        javax.swing.JOptionPane.showMessageDialog(null,"The Number is incorrect");
                    }

                }
                catch(Exception blah)
                {
                    javax.swing.JOptionPane.showMessageDialog(null,"Not a Number");
                }
            }
            if (var.MouseX >= (Width/2-80)*winZoom && var.MouseY >=  (Height/2)*winZoom && var.MouseX < (Width/2+100)*winZoom && var.MouseY < (Height/2+20)*winZoom && !var.antiDouble)
            {
                var.antiDouble = true;
                String NH = JOptionPane.showInputDialog(null,"Enter the new Screen's Height ( In pixels )");
                try
                {
                    int newHeight = Integer.parseInt(NH);
                    if(newHeight >= 100 && newHeight <= 1500)
                    {
                        Height = newHeight;
                        this.setSize(Width * winZoom, (Height + var.optionsHeight) * winZoom); //update the frame's size
						dim = getSize();
						offscreen = createImage(dim.width, dim.height);
						bufferGraphics = offscreen.getGraphics();
                    }
                    else
                    {
                        javax.swing.JOptionPane.showMessageDialog(null,"The Number is incorrect");
                    }

                }
                catch(Exception blah)
                {
                    javax.swing.JOptionPane.showMessageDialog(null,"Not a Number");
                }
            }
            if (var.MouseX >= (Width/2-80)*winZoom && var.MouseY >= (Height/2+40)*winZoom && var.MouseX < (Width/2+100)*winZoom && var.MouseY < (Height/2+60)*winZoom && !var.antiDouble)
            {
                var.antiDouble = true;
                String NZ = JOptionPane.showInputDialog(null,"Enter the new Screen's Zoom ( Normally 1 to 5");
                try
                {
                    int newZoom = Integer.parseInt(NZ);
                    if(newZoom >= 1 && newZoom <= 20)
                    {
                        winZoom = (byte)newZoom;
                        var.realZoom = var.Zoom * winZoom;
                        this.setSize(Width * winZoom, (Height + var.optionsHeight) * winZoom); //update the frame's size
						dim = getSize();
						offscreen = createImage(dim.width, dim.height);
						bufferGraphics = offscreen.getGraphics();
                    }
                    else
                    {
                        javax.swing.JOptionPane.showMessageDialog(null,"The Number is incorrect");
                    }

                }
                catch(Exception blah)
                {
                    javax.swing.JOptionPane.showMessageDialog(null,"Not a Number");
                }
            }
            if (var.MouseX >= (Width/2-80)*winZoom && var.MouseY >= (Height/2+80)*winZoom && var.MouseX < (Width/2+100)*winZoom && var.MouseY < (Height/2+100)*winZoom && !var.antiDouble)
            {
                int blah1 = 0xfefefe;
                var.antiDouble = true;
                String NBGC = JOptionPane.showInputDialog(null,"Enter the new Screen's background colour ( Hex )");
                try
                {
                    int newBG = Integer.parseInt(NBGC);
                    this.setBackground(new Color(blah1));
                }
                catch(Exception blah)
                {
                    javax.swing.JOptionPane.showMessageDialog(null,"Not a Hex Value ( 0xFFFFFF)");
                }
            }
            resetItems();
        }
    }

    public void mouseReleased(MouseEvent e) {
        var.Drawing = false;
    }

    public void keyPressed(KeyEvent evt) {
        if(!var.antiDouble)
        {
            if (evt.getKeyChar() == 'c') {
                if (var.state == 0)
                    var.state = 5;
                else if (var.state == 5)
                    var.state = 0;
                var.antiDouble = true;
            }
            if (evt.getKeyChar() == 'n') {
                nyan();
            }
            if (evt.getKeyChar() == 'p') {
                console.printtxt("P was pressed!");
                console.printtxt("Pausing / Unpausing");
                var.Simulating = !var.Simulating;
                var.antiDouble = true;
            }
			if (evt.getKeyChar() == 'f') {
				var.tempSimulating = !var.tempSimulating;
				var.Simulating = false;
				var.antiDouble = true;
            }
            if (evt.getKeyChar() == 's') {
                if (var.Shape == 0)
                    var.Shape = 1;
                else
                    var.Shape = 0;
                var.antiDouble = true;
            }
            if (evt.getKeyChar() == 'o') {
                if (var.state == 0)
                    var.state = 3;
                else
                    var.state = 0;
                var.antiDouble = true;
            }
            if (evt.getKeyChar() == 'x' && var.Zoom > 1) {
                console.printtxt("X was pressed!");
                console.printtxt("Zooming out!");
                console.printtxt("");
                console.printtxt("---");
                console.printtxt("Current zoom: " + Byte.toString(var.Zoom));
                var.antiDouble = true;
                var.ScrollX = var.ScrollX/var.Zoom*(var.Zoom-1);
                var.ScrollY = var.ScrollY/var.Zoom*(var.Zoom-1);
                var.Zoom--;
                var.realZoom = var.Zoom * winZoom;
                if (var.Zoom == 1) {
                    var.ScrollX = 0;
                    var.ScrollY = 0;
                }
            }
            if (evt.getKeyChar() == 'z' && var.Zoom < 127) {
                console.printtxt("Z was pressed!");
                console.printtxt("Zooming in");
                console.printtxt("");
                console.printtxt("---");
                console.printtxt("Current zoom: " + Byte.toString(var.Zoom));
                var.ScrollX = var.CurrentX;
                var.ScrollY = var.CurrentY;
                var.Zoom++;
                var.realZoom = var.Zoom * winZoom;
                var.antiDouble = true;
            }
            if (var.Zoom > 1) {
                if (evt.getKeyCode() == KeyEvent.VK_LEFT)
                    var.ScrollX -= 5;
                if (evt.getKeyCode() == KeyEvent.VK_RIGHT)
                    var.ScrollX += 5;
                if (evt.getKeyCode() == KeyEvent.VK_UP)
                    var.ScrollY -= 5;
                if (evt.getKeyCode() == KeyEvent.VK_DOWN)
                    var.ScrollY += 5;
                if (var.ScrollX < 0)
                    var.ScrollX = 5;
                if (var.ScrollX > Width* var.Zoom - Width)
                    var.ScrollX = Width* var.Zoom - Width;
                if (var.ScrollY < 0)
                    var.ScrollY = 0;
                if (var.ScrollY > Height* var.Zoom - Height)
                    var.ScrollY = Height* var.Zoom - Height;
            }
            if (evt.getKeyCode() == KeyEvent.VK_ESCAPE && !(var.state == 2)) {
                switch (var.state) {
                    case 0:
                        var.state = 1;
                        break;
                    case 3:
                        var.state = 1;
                        break;
                    default:
                        var.state = 0;
                        break;
                }
                var.antiDouble = true;
            }
            if (evt.getKeyCode() == KeyEvent.VK_SPACE && !(var.state == 1)) {
                if (var.state == 2) {
                    var.state = 0;
                } else {
                    var.state = 2;
                }

                var.antiDouble = true;
            }
            if (evt.getKeyChar() == 'd')
            {
                update.setDebug(!update.simDebug());
                var.antiDouble = true;
            }
            if (evt.getKeyChar() == 'm')
            {
                update.setElectricity(!update.simElectricity());
                var.antiDouble = true;
            }
        }
    }


    public void keyReleased(KeyEvent evt) {
    }

    public void keyTyped(KeyEvent evt) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        var.Size -= e.getWheelRotation();
        if (var.Size < 0) var.Size = 0;
        if (var.Size > 50) var.Size = 50;
    }

    /*			___________
       ===	 == /		   \
     = === ==� ==|  . .	   |
     = === === �� .   .  .  /\__/\
     = === === =�� .	 . | ^ ^ |
     = === === ==|		 |  w  |
     = === === == \________\_____/
     =	 ===   �� ��	  ��  ��
     */
    private void nyan() {
        String[] nyan = new String[10];
        nyan[0] = "			   ___________";
        nyan[1] = "  ===	 ==  /		   \\";
        nyan[2] = "= === ==� == |  . .	   |";
        nyan[3] = "= === ==� == |  . .	   |";
        nyan[4] = "= === ===��  | .   .  /\\__/\\";
        nyan[5] = "= === === =  �� .	.| ^ ^ |";
        nyan[6] = "= === === == |		|  w  |";
        nyan[7] = "= === === == \\________\\_____/";
        nyan[8] = "=	 ===   �� ��	  ��  ��";
        nyan[9] = "Artwork by boxmein.";
        console.printtxt("");
        for (int nyani = 9; nyani > 0; nyani--)
            console.printtxt(nyan[nyani]);

    }
}