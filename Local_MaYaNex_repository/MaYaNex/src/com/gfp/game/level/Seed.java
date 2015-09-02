package com.gfp.game.level;

import java.util.Random;

public class Seed
{

	public Random rm = new Random();

	public Random getSeed(int num)
	{
		rm = new Random();
		return rm;
	}
}
