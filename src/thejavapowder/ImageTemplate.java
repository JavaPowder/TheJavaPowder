package thejavapowder;

import java.awt.*;

public class ImageTemplate {

	public ImageTemplate(Image image, int x, int y, int width, int height)
	{
		this.image = image;
		this.x = x;
		this.y = y;
		this.Width = width;
		this.Height = height;
	}

	public final Image image;

	public final int x;
	public final int y;

	public final int Width;
	public final int Height;

}
