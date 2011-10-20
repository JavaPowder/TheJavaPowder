package thejavapowder;

public class Methods {

    Variables var = new Variables();
    char t;

    /*
          var.Coffee.reactives,
          var.Wall.reactives,
          var.Methane.reactives,
          var.Water.reactives,
          var.Iron.reactives,
          var.Battery.reactives,
          var.Copper.reactives,
          var.SemiConductorA.reactives,
          var.SemiConductorB.reactives,
          var.Screen.reactives,
          var.Resistor.reactives,
          var.RechargableBattery.reactives,
          var.PowerDrainer.reactives,
          var.Crossing.reactives,
          var.Switch.reactives
    */

    final byte[] weightA = new byte[]{
            var.Coffee.weight,
            var.Wall.weight,
            var.Methane.weight,
            var.Water.weight,
            var.Iron.weight,
            var.Battery.weight,
            var.Copper.weight,
            var.SemiConductorA.weight,
            var.SemiConductorB.weight,
            var.Screen.weight,
            var.Resistor.weight,
            var.RechargableBattery.weight,
            var.PowerDrainer.weight,
            var.Crossing.weight,
            var.Switch.weight,
            var.Fire.weight
    };

     final char[] typeA = new char[]{
        var.Coffee.state,
        var.Wall.state,
        var.Methane.state,
        var.Water.state,
        var.Iron.state,
        var.Battery.state,
        var.Copper.state,
        var.SemiConductorA.state,
        var.SemiConductorB.state,
        var.Screen.state,
        var.Resistor.state,
        var.RechargableBattery.state,
        var.PowerDrainer.state,
        var.Crossing.state,
        var.Switch.state,
        var.Fire.state
    };

    @SuppressWarnings("static-access")
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

    @SuppressWarnings("static-access")
    public void getReactives(byte id) {
        /*if(id != -127)
         {
            var.reactives = this.reactivesA[id];
            return;
         }*/
        switch (id) {
            case 0:
                this.var.reactives = var.Coffee.reactives;
                break;
            case 1:
                this.var.reactives = var.Wall.reactives;
                break;
            case 2:
                this.var.reactives = var.Methane.reactives;
                break;
            case 3:
                this.var.reactives = var.Water.reactives;
                break;
            case 4:
                this.var.reactives = var.Iron.reactives;
                break;
            case 5:
                this.var.reactives = var.Battery.reactives;
                break;
            case 6:
                this.var.reactives = var.Copper.reactives;
                break;
            case 7:
                this.var.reactives = var.SemiConductorA.reactives;
                break;
            case 8:
                this.var.reactives = var.SemiConductorB.reactives;
                break;
            case 9:
                this.var.reactives = var.Screen.reactives;
                break;
            case 10:
                this.var.reactives = var.Resistor.reactives;
                break;
            case 11:
                this.var.reactives = var.RechargableBattery.reactives;
                break;
            case 12:
                this.var.reactives = var.PowerDrainer.reactives;
                break;
            case 13:
                this.var.reactives = var.Crossing.reactives;
                break;
            case 14:
                this.var.reactives = var.Switch.reactives;
                break;
            case 15:
                this.var.reactives = var.Fire.reactives;
                break;

        }
    }


    @SuppressWarnings("static-access")
    public int getWeight(byte ID) {
        if(ID != -127)
        {
        return weightA[ID];
        }
        else
        {
            return -127;
        }
    }


    @SuppressWarnings("static-access")
    public void getReaction(byte id, byte reactId) {
        switch (id) {
            case 0:
                var.reaction = var.Coffee.react[reactId];
                break;
            case 1:
                var.reaction = var.Wall.react[reactId];
                break;
            case 2:
                var.reaction = var.Methane.react[reactId];
                break;
            case 3:
                var.reaction = var.Water.react[reactId];
                break;
            case 4:
                var.reaction = var.Iron.react[reactId];
                break;
            case 5:
                var.reaction = var.Battery.react[reactId];
                break;
            case 6:
                var.reaction = var.Copper.react[reactId];
                break;
            case 7:
                var.reaction = var.SemiConductorA.react[reactId];
                break;
            case 8:
                var.reaction = var.SemiConductorB.react[reactId];
                break;
            case 9:
                var.reaction = var.Screen.react[reactId];
                break;
            case 10:
                var.reaction = var.Resistor.react[reactId];
                break;
            case 11:
                var.reaction = var.RechargableBattery.react[reactId];
                break;
            case 12:
                var.reaction = var.PowerDrainer.react[reactId];
                break;
            case 13:
                var.reaction = var.Crossing.react[reactId];
                break;
            case 14:
                var.reaction = var.Switch.react[reactId];
                break;
            case 15:
                var.reaction = var.Fire.react[reactId];
                break;
        }
    }

    @SuppressWarnings("static-access")
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

    @SuppressWarnings("static-access")
    public char getType(int x, int y) {
        if(var.Map[x][y] != -127)
        {
             t = typeA[var.Map[x][y]];
        }
        else
        {
            t = 's';
        }
        return t;

    }

    @SuppressWarnings("static-access")
    public boolean GetConductive(byte id) {
        switch (id) {
            case 0:
                var.conductive = var.Coffee.conductive;
                break;
            case 1:
                var.conductive = var.Wall.conductive;
                break;
            case 2:
                var.conductive = var.Methane.conductive;
                break;
            case 3:
                var.conductive = var.Water.conductive;
                break;
            case 4:
                var.conductive = var.Iron.conductive;
                break;
            case 5:
                var.conductive = var.Battery.conductive;
                break;
            case 6:
                var.conductive = var.Copper.conductive;
                break;
            case 7:
                var.conductive = var.SemiConductorA.conductive;
                break;
            case 8:
                var.conductive = var.SemiConductorB.conductive;
                break;
            case 9:
                var.conductive = var.Screen.conductive;
                break;
            case 10:
                var.conductive = var.Resistor.conductive;
                break;
            case 11:
                var.conductive = var.RechargableBattery.conductive;
                break;
            case 12:
                var.conductive = var.PowerDrainer.conductive;
                break;
            case 13:
                var.conductive = var.Crossing.conductive;
                break;
            case 14:
                var.conductive = var.Switch.conductive;
                break;
            case 15:
                var.conductive = var.Fire.conductive;
                break;
            case -127:
                var.conductive = false;
                break;
        }
        return var.conductive;
    }

}

