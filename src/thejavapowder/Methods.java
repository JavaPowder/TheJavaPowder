package thejavapowder;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;

public class Methods {

	private final Variables var = thejavapowder.TheJavaPowder.var;

    public void createParticle(int x, int y, byte id, byte[][] Map) {
        for (int i = 0; i < var.reaction[7]; i++) {
            if (Map[x][y - 1] == -127) {
                Map[x][y - 1] = id;
            } else if (Map[x][y + 1] == -127) {
                Map[x][y + 1] = id;
            } else if (Map[x + 1][y] == -127) {
                Map[x + 1][y] = id;
            } else if (Map[x - 1][y] == -127) {
                Map[x - 1][y] = id;
            } else if (Map[x + 1][y + 1] == -127) {
                Map[x + 1][y + 1] = id;
            } else if (Map[x - 1][y + 1] == -127) {
                Map[x - 1][y + 1] = id;
            } else if (Map[x + 1][y - 1] == -127) {
                Map[x + 1][y - 1] = id;
            } else if (Map[x - 1][y - 1] == -127) {
                Map[x - 1][y - 1] = id;
            }
        }
    }
	
	public void createElement(int x, int y, byte id, byte[][] Map, float[][] HMap)
	{
		Map[x][y] = id;
		HMap[x][y] = (short)var.Elements[id].defaultTemp;
	}

	public void clearTile(int x, int y, byte[][] Map, byte[][] PMap, int[][] VMap)
	{
		Map[x][y] = -127;//Clean the Map
		VMap[x][y] = 0;//Clean the VMap
		PMap[x][y] = 0;//Clean the PMap
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

    public void getSurroundings(int x, int y, int Width, int Height, byte[][] Map) {
        if (x < Width - 1 && x > 0 && y < Height - 1 && y > 0) {
            var.surArray[0] = Map[x - 1][y - 1];
            var.surArray[1] = Map[x][y - 1];
            var.surArray[2] = Map[x + 1][y - 1];
            var.surArray[3] = Map[x - 1][y];
            // Note the abscence of the center square
            var.surArray[4] = Map[x + 1][y];
            var.surArray[5] = Map[x - 1][y + 1];
            var.surArray[6] = Map[x][y + 1];
            var.surArray[7] = Map[x + 1][y + 1];
        }

    }

    public boolean canMove(final byte p1, final byte p2, final boolean weight)
    {
        if (weight)
            return p2 == -127 || var.Elements[p1].weight > var.Elements[p2].weight; //Move onto empty spaces or spaces where the element has less weight
        else
            return p2 == -127; //Only move onto empty spaces
    }

    public boolean tryMove(final int x, final int y, final int i, final boolean change, final boolean pressure, int Width, int Height, byte[][] Map, float[][] HMap, byte[][] LMap, float[][] VxMap, float[][] VyMap)
        {
            int j, k, num = 7;
			for (k = 1; k > -2; k--) //Go through all 8 spaces around a particle backwards
				for (j = 1; j > -2; j--)
				{
					if (j != 0 || k != 0) //If it's not the center space
					{
						int x2 = x+j+(int)(VxMap[x/4][y/4]*Math.random());
						int y2 = y+k+(int)(VyMap[x/4][y/4]*Math.random());

						if (i == num &&
							x2 < Width &&
							y2 > 0 &&
							y2 < Height &&
							x2 > 0&&
							canMove(Map[x][y],Map[x2][y2],change)) //If it's the space you were trying to move to and you can move there
						{
							moveElement(x, y, x2, y2,change, pressure, Width, Height, Map, HMap, LMap, VxMap, VyMap); //Move the particle
							return true; //return that it moved
						}
						if (i == num) //If it's the space you were trying to move to but you didn't move
							return false; //return that you didn't move
						num--;
					}
				}
            return false; //return that you didn't move
        }

        public void moveElement(final int x1, final int y1, final int x2, final int y2, final boolean change, boolean pressure, int Width, int Height, byte[][] Map, float[][] HMap, byte[][] LMap, float[][] VxMap, float[][] VyMap)
        {
            if(change)//If we are exchanging the values (Because the weight of the particle we are moving is bigger then the target)
            {
                 final byte element = Map [x2][y2];
                 final float temp   = HMap[x2][y2];
	             final byte life    = LMap[x2][y2];
                 HMap[x2][y2]   = HMap[x1][y1];
                 Map [x2][y2]   = Map [x1][y1];
	             LMap [x2][y2]  = LMap [x1][y1];
                 Map [x1][y1]   = element;
                 HMap[x1][y1]   = temp;
	             LMap[x1][y1]   = life;
            }
            else
            {
                Map [x2][y2] = Map[x1][y1];
                HMap[x2][y2] = HMap[x1][y1];
	            LMap[x2][y2] = LMap[x1][y1];
                Map [x1][y1] = -127;
                HMap[x1][y1] = 0;
	            LMap[x1][y1] = 0;
	            if(pressure && validAirSpace(x1 / 4, y1 / 4, Width, Height)) //Just a test to see if pressure works
	            {
					int xchange = x2-x1;
					xchange = Math.min(1,Math.max(-1,xchange));
					int ychange = y2-y1;
					ychange = Math.min(1,Math.max(-1,ychange));
					VxMap[x1/4][y1/4] += xchange/50.0;
					VyMap[x1/4][y1/4] += ychange/50.0;
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

	public boolean validAirSpace(int x, int y, int Width, int Height)
	{
		return (x < Width/4 && x >= 0 && y < Height/4 && y >= 0);
	}


}

