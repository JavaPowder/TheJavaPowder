package thejavapowder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TheJavaPowder extends JFrame implements Runnable, ActionListener, ItemListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {
    /*Main class
       * Paints, Listener for Events and loops through all the things that need to be done
       *
       * Known Bugs to fix:
       *
       * When there's alot of particles, it doesn't draw smoothly
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
       */

    public static void main(String[] args) {
        new TheJavaPowder();
    }

    public TheJavaPowder() {
        run();
    }

    public static Variables var = new Variables();
    public static Update update = new Update();
    public static Console console = new Console();
    public static FileSaver saver = new FileSaver();

    public void run() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                saver.savePref();
                System.exit(0);
            }
        });

        init();

        while (!quit) {
            update.update();
            repaint();
        }
    }

    //Thread t;


    Image scaPng                = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/a-semiconductor.png"));
    Image scbPng                = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/b-semiconductor.png"));
    Image batteryPng            = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/battery.png"));
    Image coffeePng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/coffee.png"));
    Image copperPng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/copper.png"));
    //Image creditPng           = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/credit.png"));
    Image ironPng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/iron.png"));
    //Image logicGatePng        = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logicgate.png"));
    Image methanePng            = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/methane.png"));
    Image nonePng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/none.png"));
    Image powerDrainerPng       = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/power-drainer.png"));
    Image rechargableBatteryPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/rechargable-battery.png"));
    Image resistorPng           = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/resistor.png"));
    Image screenPng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/screen.png"));
    Image switchPng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/switch.png"));
    Image wallPng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/wall.png"));
    Image waterPng              = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/water.png"));
    Image crossingPng           = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/wire-crossing.png"));
    Image firePng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/fire.png"));
    Image woodPng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/wood.png"));
    Image petrolPng             = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/petrol.png"));

    Image playPng               = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/play.png"));
    Image settingsPng           = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/settings.png"));
    Image javaPowderPng         = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/javapowder.png"));
    Image consolePng            = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/console.png"));

    Image[] thumbnails = new Image[]{
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
            petrolPng};

    Graphics bufferGraphics;
    Image offscreen;
    Dimension dim;

    byte FPS;
    int PaintFPS    = 0;
    int UpdateFPS   = 0;
    int PaintAFPS   = 0;
    int WaitTime    = 0;
    int TotalFrame  = 0;
    long StartTime  = 0;
    long EndTime    = 0;
    long TotalFPS   = 0;
    float Time      = 1000;
    boolean quit    = false;

    // The console area.
    static JTextArea consolearea = new JTextArea("", 20, 40);
    //JScrollPane scrollPane = new JScrollPane(consolearea);

    //FileSaver fileSaver = new FileSaver();

    // Endline constant
    final String end = System.getProperty("line.separator");


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

    public void init() {
	    if(saver.hasPref())
	    {
			int[] settings = saver.getPref();
			var.Width = settings[0];
			var.Height = settings[1];
			var.winZoom = (byte)settings[2];
	    }
	    else
	    {
		    saver.savePref();
	    }
        var.Map = new byte[var.Width][var.Height];
        var.HMap = new float[var.Width][var.Height];
        var.VMap = new int[var.Width][var.Height];
        var.PMap = new byte[var.Width][var.Height];
		var.PrMap = new float [var.Width/4][var.Height/4];

        this.setVisible(true);
        this.setResizable(false);
        this.setBackground(Color.black);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);
        this.setSize(var.Width * var.winZoom, (var.Height + var.optionsHeight) * var.winZoom); //manually set your Frame's size

        dim = getSize();

        var.realZoom = var.Zoom * var.winZoom;
        var.DrawX = (var.CurrentX + (var.ScrollX / 2)) / var.Zoom;
        var.DrawY = (var.CurrentY + (var.ScrollY / 2)) / var.Zoom;
        var.LastDrawX = (var.CurrentX + (var.ScrollX / 2)) / var.Zoom;
        var.LastDrawY = (var.CurrentY + (var.ScrollY / 2)) / var.Zoom;
        offscreen = createImage(dim.width, dim.height);

        bufferGraphics = offscreen.getGraphics();
        // Initialises empty particle maps
        for (int x = 0; x < var.Width; x++) {
            for (int y = 0; y < var.Height; y++) {
                var.Map[x][y] = -127;
                var.PMap[x][y] = 0;
                var.VMap[x][y] = 0;
                var.HMap[x][y] = 0;
            }
        }
		for (int x = 0; x < var.Width/4; x++)
            for (int y = 0; y < var.Height/4; y++) {
				var.PrMap[x][y] = 0;
            }

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
        //var.Elements[2].react[15] = new byte[]{15, 0, 0, 0, 50, 0, 15, 3}; //Methane+Fire = Fire
        var.Elements[3].react[15] = new byte[]{3, 1, 0, 0, 50, 0, 3, 0}; //Water+Fire = Destroyed
    }




    @Override
    public void paint(Graphics g) {
        var.antiDouble = false;
        var.Brightness = 0;

        EndTime = System.currentTimeMillis();
        Time = EndTime - StartTime;
        FPS = (byte) (1000 / Time);
        TotalFPS += FPS;

        TotalFrame++;
        StartTime = System.currentTimeMillis();

        if (WaitTime > 29) {
            PaintFPS = FPS;
            UpdateFPS = (int) (update.TotalFPS / update.TotalFrame);
            PaintAFPS = (int) (TotalFPS / TotalFrame);
            WaitTime = 0;
        } else {
            WaitTime++;
        }

        var.CurrentX = (var.MouseX + var.ScrollX) / var.winZoom;
        var.CurrentY = (var.MouseY + var.ScrollY) / var.winZoom;

        var.DrawX = (var.MouseX + (var.ScrollX * var.winZoom)) / var.realZoom;
        var.DrawY = (var.MouseY + (var.ScrollY * var.winZoom)) / var.realZoom;

        if (bufferGraphics == null) return;
        bufferGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());



        // The Colouring loop
        if (var.state == 0 || var.state == 2 || var.state == 5) {//The game, the element menu or the console



            for (int x = 0; x < var.Width; x++) {
                for (int y = 0; y < var.Height; y++) {
                    if (var.Map[x][y] >= 0) {
                        int size = var.realZoom-1;//The - 1 makes the pixes looked zoomed in
                        if (var.Zoom == 1) size++;
                        bufferGraphics.setColor(new Color(var.Elements[var.Map[x][y]].colour));
                        bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, size, size);
                    }
                    if (var.VMap[x][y] > 1 && var.Map[x][y] != 9 && var.Map[x][y] != 5 && var.Map[x][y] != 11 && var.Map[x][y] != 10 && var.Map[x][y] != 13 && var.Map[x][y] != 14)//If there is Voltage but it's not the Screen, Battery or R-Battery
                    {
                        int size = var.realZoom-1;//The - 1 makes the pixes looked zoomed in
                        if (var.Zoom == 1) size++;
                        bufferGraphics.setColor(new Color(1f, 0f, 0f, var.VMap[x][y] / 1500f));//Make it Red depending on the Voltage
                        bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, size, size);//Draw
                    }
                }
            }



            bufferGraphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));

            if (var.Shape == 0) {
                int size = var.Size * var.realZoom;
                int x = var.MouseX;
                int y = var.MouseY;
                bufferGraphics.drawRect(x - size, y - size, size * 2, size *2);
            } else if (var.Shape == 1) {
                int x = var.MouseX, y = var.MouseY, rd = var.Size * var.realZoom;
                int tempy = y, oldy;
                for (int i = x - rd; i <= x; i++) {
                    oldy = tempy;
                    double distance = Math.sqrt(Math.pow((double) x - (double) i, (double) 2) + Math.pow((double) y - (double) tempy, (double) 2));
                    while (distance <= rd) {
                        tempy = tempy - 1;
                        distance = Math.sqrt(Math.pow((double) x - (double) i, (double) 2) + Math.pow((double) y - (double) tempy, (double) 2));
                    }
                    tempy = tempy + 1;
                    if (oldy != tempy) oldy = oldy - 1;
                    for (int j = tempy; j <= oldy; j++) {
                        if (i > 0 && j > 0 && i < var.Width * var.winZoom && j < var.Height * var.winZoom)
                            bufferGraphics.drawRect(i, j, 1, 0);
                        if (2 * x - i > 0 && j > 0 && 2 * x - i < var.Width * var.winZoom && j < var.Height * var.winZoom)
                            bufferGraphics.drawRect(2 * x - i, j, 1, 0);
                        if (i > 0 && 2 * y - j > 0 && i < var.Width * var.winZoom && 2 * y - j < var.Height * var.winZoom)
                            bufferGraphics.drawRect(i, 2 * y - j, 1, 0);
                        if (2 * x - i > 0 && 2 * y - j > 0 && 2 * x - i < var.Width * var.winZoom && 2 * y - j < var.Height * var.winZoom)
                            bufferGraphics.drawRect(2 * x - i, 2 * y - j, 1, 0);
                    }
                }
            }

            if ((var.MouseX / var.winZoom) < var.Width && var.MouseX > 0 && (var.MouseY / var.winZoom) < var.Height && var.MouseY > 0) {

	            bufferGraphics.setColor(Color.WHITE);

                if (var.Map[var.DrawX][var.DrawY] != -127)
                    bufferGraphics.drawString("ID:" + var.Elements[var.Map[var.DrawX][var.DrawY]].name, 10, 30 + 5 * var.winZoom);//Draw the Hovered Element Name
                else
                    bufferGraphics.drawString("ID: None", 10, 30 + 5 * var.winZoom);//Draw the Hovered Element Name

                bufferGraphics.drawString("Voltage:" + var.VMap[var.DrawX][var.DrawY], 10, 30 + 15 * var.winZoom);//Draw the Hovered Voltage
                bufferGraphics.drawString("Property:" + var.PMap[var.DrawX ][var.DrawY], 10, 30 + 25 * var.winZoom);//Draw the Property Level
                bufferGraphics.drawString("Temperature:" + var.HMap[var.DrawX][var.DrawY] + " C", 10, 30 + 35 * var.winZoom);//Draw the Temperature
				if (var.DrawX < var.Width-4 && var.DrawX >= 0 && var.DrawY < var.Height-4 && var.DrawY >= 0)
					if (var.PrMap[var.DrawX/4][var.DrawY/4] > .01)
						bufferGraphics.drawString("Pressure:" + var.PrMap[var.DrawX/4][var.DrawY/4], 10, 30 + 45 * var.winZoom);//Draw the Pressure
					else
					    bufferGraphics.drawString("Pressure:" + 0, 10, 30 + 45 * var.winZoom);//Draw the Pressure
				bufferGraphics.drawString("FPS:" + PaintFPS, 10, 30 + 55 * var.winZoom);//Draw the FPS
				bufferGraphics.drawString("Average FPS:" + PaintAFPS, 10, 30 + 65 * var.winZoom);//Draw the Average FPS
				bufferGraphics.drawString("Update FPS:" + UpdateFPS, 10, 30 + 75 * var.winZoom);//Draw the Update FPS
				bufferGraphics.drawString("Mousex:" + var.DrawX, 10, 30 + 85 * var.winZoom);//Draw the Mouse X Coordinate
				bufferGraphics.drawString("Mousey:" + var.DrawY, 10, 30 + 95 * var.winZoom);//Draw the Mouse Y Coordinate
            }

            if (var.state == 2)//If we are choosing an element
            {
                bufferGraphics.setColor(new Color(0x777777));
                for (int i = 0; i < var.Elements.length; i++) {
                    if ( i >= thumbnails.length || !bufferGraphics.drawImage(thumbnails[i], (50 * var.winZoom + i * 40 * var.winZoom) - var.iconX, 25 * var.winZoom + var.iconY, 40 * var.winZoom, 40 * var.winZoom, this)) {
                        bufferGraphics.drawImage(nonePng, (50 * var.winZoom + i * 40 * var.winZoom) - var.iconX, 25 * var.winZoom + var.iconY, 40 * var.winZoom, 40 * var.winZoom, this);
                        bufferGraphics.drawString(var.Elements[i].name, (52 * var.winZoom + i * 40 * var.winZoom) - var.iconX, 35 * var.winZoom + var.iconY);//Draw the Element's name before the picture appears
                    }
                    if (var.overEl == i)
                    {
                        bufferGraphics.drawString(var.Elements[i].description, 52 * var.winZoom, 125 * var.winZoom);//Draw the Element's description
                    }

                    if (i % 13 == 12) {
                        var.iconY += 40 * var.winZoom;
                        var.iconX += 40 * var.winZoom + i * 40 * var.winZoom;
                    }
                }
                var.iconY = 0;
                var.iconX = 0;
                bufferGraphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
            } else if (var.state == 5) {
                if (!bufferGraphics.drawImage(consolePng, 0, 25, var.Width * var.winZoom, var.Height / 3 * var.winZoom, this)) {
                    bufferGraphics.drawString("Derp", 300, 300);
                }
                bufferGraphics.setColor(new Color(0x00ED00));
                bufferGraphics.drawString("JavaPowder Console *Alpha*", 20, var.Height / 3 * var.winZoom + 5);
            }
        } else if (var.state == 1) {
            if (!bufferGraphics.drawImage(playPng, 504, 243, 204, 60, this) ||
                    !bufferGraphics.drawImage(settingsPng, 504, 343, 204, 60, this) ||
                    !bufferGraphics.drawImage(javaPowderPng, 404, 143, 404, 60, this)) {

                bufferGraphics.drawString("Derp", 300, 300);
            }
        } else if (var.state == 3) {//The settings menu

            bufferGraphics.setColor(new Color(0x00AA00));
            bufferGraphics.fillRect((var.Width/2-80)*var.winZoom, (var.Height/2-40)*var.winZoom, 160, 20);
            bufferGraphics.setColor(new Color(0x000000));
            bufferGraphics.drawString("Change the Window's Width", (var.Width/2-78)*var.winZoom, 12+(var.Height/2-40)*var.winZoom);

            bufferGraphics.setColor(new Color(0x00AA00));
            bufferGraphics.fillRect((var.Width/2-80)*var.winZoom, (var.Height/2)*var.winZoom, 160, 20);
            bufferGraphics.setColor(new Color(0x000000));
            bufferGraphics.drawString("Change the Window's Height", (var.Width/2-78)*var.winZoom, 12+(var.Height/2)*var.winZoom);

            bufferGraphics.setColor(new Color(0x00AA00));
            bufferGraphics.fillRect((var.Width/2-80)*var.winZoom, (var.Height/2+40)*var.winZoom, 160, 20);
            bufferGraphics.setColor(new Color(0x000000));
            bufferGraphics.drawString("Change the Window's Zoom", (var.Width/2-78)*var.winZoom, 12+(var.Height/2+40)*var.winZoom);

            bufferGraphics.setColor(new Color(0x00AA00));
            bufferGraphics.fillRect((var.Width/2-80)*var.winZoom, (var.Height/2+80)*var.winZoom, 160, 20);
            bufferGraphics.setColor(new Color(0x000000));
            bufferGraphics.drawString("Switch the background colour", (var.Width/2-78)*var.winZoom, 12+(var.Height/2+80)*var.winZoom);
        }

        bufferGraphics.setColor(new Color(0x00007F));
        bufferGraphics.fillRect(4*2, (var.Height+1) * var.winZoom, 37 * 2, 10 * var.winZoom);
        bufferGraphics.fillRect(45*2, (var.Height+1) * var.winZoom, 54 * 2, 10 * var.winZoom);
        bufferGraphics.fillRect(103*2, (var.Height+1) * var.winZoom, 27 * 2, 10 * var.winZoom);
        bufferGraphics.fillRect(134*2, (var.Height+1) * var.winZoom, 54 * 2, 10 * var.winZoom);
        bufferGraphics.fillRect(192*2, (var.Height+1) * var.winZoom, 34 * 2, 10 * var.winZoom);
        bufferGraphics.setColor(new Color(0x7F0000));
        bufferGraphics.fillRect(230*2, (var.Height+1) * var.winZoom, 34 * 2, 10 * var.winZoom);
        bufferGraphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
        bufferGraphics.drawString("Reset Scene", 5*2, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Reset Average FPS", 46*2, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Electricity", 104*2, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Change Properties", 135*2, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Save Scene", 193*2, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Load Scene", 231*2, (var.Height+8) * var.winZoom);
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
            int x = 50 * var.winZoom, y = 25 * var.winZoom, num = 0;
            while (num < var.NUM_ELS) {
                if (var.MouseX > x && var.MouseY > y && var.MouseX < x + (40*var.winZoom) && var.MouseY < y + (40*var.winZoom)) {
                    var.overEl = (byte)(num);
                }
                num++;
                x += 40 * var.winZoom;
                if (num % 13 == 0)
                {
                    x = 50 * var.winZoom;
                    y += 40 * var.winZoom;
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

        if (var.MouseY >= (var.Height+1)*var.winZoom && var.MouseY <= (var.Height+11)*var.winZoom)
        {
            int xc = var.MouseX;
            if (xc >= 8 && xc <= 82)
            {
                for (int x = var.Width - 1; x > 1; x--) {
                    for (int y = var.Height - 1; y > 1; y--)//For each Space
                    {
                        var.Map[x][y] = -127;
                        var.VMap[x][y] = 0;
                        var.PMap[x][y] = 0;
                    }

                }
                var.active = false;
                console.printtxt("Scene Reset.");
                var.active = false;
            }
            else if (xc >= 90 && xc <= 198)
            {
                TotalFPS = 0;
                TotalFrame = 0;
                var.active = false;
            }
            else if (xc >= 206 && xc <= 260)
            {
                var.Equipped = -126;
                var.active = false;
            }
            else if (xc >= 268 && xc <= 376)
            {
                var.Equipped = -125;
                var.active = false;
            }
            else if (xc >= 384 && xc <= 452)
            {
                FileSaver.SaveFile(JOptionPane.showInputDialog(null,"Enter a Save Name"));
                var.active = false;
            }
            else if (xc >= 460 && xc <= 528)
            {
                FileSaver.LoadFile(JOptionPane.showInputDialog(null,"Enter the Name of a Save to Open"));
                var.active = false;
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
            if (var.MouseX > 504 && var.MouseY > 243 && var.MouseX < 708 && var.MouseY < 303) {
                var.state = 0;
                var.active = false;
            }
            if (var.MouseX > 504 && var.MouseY > 340 && var.MouseX < 708 && var.MouseY < 400) {
                var.state = 3;
                var.active = false;
            }
        } else if (var.state == 2) {
            int x = 50 * var.winZoom, y = 25 * var.winZoom, num = 0;
            while (num < var.NUM_ELS) {
                if (var.MouseX > x && var.MouseY > y && var.MouseX < x + (40*var.winZoom) && var.MouseY < y + (40*var.winZoom)) {
                    var.Equipped = (byte)(num);
                    var.state = 0;
                    var.active = false;
                }
                num++;
                x += 40 * var.winZoom;
                if (num % 13 == 0)
                {
                    x = 50 * var.winZoom;
                    y += 40 * var.winZoom;
                }
            }
        }
        else if(var.state == 3)
        {
            if (var.MouseX >= (var.Width/2-80)*var.winZoom && var.MouseY >=  (var.Height/2-40)*var.winZoom && var.MouseX < (var.Width/2+100)*var.winZoom && var.MouseY < (var.Height/2-20)*var.winZoom && !var.antiDouble)
            {
                var.antiDouble = true;
                String NW = JOptionPane.showInputDialog(null,"Enter the new Screen's Width ( In pixels )");
                try
                {
                    int newWidth = Integer.parseInt(NW);
                    if(newWidth >= 100 && newWidth <= 1500)
                    {
                        var.Width = newWidth;
                        this.setSize(var.Width * var.winZoom, (var.Height + var.optionsHeight) * var.winZoom); //update the frame's size
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
            if (var.MouseX >= (var.Width/2-80)*var.winZoom && var.MouseY >=  (var.Height/2)*var.winZoom && var.MouseX < (var.Width/2+100)*var.winZoom && var.MouseY < (var.Height/2+20)*var.winZoom && !var.antiDouble)
            {
                var.antiDouble = true;
                String NH = JOptionPane.showInputDialog(null,"Enter the new Screen's Height ( In pixels )");
                try
                {
                    int newHeight = Integer.parseInt(NH);
                    if(newHeight >= 100 && newHeight <= 1500)
                    {
                        var.Height = newHeight;
                        this.setSize(var.Width * var.winZoom, (var.Height + var.optionsHeight) * var.winZoom); //update the frame's size
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
            if (var.MouseX >= (var.Width/2-80)*var.winZoom && var.MouseY >= (var.Height/2+40)*var.winZoom && var.MouseX < (var.Width/2+100)*var.winZoom && var.MouseY < (var.Height/2+60)*var.winZoom && !var.antiDouble)
            {
                var.antiDouble = true;
                String NZ = JOptionPane.showInputDialog(null,"Enter the new Screen's Zoom ( Normally 1 to 5");
                try
                {
                    int newZoom = Integer.parseInt(NZ);
                    if(newZoom >= 1 && newZoom <= 20)
                    {
                        var.winZoom = (byte)newZoom;
                        var.realZoom = var.Zoom * var.winZoom;
                        this.setSize(var.Width * var.winZoom, (var.Height + var.optionsHeight) * var.winZoom); //update the frame's size
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
            if (var.MouseX >= (var.Width/2-80)*var.winZoom && var.MouseY >= (var.Height/2+80)*var.winZoom && var.MouseX < (var.Width/2+100)*var.winZoom && var.MouseY < (var.Height/2+100)*var.winZoom && !var.antiDouble)
            {
                int blah1 = 0xfefefe;
                var.antiDouble = true;
                String NBGC = JOptionPane.showInputDialog(null,"Enter the new Screen's background colour ( Hex )");
                try
                {
                    //int newBG = Integer.parseInt(NBGC);
                    this.setBackground(new Color(blah1));
                }
                catch(Exception blah)
                {
                    javax.swing.JOptionPane.showMessageDialog(null,"Not a Hex Value ( 0xFFFFFF)");
                }
            }
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
                console.printtxt("Current zoom: " + Byte.toString(var.Zoom));
                var.antiDouble = true;
                var.ScrollX = var.ScrollX/var.Zoom*(var.Zoom-1);
                var.ScrollY = var.ScrollY/var.Zoom*(var.Zoom-1);
                var.Zoom--;
                var.realZoom = var.Zoom * var.winZoom;
                if (var.Zoom == 1) {
                    var.ScrollX = 0;
                    var.ScrollY = 0;
                }
            }
            if (evt.getKeyChar() == 'z' && var.Zoom < 127) {
                console.printtxt("Z was pressed!");
                console.printtxt("Zooming in");
                console.printtxt("Current zoom: " + Byte.toString(var.Zoom));
                var.ScrollX = var.CurrentX;
                var.ScrollY = var.CurrentY;
                var.Zoom++;
                var.realZoom = var.Zoom * var.winZoom;
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
                if (var.ScrollX > var.Width* var.Zoom - var.Width)
                    var.ScrollX = var.Width* var.Zoom - var.Width;
                if (var.ScrollY < 0)
                    var.ScrollY = 0;
                if (var.ScrollY > var.Height* var.Zoom - var.Height)
                    var.ScrollY = var.Height* var.Zoom - var.Height;
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
                var.DebugMode = !var.DebugMode;
                var.antiDouble = true;
            }
            if (evt.getKeyChar() == 'm')
            {
                if (var.currentMode == 0)
                    var.currentMode = 1;
                else
                    var.currentMode = 0;
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

    public void itemStateChanged(ItemEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
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
    public void nyan() {
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