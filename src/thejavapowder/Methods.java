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
						if (i == num && canMove(var.Map[x][y],var.Map[x+j][y+k],change)) //If it's the space you were trying to move to and you can move there
						{
							moveElement(x,y,x+j,y+k,change); //Move the particle
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
                 var.HMap[x2][y2]   = var.HMap[x1][y1];
                 var.Map [x2][y2]   = var.Map [x1][y1];
                 var.Map [x1][y1]   = element;
                 var.HMap[x1][y1]   = temp;
            }
            else
            {
                var.Map [x2][y2] = var.Map[x1][y1];
                var.HMap[x2][y2] = var.HMap[x1][y1];
                var.Map [x1][y1] = -127;
                var.HMap[x1][y1] = 0;
	            if(var.pressure) //Just a test to see if pressure works
	            {
					var.PrMap[x1/4][y1/4] += .1;
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

}

