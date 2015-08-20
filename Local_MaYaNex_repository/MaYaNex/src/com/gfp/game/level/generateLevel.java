package com.gfp.game.level;

import java.util.Random;

import com.gfp.game.Game;
import com.gfp.game.level.Tiles.Tile;

public class generateLevel extends Level
{

	private int waterAmount = 0;

	public generateLevel(String imagePath)
	{
		super(imagePath);
		addWall();
		//generateRivers(4, true, Tile.BUSHGRASS.getid());
		generateRivers(4, true, Tile.WATER.getid());
		generateDessert();
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
		Random rm = new Random();
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

				if (x <= -1)
				{
					wall = true;
					x++;
				}
				if (x >= width)
				{
					wall = true;
					x--;
				}
				if (y <= -1)
				{
					wall = true;
					y++;
				}
				if (y >= height)
				{
					wall = true;
					y--;
				}

				this.tiles[this.cordstoTile(x * 8, y * 8)] = tile;

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
}
