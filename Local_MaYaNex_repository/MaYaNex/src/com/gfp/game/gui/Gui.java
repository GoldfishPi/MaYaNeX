package com.gfp.game.gui;

import com.gfp.game.entities.Entity;
import com.gfp.game.entities.Player;
import com.gfp.game.gfx.Colours;
import com.gfp.game.gfx.Font;
import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Level;

public class Gui extends Entity
{
	private Player player;
	private Screen screen;
	
	public Gui(Level level, Screen screen, Player player)
	{
		
		super(level);
		this.screen = screen;
		this.player = player;
	}

	@Override
	public void tick()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Screen screen)
	{
		int x = player.x;
		int y = player.y;
		int health = player.health;
		int xHealthHud = 5;
		int yHealthHud = 5;
		int xHealth = xHealthHud;
		int yHealth = yHealthHud;
		int initHealth = 3;
		int xOutline = xHealthHud;
		int yOutline = yHealthHud;
		int modifier = 8 * 1;
		int xOffset = player.x - modifier / 2;
		int yOffset = player.y - modifier / 2 - 4;
		
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

		
	}
}
