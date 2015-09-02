package com.gfp.game.entities;

import com.gfp.game.InputHandler;
import com.gfp.game.gfx.Colours;
import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Level;

public class Bullet extends Mob
{
	InputHandler input;

	public Bullet(Level level, String name, int x, int y, int speed, InputHandler input)
	{
		super(level, name, x, y, speed, -1);
		this.input = input;
	}

	@Override
	public boolean hasCollided(int xa, int ya)
	{
		boolean gm = false;
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

		if (input.K.Pressed)
		{
			xa++;
		}
		move(xa, ya);
		isMoving = true;
		/*
		 * if( xa != 0 || ya != 0 ) { move( xa, ya ); isMoving = true; } else {
		 * isMoving = false; }
		 */

	}

	@Override
	public void render(Screen screen)
	{
		screen.render(x, y, 3 + 27 * 32, Colours.get(555, 555, 555, 555), 0x00, 1);

	}

}
