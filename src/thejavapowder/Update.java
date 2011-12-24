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

    Random rand = new Random();
    Variables var = thejavapowder.TheJavaPowder.var;
    Methods meth = new Methods();
	draw draw = new draw();

    public void update() {

        if ((var.Simulating || var.tempSimulating) && var.state == 0) {
            for (int x = var.Width - 1; x > 1; x--) {
                for (int y = var.Height - 1; y > 1; y--)//For each Space
                {
                    if (var.Map[x][y] != -127) {

	                    if (var.reactions)//Reactive Engine
                        {
	                        UpdateReactions(x, y);
                        }
	                    if(var.Map[x][y] != -127)
	                    {
		                    if(var.physics)
							{
								UpdateElement(x,y);
							}
							if(var.Map[x][y] != -127)
							{
								if(var.life)
								{										
								UpdateLife(x, y);
								}
							if(var.Map[x][y] != -127)
							{	
								if(var.stateChanges)
								{
									CheckStateChanges(x,y);
								}
								if (var.electricity)
								{
									UpdateVoltage(x, y);
								}
								if(var.burn || var.heat)
								{
									UpdateHeat(x, y);
								}
							}								
	                        }
	                    }

                    }
                }
            }
			if(var.pressure)
			{
				UpdateAir();
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
		var.tempSimulating = false;
    }

	private void UpdateAir()
	{
		for (int x = 1; x < var.Width/4; x++) //For every space on the pressure map
			for (int y = 1; y < var.Height/4; y++)
			{
				float airchange = var.VxMap[x-1][y] - var.VxMap[x][y]; // Calculate how much x and y velocity to transfer
				airchange -= var.VyMap[x][y-1] - var.VyMap[x][y];
				var.PrMap[x][y] *= .9999; // Reduce pressure a little
				var.PrMap[x][y] += airchange*.3;  // Transfer velocity to pressure
			}
		for (int x = 0; x < var.Width/4-1; x++) //For every space on the velocity maps
			for (int y = 0; y < var.Height/4-1; y++)
			{
				float airchangex = var.PrMap[x][y] - var.PrMap[x+1][y];  // Calculate how much pressure to transfer
				float airchangey = var.PrMap[x][y] - var.PrMap[x][y+1];
				var.VxMap[x][y] *= .999; // Reduce velocity a little
				var.VyMap[x][y] *= .999;
				var.VxMap[x][y] += airchangex*.4; // Transfer pressure to velocity
				var.VyMap[x][y] += airchangey*.4;
			}
		for (int x = 0; x < var.Width/4; x++) //For every space on the pressure map
			for (int y = 0; y < var.Height/4; y++)
			{
				if (Math.abs(var.PrMap[x][y]) < .0000001) //If the pressure is really small
					var.PrMap[x][y] = 0;        //Make it 0
				if (Math.abs(var.VxMap[x][y]) < .0000001) //If the x velocity is really small
					var.VxMap[x][y] = 0;        //Make it 0
				if (Math.abs(var.VyMap[x][y]) < .0000001) //If the y velocity is really small
					var.VyMap[x][y] = 0;        //Make it 0
				if (var.PrMap[x][y] != 0 || var.VxMap[x][y] != 0 || var.VyMap[x][y] != 0)
				{
					float airChange = 0;
					float airChangeX = 0;
					float airChangeY = 0;
					for (int i = -1; i <= 1; i++) //For every pressure space around this spot
						for (int j = -1; j <= 1; j++)
							if (meth.validAirSpace(x+i,y+j)) //If it's a valid spot
							{
								airChange -= (var.PrMap[x][y] - var.PrMap[x+i][y+j])/5; //calculate how much air to transfer to this spot
								airChangeX -= (var.VxMap[x][y] - var.VxMap[x+i][y+j])/5*.7; //calculate how much x velocity to transfer to this spot
								airChangeY -= (var.VyMap[x][y] - var.VyMap[x+i][y+j])/5*.7; //calculate how much y velocity to transfer to this spot
							}
					var.OldPrMap[x][y] = airChange + (var.PrMap[x][y]*.99f); //Tranfer it
					var.OldVxMap[x][y] = airChangeX + (var.VxMap[x][y]*.99f);
					var.OldVyMap[x][y] = airChangeY + (var.VyMap[x][y]*.99f);
				}
			}
		System.arraycopy(var.OldPrMap,0,var.PrMap,0,var.PrMap.length); //Copy the new pressure array into the real pressure array
		System.arraycopy(var.OldVxMap,0,var.VxMap,0,var.VxMap.length); //Copy the new x velocity array into the real x velocity array
		System.arraycopy(var.OldVyMap,0,var.VyMap,0,var.VyMap.length); //Copy the new y velocity array into the real y velocity array
	}

	private void UpdateReactions(int x, int y)
	{
		meth.getReactives(var.Map[x][y]);
		if (var.reactives != null)
		{
			meth.getSurroundings(x, y);
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

								if (var.reaction[1] == 1)
									var.Map[x - 1][y - 1] = -127;

								var.VMap[x][y] += var.reaction[2];
								var.PMap[x][y] = var.reaction[3];
								var.HMap[x][y] += var.reaction[4];
								if (meth.validAirSpace(x/4,y/4))
									var.PrMap[x/4][y/4] += var.reaction[5];

								if (var.reaction[6] != var.Map[x][y])//If you're not creating what the particle is
									meth.createParticle(x, y, var.reaction[6]);//Create it
							}
						}
					}
				}
			}
		}
	}

    private void UpdateVoltage(final int x, final int y) {
        if (var.Map[x][y] == 5)//If it's a battery, give it infinite voltage
        {
            var.VMap[x][y] = 1000;
        }
        if (var.VMap[x][y] > 1)//If there's Voltage
        {
            switch (var.Map[x][y])
            {
	            case 4://Iron
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0))//For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(var.Map[x+xc][y+yc]) || var.Map[x+xc][y+yc] == 13)//If the surrounding particle is conductive
                            {
                                if (var.Map[x+xc][y+yc] != 11 && var.Map[x+xc][y+yc] != 13)//If it's not a rechargable battery or crossing
                                {
                                    if (var.VMap[x+xc][y+yc] < var.VMap[x][y]) {
                                        var.VMap[x][y] -= 3;
                                        var.VMap[x+xc][y+yc] = var.VMap[x][y];
                                    }
                                }
                                else if (var.Map[x+xc][y+yc] == 11) {
                                    var.VMap[x][y] -= 20;
                                    var.VMap[x+xc][y+yc] += 20;
                                }
                                else if (var.Map[x+xc][y+yc] == 13)//If it's crossing
                                {
									for (int i = 2; i < 5; i++)
									{
										if (meth.GetConductive(var.Map[x+xc*i][y+yc*i]))
										{
											var.VMap[x][y] -= 3;
                                        	var.VMap[x+xc*i][y+yc*i] = var.VMap[x][y];
										}
									}
                                }
                            }
                        }

	        break;
	        case 5://Battery

                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(var.Map[x+xc][y+yc])) {
                                var.VMap[x+xc][y+yc] = var.VMap[x][y];
                            }
                        }
            break;
	            case 6://Copper

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
            break;
	            case 7://Semi Conductor A

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
            break;
	            case 8://Semi Conductor B

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
            break;
	            case 9://Screen

                if (var.VMap[x][y] >= 50) {
                    var.VMap[x][y] -= 50;
                } else {
                    var.VMap[x][y] = 0;
                }

            break;
	            case 11://Rechargable Battery

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

            break;
	            case 10://Resistor

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
            break;
	            case 12://Power Drainer

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
            break;
	            case 14://Switch

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
		            break;
            }
        }
    }//End of Voltage Update

	private void UpdateLife(int x, int y)
	{
	}
	
	private void UpdateHeat(int x, int y)
	{
		int i, j, num = 0;
		if (var.Map[x][y] < 0 )
			return;
		if(var.burn)
		{
		for (j = -1; j < 2; j++) //Loop through each spot around the particle
			for (i = -1; i < 2; i++)
			{
				if (i != 0 || j != 0) //If it's not the center space
				{
					if (var.surArray[num] == 15) //If burning is active and this space has fire in it
					{
						var.RandomNum = rand.nextInt(50); //Get a random value
						if (var.RandomNum < var.Elements[var.Map[x][y]].burn) //If that random value was less than the burn value of the element
						{
							var.Map[x][y] = 15; //Change this particle to fire
							var.HMap[x][y] += var.Elements[var.Map[x][y]].burn * 25; //Increase the heat
						}
					}
					if (var.heat && var.Map[x+i][y+j] != -127) //If heat conduction is on and the space around the particle is not empty
					{
						final float heatTransfer = (var.HMap[x][y] - var.HMap[x+i][y+j])/5; //Calculate how much heat to transfer
						var.HMap[x][y] -= heatTransfer;     //Transfer heat
						var.HMap[x+i][y+j] += heatTransfer; //Transfer heat
					}
					num++;
				}
			}
		}
		if(var.Map[x][y] == 15)
		{
			var.HMap[x][y]--;
			if(var.HMap[x][y] < 100)
				meth.clearTile(x, y);
		}
	}

    private void UpdateElement(final int x, final int y) {
        if (y <= 2 || y >= var.Height - 2 || x >= var.Width - 2 || x <= 2)//If it's out border
        {
            var.Map[x][y] = -127;//Destroy it
            return;
        }
	    if(var.Map[x][y] != -127)
	    {
	    final char type = var.Elements[var.Map[x][y]].state;
        if(var.Map[x][y] != -127 && type != 's')
        {

            double[] chances = {0,0,0,0,0,0,0,0};//An array of the possibilities of moving
			/* 0 1 2
			   3   4
			   5 6 7 */
            int i, j = 0;

            switch(type)//Depending on the type of the element
            {
                case 'p'://Powder
                    chances[5] = .2; //20% chance of going diagonally down left or right
                    chances[6] = .6; //60% chance of going straight down
                    chances[7] = .2;
                    break;

                case 'l'://Liquids
                    chances[5] = .125; //1/8 chance of moving diagonally down left or right
                    chances[6] = .75;  //3/4 chance of going straight down
                    chances[7] = .125;
                    if (!meth.canMove(var.Map[x][y],var.Map[x][y+1],false)) //But if there is a particle below it
                    {
                        chances[3] = (double)1/6; //1/6 chance of moving left or right to either side
                        chances[5] = (double)1/3; //1/3 chance of moving diagonally down right or left
                        chances[6] = 0;           //0% chance of going down (it's blocked)
                        chances[7] = (double)1/3;
                        chances[4] = (double)1/6;
                    }
                    break;

                case 'g'://Gas
                    for (i = 0; i < 8; i++)
                    {
                        chances[i] = .125; //A random chance of moving in any direction
                    }
					if (var.Map[x][y] == 15)
					{
						var.PrMap[x/4][y/4] += .01;
						var.VyMap[x/4][y/4] -= .01;
					}
                    break;
            }

            double randnum, total;
            boolean moved = false, triedmove;
			i = 0;

            while (i < 5 && !moved) //Only try to move five times, it might not be possible
            {
                randnum = rand.nextDouble(); //Pick a number between 0 and 1 to decide which spot to move to
                triedmove = false;
                total = 0;
                while(j < 8 && !triedmove)   //Loop through each spot
                {
                    total += chances[j];     //As it loops through the spots, it adds up the chances of moving to each spot
                    if (total > randnum)     //If this spot was chosen
                    {
                        triedmove = true;    //Stop looping through each spot
                        moved = meth.tryMove(x,y,j,false); //Try to move
                    }
                    j++;
                }
                i++;
            }
            i = j = 0;
	        while (i < 5 && !moved) //If it still haven't moved, try to move it onto other particles with less weight
			{                       //It is the exact same code as above
				randnum = rand.nextDouble();
				triedmove = false;
				total = 0;
				while(j < 8 && !triedmove)
				{
					total += chances[j];
					if (total > randnum)
					{
						triedmove = true;
						moved = meth.tryMove(x,y,j,true); //Move by moving onto particles with less weight
					}
					j++;
				}
				i++;
			}
		}
	    }
	}

	private void CheckStateChanges(final int x, final int y)
	{

	}
}
