package thejavapowder;

import com.sun.xml.internal.ws.policy.PolicyMap;

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

    boolean electricity   = false;
    boolean heat          = true;
    boolean burn 		  = true;
    boolean pressure      = true;
    boolean debug         = false;
    boolean reactions     = true;
    boolean physics       = true;
    boolean stateChanges  = true;
    boolean life          = true;


    private final Random rand = new Random();
    private final Variables var = thejavapowder.TheJavaPowder.var;
    private final Methods meth = new Methods();
	private final draw draw = new draw();
    /*
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
    OldVyMap    = new float [Width/4][Height/4];// The Old Y Velocity Map  */

    public void update(int Width, int Height, int winZoom, byte[][] Map, float[][] HMap, int[][] VMap, byte[][] PMap, byte[][] LMap, float[][] PrMap, float[][] VxMap, float[][] VyMap, float[][] OldPrMap, float[][] OldVxMap, float[][] OldVyMap) {

        if ((var.Simulating || var.tempSimulating) && var.state == 0) {
            for (int x = Width - 1; x > 1; x--) {
                for (int y = Height - 1; y > 1; y--)//For each Space
                {
                    if (Map[x][y] != -127) {

	                    if (reactions)//Reactive Engine
                        {
	                        UpdateReactions(x, y, Width, Height, Map, VMap, PMap, HMap, PrMap);
                        }
	                    if(Map[x][y] != -127)
	                    {
		                    if(physics)
							{
								UpdateElement(x, y, pressure, Width, Height, Map, HMap, VMap, LMap, PrMap, VxMap, VyMap);
							}
							if(Map[x][y] != -127)
							{
								if(life)
								{										
								UpdateLife(x, y);
								}
								if(Map[x][y] != -127)
								{
									if(stateChanges)
									{
										CheckStateChanges(x,y);
									}
									if(Map[x][y] != -127)
									{
										if (electricity)
										{
											UpdateVoltage(x, y, Map, VMap, PMap);
										}
										if(Map[x][y] != -127)
										{
											if(burn || heat)
											{
												UpdateHeat(x, y, burn, heat, pressure, Map, PMap, VMap, HMap);
											}
										}
									}
								}
	                        }
	                    }

                    }
                }
            }
			if(pressure)
			{
				UpdateAir(Width, Height, VxMap, VyMap, PrMap, OldVxMap, OldVyMap, OldPrMap);
			}
        }//End of Updating maps
        if (var.active && var.state == 0) {//If drawing is active and we are in the game screen
                if (var.Drawing) {//If it should be drawing
                    draw.create_line(var.DrawX, var.DrawY, var.LastDrawX, var.LastDrawY, var.Size, var.Equipped, Width, Height, winZoom, Map, VMap, PMap, HMap);//Draw
                }
            } else {
                if (var.wait < 1) {//If the wait time to draw is over
                    var.active = true;//Activate the drawing
                    var.wait = 40;//Reset the timer
                } else {//If it can't draw and the timer is not up
                    var.wait -= 1;//Make the timer progress
                }
            }
		var.LastDrawX = var.DrawX;//Update the drawing points
		var.LastDrawY = var.DrawY;//Update the drawing points
		var.tempSimulating = false;
    }

	private void UpdateAir(int Width, int Height, float[][] VxMap, float[][] VyMap, float[][] PrMap, float[][] OldVxMap, float[][] OldVyMap, float[][] OldPrMap)
	{
		for (int x = 1; x < Width/4; x++) //For every space on the pressure map
			for (int y = 1; y < Height/4; y++)
			{
				float airchange = VxMap[x-1][y] - VxMap[x][y]; // Calculate how much x and y velocity to transfer
				airchange -= VyMap[x][y-1] - VyMap[x][y];
				PrMap[x][y] *= .9999; // Reduce pressure a little
				PrMap[x][y] += airchange*.3;  // Transfer velocity to pressure
			}
		for (int x = 0; x < Width/4-1; x++) //For every space on the velocity maps
			for (int y = 0; y < Height/4-1; y++)
			{
				float airchangex = PrMap[x][y] - PrMap[x+1][y];  // Calculate how much pressure to transfer
				float airchangey = PrMap[x][y] - PrMap[x][y+1];
				VxMap[x][y] *= .999; // Reduce velocity a little
				VyMap[x][y] *= .999;
				VxMap[x][y] += airchangex*.4; // Transfer pressure to velocity
				VyMap[x][y] += airchangey*.4;
			}
		for (int x = 0; x < Width/4; x++) //For every space on the pressure map
			for (int y = 0; y < Height/4; y++)
			{
				if (Math.abs(PrMap[x][y]) < .0000001) //If the pressure is really small
					PrMap[x][y] = 0;        //Make it 0
				if (Math.abs(VxMap[x][y]) < .0000001) //If the x velocity is really small
					VxMap[x][y] = 0;        //Make it 0
				if (Math.abs(VyMap[x][y]) < .0000001) //If the y velocity is really small
					VyMap[x][y] = 0;        //Make it 0
				if (PrMap[x][y] != 0 || VxMap[x][y] != 0 || VyMap[x][y] != 0)
				{
					float airChange = 0;
					float airChangeX = 0;
					float airChangeY = 0;
					for (int i = -1; i <= 1; i++) //For every pressure space around this spot
						for (int j = -1; j <= 1; j++)
							if (meth.validAirSpace(x + i, y + j, Width, Height)) //If it's a valid spot
							{
								airChange -= (PrMap[x][y] - PrMap[x+i][y+j])/5; //calculate how much air to transfer to this spot
								airChangeX -= (VxMap[x][y] - VxMap[x+i][y+j])/5*.7; //calculate how much x velocity to transfer to this spot
								airChangeY -= (VyMap[x][y] - VyMap[x+i][y+j])/5*.7; //calculate how much y velocity to transfer to this spot
							}
					OldPrMap[x][y] = airChange + (PrMap[x][y]*.99f); //Transfer it
					OldVxMap[x][y] = airChangeX + (VxMap[x][y]*.99f);
					OldVyMap[x][y] = airChangeY + (VyMap[x][y]*.99f);
				}
			}
		System.arraycopy(OldPrMap,0,PrMap,0,PrMap.length); //Copy the new pressure array into the real pressure array
		System.arraycopy(OldVxMap,0,VxMap,0,VxMap.length); //Copy the new x velocity array into the real x velocity array
		System.arraycopy(OldVyMap,0,VyMap,0,VyMap.length); //Copy the new y velocity array into the real y velocity array
	}

	private void UpdateReactions(int x, int y, int Width, int Height, byte[][] Map, int[][] VMap, byte[][] PMap, float[][] HMap, float[][] PrMap)
	{
		meth.getReactives(Map[x][y]);
		if (var.reactives != null)
		{
			meth.getSurroundings(x, y, Width, Height, Map);
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

						if (Map[x][y] >= 0 && Map[x - 1][y - 1] >= 0)
						{
							meth.getReaction(Map[x][y], Map[x - 1][y - 1]);//Get the Reaction needed
							if (var.reaction != null)
							{
								Map[x][y] = var.reaction[0];

								if (var.reaction[1] == 1)
									Map[x - 1][y - 1] = -127;

								VMap[x][y] += var.reaction[2];
								PMap[x][y] = var.reaction[3];
								HMap[x][y] += var.reaction[4];
								if (meth.validAirSpace(x / 4, y / 4, Width, Height))
									PrMap[x/4][y/4] += var.reaction[5];

								if (var.reaction[6] != Map[x][y])//If you're not creating what the particle is
									meth.createParticle(x, y, var.reaction[6], Map);//Create it
							}
						}
					}
				}
			}
		}
	}

    private void UpdateVoltage(final int x, final int y, byte[][] Map, int[][] VMap, byte[][] PMap) {
        if (Map[x][y] == 5)//If it's a battery, give it infinite voltage
        {
            VMap[x][y] = 1000;
        }
        if (VMap[x][y] > 1)//If there's Voltage
        {
            switch (Map[x][y])
            {
	            case 4://Iron
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0))//For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(Map[x+xc][y+yc]) || Map[x+xc][y+yc] == 13)//If the surrounding particle is conductive
                            {
                                if (Map[x+xc][y+yc] != 11 && Map[x+xc][y+yc] != 13)//If it's not a rechargable battery or crossing
                                {
                                    if (VMap[x+xc][y+yc] < VMap[x][y]) {
                                        VMap[x][y] -= 3;
                                        VMap[x+xc][y+yc] = VMap[x][y];
                                    }
                                }
                                else if (Map[x+xc][y+yc] == 11) {
                                    VMap[x][y] -= 20;
                                    VMap[x+xc][y+yc] += 20;
                                }
                                else if (Map[x+xc][y+yc] == 13)//If it's crossing
                                {
									for (int i = 2; i < 5; i++)
									{
										if (meth.GetConductive(Map[x+xc*i][y+yc*i]))
										{
											VMap[x][y] -= 3;
                                        	VMap[x+xc*i][y+yc*i] = VMap[x][y];
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
                            if (meth.GetConductive(Map[x+xc][y+yc])) {
                                VMap[x+xc][y+yc] = VMap[x][y];
                            }
                        }
            break;
	            case 6://Copper

                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (meth.GetConductive(Map[x+xc][y+yc])) {
                                if (VMap[x][y] > 29) {
                                        VMap[x][y] -= 30;
                                        VMap[x+xc][y+yc] += 30;
                                } else if (Map[x+xc][y+yc] != 11) {
                                    if (VMap[x+xc][y+yc] < VMap[x][y]) {
                                        VMap[x][y] -= 1;
                                        VMap[x+xc][y+yc] = VMap[x][y];
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
                            if (Map[x+xc][y+yc] == 8 || Map[x+xc][y+yc] == 7) {
                                if (VMap[x+xc][y+yc] < VMap[x][y]) {
                                    VMap[x][y] -= 1;
                                    VMap[x+xc][y+yc] = VMap[x][y];
                                }
                            }
                        }
            break;
	            case 8://Semi Conductor B

                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (Map[x+xc][y+yc] == 4 || Map[x+xc][y+yc] == 6 || Map[x+xc][y+yc] == 8) {
                                if (VMap[x+xc][y+yc] < VMap[x][y]) {
                                    VMap[x][y] -= 1;
                                    VMap[x+xc][y+yc] = VMap[x][y];
                                }
                            }
                        }
            break;
	            case 9://Screen

                if (VMap[x][y] >= 50) {
                    VMap[x][y] -= 50;
                } else {
                    VMap[x][y] = 0;
                }

            break;
	            case 11://Rechargable Battery

                if (VMap[x][y] > 5000) {
                    VMap[x][y] = 5000;
                }
                if (meth.GetConductive(Map[x][y + 1])) {
                    if (VMap[x][y + 1] < VMap[x][y]) {
                        if (VMap[x][y] >= PMap[x][y] * 10)//If we have more then 50 Volts
                        {
                            if (VMap[x][y + 1] < PMap[x][y] * 5)//If the Target have less then 50 Volts
                            {
                                VMap[x][y] -= PMap[x][y] * 10;
                                VMap[x][y + 1] = (PMap[x][y] * 10);
                            }

                        } else {
                            if (VMap[x][y + 1] < PMap[x][y] * 10) {
                                VMap[x][y + 1] += VMap[x][y];
                                VMap[x][y] = 0;
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
                            if (meth.GetConductive(Map[x+xc][y+yc])) {
                                if (VMap[x+xc][y+yc] < VMap[x][y]) {
                                    if (VMap[x][y] >= (PMap[x][y] * 10)) {
                                        VMap[x][y] -= (PMap[x][y] * 10);
                                        VMap[x+xc][y+yc] = (PMap[x][y] * 10);
                                    }
                                }
                            }
                        }
            break;
	            case 12://Power Drainer

                VMap[x][y] = 0;
                for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                    for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                        if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                        {
                            if (Map[x+xc][y+yc] == 4 || Map[x+xc][y+yc] == 6) {
                                if (VMap[x+xc][y+yc] > 0) {
                                    VMap[x+xc][y+yc] = 0;
                                }
                            }
                        }
            break;
	            case 14://Switch

                if (PMap[x][y] > 25) {
                    for (int xc = -1; xc <= 1; xc++)//For 3 tiles on the X axis
                        for (int yc = -1; yc <= 1; yc++)//For 3 tiles on the Y axis
                            if ((xc!=0 || yc!=0) && !(xc!=0 && yc!=0)) // For the 4 spots directly touching it
                            {
                                if (meth.GetConductive(Map[x+xc][y+yc])) {
                                    if (VMap[x+xc][y+yc] < VMap[x][y]) {
                                        VMap[x][y] -= 1;
                                        VMap[x+xc][y+yc] = VMap[x][y];
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
	
	private void UpdateHeat(int x, int y, boolean burn, boolean heat, boolean pressure, byte[][] Map, byte[][] PMap, int[][] VMap, float[][] HMap)
	{
		int i, j, num = 0;
		if (Map[x][y] < 0 )
			return;
		if(burn)
		{
		for (j = -1; j < 2; j++) //Loop through each spot around the particle
			for (i = -1; i < 2; i++)
			{
				if (i != 0 || j != 0) //If it's not the center space
				{
					if (var.surArray[num] == 15) //If burning is active and this space has fire in it
					{
						var.RandomNum = rand.nextInt(50); //Get a random value
						if (var.RandomNum < var.Elements[Map[x][y]].burn) //If that random value was less than the burn value of the element
						{
							Map[x][y] = 15; //Change this particle to fire
							HMap[x][y] += var.Elements[Map[x][y]].burn * 25; //Increase the heat
						}
					}
					if (heat && Map[x+i][y+j] != -127) //If heat conduction is on and the space around the particle is not empty
					{
						final float heatTransfer = (HMap[x][y] - HMap[x+i][y+j])/5; //Calculate how much heat to transfer
						HMap[x][y] -= heatTransfer;     //Transfer heat
						HMap[x+i][y+j] += heatTransfer; //Transfer heat
					}
					num++;
				}
			}
		}
		if(Map[x][y] == 15)
		{
			HMap[x][y]--;
			if(HMap[x][y] < 100)
				meth.clearTile(x, y, Map, PMap, VMap);
		}
	}

    private void UpdateElement(final int x, final int y, boolean pressure, int Width, int Height, byte[][] Map, float[][] HMap, int[][] VMap, byte[][] LMap, float[][] PrMap, float[][] VxMap, float[][] VyMap) {
        if (y <= 2 || y >= Height - 2 || x >= Width - 2 || x <= 2)//If it's out border
        {
            Map[x][y] = -127;//Destroy it
            return;
        }
	    if(Map[x][y] != -127)
	    {
	    final char type = var.Elements[Map[x][y]].state;
        if(Map[x][y] != -127 && type != 's')
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
                    if (!meth.canMove(Map[x][y],Map[x][y+1],false)) //But if there is a particle below it
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
					if (pressure && Map[x][y] == 15)
					{
						PrMap[x/4][y/4] += .01;
						VyMap[x/4][y/4] -= .01;
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
                        moved = meth.tryMove(x, y, j, false, pressure, Width, Height, Map, HMap, LMap, VxMap, VyMap); //Try to move
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
						moved = meth.tryMove(x, y, j, true, pressure, Width, Height, Map, HMap, LMap, VxMap, VyMap); //Move by moving onto particles with less weight
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

    
    boolean simElectricity() {
        return electricity;
    }
    
    void setElectricity(boolean state) {
        electricity = state;
    }

    boolean simHeat() {
        return heat;
    }

    void setHeat(boolean state) {
        heat = state;
    }

    boolean simBurn() {
        return burn;
    }

    void setBurn(boolean state) {
        burn = state;
    }

    boolean simPressure() {
        return pressure;
    }

    void setPressure(boolean state) {
        pressure = state;
    }
    
    boolean simDebug() {
        return debug;
    }

    void setDebug(boolean state) {
        debug = state;
    }
    
    boolean simReactions() {
        return reactions;
    }

    void setReactions(boolean state) {
        reactions = state;
    }

    boolean simPhysics() {
        return physics;
    }

    void setPhysics(boolean state) {
        physics = state;
    }

    boolean simStateChanges() {
        return stateChanges;
    }

    void setStateChanges(boolean state) {
        stateChanges = state;
    }

    boolean simLife() {
        return life;
    }

    void setLife(boolean state) {
        life = state;
    }
}
