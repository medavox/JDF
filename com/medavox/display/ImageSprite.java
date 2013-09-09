package com.medavox.display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class ImageSprite extends Sprite
{
    private BufferedImage currentImage;
	public ImageSprite(String imageName)
	{
		File imageToLoad = new File(imageName);
		try
		{
			currentImage = ImageIO.read(imageToLoad);
			//debugging info
			System.out.println("Image Width: "+currentImage.getWidth());
			System.out.println("Image Height: "+currentImage.getHeight());
			double aspectRatio = (double)currentImage.getWidth() / (double)currentImage.getHeight();
			System.out.println("Aspect Ratio: "+aspectRatio);
			repaint();
		}
		//Very old catch blocks from image validator code.
		//Could still come in handy...
		catch(IOException e)
		{
            System.err.println("IO Exception in \n" + imageToLoad);
        }
		catch(IllegalArgumentException iaError)
		{
            System.err.println("\n Illegal Argument Exception: " +
			iaError.getMessage()+"\nin " + imageToLoad);
        }
		catch(OutOfMemoryError memError)
		{
            System.err.println("OutOfMemoryError: "+memError.getMessage()+
			" on file "+imageToLoad+"; continuing ONWARDS!");
        }
	}

	public void drawSprite(Graphics2D g2)
	{
		g2.drawImage(currentImage, (int)x, (int)y, currentImage.getWidth(), currentImage.getHeight(), this);
	}
	
	public int getHeight()
	{
		return currentImage.getHeight();
	}
	public int getWidth()
	{
		return currentImage.getWidth();
	}
}
