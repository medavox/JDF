package com.medavox.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*if something moves offstage, and then we zoom out so we can see it past the 
 * bounds of the stage, do we actually render it or not? and if we don't, in what
 * way will we keep track of its position without actually rendering it?
 * */

/**Main animation thread. Draws all components which are added to its list of children,
 * and handles frame-based animation by rendering at a fixed interval per second*/
@SuppressWarnings("serial")
public class Stage extends DisplayObjectContainer implements Runnable
{
    public int stageWidth;
    public int stageHeight;
    private Thread animThread;
    protected int bgColour = 0;
    public int frameRate;
    private Runnable frameCode;
    //private boolean drawGrid = false;

    /**Create a new stage instance with the specified parameters.
     * NOTE: stageWidth and stageHeight parameters <b>do not</b> take into account
     * window decoration; this is purely the intended size of the Stage. If the 
     * Stage's container is smaller than these values, then the instance variables
     * will shrink accordingly.
     * @param stageWidth the desired width of the new stage object. 
     * @param stageHeight the desired height of the new stage object.
     * @param bgColour the desired background colour of the new stage, expressed as an #rrggbb value.
     * Java's {@link java.awt.Color} class just doesn't cut it.
     * @param frameRate the desired frames per second of the new stage.
     * Currently mutable at runtime, which could balls everything up, but we'll see.
     * @param frameCode a callback class, whose run() method is called every frame. 
     * Allows per-frame code to be run, to update things on the stage and whatnot.
     * Can be null, if no further per-frame code is necessary.
     *  */
    public Stage(int stageWidth, int stageHeight, int bgColour, int frameRate, Runnable frameCode)
    {
        init(stageWidth, stageHeight, bgColour, frameRate, frameCode);
    }
    
    public Stage(int stageWidth, int stageHeight, int bgColour, int frameRate)
    {
        init(stageWidth, stageHeight, bgColour, frameRate, null);
    }
    
    public Stage(int stageWidth, int stageHeight, int bgColour)
    {
        init(stageWidth, stageHeight, bgColour, 30, null);
    }
    /**Create  new stage with default values:<ul><li> background colour: black</li>
    </li> background colour: black</li>
    </li> framerate: 30</li>frame code: none (null)</li>
    @param stageWidth the width of the stage.
    @param stageHeight the height.*/
    public Stage(int stageWidth, int stageHeight)
    {
        init(stageWidth, stageHeight, 0, 30, null);
    }
    
    private void init(int stageWidth, int stageHeight, int bgColour, int frameRate, Runnable frameCode)
    {
        //setDoubleBuffered(true);
        this.bgColour = bgColour;
        this.frameRate = frameRate;
        this.frameCode = frameCode;
        this.stageWidth = stageWidth;
        this.stageHeight = stageHeight;
        
        //MUST make this component nonzero sized; it won't paint anything otherwise
        Dimension sz = new Dimension(stageWidth, stageHeight);
        //setSize(stageWidth, stageHeight);
        setSize(sz);
        setPreferredSize(sz);
        setMinimumSize(sz);
        
        System.out.println("width: "+ this.getWidth());
        System.out.println("height: "+ this.getHeight());
        System.out.println("stageWidth: "+ stageWidth);
        System.out.println("stageHeight: "+ stageHeight);
    }
    
    /**Called auotmatically when added to JFrame/JComponent. Primary task is to spawn and start
     * the rendering thread.*/
    public void addNotify()
    {
        System.out.println("stage added to frame");
        super.addNotify();
        /*width = getWidth();
        height = getHeight();
        height =  this.getHeight();
        width =  this.getWidth();*/
        animThread = new Thread(this);
        animThread.start();
        repaint();
    }
    
    /*TODO: create a stage width and height which is independent of the jcomponent/window size*/
    /**Standard paint method, inherited and overridden from an early graphical
     * ancestor (probably {@link java.awt.Component} homo graphicapithecus LOL).
     * Renders children to the stage (at their x and y coords) in order, starting at 0; 
     *         allows for predictable z-ordering.*/
    public void paint(Graphics g)
    {
        /*System.out.println("ACTUAL stage width: "+ this.getWidth());
        System.out.println("ACTUAL stage height: "+ this.getHeight());
        System.out.println("stageWidth: "+ stageWidth);
        System.out.println("stageHeight: "+ stageHeight);*/
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D)g;
        
        //render background colour
        Color oldColour = g.getColor();
        
        g.setColor(new Color(bgColour));
        g.fillRect(0,0, getWidth(), getHeight());
        g.setColor(oldColour);
        
        /*if(drawGrid)
        {
            int graphSize = 10;
            for(int i = 1; i < stageWidth; i += graphSize)//vertical lines
            {
                g.drawLine(i, 0, i, stageHeight);
            }
            for(int j = 1; j < stageHeight; j += graphSize)//horizontal lines
            {
                g.drawLine(0, j, stageWidth, j);
            }
        }*/
        
        //only render objects on stage if there ARE any objects on stage
        for(int i = 0; i < children.size(); i++)
        {
            //System.out.println("paint: "+children.size()+" kids on stage");
            
            /*translate doesn't want absolute coords -- it wants relative movement coords!
            so translate(0,0) does nothing;
            translate(5,5) moves the cursor 5 down and 5 right from its previous position,
                NOT move the cursor to 5,5 exactly
            so now instead of translate(0,0) we use translate(-x,-y), 
            in order to reverse changes to the cursor's position on this object's render*/
            
            //move rendering 'cursor' to next sprite's coords
            g2.translate(children.get(i).getXAsDouble(), children.get(i).getYAsDouble() );
            //System.out.println("jez " + i +" : "+children.get(i).x +","+children.get(i).y);
            
            //redraw the current sprite from the list
            children.get(i).drawSprite(g2);
            //System.out.println("drawing child "+children.get(i));
            
            //move translated cursor back to starting position
            g2.translate(-children.get(i).getXAsDouble(), -children.get(i).getYAsDouble() );
        }
        
        Toolkit.getDefaultToolkit().sync();//linux-specific anti-flicker code
        g.dispose();
    }
    /**Remove all children from the stage. The renderlist is emptied, but any
     * Sprites with additional references may not be garbage-collected.*/
    public void clearStage()
    {    
        children.clear();
    }
    /**The core rendering thread code. Executes frameCode callback (if supplied),
     * then paints all children, at a period as close as possible to the framerate.
     * If a frame execution overruns its allocated time, thread sleeping is skipped.
     * Spawns as a Thread when the Stage is added to a Component. */
    public void run()
    {
        long beforeTime, timeDiff, sleepTime, frameInterval;
        //long numFrames = 0;
        beforeTime = System.currentTimeMillis();
        frameInterval = Math.round((double)1000 / (double)frameRate);//work out the nearest whole num of milliseconds between frames
        //System.out.println("run() method called in stage");
        while(true)
        {
            //System.out.println("frame "+(numFrames++));
            //run the seperate, per-frame code supplied from Runnable in constructor.
            if(frameCode != null)
                frameCode.run();
            repaint();
            
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleepTime = frameInterval - timeDiff;
            
            //System.out.println("Sleep time: "+sleepTime);    
            if (sleepTime <= 0)//thread has overrun into the next frame's time - chopchop!
            {
                beforeTime = System.currentTimeMillis();
                continue;
            }
            else
            {
                try
                {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e)
                {
                    System.out.println("interrupted");
                }
            }
            beforeTime = System.currentTimeMillis();
        }
    }
}
