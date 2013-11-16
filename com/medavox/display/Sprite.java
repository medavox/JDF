package com.medavox.display;

import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;
/**General-purpose class for creating discrete graphical objects.
 * Sprites are intended to be added to another DisplayObjectContainer descendant,
 * such as another Sprite, or a Stage object.
 * It can contain other sprites, as in ActionScript 3, and has x and y properties,
 * along with basic (z-axis) scaling, which is not fully implemented yet.
 * Note that x and y co-ordinate values are stored internally as doubles,
 * for greater accuracy
 * @author Adam Howard*/
public abstract class Sprite extends DisplayObjectContainer
{
    protected double x;
    protected double y;
    protected double scale;
    protected int width;
    protected int height;
    //protected Image image;
    
    public void setX(double x)
    {
        this.x = x;
    }
    
    public void setY(double y)
    {
        this.y = y;
    }
    
    public void setX(int x)
    {
        this.x = (double)x;
    }
    
    public void setY(int y)
    {
        this.y = (double)y;
    }
    
    /**Gets the x value, rounding the internal double value up or down to the
     * nearest int. */
    public int getX()
    {//rounds the number up or down, to increase integral precision
        return (int)Math.floor(x+0.5d);
    }
    
    /**Gets the y value, rounding the internal double value up or down to the
     * nearest int. */    
    public int getY()
    {
        return (int)Math.floor(y+0.5d);
    }
    
    /**Get X co-ordinate as a double.*/
    public double getXAsDouble()
    {
        return x;
    }
    
    public double getYAsDouble()
    {
        return y;
    }
    /**get the width (in pixels) of the Sprite's graphic. <br /> <b> NOT YET
     IMPLEMENTED, sorry!</b>*/
    public int getWidth()
    {
        return width;
    }
    /**Get the height (in pixels) of the Sprite's graphic. <br /> <b> NOT YET
      IMPLEMENTED, sorry!</b>*/
    public int getHeight()
    {
        return height;
    }
    /**Get the current displayed size of the graphic. 1.0 is normal size 
     (pixel-for-pixel), 0.1 is 10% normal size. <br /> <b> NOT YET IMPLEMENTED, 
      sorry!</b>*/
    public double getScale()
    {
        return scale;
    }
    /**Specify a size multiplier to display the Sprite's graphic at.
     *  1.0 is normal, 0.1 is 10% normal size.
     * <br /> <b> NOT YET IMPLEMENTED, sorry!</b>*/
    public void setScale(double newScale)
    {
        scale = newScale;
    }
    /**Called once per frame for the Sprite to redraw itself.
     * Override this method to provide the sprite with a graphical component.
     * Try to make the drawing's centre fall on the origin (0,0) 
     * of the local graphics instance.*/
    public abstract void drawSprite(Graphics2D g2);
    
    public void setMother(DisplayObjectContainer dad)
    {
        this.parent = dad;
    }
    
    public DisplayObjectContainer getMother()
    {
        return parent;
    }
    
    
    //here begins a whole bunch of tedious boilerplate addMouse*listener overrides, to provide for a lack of visible swing/awt implementation
    public void addMouseListener(MouseListener l)
    {
    
    }
    
    public void addMouseMotionListener(MouseMotionListener l)
    {
    
    }
    
    public void addMouseWheelListener(MouseWheelListener l)
    {
    
    }
}
