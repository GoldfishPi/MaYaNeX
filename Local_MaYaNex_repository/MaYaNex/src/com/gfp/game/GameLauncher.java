package com.gfp.game;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameLauncher extends Applet
{
	public static final boolean DEBUG = true;
	private static Game game = new Game();

	
	//if i ever want to make this a aplet
	@SuppressWarnings("static-access")
	@Override
	public void init()
	{
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		setMaximumSize(game.DIMENSIONS);
		setMinimumSize(game.DIMENSIONS);
		setPreferredSize(game.DIMENSIONS);
		
		
	}

	@Override
	public void start()
	{
		game.start();
	}

	@Override
	public void stop()
	{
		game.stop();
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args)
	{
		game.setMinimumSize(game.DIMENSIONS);
		game.setMaximumSize(game.DIMENSIONS);
		game.setPreferredSize(game.DIMENSIONS);

		game.frame = new JFrame(game.NAME);
		game.frame.setDefaultCloseOperation(game.frame.EXIT_ON_CLOSE);
		game.frame.setLayout(new BorderLayout());

		game.frame.add(game, BorderLayout.CENTER);
		game.frame.pack();

		game.frame.setResizable(true);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		game.windowHandler = new WindowHandler(game);
		game.debug = DEBUG;

		game.start();

	}
}
