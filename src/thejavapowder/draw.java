package thejavapowder;

public class draw
{
    Variables var = thejavapowder.TheJavaPowder.var;

    public void drawPoint(int x, int y, byte id) {
        if ((var.active || !(var.Simulating || var.tempSimulating)) && x > 1 && y > 1 && x < var.Width && y < var.Height) {
            var.wait = 30;
            if (id != -126 && id != -125) {
                if (var.leftClick) {
                    if (var.Map[x][y] == -127)//If the target tile is free
                    {
                        var.Map[x][y] = id;
                        var.HMap[x][y] = (short)var.Elements[id].defaultTemp;
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

    public void drawstuff(int x, int y, byte s, byte i) {
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
        drawstuff(x1, y1, rd, id);
        if (x1 == x2) {
            if (y1 != y2) {
                int yc1 = Math.min(y1, y2);
                int yc2 = Math.max(y1, y2);
                for (int i = yc1; i < yc2; i++)
                    drawstuff(x1, i, rd, id);
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
                drawstuff(i, j, rd, id);
        }
    }
}
