package com.gfp.game.level.Tiles;

public class BasicSolidTile extends BasicTile {

	public BasicSolidTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, x, y, tileColour, levelColour);
		this.solid = true;
		this.activator = false;
	}

}
