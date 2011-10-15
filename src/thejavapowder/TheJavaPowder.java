package thejavapowder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

@SuppressWarnings("static-access")
public class TheJavaPowder extends JFrame implements Runnable, ActionListener, ItemListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {
    static final long serialVersionUID = 0; // I have no idea what this does, but it prevents a warning
    /*Main class
       * Paints, Listener for Events and loops through all the things that need to be done
       *
       * Known Bugs to fix:
       *
       * The Element menu doesn't work properly while zoomed/scrolled
       * Fix: Maybe add var.Zoom and Scroll consideration ( Not sure... )
       *
       *
       *
       *
       *
       *
       */

    public static void main(String[] args) {
        new TheJavaPowder();
    }

    public TheJavaPowder() {
        run();
    }

    Variables var = new Variables();
    Update update = new Update();
    Console console = new Console();

    public void run() {
        init();

        while (true) {
            update.update();
            repaint();
        }
    }

    Thread t;


    Image scaPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/a-semiconductor.png"));
    Image scbPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/b-semiconductor.png"));
    Image batteryPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/battery.png"));
    Image coffeePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/coffee.png"));
    Image copperPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/copper.png"));
    Image creditPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/credit.png"));
    Image ironPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/iron.png"));
    Image logicGatePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logicgate.png"));
    Image methanePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/methane.png"));
    Image powerDrainerPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/power-drainer.png"));
    Image rechargableBatteryPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/rechargable-battery.png"));
    Image resistorPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/resistor.png"));
    Image screenPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/screen.png"));
    Image switchPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/switch.png"));
    Image wallPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/wall.png"));
    Image waterPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/water.png"));
    Image crossingPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/wire-crossing.png"));

    Image playPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/play.png"));
    Image settingsPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/settings.png"));
    Image javaPowderPng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/javaPowder.png"));
    Image consolePng = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/console.png"));

    Image[] thumbnails = new Image[]{coffeePng, wallPng, methanePng, waterPng, ironPng, batteryPng, copperPng, scaPng, scbPng, screenPng, resistorPng, rechargableBatteryPng, powerDrainerPng, crossingPng, switchPng, logicGatePng};
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

    // The buttons

    JButton ElectricityB = new JButton("Electricity");

    JButton ChangePB = new JButton("Change Properties");


    JButton Save = new JButton("Save Scene");
    JButton Load = new JButton("Load Scene");
    JButton ResetFPS = new JButton("Reset Average FPS");
    JButton Reset = new JButton("Reset Scene");
    // the labels
    JLabel FPSCounter = new JLabel("1337");
    JLabel AverageFPS = new JLabel("1337");
    // The console area.
    static JTextArea consolearea = new JTextArea("", 20, 40);
    JScrollPane scrollPane = new JScrollPane(consolearea);

    // Menubar!
    JMenuBar menubar1 = new JMenuBar();

    JMenuItem menuItem1 = new JMenuItem("About");
    JMenuItem menuItem2 = new JMenuItem("Forums");

    JCheckBoxMenuItem cbMenuItem2 = new JCheckBoxMenuItem("var.DebugMode");
    JCheckBoxMenuItem cbMenuItem1 = new JCheckBoxMenuItem("Paused");

    JMenu menu1 = new JMenu("Options");
    JMenu menu2 = new JMenu("Help");
    JMenu menu3 = new JMenu("Modes");
    JMenu menu4 = new JMenu("Views");

    JRadioButtonMenuItem mode1 = new JRadioButtonMenuItem("Default");
    JRadioButtonMenuItem mode2 = new JRadioButtonMenuItem("Electronic");

    JRadioButtonMenuItem display1 = new JRadioButtonMenuItem("Default");
    JRadioButtonMenuItem display2 = new JRadioButtonMenuItem("Electronic");

    ButtonGroup menu2_radio_group = new ButtonGroup();
    ButtonGroup menu4_radio_group = new ButtonGroup();

    FileSaver fileSaver = new FileSaver();

    final String end = System.getProperty("line.separator");
    // Endline constant


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

    // The maps


