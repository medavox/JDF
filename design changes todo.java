MouseEvent
if(sprite.hitTest(mouseEvent.getPoint()) && sprite.isListnening())
{
    sprite.dispatch(mouseEvent);
}

//this is what I want to happen in a sprite's parent

//DOCs can have children, but not parents -- because a descendant is Stage, which cannot have parents
//Sprites will override the addMouseXListener methods, adding themselves (in the parent) to a list of children which are interested in MouseEvents.

//the paint() method can be changed to be optionally called by the run() code in Stage, allowing for event-only updates