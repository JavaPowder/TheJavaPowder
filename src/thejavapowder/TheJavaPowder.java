package thejavapowder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

public class TheJavaPowder extends JFrame implements Runnable, ActionListener, ItemListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {
    /*Main class
       * Paints, Listener for Events and loops through all the things that need to be done
       *
       * Known Bugs to fix:
       *
       * The Element menu doesn't work properly while zoomed/scrolled
       * Fix: Maybe add var.Zoom and Scroll consideration ( Not sure... )
       *
       * When saving the scene, two files are saved instead of one
       *
       * You still can't load scenes
       *
       *
       */

    public static void main(String[] args) {
        new TheJavaPowder();
    }

    public TheJavaPowder() {
        run();
    }

    public static Variables var = new Variables();
    Update update = new Update();
    Console console = new Console();

    public void run() {
        init();

        while (!quit) {
            update.update();
            repaint();
        }
    }

    //Thread t;


    Image scaPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/a-semiconductor.png"));
    Image scbPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/b-semiconductor.png"));
    Image batteryPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/battery.png"));
    Image coffeePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/coffee.png"));
    Image copperPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/copper.png"));
    //Image creditPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/credit.png"));
    Image ironPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/iron.png"));
    //Image logicGatePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logicgate.png"));
    Image methanePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/methane.png"));
    Image nonePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/none.png"));
    Image powerDrainerPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/power-drainer.png"));
    Image rechargableBatteryPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/rechargable-battery.png"));
    Image resistorPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/resistor.png"));
    Image screenPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/screen.png"));
    Image switchPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/switch.png"));
    Image wallPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/wall.png"));
    Image waterPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/water.png"));
    Image crossingPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/wire-crossing.png"));
    Image firePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/fire.png"));
    Image woodPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/wood.png"));
    Image petrolPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/petrol.png"));


    Image playPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/play.png"));
    Image settingsPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/settings.png"));
    Image javaPowderPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/javaPowder.png"));
    Image consolePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/console.png"));

    Image[] thumbnails = new Image[]{coffeePng, wallPng, methanePng, waterPng, ironPng, batteryPng, copperPng, scaPng, scbPng, screenPng, resistorPng, rechargableBatteryPng, powerDrainerPng, crossingPng, switchPng, firePng, woodPng, petrolPng};
    Graphics bufferGraphics;
    Image offscreen;
    Dimension dim;

    int PaintFPS = 0;
    int UpdateFPS = 0;
    int PaintAFPS = 0;

    long StartTime = 0;
    long EndTime = 0;
    long TotalFPS = 0;
    int TotalFrame = 0;

    int WaitTime = 0;

    byte FPS;
    float Time = 1000;

    byte versionID = 6;

    boolean quit = false;

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
        //Main screen options
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        // Initialises empty var.Map
        for (int x = 0; x < var.Width; x++) {
            for (int y = 0; y < var.Height; y++) {
                var.Map[x][y] = -127;
                var.PMap[x][y] = 0;
                var.VMap[x][y] = 0;
                var.HMap[x][y] = 0;
            }
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


    // Get type function


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

        if (bufferGraphics == null) return;
        bufferGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());

        bufferGraphics.drawString("Mousex:" + var.DrawX, 10, 90 * var.winZoom);//Draw the Coordinates
        bufferGraphics.drawString("Mousey:" + var.DrawY, 10, 100 * var.winZoom);//Draw the Mouse Coordinates
        bufferGraphics.drawString("FPS:" + PaintFPS, 10, 60 * var.winZoom);//Draw the FPS
        bufferGraphics.drawString("Average FPS:" + PaintAFPS, 10, 70 * var.winZoom);//Draw the Average FPS
        bufferGraphics.drawString("Update FPS:" + UpdateFPS, 10, 80 * var.winZoom);//Draw the Update FPS

        // The Colouring loop
        if (var.state == 0 || var.state == 2 || var.state == 5) {//The game, the element menu or the console


            for (int x = 0; x < var.Width; x++) {
                for (int y = 0; y < var.Height; y++) {
                    if (var.Map[x][y] >= 0) {
                        bufferGraphics.setColor(new Color(var.Elements[var.Map[x][y]].colour));
                        bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                    }
                    if (var.VMap[x][y] > 1 && var.Map[x][y] != 9 && var.Map[x][y] != 5 && var.Map[x][y] != 11 && var.Map[x][y] != 10 && var.Map[x][y] != 13 && var.Map[x][y] != 14)//If there is Voltage but it's not the Screen, Battery or R-Battery
                    {
                        bufferGraphics.setColor(new Color(1f, 0f, 0f, var.VMap[x][y] / 1500f));//Make it Red depending on the Voltage
                        bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.Zoom * var.winZoom, var.Zoom * var.winZoom);//Draw
                    }
                }
            }



