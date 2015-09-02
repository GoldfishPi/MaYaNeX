package com.gfp.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.gfp.game.entities.Bullet;
import com.gfp.game.entities.Player;
import com.gfp.game.entities.PlayerMP;
import com.gfp.game.gfx.Screen;
import com.gfp.game.gfx.SpriteSheet;
import com.gfp.game.gui.Gui;
import com.gfp.game.level.Level;
import com.gfp.game.level.generateLevel;
import com.gfp.game.level.Tiles.Tile;
import com.gfp.game.net.GameClient;
import com.gfp.game.net.GameServer;
import com.gfp.game.net.packets.Packet00Login;

public class Game extends Canvas implements Runnable
{
	//

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 160;// 300
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 5;
	public static final String NAME = "MaYiNeX";
	public static final Dimension DIMENSIONS = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);

	public JFrame frame;
	private Thread thread;

	public boolean Running = false;
	public int TickCount = 0;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private int[] colours = new int[6 * 6 * 6];// or 216

	public static Game game;
	private Screen screen;
	public static InputHandler input;
	public WindowHandler windowHandler;
	public static Level level;
	public static Player player;
	public static Gui gui;

	public GameClient socketClient;
	public GameServer socketServer;

	public static boolean debug = true;

	public Game()
	{

	}

	public void init()
	{
		// red green blue.
		game = this;
		int index = 0;
		for (int r = 0; r < 6; r++)
		{
			for (int g = 0; g < 6; g++)
			{
				for (int b = 0; b < 6; b++)
				{
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					colours[index++] = rr << 16 | gg << 8 | bb;

				}
			}
		}

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/SpriteSheet.png"));
		input = new InputHandler(this);
		level = new generateLevel("/levels/GenLevel.png");
		/* level = new Level("/levels/Level.png"); */

		/*
		 * player = new PlayerMP(level, 100, 100, input,
		 * JOptionPane.showInputDialog(this, "Please enter a username"), null,
		 * -1);
		 */
		player = new Player(level, 100, 100, input, JOptionPane.showInputDialog(this, "Please enter a username"));
		gui = new Gui(level, screen, player);
		level.addEntity(player);
		level.addEntity(gui);

		/*
		 * Packet00Login loginPacket = new Packet00Login(player.getUsername(),
		 * player.x, player.y); if (socketServer != null) {
		 * socketServer.addConnection((PlayerMP) player, loginPacket); }
		 * 
		 * loginPacket.writeData(socketClient);
		 */

	}

	synchronized void start()
	{
		Running = true;
		thread = new Thread(this, NAME + "_main");
		thread.start();
		/*
		 * if (JOptionPane.showConfirmDialog(this,
		 * "Do you want to run the server?") == 0) { socketServer = new
		 * GameServer(this); socketServer.start(); } socketClient = new
		 * GameClient(this, "localhost"); socketClient.start();
		 */
	}

	synchronized void stop()
	{
		Running = false;
		try
		{
			thread.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void run()
	{

		long LastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60;

		int Ticks = 0;
		int Frames = 0;

		long LastTimer = System.currentTimeMillis();
		double Delta = 0;

		init();

		while (Running)
		{

			long Now = System.nanoTime();
			Delta += (Now - LastTime) / nsPerTick;
			LastTime = Now;
			boolean ShouldRender = true;

			while (Delta >= 1)
			{

				Ticks++;
				tick();
				Delta -= 1;
				ShouldRender = true;

			}

			try
			{
				Thread.sleep(2);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			if (ShouldRender)
			{
				Frames++;
				render();

			}

			if (System.currentTimeMillis() - LastTimer >= 1000)
			{

				LastTimer += 1000;
				frame.setTitle(NAME + " " + Ticks + " Ticks, " + Frames + " Frames");
				Frames = 0;
				Ticks = 0;

			}

		}

	}

	public void tick()
	{
		level.tick();
		TickCount++;

		for (Tile t : Tile.tiles)
		{
			if (t == null)
			{
				break;
			}
			t.tick();

		}

	}

	public void render()
	{

		// Organizes data on canvas.
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			// The higher number here the better it will be at reducing tearing
			// in the image.
			// erik
			createBufferStrategy(5);
			return;
		}

		int xOffset = player.x - (screen.width / 2);
		int yOffset = player.y - (screen.height / 2);

		level.renderTiles(screen, xOffset, yOffset);

		level.renderEntities(screen);

		for (int y = 0; y < screen.height; y++)
		{
			for (int x = 0; x < screen.width; x++)
			{
				int ColourCode = screen.pixels[x + y * screen.width];
				if (ColourCode < 255)
				{
					pixels[x + y * WIDTH] = colours[ColourCode];

				}
			}
		}

		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		// frees up any resources the graphics objects is using.
		g.dispose();
		// show conttence of buffer.
		bs.show();

	}

	public static void debug(DebugLevel level, String message)
	{
		switch (level)
		{
		default:
		case INFO:
			if (debug)
			{
				System.out.println("[" + NAME + "]" + message);
			}
			break;
		case WARNING:
			System.out.println("[" + NAME + "] [WARNING] " + message);
			break;
		case SEVERE:
			System.out.println("[" + NAME + "] [SEVERE] " + message);
			game.stop();
			break;
		}
	}

	public static enum DebugLevel
	{
		INFO, WARNING, SEVERE;
	}

	public static void changeLevel(String levelPath, int x, int y)
	{

		level = new generateLevel(levelPath);
		level.addEntity(player);
		player.changeLevel(level);
		level.addEntity(gui);
	}

	public static void summonBullet(int x, int y)
	{
		Bullet bullet = new Bullet(level, "hi", x, y, 3, input);
		level.addEntity(bullet);
	}

}
