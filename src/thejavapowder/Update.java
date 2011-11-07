package thejavapowder;

import java.util.Random;

public class Update {
    /*Update class
      * Updates variables
      *
      * Known Bugs to fix:
      *
      * Particles react with other particles that are well over them ( Maybe Down left and right too, not tested )
      * Fix: Unknown
      *
      *
      *
      *
      *
      */

    long StartTime = 0;
    long EndTime = 0;
    long TotalFPS = 0;
    int TotalFrame = 0;


    byte FPS;
    float Time = 1000;


    Random rand = new Random();
    Variables var = thejavapowder.TheJavaPowder.var;
    Methods meth = new Methods();

    public void update() {
        EndTime = System.currentTimeMillis();

        Time = EndTime - StartTime;
        FPS = (byte) (1000 / Time);
        TotalFPS += FPS;
        TotalFrame++;

        StartTime = System.currentTimeMillis();

        //if (TotalFrame > 30) {
        //    TotalFrame = 1;
        //    TotalFPS = FPS;
        //}

        if (var.Simulating && var.state == 0) {
            for (int x = var.Width - 1; x > 1; x--) {
                for (int y = var.Height - 1; y > 1; y--)//For each Space
                {
                    if (var.Map[x][y] != -127) {

                        if (var.currentMode == 1 && meth.GetConductive(var.Map[x][y]))//If in electronic mode
                        {
                            UpdateVoltage(x, y);
                        }


                        if (var.currentMode == 0)//Reactive Engine
                        {
                            meth.getReactives(var.Map[x][y]);
                            meth.getSurroundings(x, y);

                            if (var.reactives != null)
                            {
                                for (int o = 0; o < var.reactives.length; o++)//For the number of reactive the particle have
                                {
                                    if (var.stopReactions)//If the particle already reacted
                                    {
                                        this.var.stopReactions = false;
                                        break;//Stop it
                                    }
                                    for (int i = 0; i < 8; i++)//For every space around the particle
                                    {

                                        if (var.stopReactions)//If the particle already reacted
                                        {
                                            break;//Stop it
                                        }
                                        if (var.surArray[i] == var.reactives[o])//If the tested space is one of the var.reactives
                                        {
                                            var.stopReactions = true;//Stop looping

                                            if (var.Map[x][y] >= 0 && var.Map[x - 1][y - 1] >= 0)
                                            {
                                                meth.getReaction(var.Map[x][y], var.Map[x - 1][y - 1]);//Get the Reaction needed
                                                if (var.reaction != null)
                                                {
                                                    var.Map[x][y] = var.reaction[0];

                                                    if (var.reaction[1] == 1) {
                                                        var.Map[x - 1][y - 1] = -127;
                                                    }

                                                    var.VMap[x][y] += var.reaction[2];
                                                    var.PMap[x][y] = var.reaction[3];
                                                    var.HMap[x][y] += var.reaction[4];
                                                    //PresMap[x][y] += var.reaction[5];

                                                    if (var.reaction[6] != var.Map[x][y])//If you're not creating what the particle is
                                                    {
                                                        meth.createParticle(x, y, var.reaction[6]);//Create it
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < 8; i++)//For every space around the particle
                            {
                                if (var.surArray[i] == 15)
                                {
                                    var.RandomNum = rand.nextInt(100);//Get a random Value
                                    if (var.Map[x][y] >= 0 && var.RandomNum < var.Elements[var.Map[x][y]].burn)
                                        var.Map[x][y] = 15;
                                }
                            }
                            if (meth.getType(x, y) == 'p')//If it's a powder
                            {
                                UpdatePowder(x, y);
                            }//End of powders


                            if (meth.getType(x, y) == 'l')//If it's a Liquid
                            {
                                UpdateLiquids(x, y);
                            }//End of Liquids


                            if (meth.getType(x, y) == 'g')//If it's a gas
                            {
                                UpdateGasses(x, y);
                            }//End of gases
                        }
                    }
                }
            }

        }//End of Updating maps
        if (var.active && var.state == 0) {
                if (var.Drawing) {
                    create_line(var.DrawX, var.DrawY, var.LastDrawX, var.LastDrawY, var.Size, var.Equipped);
                }
            } else {
                if (var.wait < 1) {
                    var.active = true;
                    var.wait = 30;
                } else {
                    var.wait--;
                }
            }
            var.LastDrawX = var.DrawX;
            var.LastDrawY = var.DrawY;

        for (int a = 0; a < var.Width; a++) {
            for (int b = 0; b < var.Height; b++)//For each Tile of the Boolean var.Map
            {
                var.BMap[a][b] = false;//Make it false so particles can move next turn
            }
        }


    }//End of Update

    public void drawPoint(int x, int y, byte id) {
        if ((var.active || !var.Simulating) && x > 1 && y > 1 && x < var.Width && y < var.Height) {
            var.wait = 30;
            if (id != -126 && id != -125) {
                if (var.leftClick) {
                    if (var.Map[x][y] == -127)//If the target tile is free
                    {
                        var.Map[x][y] = id;
                        var.HMap[x][y] = (short)meth.getDTemp(id);
                    }
                } else {
                    if (var.Map[x][y] != -127)//If the target tile is not free
                    {
                        var.Map[x][y] = -127;//Clean the Map
                        var.VMap[x][y] = 0;//Clean the VMap
                        var.PMap[x][y] = 0;//Clean the PMap
                    }
                }
            } else if (id == -126) {
                if (var.leftClick) {
                    if (var.Map[x][y] == 4 || var.Map[x][y] == 6) {
                        var.VMap[x][y] += 5;
                        if (var.VMap[x][y] > 1500)
                            var.VMap[x][y] = 1500;
                    }
                } else {
                    if (var.Map[x][y] != 0) {
                        var.Map[x][y] = -127;
                        var.VMap[x][y] = 0;
                        var.PMap[x][y] = 0;
                    }
                }
            } else {
                if (var.leftClick) {
                    if (var.PMap[x][y] < 50) {
                        if (var.Map[x][y] == 11 || var.Map[x][y] == 10 || var.Map[x][y] == 14) {
                            var.PMap[x][y] += 1;
                        }
                    }
                } else {
                    if (var.PMap[x][y] > 1) {
                        if (var.Map[x][y] == 11 || var.Map[x][y] == 10 || var.Map[x][y] == 14) {
                            var.PMap[x][y] -= 1;
                        }
                    }
                }
            }
        } else {
            var.wait--;
            if (var.wait < 1) {
                var.active = true;
            }
        }
    }

    public void drawCircle(int x, int y, byte rd, byte id) {
        int tempy = y;
        for (int i = x - rd; i <= x; i++) {
            double distance = Math.sqrt(Math.pow((double) x - (double) i, (double) 2) + Math.pow((double) y - (double) tempy, (double) 2));
            while (distance <= rd) {
                tempy = tempy - 1;
                distance = Math.sqrt(Math.pow((double) x - (double) i, (double) 2) + Math.pow((double) y - (double) tempy, (double) 2));
            }
            tempy = tempy + 1;
            for (int j = tempy; j <= 2 * y - tempy; j++) {
                if (i > 0 && j > 0 && i < var.Width * var.winZoom && j < var.Height * var.winZoom)
                    drawPoint(i, j, id);
                if (2 * x - i > 0 && j > 0 && 2 * x - i < var.Width * var.winZoom && j < var.Height * var.winZoom)
                    drawPoint(2 * x - i, j, id);
            }
        }
    }

    public void drawSquare(int xc, int yc, byte size, byte id) {
        for (int x = xc; x < xc + size; x++) {
            for (int y = yc; y < yc + size; y++) {
                drawPoint(x, y, id);
            }
        }
        if (size == 0)
            drawPoint(xc, yc, id);
    }

    public void draw(int x, int y, byte s, byte i) {
        if (var.Shape == 0)
            drawSquare(x - s, y - s, (byte) (s * 2), i);
        else
            drawCircle(x, y, s, i);
    }

    public void create_line(int x1, int y1, int x2, int y2, byte rd, byte id) // From old autorun.lua
    {
        if (x1 > x2) {
            int xc2 = x1;
            x1 = x2;
            x2 = xc2;
            int yc2 = y1;
            y1 = y2;
            y2 = yc2;
        }
        draw(x1, y1, rd, id);
        if (x1 == x2) {
            if (y1 != y2) {
                int yc1 = Math.min(y1, y2);
                int yc2 = Math.max(y1, y2);
                for (int i = yc1; i < yc2; i++)
                    draw(x1, i, rd, id);
            }
            return;
        }
        double slope = (double) (y2 - y1) / (x2 - x1);
        for (int i = x1; i < x2; i++) {
            double yc1 = y1 + slope * (i - x1);
            double yc2 = y1 + slope * (i + 1 - x1);
            int left = (int) Math.floor(Math.min(yc1, yc2));
            int right = (int) Math.ceil(Math.max(yc1, yc2));
            if (i == x2) right = left;
            for (int j = left; j <= right; j++)
                draw(i, j, rd, id);
        }
    }

    public void UpdateVoltage(int x, int y) {
        if (var.Map[x][y] == 5)//If it's a battery, give it infinite voltage
        {
            var.VMap[x][y] = 1000;
        }

        if (var.VMap[x][y] > 1)//If there's Voltage
        {
            if (var.Map[x][y] == 4)//Iron
            {
                for (int xc = -1; xc <= 1; xc++)
                    for (int yc = -1; yc <= 1; yc++)
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(var.Map[x+xc][y+yc]))//If the surrounding particle is conductive
                            {
                                if (var.Map[x+xc][y+yc] != 11 || var.Map[x+xc][y+yc] != 13)//If it's not a rechargable battery or crossing
                                {
                                    if (var.VMap[x+xc][y+yc] < var.VMap[x][y]) {
                                        var.VMap[x][y] -= 3;
                                        var.VMap[x+xc][y+yc] = var.VMap[x][y];
                                    }
                                }
                                if (var.Map[x+xc][y+yc] == 11) {
                                    var.VMap[x][y] -= 20;
                                    var.VMap[x+xc][y+yc] += 20;
                                }
                                if (var.Map[x+xc][y+yc] == 13)//If it's crossing
                                {
                                    if (meth.GetConductive(var.Map[x][y+yc*2])) {
                                        var.VMap[x][y] -= 3;
                                        var.VMap[x+xc*2][y+yc*2] = var.VMap[x][y];
                                    } else if (var.VMap[x+xc*2][y+yc*2] == 13) {
                                        if (meth.GetConductive(var.Map[x+xc*3][y+yc*3])) {
                                            var.VMap[x][y] -= 3;
                                            var.VMap[x+xc*3][y+yc*3] = var.VMap[x][y];
                                        } else if (var.VMap[x+xc*3][y+yc*3] == 13) {
                                            if (meth.GetConductive(var.Map[x+xc*4][y+yc*4])) {
                                                var.VMap[x][y] -= 3;
                                                var.VMap[x+xc*4][y+yc*4] = var.VMap[x][y];
                                            } else if (var.VMap[x+xc*4][y+yc*4] == 13) {
                                                if (meth.GetConductive(var.Map[x+xc*5][y+yc*5])) {
                                                    var.VMap[x][y] -= 3;
                                                    var.VMap[x+xc*5][y+yc*5] = var.VMap[x][y];
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
            } else if (var.Map[x][y] == 5)//Battery
            {
                for (int xc = -1; xc <= 1; xc++)
                    for (int yc = -1; yc <= 1; yc++)
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(var.Map[x+xc][y+yc])) {
                                var.VMap[x+xc][y+yc] = var.VMap[x][y];
                            }
                        }
            } else if (var.Map[x][y] == 6)//Copper
            {
                for (int xc = -1; xc <= 1; xc++)
                    for (int yc = -1; yc <= 1; yc++)
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(var.Map[x+xc][y+yc])) {
                                if (var.Map[x+xc][y+yc] != 11) {
                                    if (var.VMap[x+xc][y+yc] < var.VMap[x][y]) {
                                        var.VMap[x][y] -= 1;
                                        var.VMap[x+xc][y+yc] = var.VMap[x][y];
                                    }
                                } else if (var.VMap[x][y] > 29) {
                                    var.VMap[x][y] -= 30;
                                    var.VMap[x+xc][y+yc] += 30;
                                }
                            }
                        }
            } else if (var.Map[x][y] == 7)//Semi Conductor A
            {
                for (int xc = -1; xc <= 1; xc++)
                    for (int yc = -1; yc <= 1; yc++)
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (var.Map[x+xc][y+yc] == 8 || var.Map[x+xc][y+yc] == 7) {
                                if (var.VMap[x+xc][y+yc] < var.VMap[x][y]) {
                                    var.VMap[x][y] -= 1;
                                    var.VMap[x+xc][y+yc] = var.VMap[x][y];
                                }
                            }
                        }
            } else if (var.Map[x][y] == 8)//Semi Conductor B
            {
                for (int xc = -1; xc <= 1; xc++)
                    for (int yc = -1; yc <= 1; yc++)
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (var.Map[x+xc][y+yc] == 4 || var.Map[x+xc][y+yc] == 6 || var.Map[x+xc][y+yc] == 8) {
                                if (var.VMap[x+xc][y+yc] < var.VMap[x][y]) {
                                    var.VMap[x][y] -= 1;
                                    var.VMap[x+xc][y+yc] = var.VMap[x][y];
                                }
                            }
                        }
            } else if (var.Map[x][y] == 9)//Screen
            {
                if (var.VMap[x][y] >= 50) {
                    var.VMap[x][y] -= 50;
                } else {
                    var.VMap[x][y] = 0;
                }

            } else if (var.Map[x][y] == 11)//Rechargable Battery
            {
                if (var.VMap[x][y] > 5000) {
                    var.VMap[x][y] = 5000;
                }
                if (meth.GetConductive(var.Map[x][y + 1])) {
                    if (var.VMap[x][y + 1] < var.VMap[x][y]) {
                        if (var.VMap[x][y] >= var.PMap[x][y] * 10)//If we have more then 50 Volts
                        {
                            if (var.VMap[x][y + 1] < var.PMap[x][y] * 5)//If the Target have less then 50 Volts
                            {
                                var.VMap[x][y] -= var.PMap[x][y] * 10;
                                var.VMap[x][y + 1] = (short) (var.PMap[x][y] * 10);
                            }

                        } else {
                            if (var.VMap[x][y + 1] < var.PMap[x][y] * 10) {
                                var.VMap[x][y + 1] += var.VMap[x][y];
                                var.VMap[x][y] = 0;
                            }
                        }
                    }
                }

            } else if (var.Map[x][y] == 10)//Resistor
            {
                for (int xc = -1; xc <= 1; xc++)
                    for (int yc = -1; yc <= 1; yc++)
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(var.Map[x+xc][y+yc])) {
                                if (var.VMap[x+xc][y+yc] < var.VMap[x][y]) {
                                    if (var.VMap[x][y] >= (var.PMap[x][y] * 10)) {
                                        var.VMap[x][y] -= (var.PMap[x][y] * 10);
                                        var.VMap[x+xc][y+yc] = (short) (var.PMap[x][y] * 10);
                                    }
                                }
                            }
                        }
            } else if (var.Map[x][y] == 12)//Power Drainer
            {
                var.VMap[x][y] = 0;
                for (int xc = -1; xc <= 1; xc++)
                    for (int yc = -1; yc <= 1; yc++)
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (var.Map[x+xc][y+yc] == 4 || var.Map[x+xc][y+yc] == 6) {
                                if (var.VMap[x+xc][y+yc] > 0) {
                                    var.VMap[x+xc][y+yc] = 0;
                                }
                            }
                        }
            } else if (var.Map[x][y] == 14)//Switch
            {
                if (var.PMap[x][y] > 25) {
                    for (int xc = -1; xc <= 1; xc++)
                        for (int yc = -1; yc <= 1; yc++)
                            if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                            {
                                if (meth.GetConductive(var.Map[x+xc][y+yc])) {
                                    if (var.VMap[x+xc][y+yc] < var.VMap[x][y]) {
                                        var.VMap[x][y] -= 1;
                                        var.VMap[x+xc][y+yc] = var.VMap[x][y];
                                    }
                                }
                            }

                }
            }
        }


    }//End of Voltage Update

    public void UpdatePowder(int x, int y) {
        if (y <= 2 || y >= var.Height - 2 || x >= var.Width - 2 || x <= 2)//If it's out border
        {
            var.Map[x][y] = -127;//Destroy it
            return;
        }

        if (var.Map[x][y + 1] == -127)//And If the space under it is free
        {
            //Make the current space free

            var.RandomNum = rand.nextInt(5);//Get a random Value

            if (var.RandomNum == 1 && var.Map[x + 1][y + 1] == -127) {
                moveElement(x, y, x + 1, y + 1, false);
            } else if (var.RandomNum == 2 && var.Map[x - 1][y + 1] == -127) {
                moveElement(x, y, x - 1, y + 1, false);
            } else {
                moveElement(x, y, x, y + 1, false);
            }


        } else//If the space under is not free
        {
            if (var.Map[x + 1][y + 1] == -127 || var.Map[x - 1][y - 1] == -127)//If either of the one Down-Left or Down-Right are free
            {
                if (rand.nextBoolean())//Random Value Case One:
                {

                    if (var.Map[x + 1][y + 1] == -127)//If Down Right is Clear
                    {

                    moveElement(x, y, x + 1, y + 1, false);
                        return;
                    } else {
                        if (var.Map[x - 1][y + 1] == -127)//If Down Left is Clear
                        {

                            moveElement(x, y, x - 1, y + 1, false);
                            return;
                        }
                    }
                } else//Random Value Case Two
                {
                    if (var.Map[x - 1][y + 1] == -127)//If Down Left is Clear
                    {
                        moveElement(x, y, x - 1, y + 1, false);
                        return;
                    } else {
                        if (var.Map[x + 1][y + 1] == -127)//If Down Right is Clear
                        {
                           moveElement(x, y, x + 1, y + 1, false);
                            return;
                        }
                    }
                }
            }

            //////////////////////////

            if (meth.getWeight(var.Map[x][y + 1]) < meth.getWeight(var.Map[x][y]))//And If the space under it is free
            {
                //Make the current space free

                var.RandomNum = rand.nextInt(5);//Get a random Value

                if (var.RandomNum == 1 && meth.getWeight(var.Map[x + 1][y + 1]) < meth.getWeight(var.Map[x][y])) {
                    moveElement(x, y, x + 1, y + 1, true);
                } else if (var.RandomNum == 2 && meth.getWeight(var.Map[x - 1][y + 1]) < meth.getWeight(var.Map[x][y])) {
                    moveElement(x, y, x - 1, y + 1, true);
                } else {
                    moveElement(x, y, x, y + 1, true);
                }


            } else//If the space under is not free
            {
                if (meth.getWeight(var.Map[x + 1][y + 1]) < meth.getWeight(var.Map[x][y]) || meth.getWeight(var.Map[x - 1][y - 1]) < meth.getWeight(var.Map[x][y]))//If either of the one Down-Left or Down-Right are free
                {
                    if (rand.nextBoolean())//Random Value Case One:
                    {

                        if (meth.getWeight(var.Map[x + 1][y + 1]) < meth.getWeight(var.Map[x][y]))//If Down Right is Clear
                        {
                            moveElement(x, y, x + 1, y + 1, true);
                        } else {
                            if (meth.getWeight(var.Map[x - 1][y + 1]) < meth.getWeight(var.Map[x][y]))//If Down Left is Clear
                            {
                                moveElement(x, y, x - 1, y + 1, true);
                            }
                        }
                    } else//Random Value Case Two
                    {
                        if (meth.getWeight(var.Map[x - 1][y + 1]) < meth.getWeight(var.Map[x][y]))//If Down Left is Clear
                        {
                            moveElement(x, y, x - 1, y + 1, true);
                        } else {
                            if (meth.getWeight(var.Map[x + 1][y + 1]) < meth.getWeight(var.Map[x][y]))//If Down Right is Clear
                            {
                                moveElement(x, y, x + 1, y + 1, true);
                            }
                        }
                    }
                }
            }
        }
    }


    public void UpdateLiquids(int x, int y) {
        if (y <= 2 || y >= var.Height - 2 || x >= var.Width - 2 || x <= 2)//If it's out border
        {
            var.Map[x][y] = -127;//Destroy it
            return;
        }

        if (var.Map[x][y + 1] == -127)//If the Tile down is free
        {
            var.RandomNum = rand.nextInt(8);//Get a random variable

            if (var.RandomNum == 7 && var.Map[x + 1][y + 1] == -127)// 1/8 Chances that
            {
                moveElement(x, y, x + 1, y + 1, false);
                return;
            } else if (var.RandomNum == 6 && var.Map[x - 1][y + 1] == -127)// 1/8 Chances that
            {
                moveElement(x, y, x - 1, y + 1, false);
                return;

            } else// 6/8 Chances that
            {
                moveElement(x, y, x, y + 1, false);
                return;
            }


        } else//If the tile under it is occupied
        {
            for (int u = 0; u < 6; u++) {
                var.RandomNum = rand.nextInt(7);

                if (var.RandomNum <= 2 && var.Map[x + 1][y + 1] == -127)// If it's case 1 and that the Down Right tile is free
                {
                    moveElement(x, y, x + 1, y + 1, false);
                    return;

                } else if (var.RandomNum > 2 && var.RandomNum <= 4 && var.Map[x - 1][y + 1] == -127)// If it's case 2 and that the Down Left tile is free
                {
                    var.Map[x - 1][y + 1] = var.Map[x][y];//Move it there
                    var.BMap[x - 1][y + 1] = true;
                    var.Map[x][y] = -127;
                    return;
                } else if (var.RandomNum == 5 && var.Map[x + 1][y] == -127)// If it's case 1 and that the Right tile is free
                {
                   moveElement(x, y, x + 1, y, false);
                    return;
                } else if (var.RandomNum == 6 && var.Map[x - 1][y] == -127)// If it's case 2 and that the Left tile is free
                {
                    moveElement(x, y, x - 1, y, false);
                    return;
                }
            }
        }
        ////////////////////////////////

        if (meth.getWeight(var.Map[x][y + 1]) < meth.getWeight(var.Map[x][y]))//If the Tile down is free
        {
            var.RandomNum = rand.nextInt(8);//Get a random variable

            if (var.RandomNum == 7 && meth.getWeight(var.Map[x + 1][y + 1]) < meth.getWeight(var.Map[x][y]))// 1/8 Chances that
            {
                moveElement(x, y, x + 1, y + 1, true);
            } else if (var.RandomNum == 6 && meth.getWeight(var.Map[x - 1][y + 1]) < meth.getWeight(var.Map[x][y]))// 1/8 Chances that
            {
                moveElement(x, y, x - 1, y + 1, true);
            } else// 6/8 Chances that
            {
                moveElement(x, y, x, y + 1, true);
            }


        } else//If the tile under it is occupied
        {
            for (int u = 0; u < 6; u++) {
                var.RandomNum = rand.nextInt(7);

                if (var.RandomNum <= 2 && meth.getWeight(var.Map[x + 1][y + 1]) < meth.getWeight(var.Map[x][y]))// If it's case 1 and that the Down Right tile is free
                {
                    moveElement(x, y, x + 1, y + 1, true);
                    return;

                } else if (var.RandomNum > 2 && var.RandomNum <= 4 && meth.getWeight(var.Map[x - 1][y + 1]) < meth.getWeight(var.Map[x][y]))// If it's case 2 and that the Down Left tile is free
                {
                    moveElement(x, y, x - 1, y + 1, true);
                    return;
                }

                if (var.RandomNum == 5 && meth.getWeight(var.Map[x + 1][y]) < meth.getWeight(var.Map[x][y]))// If it's case 1 and that the Right tile is free
                {
                    moveElement(x, y, x + 1, y, true);
                    return;
                } else if (var.RandomNum == 6 && meth.getWeight(var.Map[x - 1][y]) < meth.getWeight(var.Map[x][y]))// If it's case 2 and that the Left tile is free
                {
                    moveElement(x, y, x - 1, y, true);
                    return;
                }
            }
        }
    }


    public void UpdateGasses(int x, int y) {
        if (y <= 2 || y >= var.Height - 2 || x >= var.Width - 2 || x <= 2)//If it's out border
        {
            var.Map[x][y] = -127;//Destroy it
            return;
        }

        if (meth.getWeight(var.Map[x][y + 1]) < meth.getWeight(var.Map[x][y]) ||
                meth.getWeight(var.Map[x][y - 1]) < meth.getWeight(var.Map[x][y]) ||
                meth.getWeight(var.Map[x + 1][y]) < meth.getWeight(var.Map[x][y]) ||
                meth.getWeight(var.Map[x - 1][y]) < meth.getWeight(var.Map[x][y]))//If the space Up, Down, Left or Right is Free
        {
            for (byte o = 0; o < 2; o++)//Safeguard, it only tries 5 times
            {
                var.RandomNum = rand.nextInt(4);//Get a random value

                if (var.RandomNum == 0)//Case one
                {
                    if (y < var.Height) {
                        if (meth.getWeight(var.Map[x][y + 1]) < meth.getWeight(var.Map[x][y]))//If the tile Up is Free
                        {
                            //Make the current tile Free
                            moveElement(x, y, x, y + 1, true);
                            return;//End the loop
                        }
                    }
                }


                if (var.RandomNum == 1)//Case 2
                {
                    if (x < var.Width - 1) {
                        if (meth.getWeight(var.Map[x + 1][y]) < meth.getWeight(var.Map[x][y]))//See Case 1
                        {

                            moveElement(x, y, x + 1, y, true);
                            return;
                        }
                    }
                }


                if (var.RandomNum == 2) {
                    if (y > 0) {
                        if (meth.getWeight(var.Map[x][y - 1]) < meth.getWeight(var.Map[x][y]))//See Case 1
                        {

                            moveElement(x, y, x, y - 1, true);
                            return;
                        }
                    }
                }


                if (var.RandomNum == 3) {
                    if (x > 0) {
                        if (meth.getWeight(var.Map[x - 1][y]) < meth.getWeight(var.Map[x][y]))//See Case 1
                        {

                            moveElement(x, y, x - 1, y, true);
                            return;
                        }
                    }
                }
            }
        }//End of 'For' Loop


        ////////////////////////

        for (int a = 0; a < 2; a++) {
            if (var.Map[x][y + 1] == -127 ||
                    var.Map[x][y - 1] == -127 ||
                    var.Map[x + 1][y] == -127 ||
                    var.Map[x - 1][y] == -127)//If the space Up, Down, Left or Right is Free
            {
                for (byte o = 0; o < 2; o++)//Safeguard, it only tries 5 times
                {
                    var.RandomNum = rand.nextInt(4);//Get a random value
                }
                if (var.RandomNum == 0)//Case one
                {
                    if (y < var.Height) {
                        if (var.Map[x][y + 1] == -127)//If the tile Up is Free
                        {
                            //Make the current tile Free
                            moveElement(x, y, x, y + 1, false);
                            return;//End the loop
                        }
                    }
                }


                if (var.RandomNum == 1)//Case 2
                {
                    if (x < var.Width - 1) {
                        if (var.Map[x + 1][y] == -127)//See Case 1
                        {

                            moveElement(x, y, x + 1, y, false);
                            return;
                        }
                    }
                }


                if (var.RandomNum == 2) {
                    if (y > 0) {
                        if (var.Map[x][y - 1] == -127)//See Case 1
                        {

                            moveElement(x, y, x, y - 1, false);
                            return;
                        }
                    }
                }


                if (var.RandomNum == 3) {
                    if (x > 0) {
                        if (var.Map[x - 1][y] == -127)//See Case 1
                        {

                            moveElement(x, y, x - 1, y, false);
                            return;
                        }
                    }
                }
            }
        }
    }
    public void moveElement(int x1, int y1, int x2, int y2, boolean change)
    {
        if(!change)//If we are exchanging the values ( Because the weight of the particle we are moving is bigger then the target )
        {
            var.Map[x2][y2] = var.Map[x1][y1];//Occupy the tile under
            var.HMap[x2][y2] = var.HMap[x1][y1];
            var.BMap[x2][y2] = true;//Make sure it won't be moved again
            var.Map[x1][y1] = -127;
            var.HMap[x1][y1] = 0;
        }
        else
        {
             var.element = var.Map[x2][y2];
             var.temp = var.HMap[x2][y2];
             var.HMap[x2][y2] = var.HMap[x1][y1];
             var.Map[x2][y2] = var.Map[x1][y1];
             var.BMap[x2][y2] = true;
             var.Map[x1][y1] = var.element;
             var.HMap[x1][y1] = var.temp;
        }
    }

}
