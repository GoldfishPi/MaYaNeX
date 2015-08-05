package com.gfp.game.level.Tiles;

import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Level;

public class PainfullTile extends BasicTile
{

	public PainfullTile( int id, int x, int y, int tileColour, int levelColour )
	{
		super( id, x, y, tileColour, levelColour );
		this.painfull = true;
	}

	

}
