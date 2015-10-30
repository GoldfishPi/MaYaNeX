package com.gfp.game.entities;

import java.util.Random;

import com.gfp.game.gfx.Colours;
import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Level;

public class Zombie extends Mob
{
	private int colour = Colours.get(-1, 000, 125, 121);
	public boolean isBerry = false;
	public boolean isSwimming = false;
	private boolean isLava = false;
	private int timeInLava = 0;
	private int tickCount = 0;
	public int charic = 25;
	private boolean slowness = true;
	
	private int range = 75;
	private int health = 0;

	public Random rm = new Random();

	public Zombie(Level level, String name, int x, int y)
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
		int index = 0;
		
		int attackDistance = 10;
		
		@SuppressWarnings("unused")
		Player[] players;
		
		for  (Entity e : level.getEntity())
		{
			if (e instanceof Player)
			{
				break;
			}
			index++;
		}
		
		
		
		int playerX = level.entities.get(index).x;
		int playerY = level.entities.get(index).y;
		
		
		
		
		if (!slowness && playerX < x + range && playerX > x - range ||!slowness && playerY > y - range && playerY < y + range)
		{
			
			
			if (playerX > x  )
			{
				xa += 1;
			}
			if (playerX < x )
			{
				xa -= 1;
			}
			if (playerY < y )
			{
				ya -= 1;
			}
			if (playerY > y)
			{
				ya += 1;
			}

			if (xa != 0 || ya != 0)
			{
				move(xa, ya);
			}

			slowness = true;

		} 
		
		
		
		else if (slowness)
		{
			slowness = false;
		}

		if (level.getTile(this.x >> 3, this.y >> 3).getid() == 3)
		{
			isSwimming = true;
		}
		if (isSwimming && level.getTile(this.x >> 3, this.y >> 3).getid() != 3)
		{
			isSwimming = false;
		}
		
		if (level.getTile(this.x >> 3, this.y >> 3).getid() == 8)
		{
			isLava = true;
		}
		if (isLava && level.getTile(this.x >> 3, this.y >> 3).getid() != 8)
		{
			isLava = false;
		}
		tickCount++;
		 
		if(playerX <= x + attackDistance && playerX >= x - attackDistance && playerY <= y + attackDistance && playerX >= y -attackDistance){
			int subHealth = 0;
			
			if (tickCount % 60 < 15)
			{
				
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30)
			{
			
			} else if (30 <= tickCount % 60 && tickCount % 60 == 45)
			{
				subHealth --;
			} else
			{
				
				
			}
			level.entities.get(index).healthMod += subHealth;
			
			
			
		}

	}

	@Override
	public void render(Screen screen)
	{
		int xTile = 0;
		int yTile = charic;

		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		final int initHealth = 3;
		
		if (movingDir == 1)
		{
			xTile += 2;
		} else if (movingDir > 1)
		{
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
			flipBottom = (movingDir - 1) % 2;
		}

		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;
		// Swimming code
		if (isSwimming)
		{
			int waterColour = 0;
			yOffset += 6;
			if (tickCount % 60 < 15)
			{
				yOffset -= 1;
				waterColour = Colours.get(-1, -1, 225, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30)
			{
				waterColour = Colours.get(-1, 225, 115, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45)
			{
				waterColour = Colours.get(-1, 115, -1, 225);
			} else
			{
				waterColour = Colours.get(-1, 225, 115, -1);
			}
			// swiming pudle

			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColour, 0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColour, 0x01, 1);
		}

		if (isLava)
		{
			int lavaColour = 0;
			yOffset += 5;

			if (tickCount % 60 < 15)
			{
				yOffset -= 1;
				lavaColour = Colours.get(-1, -1, 500, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30)
			{
				lavaColour = Colours.get(-1, 500, 300, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45)
			{
				lavaColour = Colours.get(-1, 300, -1, 500);
				yOffset += 1;
			} else
			{
				lavaColour = Colours.get(-1, 500, 300, -1);
				timeInLava += 1;
			}

			if (timeInLava >= 50)
			{
				health--;
				timeInLava = 0;
			}

			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, lavaColour, 0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, lavaColour, 0x01, 1);
		}

		else if (!isLava)
		{
			timeInLava = 0;
		}


		// player model
		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, colour, flipTop, scale); // upper
																											// body
																											// part
																											// 1
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset, (xTile + 1) + yTile * 32, colour, flipTop, scale); // upper
																																// body
																																// part
																																// 2

		if (!isSwimming && !isLava)
		{
			screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, colour, flipBottom, scale); // lower
																																		// body
																																		// part
																																		// 1

			screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour, flipBottom, scale); // lower
																																						// body
																																						// part
																																						// 2
		}

		if (isBerry && health != initHealth && !isLava && !isSwimming)
		{
			health++;
		}
	}
}
