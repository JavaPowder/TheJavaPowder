package thejavapowder;

public class Methods {

    Variables var = thejavapowder.TheJavaPowder.var;

    public void createParticle(int x, int y, byte id) {
        for (int i = 0; i < var.reaction[7]; i++) {
            if (var.Map[x][y - 1] == -127) {
                var.Map[x][y - 1] = id;
            } else if (var.Map[x][y + 1] == -127) {
                var.Map[x][y + 1] = id;
            } else if (var.Map[x + 1][y] == -127) {
                var.Map[x + 1][y] = id;
            } else if (var.Map[x - 1][y] == -127) {
                var.Map[x - 1][y] = id;
            } else if (var.Map[x + 1][y + 1] == -127) {
                var.Map[x + 1][y + 1] = id;
            } else if (var.Map[x - 1][y + 1] == -127) {
                var.Map[x - 1][y + 1] = id;
            } else if (var.Map[x + 1][y - 1] == -127) {
                var.Map[x + 1][y - 1] = id;
            } else if (var.Map[x - 1][y - 1] == -127) {
                var.Map[x - 1][y - 1] = id;
            }
        }
    }
	
	public void createElement(int x, int y, byte id)
	{
		var.Map[x][y] = id;
		var.HMap[x][y] = (short)var.Elements[id].defaultTemp;
	}

	public void clearTile(int x, int y)
	{
		var.Map[x][y] = -127;//Clean the Map
		var.VMap[x][y] = 0;//Clean the VMap
		var.PMap[x][y] = 0;//Clean the PMap
	}
		

    public void getReactives(byte ID) {
        if(ID >= 0 && ID < var.NUM_ELS)
        {
            this.var.reactives = var.Elements[ID].reactives;
        }

    }

    public void getReaction(byte id, byte reactId) {
        var.reaction = var.Elements[id].react[reactId];
    }

    public void getSurroundings(int x, int y) {
        if (x < var.Width - 1 && x > 0 && y < var.Height - 1 && y > 0) {
            var.surArray[0] = var.Map[x - 1][y - 1];
            var.surArray[1] = var.Map[x][y - 1];
            var.surArray[2] = var.Map[x + 1][y - 1];
            var.surArray[3] = var.Map[x - 1][y];
            // Note the abscence of the center square
            var.surArray[4] = var.Map[x + 1][y];
            var.surArray[5] = var.Map[x - 1][y + 1];
            var.surArray[6] = var.Map[x][y + 1];
            var.surArray[7] = var.Map[x + 1][y + 1];
        }

    }

    public boolean canMove(final byte p1, final byte p2, final boolean weight)
    {
        if (weight)
            return p2 == -127 || var.Elements[p1].weight > var.Elements[p2].weight; //Move onto empty spaces or spaces where the element has less weight
        else
            return p2 == -127; //Only move onto empty spaces
    }

    public boolean tryMove(final int x, final int y, final int i, final boolean change)
        {
            int j, k, num = 7;
			for (k = 1; k > -2; k--) //Go through all 8 spaces around a particle backwards
				for (j = 1; j > -2; j--)
				{
					if (j != 0 || k != 0) //If it's not the center space
					{
						int x2 = x+j+(int)(var.VxMap[x/4][y/4]*Math.random());
						int y2 = y+k+(int)(var.VyMap[x/4][y/4]*Math.random());

						if (i == num &&
							x2 < var.Width &&
							y2 > 0 &&
							y2 < var.Height &&
							x2 > 0&&
							canMove(var.Map[x][y],var.Map[x2][y2],change)) //If it's the space you were trying to move to and you can move there
						{
							moveElement(x,y,x2,y2,change); //Move the particle
							return true; //return that it moved
						}
						if (i == num) //If it's the space you were trying to move to but you didn't move
							return false; //return that you didn't move
						num--;
					}
				}
            return false; //return that you didn't move
        }

