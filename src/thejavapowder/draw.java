package thejavapowder;

import java.awt.*;

public class draw
{
	private final Variables var = thejavapowder.TheJavaPowder.var;
	private final Methods meth = new Methods();
	
	public void drawPoint(int x, int y, byte id, int Width, int Height, byte[][] Map, int[][] VMap, byte[][] PMap, float[][] HMap) {
		if ((var.active || !(var.Simulating || var.tempSimulating)) && x > 1 && y > 1 && x < Width && y < Height) {
			if(var.leftClick)
			{
				switch(id)
				{
                case -126:
                    if (Map[x][y] == 4 || Map[x][y] == 6) {
                        VMap[x][y] += 5;
                        if (VMap[x][y] > 1500)
                            VMap[x][y] = 1500;
                    }
                    break;
                case -125:
                    if (PMap[x][y] < 50) {
                        if (Map[x][y] == 11 || Map[x][y] == 10 || Map[x][y] == 14) {
                            PMap[x][y] += 1;
                        }
                    }
                    break;
                default:
                    if (Map[x][y] == -127)//If the target tile is free
                    {
                        meth.createElement(x, y, id, Map, HMap);
                    }
                    break;
				}
			}
			else
			{
				switch(id)
				{
					case -126:
							if (Map[x][y] != 0) {
								meth.clearTile(x, y, Map, PMap, VMap);
							}
						break;
					case -125:
							if (PMap[x][y] > 1) {
								if (Map[x][y] == 11 || Map[x][y] == 10 || Map[x][y] == 14) {
									PMap[x][y] -= 1;
								}
							}
						break;

					default:
							if (Map[x][y] != -127)//If the target tile is not free
							{
								meth.clearTile(x, y, Map, PMap, VMap);
							}
						break;
				}
			}
		}
	}

	public void drawCircle(int x, int y, byte rd, byte id, int Width, int Height, int winZoom, byte[][] Map, int[][] VMap, byte[][] PMap, float[][] HMap) {
		int tempy = y;
		for (int i = x - rd; i <= x; i++) {
			double distance = Math.sqrt(Math.pow((double) x - (double) i, (double) 2) + Math.pow((double) y - (double) tempy, (double) 2));
			while (distance <= rd) {
				tempy = tempy - 1;
				distance = Math.sqrt(Math.pow((double) x - (double) i, (double) 2) + Math.pow((double) y - (double) tempy, (double) 2));
			}
			tempy = tempy + 1;
			for (int j = tempy; j <= 2 * y - tempy; j++) {
				if (i > 0 && j > 0 && i < Width * winZoom && j < Height * winZoom)
					drawPoint(i, j, id, Width, Height, Map, VMap, PMap, HMap);
				if (2 * x - i > 0 && j > 0 && 2 * x - i < Width * winZoom && j < Height * winZoom)
					drawPoint(2 * x - i, j, id, Width, Height, Map, VMap, PMap, HMap);
			}
		}
	}

	public void drawSquare(int xc, int yc, byte size, byte id, int Width, int Height, byte[][] Map, int[][] VMap, byte[][] PMap, float[][] HMap) {
		for (int x = xc; x < xc + size; x++) {
			for (int y = yc; y < yc + size; y++) {
				drawPoint(x, y, id, Width, Height, Map, VMap, PMap, HMap);
			}
		}
		if (size == 0)
			drawPoint(xc, yc, id, Width, Height, Map, VMap, PMap, HMap);
	}

	public void drawstuff(int x, int y, byte s, byte i, int Width, int Height, int winZoom, byte[][] Map, int[][] VMap, byte[][] PMap, float[][] HMap, int Shape) {
		if (Shape == 0)
			drawSquare(x - s, y - s, (byte) (s * 2), i, Width, Height, Map, VMap, PMap, HMap);
		else
			drawCircle(x, y, s, i, Width, Height, winZoom, Map, VMap, PMap, HMap);
	}

