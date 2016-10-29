package com.gfp.game.level;

import java.util.Random;

import com.gfp.game.Game;
import com.gfp.game.entities.Bat;
import com.gfp.game.entities.Zombie;
import com.gfp.game.level.Tiles.Tile;

public class generateLevel extends Level
{
	private int waterAmount = 0;
	private Seed seed = new Seed();
	Random rm = seed.getSeed(1);
	private Zombie zombie;
	private int index;
	private Bat bat;

	public generateLevel(String imagePath)
	{
		super(imagePath);
		addWall();
		// generateRivers(4, true, Tile.BUSHGRASS.getid());
		generateRivers(4, true, Tile.WATER.getid());
		// generateRivers(20, false, Tile.LAVA.getid());
		generateDessert();
		generateBushes(20);
		generateStones(20, 20);
		addPortal();
		
		int zbieAmount = rm.nextInt(500);
		
		for (int i = 0; i < zbieAmount; i++)
		{
			int x = rm.nextInt(width*8);
			int y = rm.nextInt(height*8);
			bat = new Bat(this, null, x, y);
			
			zombie = new Zombie(this, null, x, y);
			this.addEntity(zombie);
		}
	}

	public void addWall()
	{

		for (int i = 0; i < height; i++)
		{
			this.tiles[this.cordstoTile(1, i * 8)] = Tile.STONE.getid();
			this.tiles[this.cordstoTile((height - 1) * 8, i * 8)] = Tile.STONE.getid();

		}
		for (int i = 0; i < width; i++)
		{
			this.tiles[this.cordstoTile(i * 8, 1)] = Tile.STONE.getid();
			this.tiles[this.cordstoTile(i * 8, (width - 1) * 8)] = Tile.STONE.getid();

		}

	}

	public void generateRivers(int maxWaterBodies, boolean allowLakes, byte tile)
	{

		int x;
		int y;
		int length;
		int nextTile;
		int spawnNumber = 4;

		int waterBodiesNum = rm.nextInt(maxWaterBodies);

		if (allowLakes == false)
		{
			spawnNumber = 3;
		}

		// riverNums = maxRivers;
		for (int rivers = 0; rivers < waterBodiesNum; rivers++)
		{
			boolean wall = false;
			x = rm.nextInt(width);
			y = rm.nextInt(height);
			length = rm.nextInt(100);
			this.tiles[this.cordstoTile(x * 8, y * 8)] = tile;
			// for (int i = 0; i < length; i++)
			while (wall != true)
			{
				nextTile = rm.nextInt(spawnNumber);
				if (nextTile == 0)
				{
					y--;
				} else if (nextTile == 1)
				{
					x++;
				} else if (nextTile == 2)
				{
					y++;
				} else if (nextTile == 3)
				{
					x--;
				}
				if (this.getTile(x >> 3, y >> 3).getid() == tile)
				{
					for (int checkTiles = 0; checkTiles < 4; checkTiles++)
					{
						if (nextTile == 0)
						{
							y--;
						} else if (nextTile == 1)
						{
							x++;
						} else if (nextTile == 2)
						{
							y++;
						} else if (nextTile == 3)
						{
							x--;
						}
						if (this.getTile(x >> 3, y >> 3).getid() != tile)
						{
							break;
						}
					}
				}

				if (x < width && x > 0)
				{
					if (y < height && y > 0)
					{
						this.tiles[this.cordstoTile(x * 8, y * 8)] = tile;
					}
				} else
				{
					wall = true;
				}

			}
		}
	}

	public void generateDessert()
	{
		for (int x = 0; x < this.width; x++)
		{
			for (int y = 1; y < this.height; y++)
			{
				if (this.getTile(x * 8 >> 3, y * 8 >> 3).getid() == Tile.WATER.getid())
				{
					waterAmount++;
				}
			}
		}
		Game.debug(Game.DebugLevel.INFO, Integer.toString(waterAmount));
		if (waterAmount < 100)
		{
			for (int x = 0; x < this.width; x++)
			{
				for (int y = 1; y < this.height; y++)
				{
					if (this.getTile(x * 8 >> 3, y * 8 >> 3).getid() == Tile.GRASS.getid())
					{
						this.tiles[this.cordstoTile(x * 8, y * 8)] = Tile.SAND.getid();
					}
				}
			}
		}
	}

	public void generateBushes(int maxBushes)
	{
		int amountOfBushes = rm.nextInt(maxBushes);
		Game.debug(Game.DebugLevel.INFO, Integer.toString(amountOfBushes));

		for (int i = 0; i < amountOfBushes; i++)
		{
			boolean tilePlaced = false;

			int x = rm.nextInt(width);
			int y = rm.nextInt(height);
			if (this.checkSurroundingTiles(x * 8, y * 8, Tile.GRASS.getid(), Tile.BUSHGRASS.getid()) > -1
					&& this.checkSurroundingTiles(x * 8, y * 8, Tile.STONE.getid(), Tile.BUSHGRASS.getid()) == -1
					&& this.checkSurroundingTiles(x * 8, y * 8, Tile.BUSHGRASS.getid(), Tile.BUSHGRASS.getid()) == -1)
			{
				this.tiles[this.cordstoTile(x * 8, y * 8)] = Tile.BUSHGRASS.getid();
				tilePlaced = true;
			}
			if (this.checkSurroundingTiles(x * 8, y * 8, Tile.SAND.getid(), Tile.BUSHGRASS.getid()) > -1
					&& this.checkSurroundingTiles(x * 8, y * 8, Tile.STONE.getid(), Tile.BUSHGRASS.getid()) == -1)
			{
				this.tiles[this.cordstoTile(x * 8, y * 8)] = Tile.BUSHSAND.getid();
				tilePlaced = true;
			}

		}
	}

	public void generateStones(int maxStones, int maxStonesInHedge)
	{
		int amountOfStones = rm.nextInt(maxStones);
		mainloop: for (int i = 0; i < amountOfStones; i++)
		{
			int x = rm.nextInt(width);
			int y = rm.nextInt(height);
			int tiles = rm.nextInt(maxStonesInHedge);
			headgeloop: for (int stones = 0; stones < maxStonesInHedge; stones++)
			{
				int nextStone = rm.nextInt(4);
				if (nextStone == 0)
				{
					y--;
				} else if (nextStone == 1)
				{
					x++;
				} else if (nextStone == 2)
				{
					y++;
				} else if (nextStone == 3)
				{
					x--;
				}
				if (x < width && x > 0)
				{
					if (y < height && y > 0)
					{
						this.tiles[this.cordstoTile(x * 8, y * 8)] = Tile.STONE.getid();
					}
				}
			}
		}

	}

	public void addPortal()
	{
		int x = rm.nextInt(width);
		int y = rm.nextInt(height);
		this.tiles[this.cordstoTile(x * 8, y * 8)] = Tile.GRASSPORTAL.getid();
	}
}
