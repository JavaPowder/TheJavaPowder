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
    Update update = new Update();
    Console console = new Console();
    FileSaver saver = new FileSaver();
    public void run() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                saver.savePref();
                System.exit(0);
            }
        });

        saver.loadPref();
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

        var.CursorX = (var.CurrentX - (var.ScrollX / var.winZoom)) * var.winZoom;
        var.CursorY = (var.CurrentY - (var.ScrollY / var.winZoom)) * var.winZoom;

        var.DrawX = (var.CurrentX + (var.ScrollX / 2)) / var.Zoom;//var.CurrentX / var.Zoom;
        var.DrawY = (var.CurrentY + (var.ScrollY / 2)) / var.Zoom;//var.CurrentY / var.Zoom;

        if (bufferGraphics == null) return;
        bufferGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());

        bufferGraphics.drawString("FPS:" + PaintFPS, 10, 30 + 45 * var.winZoom);//Draw the FPS
        bufferGraphics.drawString("Average FPS:" + PaintAFPS, 10, 30 + 55 * var.winZoom);//Draw the Average FPS
        bufferGraphics.drawString("Update FPS:" + UpdateFPS, 10, 30 + 65 * var.winZoom);//Draw the Update FPS
        bufferGraphics.drawString("Mousex:" + var.DrawX, 10, 30 + 75 * var.winZoom);//Draw the Coordinates
        bufferGraphics.drawString("Mousey:" + var.DrawY, 10, 30 + 85 * var.winZoom);//Draw the Mouse Coordinates

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
                    bufferGraphics.drawString("ID:" + var.Elements[var.Map[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom]].name, 10, 30 + 5 * var.winZoom);//Draw the Hovered Element Name
                else
                    bufferGraphics.drawString("ID: None", 10, 30 + 5 * var.winZoom);//Draw the Hovered Element Name

                bufferGraphics.drawString("Voltage:" + var.VMap[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom], 10, 30 + 15 * var.winZoom);//Draw the Hovered Voltage
                bufferGraphics.drawString("Property:" + var.PMap[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom], 10, 30 + 25 * var.winZoom);//Draw the Property Level
                bufferGraphics.drawString("Temperature:" + var.HMap[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom] + " C", 10, 30 + 35 * var.winZoom);//Draw the Temperature
            }

            if (var.state == 2)//If we are choosing an element
            {
                bufferGraphics.setColor(new Color(0x777777));
                for (int i = 0; i < var.Elements.length; i++) {
                    if ( i >= thumbnails.length || !bufferGraphics.drawImage(thumbnails[i], (50 * var.winZoom + i * 40 * var.winZoom) - var.iconX, 25 * var.winZoom + var.iconY, 40 * var.winZoom, 40 * var.winZoom, this)) {
                        bufferGraphics.drawImage(nonePng, (50 * var.winZoom + i * 40 * var.winZoom) - var.iconX, 25 * var.winZoom + var.iconY, 40 * var.winZoom, 40 * var.winZoom, this);
                        bufferGraphics.drawString(var.Elements[i].name, (52 * var.winZoom + i * 40 * var.winZoom) - var.iconX, 35 * var.winZoom + var.iconY);//Draw the Element's name before the picture appears
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


    // Context menu check box handler

    public void mouseDragged(MouseEvent e) {
        var.MouseX = e.getX();
        var.MouseY = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        var.MouseX = e.getX();
        var.MouseY = e.getY();
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
            if (xc >= 8/var.winZoom && xc <= 82/var.winZoom)
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
            else if (xc >= 90/var.winZoom && xc <= 198/var.winZoom)
            {
                TotalFPS = 0;
                TotalFrame = 0;
            }
            else if (xc >= 206/var.winZoom && xc <= 260/var.winZoom)
            {
                var.Equipped = -126;
            }
            else if (xc >= 268/var.winZoom && xc <= 376/var.winZoom)
            {
                var.Equipped = -125;
            }
            else if (xc >= 384/var.winZoom && xc <= 452/var.winZoom)
            {
                SaveFile();
            }
            else if (xc >= 460/var.winZoom && xc <= 528/var.winZoom)
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
            int x = 50 * var.winZoom, y = 25 * var.winZoom, num = 0;
            while (num < var.NUM_ELS) {
                if (var.CurrentX > x / var.winZoom && var.CurrentY > y / var.winZoom && var.CurrentX < x / var.winZoom + 40 && var.CurrentY < y / var.winZoom + 40) {
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
            if (var.CurrentX >= var.Width/2-80 && var.CurrentY >=  var.Height/2-40 && var.CurrentX < var.Width/2+100 && var.CurrentY < var.Height/2-20 && !var.antiDouble)
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
            if (var.CurrentX >= var.Width/2-80 && var.CurrentY >=  var.Height/2 && var.CurrentX < var.Width/2+100 && var.CurrentY < var.Height/2+20 && !var.antiDouble)
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
            if (var.CurrentX >= var.Width/2-80 && var.CurrentY >= var.Height/2+40 && var.CurrentX < var.Width/2+100 && var.CurrentY < var.Height/2+60 && !var.antiDouble)
            {
                var.antiDouble = true;
                String NZ = JOptionPane.showInputDialog(null,"Enter the new Screen's Zoom ( Normally 1 to 5");
                try
                {
                    int newZoom = Integer.parseInt(NZ);
                    if(newZoom >= 1 && newZoom <= 20)
                    {
                        var.winZoom = (byte)newZoom;
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
                var.realZoom = var.Zoom * var.winZoom - 1;
                if (var.Zoom == 1) {
                    var.ScrollX = 0;
                    var.ScrollY = 0;
                    var.realZoom = var.Zoom * var.winZoom;
                }
            }
            if (evt.getKeyChar() == 'z' && var.Zoom < 127) {
                console.printtxt("Z was pressed!");
                console.printtxt("Zooming in");
                console.printtxt("Current zoom: " + Byte.toString(var.Zoom));
                var.ScrollX = var.CurrentX;
                var.ScrollY = var.CurrentY;
                var.Zoom++;
                var.realZoom = var.Zoom * var.winZoom - 1;
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