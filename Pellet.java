/******************************************************************
Name: 			Rylan Davidson
Date: 			03/14/2024
Description: 	This is the pellet class, they just sit pretty
                and when pacman runs into them they disapear
******************************************************************/

import java.awt.Color;
import java.awt.Graphics;

public class Pellet extends Sprite {
    public static final int PELLET_SIZE = Wall.GRID_SIZE / 2;
    
    public static int CONVERT_CORD_TO_PELLET_GRID(int t)
	{
		return t - (t % PELLET_SIZE);
	}

    public Pellet(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        hasCollisionHandling = true;
    }

    public Pellet(int x, int y)
    {
        this(x, y, PELLET_SIZE, PELLET_SIZE);
    }

    public Pellet(Json ob)
  	{
        this((int)ob.getLong("x"), (int)ob.getLong("y"), (int)ob.getLong("h"), (int)ob.getLong("w"));
    }

    public boolean isClicked(int x, int y)
    {
        x = CONVERT_CORD_TO_PELLET_GRID(x);
		y = CONVERT_CORD_TO_PELLET_GRID(y);

        if(this.x == x && this.y == y)
            return true;
        return false;
    }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics g, int scrollPosy) {
        g.setColor(new Color(255, 255, 0));
        g.fillOval(x, y - scrollPosy, w, h);
    }

    @Override
    public Json marshal() {
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
     	ob.add("h", h);
        ob.add("w", w);
        return ob;
    }

    @Override
    public void handleCollision(Sprite collidingSprite) 
    { 
        if(collidingSprite.isPacman())
        {
            destroyed = true;
        }
    }

    @Override
    public String toString()
    {
        return "Pellet (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }

    @Override
    public boolean isPellet()
    {
        return true;
    } 
}
