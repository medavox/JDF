package com.medavox.display;

import java.awt.Component;
import java.util.Vector;

/**Contains functionality for containing "child" objects, to render as a complete object.
 * The idea is that when a display object is rendered, all its contained 'children'
 * are also rendered. See ActionScript 3.0's DisplayObjectContainer for details.
 * @author Adam Howard
 * @author Merlyn Cooper
 * */
public abstract class DisplayObjectContainer extends Component
{
    /**A Vector<Sprite> of all the children that this object contains.
     * Note that each child can also have its own children; however try to avoid
     * too many levels of containership, as this can create confusing, hard to
     * maintain and poorly performing code. */
    Vector<Sprite> children = new Vector<Sprite>();
    private DisplayObjectContainer parent;
    
    public DisplayObjectContainer(DisplayObjectContainer parent)
    {
        this.parent = parent;
    }
    
    /**Add the supplied {@link com.medavox.display.Sprite} to the end of 
     * this object's list of children.
     * @param spadd The {@link com.medavox.display.Sprite} to add*/
    public void addChild(DisplayObjectContainer spadd)
    {
        //System.out.println("adding child "+spadd);
        children.add(spadd);
        spadd.setParent(this);
    }
    /**Add the supplied {@link com.medavox.display.Sprite} at the specified position
     * in this object's render list of children .
     * @param spadd The {@link com.medavox.display.Sprite} to add
     * @param index the position to add this new child*/
    public void addChildAt(DisplayObjectContainer spadd, int index)
    {
        children.add(index, spadd);
        spadd.setParent(this);
    }
    
    /**Removes the specified child.
     * @param sprem the {@link com.medavox.display.Sprite} to remove.*/
    public void removeChild(DisplayObjectContainer sprem)
    {
        children.remove(sprem);
        spadd.setParent(null);
    }
    
    /**Removes the child at the specified position.
     * @param index the position in the render list of the child to remove*/
    public void removeChildAt(int index)
    {
        children.removeElementAt(index);
        spadd.setParent(null);
    }
    
    /**Returns the child at the specified position
     * @param the index of the child
     @return the child at the  supplied position*/
    public Sprite getChildAt(int index)
    {
        return children.get(index);
    }
    /**Returns the number of children this object has.
     * @return The number of chldren this object has.*/
    public int getNumChildren()
    {
        return children.size();
    }
    
    public void setParent(DisplayObjectContainer dad)
    {
        this.parent = dad;
    }
    
    public DisplayObjectContainer getParent()
    {
        return parent;
    }
}
