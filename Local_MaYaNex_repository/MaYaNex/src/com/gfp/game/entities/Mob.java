package com.gfp.game.entities;

import com.gfp.game.level.Level;
import com.gfp.game.level.Tiles.Tile;

public abstract class Mob extends Entity
{

	protected String name;
	protected int speed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;
	protected int scale = 1;
	protected int health;

	public Mob( Level level, String name, int x, int y, int speed )
	{
		super( level );
		this.name = name;
		this.x = x;
		this.y = y;
		this.speed = speed;
	}

	public void move( int xa, int ya )
	{
		if( xa != 0 && ya != 0 )
		{
			move( xa, 0 );
			move( 0, ya );
			numSteps -= 1;
			return;
		}
		numSteps += 1;
		if( !hasCollided( xa, ya ) )
		{
			if( ya < 0 )
			{
				movingDir = 0;
			}
			if( ya > 0 )
			{
				movingDir = 1;
			}
			if( xa < 0 )
			{
				movingDir = 2;
			}
			if( xa > 0 )
			{
				movingDir = 3;
			}
			x += xa * speed;
			y += ya * speed;
		}

		if( hasPainfull( xa, ya ) )
		{
			health --;
		}
	}

	public abstract boolean hasCollided( int xa, int ya );

	protected boolean isSolidTile( int xa, int ya, int x, int y,
			boolean flightMode )
	{

		if( level == null )
		{
			return false;
		}
		Tile lastTile = level.getTile( (this.x + x) >> 3, (this.y + y) >> 3 );
		Tile newTile = level.getTile( (this.x + x + xa) >> 3,
				(this.y + y + ya) >> 3 );

		if( !lastTile.equals( newTile ) && newTile.isSolid() )
		{
			return true;

		}
		return false;
	}

	public abstract boolean hasPainfull( int xa, int ya );

	protected boolean isPainfullTile( int xa, int ya, int x, int y )
	{
		if( level == null )
		{
			return false;
		}

		Tile lastTile = level.getTile( (this.x + x) >> 3, (this.y + y) >> 3 );
		Tile newTile = level.getTile( (this.x + x + xa) >> 3,
				(this.y + y + ya) >> 3 );

		if( !lastTile.equals( newTile ) && newTile.isPainfull() )
		{
			return true;
		}

		return false;
	}

	/*
	 * protected boolean isActivatorTile(int xa, int ya, int x, int y ){
	 * 
	 * Tile lastTile = level.getTile((this.x + x) >> 3,(this.y+y) >> 3); Tile
	 * newTile = level.getTile((this.x + x + xa)>> 3 , (this.y + y + ya) >> 3);
	 * 
	 * if(!lastTile.equals(newTile) && newTile.isActivator()){
	 * InteractionTile.open = true; return true;
	 * 
	 * } return false; }
	 */

	public String getName()
	{
		return name;
	}
	
	protected int health(){
		return health;
	}
}