            //bufferGraphics.fillRect((var.CurrentX - var.ScrollX / 2)* var.winZoom, (var.CurrentY - var.ScrollY / 2) * var.winZoom, (var.Size * var.Zoom) * var.winZoom, (var.Size * var.Zoom) * var.winZoom);//Draw the Cursor
            bufferGraphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));

            if (var.Shape == 0) {
                int size = var.Size * var.Zoom * var.winZoom;
                int x = var.CursorX;
                int y = var.CursorY;
                bufferGraphics.drawRect(x - size, y - size, size * 2, size *2);
            } else if (var.Shape == 1) {
                int x = var.CursorX, y = var.CursorY, rd = var.Size * var.Zoom * var.winZoom;
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

            if (var.CurrentX < var.Width * var.Zoom && var.CurrentX > 0 && var.CurrentY < var.Height * var.Zoom && var.CurrentY > 0) {

                if (var.Map[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom] != -127)
                    bufferGraphics.drawString("ID:" + var.Elements[var.Map[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom]].name, 10, 20 * var.winZoom);//Draw the Hovered Element Name
                else
                    bufferGraphics.drawString("ID: None", 10, 20 * var.winZoom);//Draw the Hovered Element Name

                bufferGraphics.drawString("Voltage:" + var.VMap[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom], 10, 30 * var.winZoom);//Draw the Hovered Voltage
                bufferGraphics.drawString("Property:" + var.PMap[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom], 10, 40 * var.winZoom);//Draw the Property Level
                bufferGraphics.drawString("Temperature:" + var.HMap[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom] + " C", 10, 50 * var.winZoom);//Draw the Temperature
            }

            if (var.state == 2)//If we are choosing an element
            {
                bufferGraphics.setColor(new Color(0x777777));
                for (int i = 0; i < var.Elements.length; i++) {
                    if ( i >= thumbnails.length || !bufferGraphics.drawImage(thumbnails[i], (100 + i * 40 * var.winZoom) - var.iconX, 50 + var.iconY, 40 * var.winZoom, 40 * var.winZoom, this)) {
                        bufferGraphics.drawImage(nonePng, (100 + i * 40 * var.winZoom) - var.iconX, 50 + var.iconY, 40 * var.winZoom, 40 * var.winZoom, this);
                        bufferGraphics.drawString(var.Elements[i].name, (105 + i * 40 * var.winZoom) - var.iconX, 70 + var.iconY);//Draw the Element's name before the picture appears
                    }
                    //System.out.println("" + i + "X " + ((100 + i * 40 * var.winZoom) - var.iconX) + "Y " + (50 + var.iconY) + "Final X " +  (((100 + i * 40 * var.winZoom) - var.iconX) + 40 * var.winZoom) + "Final Y " +  (( 50 + var.iconY) + (40 * var.winZoom)) );


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
            bufferGraphics.fillRect(100, 100, 160, 20);
            bufferGraphics.setColor(new Color(0x000000));
            bufferGraphics.drawString("Change the Window's Width", 102 , 120);
        }

        bufferGraphics.setColor(new Color(0x00007F));
        bufferGraphics.fillRect(4*var.winZoom, (var.Height+1) * var.winZoom, 37 * var.winZoom, 10 * var.winZoom);
        bufferGraphics.fillRect(45*var.winZoom, (var.Height+1) * var.winZoom, 54 * var.winZoom, 10 * var.winZoom);
        bufferGraphics.fillRect(103*var.winZoom, (var.Height+1) * var.winZoom, 27 * var.winZoom, 10 * var.winZoom);
        bufferGraphics.fillRect(134*var.winZoom, (var.Height+1) * var.winZoom, 54 * var.winZoom, 10 * var.winZoom);
        bufferGraphics.fillRect(192*var.winZoom, (var.Height+1) * var.winZoom, 34 * var.winZoom, 10 * var.winZoom);
        bufferGraphics.setColor(new Color(0x7F0000));
        bufferGraphics.fillRect(230*var.winZoom, (var.Height+1) * var.winZoom, 34 * var.winZoom, 10 * var.winZoom);
        bufferGraphics.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
        bufferGraphics.drawString("Reset Scene", 5*var.winZoom, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Reset Average FPS", 46*var.winZoom, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Electricity", 104*var.winZoom, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Change Properties", 135*var.winZoom, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Save Scene", 193*var.winZoom, (var.Height+8) * var.winZoom);
        bufferGraphics.drawString("Load Scene", 231*var.winZoom, (var.Height+8) * var.winZoom);
        g.drawImage(offscreen, 0, 0, this);
    }


    // Context menu check box handler

    public void mouseDragged(MouseEvent e) {

        var.CurrentX = (e.getX() + var.ScrollX) / var.winZoom;
        var.CurrentY = (e.getY() + var.ScrollY) / var.winZoom;

        var.CursorX = (var.CurrentX - var.ScrollX / 2) * var.winZoom;
        var.CursorY = (var.CurrentY - var.ScrollY / 2) * var.winZoom;

        var.DrawX = (var.CurrentX + (var.ScrollX / 2)) / var.Zoom;
        var.DrawY = (var.CurrentY + (var.ScrollY / 2)) / var.Zoom;
    }

    public void mouseMoved(MouseEvent e) {
        var.CurrentX = (e.getX() + var.ScrollX) / var.winZoom;
        var.CurrentY = (e.getY() + var.ScrollY) / var.winZoom;

        var.CursorX = (var.CurrentX - var.ScrollX / 2) * var.winZoom;
        var.CursorY = (var.CurrentY - var.ScrollY / 2) * var.winZoom;

        var.DrawX = (var.CurrentX + (var.ScrollX / 2)) / var.Zoom;
        var.DrawY = (var.CurrentY + (var.ScrollY / 2)) / var.Zoom;
    }


    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {

        if (var.CurrentY >= var.Height+1 && var.CurrentY <= var.Height+11)
        {
            int xc = var.CurrentX;
            if (xc >= 4 && xc <= 41)
            {
                for (int x = var.Width - 1; x > 1; x--) {
                    for (int y = var.Height - 1; y > 1; y--)//For each Space
                    {
                        var.Map[x][y] = -127;
                        var.VMap[x][y] = 0;
                        var.PMap[x][y] = 0;
                    }

                }
                console.printtxt("Scene Reset.");
            }
            else if (xc >= 45 && xc <= 99)
            {
                TotalFPS = 0;
                TotalFrame = 0;
            }
            else if (xc >= 103 && xc <= 130)
            {
                var.Equipped = -126;
            }
            else if (xc >= 134 && xc <= 188)
            {
                var.Equipped = -125;
            }
            else if (xc >= 192 && xc <= 226)
            {
                SaveFile();
            }
            else if (xc >= 230 && xc <= 264)
            {
            }
        }
        var.Drawing = true;
        if (var.state == 0) {

            if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
                var.leftClick = true;
            } else if (e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK) {
                var.leftClick = false;
            }

        }

        if (var.state == 1) {
            if (var.CurrentX > 504 / var.winZoom && var.CurrentY > 243 / var.winZoom && var.CurrentX < 708 / var.winZoom && var.CurrentY < 303 / var.winZoom) {
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 504 / var.winZoom && var.CurrentY > 340 / var.winZoom && var.CurrentX < 708 / var.winZoom && var.CurrentY < 400 / var.winZoom) {
                var.state = 3;
                var.active = false;
            }
        } else if (var.state == 2) {
            int x = 100, y = 50, num = 0;
            while (num < var.NUM_ELS) {
                if (var.CurrentX > x / var.winZoom && var.CurrentY > y / var.winZoom && var.CurrentX < (x+80) / var.winZoom && var.CurrentY < (y+80) / var.winZoom) {
                    var.Equipped = (byte)(num);
                    var.state = 0;
                    var.active = false;
                }
                num++;
                x += 80;
                if (num % 13 == 0)
                {
                    x = 100;
                    y += 80;
                }
            }
        }
        else if(var.state == 3)
        {
            if (var.CurrentX > 100 / var.winZoom && var.CurrentY >  100 / var.winZoom && var.CurrentX < 260 / var.winZoom && var.CurrentY < 120 / var.winZoom && !var.antiDouble)
            {
                var.antiDouble = true;
                String NW = JOptionPane.showInputDialog(null,"Enter the new Screen's Width ( In pixels )");
                try
                {
                    int newWidth = Integer.parseInt(NW);
                    if(newWidth < 1500 || newWidth > 100)
                    {
                        var.Width = newWidth;
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
            if (evt.getKeyChar() == 'x' && var.Zoom > 1) {
                var.realZoom = var.Zoom * var.winZoom;

                console.printtxt("X was pressed!");
                console.printtxt("Zooming out!");
                console.printtxt("Current zoom: " + Byte.toString(var.Zoom));
                var.antiDouble = true;
                if (var.Zoom == 2) {
                    var.Zoom--;
                    var.ScrollX = 0;
                    var.ScrollY = 0;
                } else {
                    var.Zoom--;
                    var.ScrollX *= 2;
                    var.ScrollY *= 2;
                }

            }
            if (evt.getKeyChar() == 'z') {
                var.realZoom = var.Zoom * var.winZoom;

                console.printtxt("Z was pressed!");
                console.printtxt("Zooming in");
                console.printtxt("Current zoom: " + Byte.toString(var.Zoom));
                var.Zoom++;
                var.ScrollX /= 2;
                var.ScrollY /= 2;
                var.antiDouble = true;
            }
            if (var.Zoom > 1) {
                if (evt.getKeyCode() == KeyEvent.VK_LEFT && var.ScrollX > 0)
                    var.ScrollX -= 5;
                if (evt.getKeyCode() == KeyEvent.VK_RIGHT && var.ScrollX < (var.Width - (var.Width / var.Zoom)) * var.Zoom)
                    var.ScrollX += 5;
                if (evt.getKeyCode() == KeyEvent.VK_UP && var.ScrollY > 0)
                    var.ScrollY -= 5;
                if (evt.getKeyCode() == KeyEvent.VK_DOWN && var.ScrollY < (var.Height - (var.Height / var.Zoom)) * var.Zoom)
                    var.ScrollY += 5;
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


    /*
          0 1 2
          3 X 4
          5 6 7
          (X = location of particle)
      */

    // File read and save code below
    File file;
    Writer writer;
    Random randomSaveName = new Random(1337);

    public void SaveFile() {
        try {
            console.printtxt("Saving...");
            file = new File("" + randomSaveName.nextInt() + ".jps");
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("javapowder-save\n");
            writer.write("version:" + versionID + "\n");
            writer.write("map:\n");
            for (int y = 0; y < var.Height; y++) {
                for (int x = 0; x < var.Width; x++) {
                    writer.write((int) var.Map[x][y]);
                }
            }
            writer.write("vmap:\n");
            for (int y = 0; y < var.Height; y++) {
                for (int x = 0; x < var.Width; x++) {
                    writer.write((int) var.VMap[x][y]);
                }
            }
            writer.write("pmap:\n");
            for (int y = 0; y < var.Height; y++) {
                for (int x = 0; x < var.Width; x++) {
                    writer.write((int) var.PMap[x][y]);
                }

                console.printtxt("Write ended successfully");
            }
        } catch (IOException excepti) {
            console.printtxt("I/O Exception!" + excepti.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
            console.printtxt("Write operation ended.");
        }

        /* // Old saving code
                if(filename != "")
                    file = new File(filename);
                else return 0;
                fos = new FileOutputStream(file);
                dos = new DataOutputStream(fos);
                // dataoutput stream has functions: writeInt, writeChars, writeByte
                // Data will be written like this:
                //0 3 6		  		|  ^->|
                //1 4 7			 	|  |  |
                //2 5 8	 etc	 	V->|  V
                dos.writeChars("*");
                for(int x = 0; x < var.Width; x++)
                {
                    for(int y = 0; y < var.Width; y++)
                    {
                        fos.write(var.Map[x][y]);

                    }
                }
                dos.writeChars("*");
                for(int x = 0; x < var.Width; x++)
                {
                    for(int y = 0; y < var.Width; y++)
                    {
                        fos.write(var.VMap[x][y]);

                    }
                }
                dos.writeChars("*");
                for(int x = 0; x < var.Width; x++)
                {
                    for(int y = 0; y < var.Width; y++)
                    {
                        fos.write(var.PMap[x][y]);


                    }
                }
                dos.writeChars("*");
            }
                catch (IOException Exc)
            {
                console.printtxt(Exc.getMessage());
                return 1;
            }
            finally
            {
                try {
                    fos.close();
                    dos.close();
                } catch (IOException e) {
                    //
                    e.printStackTrace();
                }

                return 2;*/
    }
    /*	// Call save code
          byte asdasdasd = saveFile(Name);
          switch asdasdasd
          {
              case 0: console.printtxt("Save file name not inserted"); break;
              case 1: console.printtxt("I/O Exception!"); break;
              case 2: console.printtxt("Save file correctly saved"); break;
              default: console.printtxt("Nothing has returned"); break;
          } */

    //public /*byte*/ void loadFile(String name) {
        /*

            FileInputStream read;
            InputStreamReader reader;
            for(int a = 0; a < var.Width * var.Height * 2; a += 2)
            {
            try {
                read = new FileInputStream(name);
                reader = new InputStreamReader(read);
                // It needs to split the file by byte and then read the array. Please learn File IO by streamreaders.
                // something
                File
                var.Map[a][] =
                var.VMap[++a][] =

            }
            }*/
    //}

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