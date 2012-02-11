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
                saver.savePref(var.Width, var.Height, var.winZoom);
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
	        update.update();
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
            var.coffeePng,
		    var.wallPng,
		    var.methanePng,
		    var.waterPng,
		    var.ironPng,
		    var.batteryPng,
		    var.copperPng,
		    var.scaPng,
		    var.scbPng,
		    var.screenPng,
		    var.resistorPng,
		    var.rechargableBatteryPng,
		    var.powerDrainerPng,
		    var.crossingPng,
		    var.switchPng,
		    var.firePng,
		    var.woodPng,
		    var.petrolPng,
		    var.nonePng,
		    var.nonePng,
		    var.nonePng,
		    var.nonePng,
		    var.nonePng};





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
	    int wBuff = saver.getPref()[0];
	    int hBuff = saver.getPref()[1];
	    byte winBuff = (byte)saver.getPref()[2];
	    var.Width = wBuff;
	    var.Height = hBuff;
	    var.winZoom = winBuff;
	    meth.resetItems();
	    this.setIconImage(var.javaPowderPng);
        

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



    @Override
    public void paint(Graphics g) {

        var.antiDouble = false;
        var.Brightness = 0;

        var.CurrentX = (var.MouseX + var.ScrollX) / var.winZoom;
        var.CurrentY = (var.MouseY + var.ScrollY) / var.winZoom;

        var.DrawX = (var.MouseX + (var.ScrollX * var.winZoom)) / var.realZoom;
        var.DrawY = (var.MouseY + (var.ScrollY * var.winZoom)) / var.realZoom;

        if (bufferGraphics == null) return;
        bufferGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());



        // The Colouring loop
        if (var.state == 0 || var.state == 2 || var.state == 5) {//The game, the element menu or the console

	        bufferGraphics.setColor(new Color(0x00007F));
	        bufferGraphics.fillRect(4 * 2, (var.Height + 1) * var.winZoom, 37 * 2, 10 * var.winZoom);
	        bufferGraphics.fillRect(45 * 2, (var.Height + 1) * var.winZoom, 54 * 2, 10 * var.winZoom);

	        for(int i = 0; i < var.images.length; i++)
	        {
		        bufferGraphics.drawImage(var.images[i].image, var.images[i].x, var.images[i].y, var.images[i].Width, var.images[i].Height, this);
	        }
			bufferGraphics.setColor(Color.WHITE);
	        bufferGraphics.drawString("Reset Scene", 5*2, (var.Height+8) * var.winZoom);
	        bufferGraphics.drawString("Reset Average FPS", 46*2, (var.Height+8) * var.winZoom);


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
			draw.drawCursor(var.DrawX, var.DrawY, var.Size, bufferGraphics);

            if (var.state == 0 && (var.MouseX / var.winZoom) < var.Width && var.MouseX > 0 && (var.MouseY / var.winZoom) < var.Height && var.MouseY > 0) {
	            bufferGraphics.setColor(Color.WHITE);

                if (var.Map[var.DrawX][var.DrawY] != -127)
                    bufferGraphics.drawString("ID:" + var.Elements[var.Map[var.DrawX][var.DrawY]].name, 10, 30 + 5 * var.winZoom);//Draw the Hovered Element Name
                else
                    bufferGraphics.drawString("ID: None", 10, 30 + 5 * var.winZoom);//Draw the Hovered Element Name

                bufferGraphics.drawString("Voltage:" + var.VMap[var.DrawX][var.DrawY], 10, 30 + 15 * var.winZoom);//Draw the Hovered Voltage
                bufferGraphics.drawString("Property:" + var.PMap[var.DrawX ][var.DrawY], 10, 30 + 25 * var.winZoom);//Draw the Property Level
                bufferGraphics.drawString("Temperature:" + var.HMap[var.DrawX][var.DrawY] + " C", 10, 30 + 35 * var.winZoom);//Draw the Temperature
				if (var.DrawX < var.Width-4 && var.DrawX >= 0 && var.DrawY < var.Height-4 && var.DrawY >= 0)
					bufferGraphics.drawString("Pressure:" + var.PrMap[var.DrawX/4][var.DrawY/4], 10, 30 + 45 * var.winZoom);//Draw the Pressure
				if (var.DrawX < var.Width-4 && var.DrawX >= 0 && var.DrawY < var.Height-4 && var.DrawY >= 0)
					bufferGraphics.drawString("X Velocity:" + var.VxMap[var.DrawX/4][var.DrawY/4], 10, 30 + 55 * var.winZoom);//Draw the Pressure
				if (var.DrawX < var.Width-4 && var.DrawX >= 0 && var.DrawY < var.Height-4 && var.DrawY >= 0)
					bufferGraphics.drawString("Y Velocity:" + var.VyMap[var.DrawX/4][var.DrawY/4], 10, 30 + 65 * var.winZoom);//Draw the Pressure
				bufferGraphics.drawString("FPS:" + var.PaintFPS, 10, 30 + 75 * var.winZoom);//Draw the FPS
				bufferGraphics.drawString("Mouse-x:" + var.DrawX, 10, 30 + 85 * var.winZoom);//Draw the Mouse X Coordinate
				bufferGraphics.drawString("Mouse-y:" + var.DrawY, 10, 30 + 95 * var.winZoom);//Draw the Mouse Y Coordinate
            }

            if (var.state == 2)//If we are choosing an element
            {
                bufferGraphics.setColor(new Color(0x777777));
                for (int i = 0; i < var.Elements.length; i++) {
	                if ( i >= thumbnails.length || thumbnails[i] == var.nonePng ||
		                !bufferGraphics.drawImage(
				         thumbnails[i],//The next icon
				        (50 * var.winZoom + i * 40 * var.winZoom) - var.iconX,
				         25 * var.winZoom + var.iconY,
				         40 * var.winZoom,
				         40 * var.winZoom, this)) {


	                    bufferGraphics.drawImage(var.nonePng, (50 * var.winZoom + i * 40 * var.winZoom) - var.iconX, 25 * var.winZoom + var.iconY, 40 * var.winZoom, 40 * var.winZoom, this);
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
                if (!bufferGraphics.drawImage(var.consolePng, 0, 25, var.Width * var.winZoom, var.Height / 3 * var.winZoom, this)) {
                    bufferGraphics.drawString("Derp", 300, 300);
                }
                bufferGraphics.setColor(new Color(0x00ED00));
                bufferGraphics.drawString("JavaPowder Console *Alpha*", 20, var.Height / 3 * var.winZoom + 5);
            }
        } else if (var.state == 1) {

	        for(int i = 0; i < var.imagesMenu.length; i++)
	        {
		        bufferGraphics.drawImage(var.imagesMenu[i].image, var.imagesMenu[i].x, var.imagesMenu[i].y, var.imagesMenu[i].Width, var.imagesMenu[i].Height, this);
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

        if (var.MouseY >= var.Height*var.winZoom)
        {
            int xc = var.MouseX;
			int yc = var.MouseY;
            if (xc >= 8 && xc <= 82 && yc <= (var.Height+11)*var.winZoom)
            {
                for (int x = var.Width - 1; x > 1; x--) {
                    for (int y = var.Height - 1; y > 1; y--)//For each Space
                    {
                        var.Map[x][y] = -127;
                        var.VMap[x][y] = 0;
                        var.PMap[x][y] = 0;
                        var.LMap[x][y] = 0;
                    }
                }
                for (int x = (var.Width - 1)/4; x >= 0; x--) {
                    for (int y = (var.Height - 1)/4; y >= 0; y--)//For each Space
                    {
	                    var.PrMap[x][y] = 0;
	                    var.VxMap[x][y] = 0;
	                    var.VyMap[x][y] = 0;
	                    var.OldPrMap[x][y] = 0;
	                    var.OldVxMap[x][y] = 0;
	                    var.OldVyMap[x][y] = 0;
                    }
                }
                console.printtxt("Scene Reset.");
                var.Drawing = false; var.active = false;
            }
            else if (xc >= 90 && xc <= 198 && yc <= (var.Height+11)*var.winZoom)
            {
                TotalFPS = 0;
                TotalFrame = 0;
                var.Drawing = false; var.active = false;
            }
            else if (xc >= var.images[0].x && xc <= var.images[0].x + var.images[0].Width && yc >= var.images[0].y && yc <= var.images[0].y + var.images[0].Height)
            {
                var.Equipped = -126;
                var.Drawing = false; var.active = false;
            }
            else if (xc >= var.images[1].x && xc <= var.images[1].x + var.images[1].Width && yc >= var.images[1].y && yc <= var.images[1].y + var.images[1].Height)
            {
                var.Equipped = -125;
                var.Drawing = false; var.active = false;
            }
            else if (xc >= var.images[2].x && xc <= var.images[2].x + var.images[2].Width && yc >= var.images[2].y && yc <= var.images[2].y + var.images[2].Height)
            {
	            FileSaver.LoadFile(JOptionPane.showInputDialog(null,"Enter the Name of a Save to Open"));
	            var.Drawing = false; var.active = false;
            }
            else if (xc >= var.images[3].x && xc <= var.images[3].x + var.images[3].Width)
            {
                FileSaver.SaveFile(JOptionPane.showInputDialog(null,"Enter a Save Name"));
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
            if (var.MouseX > var.imagesMenu[0].x && var.MouseY > var.imagesMenu[0].y && var.MouseX < var.imagesMenu[0].x + var.imagesMenu[0].Width && var.MouseY < var.imagesMenu[0].y + var.imagesMenu[0].Height) {
                var.state = 0;
                var.Drawing = false; var.active = false;
            }
            if (var.MouseX > var.imagesMenu[1].x && var.MouseY > var.imagesMenu[1].y && var.MouseX < var.imagesMenu[1].x + var.imagesMenu[1].Width && var.MouseY < var.imagesMenu[1].y + var.imagesMenu[1].Height) {
                var.state = 3;
                var.Drawing = false; var.active = false;
            }
        } else if (var.state == 2) {
            int x = 50 * var.winZoom, y = 25 * var.winZoom, num = 0;
            while (num < var.NUM_ELS) {
                if (var.MouseX > x && var.MouseY > y && var.MouseX < x + (40*var.winZoom) && var.MouseY < y + (40*var.winZoom)) {
                    var.Equipped = (byte)(num);
                    var.state = 0;
                    var.Drawing = false; var.active = false;
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
            if (var.MouseX >= (var.Width/2-80)*var.winZoom && var.MouseY >= (var.Height/2+80)*var.winZoom && var.MouseX < (var.Width/2+100)*var.winZoom && var.MouseY < (var.Height/2+100)*var.winZoom && !var.antiDouble)
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
            meth.resetItems();
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
                var.realZoom = var.Zoom * var.winZoom;
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
                var.debug = !var.debug;
                var.antiDouble = true;
            }
            if (evt.getKeyChar() == 'm')
            {
				var.electricity = !var.electricity;
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