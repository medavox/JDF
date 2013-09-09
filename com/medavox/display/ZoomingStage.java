package com.medavox.display;
import java.awt.Graphics;
//import java.awt.image.BufferedImage;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Toolkit;
//import java.awt.AWTEvent;

public class ZoomingStage extends Stage implements MouseWheelListener, MouseMotionListener, MouseListener
{
    protected boolean mousePressed = false;

    private double scale;//the current zoom. 1 is normal, <1 is zoomed out, >1 is zoomed in
    private double zoomAmount = 1.1;//the amount the zoom changes by, each MouseWheelEvent
    private double cameraViewX;//the x coordinate of the top-left corner of the zooming "stage"
    private double cameraViewY;//the y coordinate of the top-left corner of the zooming "stage"
    private int prevDragX;//the x coord of the mouse in the previous drag event call
    private int prevDragY;

    public ZoomingStage(int stageWidth,
                        int stageHeight,
                        int bgColour,
                        int frameRate,
                        Runnable frameCode)
    {
        super(stageWidth, stageHeight, bgColour, frameRate, frameCode);
        scale = 1;
        cameraViewX = 0;
        cameraViewY = 0;
        //enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        //enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }
    
    public ZoomingStage(int initialCameraViewX,
                        int initialCameraViewY,
                        double initialScale,
                        int stageWidth,
                        int stageHeight,
                        int bgColour,
                        int frameRate,
                        Runnable frameCode)
    {
        super(stageWidth, stageHeight, bgColour, frameRate, frameCode);
        scale = initialScale;
        cameraViewX = initialCameraViewX;
        cameraViewY = initialCameraViewY;
        //enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        //enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }
    
    /*TODO: create a stage width and height which is independent of the jcomponent/window size
      TODO: make it so that camera position centers on player avatar rather than mouse, upon mousewheel zooming*/
    /**Standard paint method, inherited and overridden from an early graphical
     * ancestor (probably {@link java.awt.Component} homo graphicalopithecus LOL).
     * Renders children to the stage (at their x and y coords) in order, starting at 0; 
     * allows predictable z-ordering.*/
    public void paint(Graphics g)
    {
        //super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        
        //render background colour
        Color oldColour = g.getColor();
        
        g2.translate(cameraViewX, cameraViewY);//offset all further draw op coords by the placement offset
        g2.scale(scale, scale);//scale all further draw ops by the current scale
        
        //draw black bg with white border
        /*g.setColor(new Color(bgColour));
        g.fillRect(0,0, stageWidth, stageHeight);
        g.setColor(Color.WHITE);
        g.drawRect(0,0, stageWidth, stageHeight);
        g.setColor(oldColour);*/
        
        //draw children to stage
        for(int i = 0; i < children.size(); i++)
        {//TODO:fix slight tendency for zooming in to pan upwards/scroll down
            //move drawing head to next sprite's coords
            g2.translate(children.get(i).getXAsDouble(), children.get(i).getYAsDouble() );
            
            //redraw the current sprite from the list
            children.get(i).drawSprite(g2);
            //System.out.println("drawing child "+children.get(i));
            
			/*OLD
            //will be needed again if/when we draw map terrain from an image
            double placementWidth = (double)currentImage.getWidth() * scale;
            double placementHeight = (double)currentImage.getHeight() * scale;
                        
            g.drawImage(currentImage, (int)cameraViewX, (int)cameraViewY,
            (int)placementWidth, (int)placementHeight, this);*/

            //move translated drawing head back to (relative -- zoomed) starting position
            g2.translate(-children.get(i).getXAsDouble(), -children.get(i).getYAsDouble() );
            
        }
        g2.scale(1/scale, 1/scale);
        g2.translate(-cameraViewX, -cameraViewY);
        
        Toolkit.getDefaultToolkit().sync();//linux-specific anti-flicker code
        g.dispose();
    }
    
    public void mouseWheelMoved(MouseWheelEvent mwe)
    {
        //double pixelsLeft = cameraViewX - mwe.getX();//switched since this last worked
        //double pixelsAbove = cameraViewY - mwe.getY();
        
        double pixelsLeft = mwe.getX() - cameraViewX;//number of pixels left of the mouse
        double pixelsAbove = mwe.getY() - cameraViewY;//number of pixels above the mouse
        
        double oldScale = scale;
        
        double newPixelsAbove;
        double newPixelsLeft;
        //update the scale;
        if(mwe.getWheelRotation() < 0)
        {//zooming in
            scale *= zoomAmount;
            repaint();
            newPixelsAbove = pixelsAbove * zoomAmount;
            newPixelsLeft = pixelsLeft * zoomAmount;
        }
        else
        {//zooming out
            scale /= zoomAmount;
            repaint();
            newPixelsAbove = pixelsAbove / zoomAmount;
            newPixelsLeft = pixelsLeft / zoomAmount;
        }
        //TODO: tidy up bad int and double conversions, avoid double arithmetic where possible
        //-- to stop the zooming messing up towards the extremes, as in 0.50000000000001
        System.out.println("scale change: "+oldScale+" to "+scale);
        
        cameraViewX += (pixelsLeft - newPixelsLeft);
        cameraViewY += (pixelsAbove - newPixelsAbove);
    }
    
    //Unused methods, but required for implementation of MouseListener and MouseMotionListener interfaces
    public void mouseMoved(MouseEvent e){}
    public void	mouseClicked(MouseEvent e){}
    public void	mouseEntered(MouseEvent e){}
    public void	mouseExited(MouseEvent e){}
    
    public void	mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            //System.out.println("mousePressed");
            prevDragX = e.getX();
            prevDragY = e.getY();
            mousePressed = true;
        }
    }
    public void	mouseReleased(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            //System.out.println("mouseReleased");
            mousePressed = false;            
        }
    }
    
    public void mouseDragged(MouseEvent e)
    {
        //System.out.println("DragX:"+e.getX()+"\nDragY:"+e.getY());
        if(mousePressed)
        {
            int diffX = e.getX() - prevDragX;
            int diffY = e.getY() - prevDragY;
            cameraViewX += diffX;
            cameraViewY += diffY;
            repaint();
            prevDragX = e.getX();
            prevDragY = e.getY();
        }
    }
}
