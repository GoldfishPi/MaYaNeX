package com.gfp.game.entities;

import com.gfp.game.gfx.Colours;
import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Level;

public class Zombie extends Mob
{
	private int colour = Colours.get(-1, 000, 400, 121);
	public boolean isBerry = false;
	public boolean isSwimming = false;
	private boolean isLava = false;
	private int timeInLava = 0;
	private int tickCount = 0;
	public int charic = 25;
	private boolean slowness = true;

	public Zombie(Level level, String name, int x, int y)
	{
		super(level, name, x, y, 1);
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
	public boolean hasPainfull(int xa, int ya)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void tick()
	{
		int xa = 0;
		int ya = 0;
		int index = 0;
		for (Entity e : level.entities)
		{
			if (e instanceof PlayerMP)
			{
				break;
			}
			index++;
		}
		if (!slowness)
		{
			if (level.entities.get(index).x > x)
			{
				xa += 1;
			}
			if (level.entities.get(index).x < x)
			{
				xa -= 1;
			}
			if (level.entities.get(index).y < y)
			{
				ya -= 1;
			}
			if (level.entities.get(index).y > y)
			{
				ya += 1;
			}
			if (xa != 0 || ya != 0)
			{
				move(xa, ya);
			}
			slowness = true;
		}
		else if(slowness){
			slowness = false;
		}
		tickCount++;

	}

	@Override
	public void render(Screen screen)
	{
		int xTile = 0;
		int yTile = charic;

		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		int xHealthHud = 5;
		int yHealthHud = 5;
		int xHealth = xHealthHud;
		int yHealth = yHealthHud;
		final int initHealth = 3;
		int xOutline = xHealthHud;
		int yOutline = yHealthHud;

		if (movingDir == 1)
		{
			xTile += 2;
		} else if (movingDir > 1)
		{
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
			flipBottom = (movingDir - 1) % 2;
			/*
			 * if (isMoving == true) { xTile += 4 + ((numSteps >> walkingSpeed)
			 * & 1) * 2; flipTop = (movingDir - 1) % 2; flipBottom = (movingDir
			 * - 1) % 2; } else { xTile += 4; }
			 */
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

		// health
		for (int i = 0; i < health; i++)
		{
			screen.render(screen.xOffset + xHealth, screen.yOffset + yHealth, 1 + 27 * 32, Colours.get(-1, 500, 500, 000), 0x00, 1);
			screen.render(screen.xOffset + xHealth - 7, screen.yOffset + yHealth, 1 + 27 * 32, Colours.get(-1, 500, 500, 000), 0x01, 1);

			xHealth += 10;
		}

		for (int i = 0; i < initHealth; i++)
		{
			// screen.xOffset + xOutline, screen.yOffset + yOutline, 1 + 27 * 32
			// old position
			screen.render(screen.xOffset + xOutline, screen.yOffset + yOutline, 1 + 27 * 32, Colours.get(-1, -1, 500, 000), 0x00, 1);
			screen.render(screen.xOffset + xOutline - 7, screen.yOffset + yOutline, 1 + 27 * 32, Colours.get(-1, -1, 500, 000), 0x01, 1);

			xOutline += 10;
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