        public void moveElement(final int x1, final int y1, final int x2, final int y2, final boolean change)
        {
            if(change)//If we are exchanging the values (Because the weight of the particle we are moving is bigger then the target)
            {
                 final byte element = var.Map [x2][y2];
                 final float temp   = var.HMap[x2][y2];
	             final byte life     = var.LMap[x2][y2];
                 var.HMap[x2][y2]   = var.HMap[x1][y1];
                 var.Map [x2][y2]   = var.Map [x1][y1];
	             var.LMap [x2][y2]   = var.LMap [x1][y1];
                 var.Map [x1][y1]   = element;
                 var.HMap[x1][y1]   = temp;
	             var.LMap[x1][y1]   = life;
            }
            else
            {
                var.Map [x2][y2] = var.Map[x1][y1];
                var.HMap[x2][y2] = var.HMap[x1][y1];
	            var.LMap[x2][y2] = var.LMap[x1][y1];
                var.Map [x1][y1] = -127;
                var.HMap[x1][y1] = 0;
	            var.LMap[x1][y1] = 0;
	            if(var.pressure && validAirSpace(x1/4,y1/4)) //Just a test to see if pressure works
	            {
					int xchange = x2-x1;
					xchange = Math.min(1,Math.max(-1,xchange));
					int ychange = y2-y1;
					ychange = Math.min(1,Math.max(-1,ychange));
					var.VxMap[x1/4][y1/4] += xchange/50.0;
					var.VyMap[x1/4][y1/4] += ychange/50.0;
	            }
            }
        }

    public boolean GetConductive(byte id) {
        if (id >= 0 && id < var.NUM_ELS)
        {
            var.conductive = var.Elements[id].conductive;
            return var.conductive;
        }
        var.conductive = false;
        return false;
    }

	public boolean validAirSpace(int x, int y)
	{
		return (x < var.Width/4 && x >= 0 && y < var.Height/4 && y >= 0);
	}

	public void resetItems()
	{
		int iconNum = 1;
		int borders = 0;
		var.images = new ImageTemplate[]{
				new ImageTemplate(var.elem_electricPng, 206, (var.Height)  * var.winZoom , 27 * var.winZoom, 30 * var.winZoom ),
				new ImageTemplate(var.elem_setpropPng, 206 + 31 * var.winZoom, (var.Height) * var.winZoom, 54 * var.winZoom, 30 * var.winZoom ),
				new ImageTemplate(var.file_loadPng, 206 + 89 * var.winZoom, (var.Height) * var.winZoom, 34 * var.winZoom, 30 * var.winZoom ),
				new ImageTemplate(var.file_savePng, 206 + 127 * var.winZoom, (var.Height) * var.winZoom, 34 * var.winZoom, 30 * var.winZoom ),
		};
		var.imagesMenu = new ImageTemplate[]{
				new ImageTemplate(var.javaPowderPng, 202 * var.winZoom, 21 * var.winZoom, 202 * var.winZoom, 171 * var.winZoom ),
				new ImageTemplate(var.settingsPng, 252 * var.winZoom, 211 * var.winZoom, 102 * var.winZoom, 50 * var.winZoom ),
				new ImageTemplate(var.braincleptPng, 252 * var.winZoom, 301 * var.winZoom, 101 * var.winZoom, 85 * var.winZoom ),
		};

		for(int i = 0; i < 50; i++)//For an good amount of times
		{
			if( i * (40 * var.winZoom) - 40 > var.Width)//If the total length is over the screen
			{
				iconNum = i - 1;//Set the maximum amount of icons
				borders = (var.Width - 40 * iconNum) / 2;//Calculate the free borders
			}
		}
		var.Map         = new byte  [var.Width][var.Height];// The Particle type map
		var.HMap        = new float [var.Width][var.Height];// The Heat type map
		var.VMap        = new int   [var.Width][var.Height];  // The Voltage type map
		var.PMap        = new byte  [var.Width][var.Height];// The Particle Properties Map
		var.LMap        = new byte  [var.Width][var.Height];// The Particle Life Map
		var.PrMap       = new float [var.Width/4][var.Height/4];// The Pressure Map
		var.VxMap       = new float [var.Width/4][var.Height/4];// The X Velocity Map
		var.VyMap       = new float [var.Width/4][var.Height/4];// The Y Velocity Map
		var.OldPrMap    = new float [var.Width/4][var.Height/4];// The Old Pressure Map
		var.OldVxMap    = new float [var.Width/4][var.Height/4];// The Old X Velocity Map
		var.OldVyMap    = new float [var.Width/4][var.Height/4];// The Old Y Velocity Map
		
		for (int x = 0; x < var.Width; x++) {
			for (int y = 0; y < var.Height; y++) {
				var.Map[x][y] = -127;
				var.PMap[x][y] = 0;
				var.VMap[x][y] = 0;
				var.HMap[x][y] = 0;
				var.LMap[x][y] = 0;
			}
		}
		for (int x = 0; x < var.Width/4; x++)
			for (int y = 0; y < var.Height/4; y++) {
				var.PrMap[x][y] = 0;
				var.VxMap[x][y] = 0;
				var.VyMap[x][y] = 0;
				var.OldPrMap[x][y] = 0;
				var.OldVxMap[x][y] = 0;
				var.OldVyMap[x][y] = 0;
			}

	}
}

