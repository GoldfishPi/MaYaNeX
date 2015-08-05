package com.gfp.game.entities;

import java.net.InetAddress;

import com.gfp.game.InputHandler;
import com.gfp.game.level.Level;

public class PlayerMP extends Player
{
	
	public InetAddress ipAddress;
	public int port;

	public PlayerMP( Level level, int x, int y, InputHandler input,
			String username, int health, InetAddress ipAddress, int port )
	{
		super( level, x, y, input, username, health );
		// TODO Auto-generated constructor stub
	}
	public PlayerMP( Level level, int x, int y,
			String username, int health, InetAddress ipAddress, int port )
	{
		super( level, x, y, null, username, health );
		// TODO Auto-generated constructor stub
	}
	
	
	public void tick(){
		super.tick();
	}

}
