package com.gfp.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.gfp.game.Game;
import com.gfp.game.entities.Entity;
import com.gfp.game.entities.PlayerMP;
import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Tiles.Tile;

public class Level
{

	public byte[] tiles;
	public int width;
	public int height;

	private String imagePath;
	private BufferedImage image;

	public List<Entity> entities = new ArrayList<Entity>();

	public Level(String imagePath)
	{

		if (imagePath != null)
		{
			this.imagePath = imagePath;
			this.loadLevelFromFile();

		} else if (imagePath == null)
		{
			tiles = new byte[width * height];
			this.width = 64;
			this.height = 64;
			this.generateLevel();
		}
	}

	private void loadLevelFromFile()
	{
		try
		{

			this.image = ImageIO.read(Level.class.getResource(this.imagePath));
			this.width = image.getWidth();
			this.height = image.getHeight();

			tiles = new byte[width * height];
			this.loadTiles();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void loadTiles()
	{
		int[] tileColour = this.image.getRGB(0, 0, width, height, null, 0, width);
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				tileCheck: for (Tile t : Tile.tiles)
				{
					if (t != null && t.getLevelColour() == tileColour[x + y * width])
					{
						this.tiles[x + y * width] = t.getid();
						break tileCheck;
					}
				}
			}
		}
	}

	public void saveLevelToFile()
	{
		try
		{

			ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveLevel(String username, Level level)
	{
		try
		{
			FileWriter fw = new FileWriter("Local_MaYaNex_repository/MaYaNex/saves/" + username + ".sav");
			PrintWriter pw = new PrintWriter(fw);
			for (int x = 0; x < level.width; x++)
			{
				for (int y = 0; y < level.height; y++)
				{
					pw.println(level.getTile(x, y).getid());
				}
			}
		} catch (IOException e)
		{
			Game.debug(Game.DebugLevel.SEVERE, "We cant find your shit Your save file is missing of corupt");
			e.printStackTrace();
		}
	}
	
	public void loadLevel(String username, Level level)
	{
		try
		{
			FileReader fr = new FileReader("Local_MaYaNex_repository/MaYaNex/saves/" + username + ".sav");
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void alterTile(int x, int y, Tile newTile)
	{
		this.tiles[x + y * width] = newTile.getid();
		image.setRGB(x, y, newTile.getLevelColour());
	}

	public void generateLevel()
	{

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (x * y % 10 < 5)
				{
					tiles[x + y * width] = Tile.GRASS.getid();
				} else
				{
					tiles[x + y * width] = Tile.STONE.getid();
				}
			}
		}

	}
	
	public synchronized List<Entity>getEntity(){
		return this.entities;
	}

	public void tick()
	{
		for (Entity e : getEntity())
		{
			e.tick();
		}
	}

	public void renderTiles(Screen screen, int xOffset, int yOffset)
	{

		if (xOffset < 0)
			xOffset = 0;
		if (xOffset > ((width << 3) - screen.width))
			xOffset = ((width << 3) - screen.width);
		if (yOffset < 0)
			yOffset = 0;
		if (yOffset > ((height << 3) - screen.height))
			yOffset = ((height << 3) - screen.height);

		screen.setOffset(xOffset, yOffset);

		for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++)
		{
			for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++)
			{
				getTile(x, y).render(screen, this, x << 3, y << 3);

			}
		}
	}

	public void renderEntities(Screen screen)
	{
		for (Entity e : getEntity())
		{
			e.render(screen);
		}
	}

	public Tile getTile(int x, int y)
	{
		if (0 > x || x >= width || 0 > y || y >= height)
			return Tile.VOID;
		return Tile.tiles[tiles[x + y * width]];
	}

	public int cordstoTile(int x, int y)
	{
		int tileCord;
		int xCord = x / 8;
		int yCord = 0;
		for (int i = 0; i < y / 8; i++)
		{
			yCord += width;
		}
		tileCord = xCord + yCord;
		return tileCord;
	}

	public boolean switchTiles(int x, int y, int xTile, int yTile, Tile tile)
	{
		if (this.cordstoTile(x, y) == this.cordstoTile(xTile, yTile))
		{

		}
		return false;
	}

	public int checkSurroundingTiles(int x, int y, int lookingTileId, int replacTileId)
	{
		if (this.getTile(x >> 3, y + 8 >> 3).getid() == lookingTileId)
		{
			return 0;
		} else if (this.getTile(x - 8 >> 3, y >> 3).getid() == lookingTileId)
		{
			return 1;
		}
		if (this.getTile(x >> 3, y - 8 >> 3).getid() == lookingTileId)
		{
			return 2;
		} else if (this.getTile(x + 8 >> 3, y >> 3).getid() == lookingTileId)
		{
			return 3;
		} else
		{
			return -1;
		}
	}

	public void addEntity(Entity entity)
	{
		this.getEntity().add(entity);

	}

	public void removePlayerMP(String username)
	{
		int index = 0;
		for (Entity e : getEntity())
		{
			if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username))
			{
				break;
			}
			index++;
		}
		this.getEntity().remove(index);

	}

	private int getPlayerMPIndex(String username)
	{
		int index = 0;
		for (Entity e : getEntity())
		{
			if (e instanceof PlayerMP && ((PlayerMP) e).getUsername().equals(username))
			{
				break;
			}
			index++;
		}
		return index;

	}

	public void movePlayer(String username, int x, int y, int numSteps, boolean isMoving, int movingDir)
	{
		int index = getPlayerMPIndex(username);
		PlayerMP player = (PlayerMP)this.getEntity().get(index);
		player.x = x;
		player.y = y;
		player.setMoving(isMoving);
		player.setNumSteps(numSteps);
		player.setMovingDir(movingDir);
		
	}

}
