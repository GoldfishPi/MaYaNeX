package com.gfp.game.entities;

import java.util.Random;

import com.gfp.game.gfx.Colours;
import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Level;

public class Bat extends Mob
{
	private int colour = Colours.get(-1, 000, 500, 555);
	public boolean isBerry = false;
	public boolean isSwimming = false;
	private boolean isLava = false;
	private int timeInLava = 0;
	private int tickCount = 0;
	public int charic = 23;
	private boolean slowness = true;
	
	private int range = 75;
	private int health = 0;
	private int charicPose=0;
	private int[] poses={0,2,4};
	public Random rm = new Random();

	public Bat(Level level, String name, int x, int y)
	{
		super(level, name, x, y, 1, 3);
		
	}

	@Override
	public boolean hasCollided(int xa, int ya)
	{
		
		// boolean gm = false;
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;

		for (int x = xMin; x < xMax; x++)
		{
			if (isSolidTile(xa, ya, x, yMin))
			{

			}
		}
		for (int x = xMin; x < xMax; x++)
		{
			if (isSolidTile(xa, ya, x, yMax))
			{
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++)
		{
			if (isSolidTile(xa, ya, xMin, y))
			{
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++)
		{
			if (isSolidTile(xa, ya, xMax, y))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void tick()
	{
		int xa = 0;
		int ya = 0;
		xa +=1;
		
		move(xa, ya);
		tickCount ++;
	}

	@Override
	public void render(Screen screen)
	{
		
		int xTile = 0;
		int yTile = charic;
		
		

		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;
		
		if (tickCount % 60 < 15)
		{
			yOffset-=1;
			charicPose = poses[0];
			
			
		} else if (15 <= tickCount % 60 && tickCount % 60 < 30)
		{
			yOffset+=1;
			charicPose = poses[2];
			
		} else if (30 <= tickCount % 60 && tickCount % 60 < 45)
		{
			charicPose = poses[0];
			yOffset-=1;
			
			
		} else
		{
			charicPose = poses[2];
			yOffset+=1;
			
		}
		
		screen.render(xOffset, yOffset, 0+charicPose+(charic)*32, colour, 0x000, 1);
		screen.render(xOffset+ 8, yOffset, 1+charicPose+(charic)*32, colour, 0x000, 1);
		screen.render(xOffset, yOffset + 8, 0+charicPose+(charic+1)*32, colour, 0x000, 1);
		screen.render(xOffset + 8, yOffset + 8, 1+charicPose+(charic+1)*32, colour, 0x000, 1);
	}
}
