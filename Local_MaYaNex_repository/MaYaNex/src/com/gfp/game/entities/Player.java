package com.gfp.game.entities;

import com.gfp.game.Game;
import com.gfp.game.InputHandler;
import com.gfp.game.gfx.Colours;
import com.gfp.game.gfx.Font;
import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Level;
import com.gfp.game.level.Tiles.Tile;
import com.gfp.game.net.packets.Packet02Move;

public class Player extends Mob
{

	private InputHandler input;
	private int colour = Colours.get(-1, 000, 500, 543);
	private int scale = 1;
	public boolean gm = true;
	protected boolean isSwimming = false;
	private int tickCount;
	public int charic = 28;
	private String username;
	public int health = 3;
	protected boolean isLava = false;
	private int timeInLava = 0;
	protected boolean isBerry = false;
	protected boolean isPortal = false;
	
	public Bullet bullet;
	private boolean placeTile = false;
	

	public Player(Level level, int x, int y, InputHandler input, String username)
	{
	
		super(level, "Player", x, y, 1, 1);
		this.input = input;
		this.username = username;
		this.health = health;
	}

	public void tick()
	{

		int xa = 0;
		int ya = 0;
		if (input != null)
		{
			if (input.Up.isPressed())
			{
				ya -= 1;
			}
			if (input.Down.isPressed())
			{
				ya += 1;
			}
			if (input.Left.isPressed())
			{
				xa -= 1;
			}
			if (input.Right.isPressed())
			{
				xa += 1;

			}
			if (input.J.isPressed() /*&& isPortal*/)
			{
				Game.changeLevel("/levels/genLevel.png", 100, 100);
			}

			if (input.K.isPressed())
			{
				placeTile = true;
				level.saveLevel(username, level);;
				
			}
			if (placeTile == true && !input.K.isPressed())
			{
				placeTile = false;
			}

			if (input.One.isPressed())
			{
				if (gm == false)
				{
					gm = true;
					isSwimming = true;
				} else if (gm == true)
				{
					gm = false;
					isSwimming = false;
				}
			}
		}

		if (xa != 0 || ya != 0)
		{
			move(xa, ya);
			isMoving = true;
			/*Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, isMoving, this.movingDir);
			packet.writeData(Game.game.socketClient);*/
		} else
		{
			isMoving = false;
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
		if (level.getTile(this.x >> 3, this.y >> 3).getid() == 5)
		{
			isBerry = true;
		}

		if (isBerry && level.getTile(this.x >> 3, this.y >> 3).getid() != 5)
		{
			isBerry = false;
		}
		if (level.getTile(this.x >> 3, this.y >> 3).getid() == 11)
		{
			isPortal = true;
		}

		if (isPortal && level.getTile(this.x >> 3, this.y >> 3).getid() != 11)
		{
			isPortal = false;
		}

		if (isLava)
		{
			// health--;
		}

		tickCount++;

	}

	public void render(Screen screen)
	{
		int xTile = 0;
		int yTile = charic;

		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;


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
				Game.debug(Game.DebugLevel.INFO, Integer.toString(health));
			}

			if (timeInLava >= 25)
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

		if (username != null)
		{
			Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8), yOffset - 10, Colours.get(-1, -1, -1, 555), 1);
		}
		if (placeTile)
		{
			placeTile = false;
			int openCheck = level.checkSurroundingTiles(x, y, 10, 9);
			int closeCheck = level.checkSurroundingTiles(x, y, 9, 10);
			Game.debug(Game.DebugLevel.INFO, Integer.toString(openCheck));
			if (openCheck == 2)
			{
				level.tiles[level.cordstoTile(x, y - 8)] = Tile.OPENDOOR.getid();
			}else if (openCheck == 0)
			{
				level.tiles[level.cordstoTile(x, y + 8)] = Tile.OPENDOOR.getid();
			}
			else if (closeCheck == 2)
			{
				level.tiles[level.cordstoTile(x, y - 8)] = Tile.CLOSEDOOR.getid();
			}else if (closeCheck == 0)
			{
				level.tiles[level.cordstoTile(x, y + 8)] = Tile.CLOSEDOOR.getid();
			}
		}
	}

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

	public void toggleDoor()
	{

	}

	public String getUsername()
	{
		return this.username;
	}
	
}