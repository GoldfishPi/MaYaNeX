package com.gfp.game.entities;

import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Level;

public abstract class Entity {

	public int x;
	public int y;
	public int healthMod;
	protected Level level;
	
	public Entity(Level level){
		
		init(level);
		
	}
	
	public final void init(Level level){
		this.level = level;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen);
	
}