    @SuppressWarnings("static-access")
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
        this.setSize(var.Width * var.winZoom, var.Height * var.winZoom); //manually set your Frame's size

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
            }
        }
        // Create the button; the icon will appear to the left of the label
        // Creates the GUI
        JFrame frame = new JFrame("Java Powder");
        JPanel panel = new JPanel();

        frame.setResizable(false);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        consolearea.setVisible(false);

        panel.add(ElectricityB);
        panel.add(ChangePB);


        panel.add(Save);
        panel.add(Load);

        panel.add(Reset);
        panel.add(FPSCounter);
        panel.add(AverageFPS);
        panel.add(ResetFPS);

        panel.add(consolearea);
        panel.add(scrollPane);
        consolearea.setEditable(false);
        consolearea.setLineWrap(true);

        // ContextMenu added in 0.2


        menubar1.add(menu1);
        menubar1.add(menu2);
        menubar1.add(menu3);
        menubar1.add(menu4);

        menu3.add(mode1);
        menu3.add(mode2);

        menu2_radio_group.add(mode1);
        menu2_radio_group.add(mode2);

        menu4_radio_group.add(display1);
        menu4_radio_group.add(display2);

        cbMenuItem1.setMnemonic(KeyEvent.VK_P);
        menu1.add(cbMenuItem1);

        cbMenuItem2.setMnemonic(KeyEvent.VK_D);
        menu1.add(cbMenuItem2);

        menu2.add(menuItem1);
        menu2.add(menuItem2);

        menu4.add(display1);
        menu4.add(display2);

        consolearea.setFont(new Font("Courier New", Font.BOLD, 10));

        frame.setJMenuBar(menubar1);
        // Adds action listeners

        ElectricityB.addActionListener(this);
        ChangePB.addActionListener(this);


        Reset.addActionListener(this);
        Save.addActionListener(this);
        Load.addActionListener(this);

        this.addMouseListener(this);
        this.addKeyListener(this);
        addMouseMotionListener(this);
        frame.add(panel);
        // for context menu

        cbMenuItem1.addItemListener(this);
        cbMenuItem2.addItemListener(this);
        mode1.addItemListener(this);
        mode2.addItemListener(this);
        menuItem1.addActionListener(this);
        menuItem1.addActionListener(this);
        display1.addActionListener(this);
        display2.addActionListener(this);

        menu4_radio_group.add(mode2);
        menu4_radio_group.add(mode1);

        menubar1.add(menu1);
        menubar1.add(menu2);
        menubar1.add(menu3);

        menu1.add(cbMenuItem1);
        menu1.add(cbMenuItem2);

        menu2.add(menuItem1);
        menu2.add(menuItem2);

        menu3.add(mode1);
        menu3.add(mode2);

        consolearea.append("Java Powder initialised." + end); // Added endline constant appending

        //Elements Initialization
        //Order of creation: new Element(amount-of-burn,weight,conductive(boolean),state("g","p","s","l"), Name, Description, colour(HEX))

        var.Coffee.react[3] = new byte []{2,1,0,0,0,0,2};
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

        if (bufferGraphics == null) return;
        bufferGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());

        // The Colouring loop
        if (var.state == 0 || var.state == 2 || var.state == 5) {


            for (int x = 0; x < var.Width; x++) {
                for (int y = 0; y < var.Height; y++) {
                    switch (var.Map[x][y]) {
                        case 0://Coffee
                            bufferGraphics.setColor(new Color(var.Coffee.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 1://Wall
                            bufferGraphics.setColor(Color.gray);
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 2://Methane
                            bufferGraphics.setColor(new Color(var.Methane.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 3://Water
                            bufferGraphics.setColor(Color.blue);
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 4://Iron
                            bufferGraphics.setColor(Color.darkGray);
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 5://Battery
                            bufferGraphics.setColor(Color.green);
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 6://Copper
                            bufferGraphics.setColor(new Color(var.Copper.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 7://Semi Conductor A
                            bufferGraphics.setColor(new Color(var.SemiConductorA.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 8://Semi Conductor B
                            bufferGraphics.setColor(new Color(var.SemiConductorB.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 9://Screen
                            bufferGraphics.setColor(new Color(getVoltage(x, y), getVoltage(x, y), getVoltage(x, y)));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 10://Resistor
                            bufferGraphics.setColor(new Color(var.Resistor.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 11://Rechargable Battery
                            bufferGraphics.setColor(new Color(var.RechargableBattery.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 12://Power Drainer
                            bufferGraphics.setColor(new Color(var.PowerDrainer.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 13://Crossing
                            bufferGraphics.setColor(new Color(var.Crossing.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        case 14://Switch
                            bufferGraphics.setColor(new Color(var.Resistor.colour));
                            bufferGraphics.fillRect((x * var.Zoom - var.ScrollX) * var.winZoom, (y * var.Zoom - var.ScrollY) * var.winZoom, var.realZoom, var.realZoom);
                            break;
                        default:
                            break;
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
                bufferGraphics.drawLine(x - size, y - size, x + size, y - size);//Draw the Cursor
                bufferGraphics.drawLine(x - size, y - size, x - size, y + size);//Draw the Cursor
                bufferGraphics.drawLine(x + size, y - size, x + size, y + size);//Draw the Cursor
                bufferGraphics.drawLine(x - size, y + size, x + size, y + size);//Draw the Cursor
            } else if (var.Shape == 1) {
                int x = var.CursorX, y = var.CursorY, rd = var.Size * var.Zoom * var.winZoom;
                int tempy = y, oldy = y;
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

            if (var.CurrentX < var.Width * var.Zoom - var.Size && var.CurrentX > 0 && var.CurrentY < var.Height * var.Zoom - var.Size && var.CurrentY > 0) {

                bufferGraphics.drawString("ID:" + var.Map[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom], 10, 20 * var.winZoom);//Draw the Hovered Element ID
                bufferGraphics.drawString("Voltage:" + var.VMap[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom], 10, 30 * var.winZoom);//Draw the Hovered Voltage
                bufferGraphics.drawString("Property:" + var.PMap[var.CurrentX / var.Zoom][var.CurrentY / var.Zoom], 10, 40 * var.winZoom);//Draw the Property Level
                if (WaitTime > 29) {
                    PaintFPS = FPS;
                    UpdateFPS = (int) (update.TotalFPS / update.TotalFrame);
                    PaintAFPS = (int) (TotalFPS / TotalFrame);
                    WaitTime = 0;
                } else {
                    WaitTime++;
                }
                bufferGraphics.drawString("FPS:" + PaintFPS, 10, 50 * var.winZoom);//Draw the FPS
                bufferGraphics.drawString("Average FPS:" + PaintAFPS, 10, 60 * var.winZoom);//Draw the Average FPS
                bufferGraphics.drawString("Update FPS:" + UpdateFPS, 10, 70 * var.winZoom);//Draw the Average FPS
                bufferGraphics.drawString("Mousex:" + var.DrawX, 10, 80 * var.winZoom);//Draw the Coordinates
                bufferGraphics.drawString("Mousey:" + var.DrawY, 10, 90 * var.winZoom);//Draw the Mouse Coordinates
            }

            if (var.state == 2)//If we are choosing an element
            {
                for (int i = 0; i < thumbnails.length; i++) {
                    if (bufferGraphics.drawImage(thumbnails[i], (100 + i * 40 * var.winZoom) - var.iconX, 50 + var.iconY, 40 * var.winZoom, 40 * var.winZoom, this) == false) {
                        bufferGraphics.drawString("Deeerp", 100, 70 * var.winZoom);//Draw the Average FPS
                    }
                    //System.out.println("" + i + "X " + ((100 + i * 40 * var.winZoom) - var.iconX) + "Y " + (50 + var.iconY) + "Final X " +  (((100 + i * 40 * var.winZoom) - var.iconX) + 40 * var.winZoom) + "Final Y " +  (( 50 + var.iconY) + (40 * var.winZoom)) );


                    if (i == 12 || i == 24 || i == 36) {
                        var.iconY += 40 * var.winZoom;
                        var.iconX += 40 * var.winZoom + i * 40 * var.winZoom;
                    }
                }
                var.iconY = 0;
                var.iconX = 0;
            } else if (var.state == 5) {
                if (bufferGraphics.drawImage(consolePng, 0, 25, var.Width * var.winZoom, var.Height / 3 * var.winZoom, this) == false) {
                    bufferGraphics.drawString("Derp", 300, 300);
                }
                bufferGraphics.setColor(new Color(0x00ED00));
                bufferGraphics.drawString("JavaPowder Console *Alpha*", 20, var.Height / 3 * var.winZoom + 5);
            }
        } else if (var.state == 1) {

            if (bufferGraphics.drawImage(playPng, 504, 243, 204, 60, this) == false ||
                    bufferGraphics.drawImage(settingsPng, 504, 343, 204, 60, this) == false ||
                    bufferGraphics.drawImage(javaPowderPng, 404, 143, 404, 60, this) == false) {

                bufferGraphics.drawString("Derp", 300, 300);
            }
        }

        g.drawImage(offscreen, 0, 0, this);
    }


    // Context menu check box handler

    @SuppressWarnings("static-access")
    public void mouseDragged(MouseEvent e) {

        var.CurrentX = (e.getX() + var.ScrollX) / var.winZoom;
        var.CurrentY = (e.getY() + var.ScrollY) / var.winZoom;

        var.CursorX = (var.CurrentX - var.ScrollX / 2) * var.winZoom;
        var.CursorY = (var.CurrentY - var.ScrollY / 2) * var.winZoom;

        var.DrawX = (var.CurrentX + (var.ScrollX / 2)) / var.Zoom;
        var.DrawY = (var.CurrentY + (var.ScrollY / 2)) / var.Zoom;
    }

    @SuppressWarnings("static-access")
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

    @SuppressWarnings("static-access")
    public void mousePressed(MouseEvent e) {

        var.Drawing = true;
        if (var.state == 0) {

            if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
                var.leftClick = true;
            } else if (e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK) {
                var.leftClick = false;
            }
        }

        if (var.state == 1) {// 504, 343, 204, 60

            if (var.CurrentX > 504 / var.winZoom && var.CurrentY > 243 / var.winZoom && var.CurrentX < 708 / var.winZoom && var.CurrentY < 303 / var.winZoom) {
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 504 / var.winZoom && var.CurrentY > 343 / var.winZoom && var.CurrentX < 204 / var.winZoom && var.CurrentY < 60 / var.winZoom) {
                var.state = 0;
                var.active = false;
            }
        } else if (var.state == 2) {
            if (var.CurrentX > 100 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 180 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 0;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 180 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 260 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 1;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 260 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 340 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 2;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 340 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 420 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 3;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 420 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 500 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 4;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 500 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 580 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 5;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 580 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 660 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 6;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 660 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 740 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 7;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 740 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 820 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 8;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 820 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 900 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 9;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 900 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 980 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 10;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 980 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 1060 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 11;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 1060 / var.winZoom && var.CurrentY > 50 / var.winZoom && var.CurrentX < 1140 / var.winZoom && var.CurrentY < 130 / var.winZoom) {
                var.Equipped = 12;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 100 / var.winZoom && var.CurrentY > 130 / var.winZoom && var.CurrentX < 180 / var.winZoom && var.CurrentY < 210 / var.winZoom) {
                var.Equipped = 13;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 180 / var.winZoom && var.CurrentY > 130 / var.winZoom && var.CurrentX < 260 / var.winZoom && var.CurrentY < 210 / var.winZoom) {
                var.Equipped = 14;
                var.state = 0;
                var.active = false;
            }
            if (var.CurrentX > 260 / var.winZoom && var.CurrentY > 130 / var.winZoom && var.CurrentX < 340 / var.winZoom && var.CurrentY < 210 / var.winZoom) {
                var.Equipped = 15;
                var.state = 0;
                var.active = false;
            }
        }
    }

    @SuppressWarnings("static-access")
    public void mouseReleased(MouseEvent e) {
        var.Drawing = false;


    }

    @SuppressWarnings("static-access")
    public void keyPressed(KeyEvent evt) {
        if (evt.getKeyChar() == 'c' && !var.antiDouble) {
            if (var.state == 0)
                var.state = 5;
            else if (var.state == 5)
                var.state = 0;
            var.antiDouble = true;
        }
        if (evt.getKeyChar() == 'n') {
            nyan();
        }
        if (evt.getKeyChar() == 'p' && !var.antiDouble) {
            console.printtxt("P was pressed!");
            console.printtxt("Pausing / Unpausing");
            var.Simulating = !var.Simulating;
            var.antiDouble = true;
        }
        if (evt.getKeyChar() == 's' && !var.antiDouble) {
            if (var.Shape == 0)
                var.Shape = 1;
            else
                var.Shape = 0;
            var.antiDouble = true;
        }
        if (evt.getKeyChar() == 'x' && var.Zoom > 1 && !var.antiDouble) {
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
        if (evt.getKeyChar() == 'z' && !var.antiDouble) {
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
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE && !var.antiDouble && !(var.state == 2)) {
            if (var.state == 0) {
                var.state = 1;
            } else {
                var.state = 0;
            }
            var.antiDouble = true;
        }
        if (evt.getKeyCode() == KeyEvent.VK_SPACE && !var.antiDouble && !(var.state == 1)) {
            if (var.state == 2) {
                var.state = 0;
            } else {
                var.state = 2;
            }
            var.antiDouble = true;
        }
    }

    public void keyReleased(KeyEvent evt) {
    }

    public void keyTyped(KeyEvent evt) {
    }

    @SuppressWarnings("static-access")
    public void mouseWheelMoved(MouseWheelEvent e) {
        var.Size -= e.getWheelRotation();
        if (var.Size < 0) var.Size = 0;
        if (var.Size > 50) var.Size = 50;
    }

    @SuppressWarnings("static-access")
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (source == cbMenuItem1) {
                var.Simulating = false;
                console.printtxt("Pausing.");
            } else if (source == cbMenuItem2) {
                console.printtxt("var.DebugMode On");
                var.DebugMode = true;
                consolearea.setVisible(true);
            } else if (source == mode1) {
                console.printtxt("Current mode: 0");
                var.currentMode = 0;
            } else if (source == mode2) {
                console.printtxt("Current mode: 1");
                var.currentMode = 1;
            } else if (source == display1) {
                System.exit(0);
            }
        }
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            if (source == cbMenuItem1) {
                console.printtxt("Unpausing.");
                var.Simulating = true;
            } else if (source == cbMenuItem2) {
                console.printtxt("var.DebugMode Off");
                var.DebugMode = false;
                consolearea.setVisible(false);
            }
        }


    }


    @SuppressWarnings("static-access")
    public float getVoltage(int x, int y) {
        if ((var.VMap[x][y] / 1500f < 1.0f) && (var.VMap[x][y] / 1500f > 0.0f)) {
            var.Brightness = var.VMap[x][y] / 1500f;
        }
        return var.Brightness;
    }


    @SuppressWarnings("static-access")
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source == ChangePB)
            var.Equipped = -125;
        else if (source == ElectricityB)
            var.Equipped = -126;
        else if (source == ResetFPS) {
            TotalFPS = 0;
            TotalFrame = 0;
        } else if (source == Reset) {
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

    @SuppressWarnings("static-access")
    public void SaveFile() {
        try {
            console.printtxt("Saving...");
            file = new File("" + randomSaveName.nextInt() + ".jps");
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("javapowder-save\n");
            writer.write("version:" + versionID + "\n");
            writer.write("map:\n");
            for (int y = 0; y < var.Width; y++) {
                for (int x = 0; x < var.Width; x++) {
                    writer.write((int) var.Map[x][y]);
                }
            }
            writer.write("vmap:\n");
            for (int y = 0; y < var.Width; y++) {
                for (int x = 0; x < var.Width; x++) {
                    writer.write((int) var.VMap[x][y]);
                }
            }
            writer.write("pmap:\n");
            for (int y = 0; y < var.Width; y++) {
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

    public /*byte*/ void loadFile(String name) {
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