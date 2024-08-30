/******************************************************************
Name: 			Rylan Davidson
Date: 			03/14/2024
Description: 	This is the ghost class, it extends sprite
                when pacman runs into the ghost they die, and they 
                weave back and forth.
******************************************************************/

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Ghost extends Sprite {
    //Used to make the ghost weave back and forth
    private int startingX;
    private int weavingDirectionSpeed;

    //This number includes the death ghost type
    private final int NUMBER_OF_GHOST_TYPES = 6;
	private final int NUMBER_OF_FRAMES_PER_GHOST = 4;

    private int randomlySelectedGhostType;
    private int currentFrame;
    
    //These determine how long the death animation plays, the deathtimeaseyes determines how long they spend as silly eyeballs
    private int frameCountdownToDeath;
    private int deathTimeAsEyes;

    private static BufferedImage[][] ghostImages;

    private boolean isDying;

    public Ghost(int x, int y, int w, int h)
    {
        super(x, y, w, h);

        startingX = x;
        weavingDirectionSpeed = 4;
        frameCountdownToDeath = 50;
        deathTimeAsEyes = frameCountdownToDeath/5;

        Random rand = new Random();
        randomlySelectedGhostType = rand.nextInt(NUMBER_OF_GHOST_TYPES - 2);

        isDying = false;

        hasCollisionHandling = true;

        if(ghostImages == null)
        {
            ghostImages = new BufferedImage[NUMBER_OF_GHOST_TYPES][NUMBER_OF_FRAMES_PER_GHOST];
            for(int i = 0; i < NUMBER_OF_GHOST_TYPES; i++)
            {
                for(int j = 0; j < NUMBER_OF_FRAMES_PER_GHOST; j++)
                {
                    ghostImages[i][j] = View.LOAD_IMAGE("Images\\ghost" + i + j + ".png");
                    System.out.println("Loaded Ghost Image"+i+j);
                }
            }
        }
    }

    public Ghost(int x, int y)
    {
        this(x, y, 30, 30);
    }

    public Ghost(Json ob)
    {
        this((int)ob.getLong("startingX"), (int)ob.getLong("y"), (int)ob.getLong("h"), (int)ob.getLong("w"));
        randomlySelectedGhostType = (int)ob.getLong("ghostType");
    }

    @Override
    public void update() 
    {
        if(!isDying)
        {
            x += weavingDirectionSpeed;
            if(x > startingX + 20)
            {
                weavingDirectionSpeed *= -1;
            }else if(x < startingX - 20)
            {
                weavingDirectionSpeed *= -1;
            }
        }else{
            frameCountdownToDeath--;
        }
        if(frameCountdownToDeath <= 0)
        {
            destroyed = true;
        }
    }

    @Override
    public void draw(Graphics g, int scrollPosY) {
        //Example of using the direction enum, when you do .ordinal it gives the position of the direction in the enum
        if(isDying && frameCountdownToDeath > deathTimeAsEyes)
        {
            g.drawImage(ghostImages[1][currentFrame/2], x, y - scrollPosY, w, h, null);
        }else if(isDying) {
            g.drawImage(ghostImages[0][currentFrame/2], x, y - scrollPosY, w, h, null);
        }else {
            g.drawImage(ghostImages[randomlySelectedGhostType + 2][currentFrame/2], x, y - scrollPosY, w, h, null);
        }
		updateFrame();
    }

    public void updateFrame()
	{
        if(isDying)
        {
            currentFrame = (currentFrame + 1) % (NUMBER_OF_FRAMES_PER_GHOST * 2);
        }else{
            currentFrame = (currentFrame + 1) % (NUMBER_OF_FRAMES_PER_GHOST);
            if(weavingDirectionSpeed > 0)
            {
                currentFrame += 4;
            }
        }
	}

    @Override
    public Json marshal() {
        Json ob = Json.newObject();
        ob.add("startingX", startingX);
        ob.add("y", y);
     	ob.add("h", h);
        ob.add("w", w); 
        ob.add("ghostType", randomlySelectedGhostType);
        return ob;
    }

    @Override
    public String toString()
    {
        return "Ghost (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }

    @Override
    public void handleCollision(Sprite collidingSprite) {
        if(collidingSprite.isPacman())
        {
            isDying = true;
        }
    }

    @Override
    public boolean isGhost()
    {
        return true;
    }
}
