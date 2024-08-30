/******************************************************************
Name: 			Rylan Davidson
Date: 			03/14/2024
Description: 	This is the fruit class, they bounce around the screen  
                and then disapear when pacman runs into them
******************************************************************/

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Fruit extends Sprite {
    private static BufferedImage[] fruitImages;

    private final int NUMBER_OF_FRUIT = 7;

    private int ySpeedDirection, xSpeedDirection, speed;
    private int preX, preY;

    private int randomlySelectedFruitType;
    private int randomlySelectedDirection;

    private int startingX, startingY;

    public Fruit(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        hasCollisionHandling = true;

        startingX = x;
        startingY = y;

        preX = x;
        preY = y;

        Random rand = new Random();
        randomlySelectedFruitType = rand.nextInt(NUMBER_OF_FRUIT);
        randomlySelectedDirection = rand.nextInt(8);

        speed = 3;
        selectDirection(randomlySelectedDirection);

        if(fruitImages == null)
        {
            fruitImages = new BufferedImage[NUMBER_OF_FRUIT];
            for(int i = 0; i < NUMBER_OF_FRUIT; i++)
            {
                fruitImages[i] = View.LOAD_IMAGE("Images\\fruit" + i + ".png");
                System.out.println("Loaded Fruit Image"+i);
            }
        }
    }

    public Fruit(int x, int y)
    {
        this(x, y, 20, 20);
    }

    public Fruit(Json ob)
    {
        this((int)ob.getLong("startingX"), (int)ob.getLong("startingY"), (int)ob.getLong("h"), (int)ob.getLong("w"));
        randomlySelectedFruitType = (int)ob.getLong("randomFruit");
    }

    private void selectDirection(int i)
    {
        switch(i)
        {
            case 0: xSpeedDirection = 0; ySpeedDirection = -1; break;
            case 1: xSpeedDirection = 1; ySpeedDirection = -1; break;
            case 2: xSpeedDirection = 1; ySpeedDirection = 0; break;
            case 3: xSpeedDirection = 1; ySpeedDirection = 1; break;
            case 4: xSpeedDirection = 0; ySpeedDirection = 1; break;
            case 5: xSpeedDirection = -1; ySpeedDirection = 1; break;
            case 6: xSpeedDirection = -1; ySpeedDirection = 0; break;
            case 7: xSpeedDirection = -1; ySpeedDirection = -1; break;
            default: xSpeedDirection = 1; ySpeedDirection = 1; break;
        }
        xSpeedDirection *= speed;
        ySpeedDirection *= speed;
    }

    @Override
    public void setY(int y)
    {
        this.y = y;
        startingY = y;
    }

    //Used to determine the direction pacman should be adjusted based on the collision
    public int getPreLeftSidePosition()
    {
        return preX;
    }
    public int getPreRightSidePosition()
    {
        return preX + w;
    }
    public int getPreTopSidePosition()
    {
        return preY;
    }
    public int getPreBottomSidePosition()
    {
        return preY + h;
    }

    @Override
    public boolean isFruit()
    {
        return true;
    }

    @Override
    public void update() 
    {
        preX = x;
        preY = y;
        x += xSpeedDirection;
        y += ySpeedDirection;
    }

    @Override
    public void draw(Graphics g, int scrollPosY) 
    {
        g.drawImage(fruitImages[randomlySelectedFruitType], x, y - scrollPosY, w, h, null);
    }

    @Override
    public Json marshal() {
        Json ob = Json.newObject();
        ob.add("startingX", startingX);
        ob.add("startingY", startingY);
     	ob.add("h", h);
        ob.add("w", w);
        ob.add("randomFruit", randomlySelectedFruitType);
        return ob;
    }

    @Override
    public void handleCollision(Sprite collidingSprite) 
    {
        if(collidingSprite.isWall())
        {
            if(getPreRightSidePosition() <= collidingSprite.getLeftSidePosition())
            {
                x = collidingSprite.getLeftSidePosition() - w - 1;
                xSpeedDirection *= -1;
            }
            if(getPreLeftSidePosition() >= collidingSprite.getRightSidePosition())
            {
                x = collidingSprite.getRightSidePosition() + 1;
                xSpeedDirection *= -1;
            }
            if(getPreBottomSidePosition() <= collidingSprite.getTopSidePosition())
            {
                y = collidingSprite.getTopSidePosition() - h - 1;
                ySpeedDirection *= -1;
            }
            if(getPreTopSidePosition() >= collidingSprite.getBottomSidePosition())
            {
                y = collidingSprite.getBottomSidePosition() + 1;
                ySpeedDirection *= -1;
            }
        }
        if(collidingSprite.isPacman())
            destroyed = true;
    }

    @Override
    public String toString()
    {
        return "Fruit (x,y) = (" + x + ", " + y + "), w = " + w + ", h = " + h;
    }
}