	public void create_line(int x1, int y1, int x2, int y2, byte rd, byte id, int Width, int Height, int winZoom, byte[][] Map, int[][] VMap, byte[][] PMap, float[][] HMap, int Shape) // From old autorun.lua
	{
		if (x1 > x2) {
			int xc2 = x1;
			x1 = x2;
			x2 = xc2;
			int yc2 = y1;
			y1 = y2;
			y2 = yc2;
		}
		drawstuff(x1, y1, rd, id, Width, Height, winZoom, Map, VMap, PMap, HMap, Shape);
		if (x1 == x2) {
			if (y1 != y2) {
				int yc1 = Math.min(y1, y2);
				int yc2 = Math.max(y1, y2);
				for (int i = yc1; i < yc2; i++)
					drawstuff(x1, i, rd, id, Width, Height, winZoom, Map, VMap, PMap, HMap, Shape);
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
				drawstuff(i, j, rd, id, Width, Height, winZoom, Map, VMap, PMap, HMap, Shape);
		}
	}

	public void drawCursorPoint(int x, int y, Graphics bufferGraphics, int winZoom, int realZoom, int Zoom, int ScrollX, int ScrollY) {
		int size = realZoom-1;//The - 1 makes the pixes looked zoomed in
		if (Zoom == 1) size++;
		    bufferGraphics.fillRect((x * Zoom - ScrollX) * winZoom, (y * Zoom - ScrollY) * winZoom, size, size);
	}

	public void drawCircleCursor(int x, int y, byte rd, Graphics bufferGraphics, int Width, int Height, int winZoom, int realZoom, int Zoom, int ScrollX, int ScrollY) {
		int tempy = y, oldy;
		for (int i = x - rd; i <= x; i++) {
			oldy = tempy;
			double distance = Math.sqrt(Math.pow((double) x - (double) i, (double) 2) + Math.pow((double) y - (double) tempy, (double) 2));
			while (distance <= rd) {
				tempy = tempy - 1;
				distance = Math.sqrt(Math.pow((double) x - (double) i, (double) 2) + Math.pow((double) y - (double) tempy, (double) 2));
			}
			tempy = tempy + 1;
			if (oldy != tempy)
				oldy--;
			for (int j = tempy; j <= oldy; j++) {
				int i2 = 2*x-i, j2 = 2*y-j;
				if (i2 != i && i > 0 && j > 0 && i < Width * winZoom && j < Height * winZoom)
					drawCursorPoint(i, j, bufferGraphics, winZoom, realZoom, Zoom, ScrollX, ScrollY);
				if (i2 > 0 && j > 0 && i2 < Width * winZoom && j < Height * winZoom)
					drawCursorPoint(i2, j, bufferGraphics, winZoom, realZoom, Zoom, ScrollX, ScrollY);
				if (j2 != j && i > 0 && j2 > 0 && i < Width * winZoom && j2 < Height * winZoom)
					drawCursorPoint(i, j2, bufferGraphics, winZoom, realZoom, Zoom, ScrollX, ScrollY);
				if (i2 != i && j2 != j && i2 > 0 && j2 > 0 && i2 < Width * winZoom && j2 < Height * winZoom)
					drawCursorPoint(i2, j2, bufferGraphics, winZoom, realZoom, Zoom, ScrollX, ScrollY);
			}
		}
	}

	public void drawSquareCursor(int xc, int yc, byte size, Graphics bufferGraphics, int winZoom, int realZoom, int Zoom, int ScrollX, int ScrollY) {
		for (int x = xc; x < xc + size; x++) {
			if (x == xc || x == xc + size - 1)
			{
				for (int y = yc; y < yc + size; y++) {
					drawCursorPoint(x, y, bufferGraphics, winZoom, realZoom, Zoom, ScrollX, ScrollY);
				}
			}
			else
			{
				drawCursorPoint(x, yc, bufferGraphics, winZoom, realZoom, Zoom, ScrollX, ScrollY);
				drawCursorPoint(x, yc + size - 1, bufferGraphics, winZoom, realZoom, Zoom, ScrollX, ScrollY);
			}
		}
		if (size == 0)
			drawCursorPoint(xc, yc, bufferGraphics, winZoom, realZoom, Zoom, ScrollX, ScrollY);
	}

	public void drawCursor(int x, int y, byte s, Graphics bufferGraphics, int Width, int Height, int winZoom, int Shape, int realZoom, int Zoom, int ScrollX, int ScrollY) {
		if (Shape == 0)
			drawSquareCursor(x - s, y - s, (byte) (s * 2), bufferGraphics, winZoom, realZoom, Zoom, ScrollX, ScrollY);
		else
			drawCircleCursor(x, y, s, bufferGraphics, Width, Height, winZoom, realZoom, Zoom, ScrollX, ScrollY);
	}
}
