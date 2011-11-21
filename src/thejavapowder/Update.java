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
	Draw draw = new Draw();

    public void update() {
        EndTime = System.currentTimeMillis();

        Time = EndTime - StartTime;
        FPS = (byte) (1000 / Time);
        TotalFPS += FPS;
        TotalFrame++;

        StartTime = System.currentTimeMillis();

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
                                for (int o = 0; o < var.reactives.length; o++)//For the number of reactives the particle has
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
                            if(var.Map[x][y] != -127)
                            {
                                UpdateElement(x,y);
                                CheckStateChanges(x,y);
                            }
                        }
                    }
                }
            }
        }//End of Updating maps
        if (var.active && var.state == 0) {//If drawing is active and we are in the game screen
                if (var.Drawing) {//If it should be drawing
                    draw.create_line(var.DrawX, var.DrawY, var.LastDrawX, var.LastDrawY, var.Size, var.Equipped);//Draw
                }
            } else {
                if (var.wait < 1) {//If the wait time to draw is over
                    var.active = true;//Activate the drawing
                    var.wait = 30;//Reset the timer
                } else {//If it can't draw and the timer is not up
                    var.wait--;//Make the timer progress
                }
            }
            var.LastDrawX = var.DrawX;//Update the drawing points
            var.LastDrawY = var.DrawY;//Update the drawing points
    }

    public void UpdateVoltage(final int x, final int y) {
        if (var.Map[x][y] == 5)//If it's a battery, give it infinite voltage
        {
            var.VMap[x][y] = 1000;
        }

        if (var.VMap[x][y] > 1)//If there's Voltage
        {
            if (var.Map[x][y] == 4)//Iron
            {
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0))//Forthe 4 spots directly touching it
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
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(var.Map[x+xc][y+yc])) {
                                var.VMap[x+xc][y+yc] = var.VMap[x][y];
                            }
                        }
            } else if (var.Map[x][y] == 6)//Copper
            {
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(var.Map[x+xc][y+yc])) {
                                if (var.VMap[x][y] > 29) {
                                        var.VMap[x][y] -= 30;
                                        var.VMap[x+xc][y+yc] += 30;
                                } else if (var.Map[x+xc][y+yc] != 11) {
                                    if (var.VMap[x+xc][y+yc] < var.VMap[x][y]) {
                                        var.VMap[x][y] -= 1;
                                        var.VMap[x+xc][y+yc] = var.VMap[x][y];
                                    }
                                }
                            }
                        }
            } else if (var.Map[x][y] == 7)//Semi Conductor A
            {
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
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
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
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
                                var.VMap[x][y + 1] = (var.PMap[x][y] * 10);
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
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(var.Map[x+xc][y+yc])) {
                                if (var.VMap[x+xc][y+yc] < var.VMap[x][y]) {
                                    if (var.VMap[x][y] >= (var.PMap[x][y] * 10)) {
                                        var.VMap[x][y] -= (var.PMap[x][y] * 10);
                                        var.VMap[x+xc][y+yc] = (var.PMap[x][y] * 10);
                                    }
                                }
                            }
                        }
            } else if (var.Map[x][y] == 12)//Power Drainer
            {
                var.VMap[x][y] = 0;
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
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
                    for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                        for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
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



    public void UpdateElement(final int x, final int y) {
        if (y <= 2 || y >= var.Height - 2 || x >= var.Width - 2 || x <= 2)//If it's out border
        {
            var.Map[x][y] = -127;//Destroy it
            return;
        }
        for (int i = 0; i < 8; i++)//For every space around the particle
        {
            if (var.surArray[i] == 15)
            {
                var.RandomNum = rand.nextInt(100);//Get a random Value
                if (var.Map[x][y] >= 0 && var.RandomNum < var.Elements[var.Map[x][y]].burn)
                {
                    var.Map[x][y] = 15;
                    var.HMap[x][y] += 50;
                }
            }
            if (var.surArray[i] != -127)
            {
                int x2 = x, y2 = y;
                if (i == 0 || i == 1 || i == 2)
                    y2--;
                if (i == 2 || i == 3 || i == 4)
                    x2++;
                if (i == 4 || i == 5 || i == 6)
                    y2++;
                if (i == 6 || i == 7 || i == 0)
                    x2--;
                if (var.Map[x][y] != -127 && var.Map[x2][y2] != -127)
                {
                    final float heatTransfer = (var.HMap[x][y] - var.HMap[x2][y2])/5;
                    var.HMap[x][y] -= heatTransfer;
                    var.HMap[x2][y2] += heatTransfer;
                }
            }
        }
        if(var.Map[x][y] != -127)
        {
            final char type = var.Elements[var.Map[x][y]].state;
            double[] chances = {0,0,0,0,0,0,0,0};//An array of the possibilities of moving
            int i, j = 0;

            switch(type)//Depending on the type of the element
            {
                case 'p'://Powder
                    chances[3] = .2;
                    chances[4] = .6;
                    chances[5] = .2;
                    break;

                case 'l'://Liquids
                    chances[3] = .125;
                    chances[4] = .75;
                    chances[5] = .125;
                    if (!meth.canMove(var.Map[x][y],var.Map[x][y+1],false))
                    {
                        chances[2] = (double)1/6;
                        chances[3] = (double)1/3;
                        chances[4] = 0;
                        chances[5] = (double)1/3;
                        chances[6] = (double)1/6;
                    }
                    break;

                case 'g'://Gas
                    for (i = 0; i < 8; i++)
                    {
                        chances[i] = .125;
                    }
                    break;
            }

            double randnum, total;
            boolean moved = false, triedmove;

            i = 0;

            while (i < 5 && !moved)
            {
                randnum = rand.nextDouble();
                triedmove = false;
                total = 0;
                while(j < 8 && !triedmove)
                {
                    total += chances[j];
                    if (total > randnum)
                    {
                        triedmove = true;
                        moved = meth.tryMove(x,y,j,false);
                    }
                    j++;
                }
                i++;
            }
            i = j = 0;
            while (i < 5 && !moved)
            {
                randnum = rand.nextDouble();
                triedmove = false;
                total = 0;
                while(j < 8 && !triedmove)
                {
                    total += chances[j];
                    if (total > randnum)
                    {
                        triedmove = true;
                        moved = meth.tryMove(x,y,j,true);
                    }
                    j++;
                }
                i++;
            }
        }
    }



    public void CheckStateChanges(final int x, final int y)
    {

    }
}
