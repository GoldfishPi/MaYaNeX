package com.gfp.game.level.Tiles;

import com.gfp.game.gfx.Screen;
import com.gfp.game.level.Level;

public class InteractionTile extends BasicSolidTile {
	private int currentAnimationIndex;
	private int[][] animationTileCords;
	private long lastIterationTime;
	private int animationSwitchDelay;
	public static boolean open = false;
			

	public InteractionTile(int id, int[][] animationCords,int tileColour, int levelColour) {
		super(id, animationCords[0][0], animationCords[0][1], tileColour,levelColour);
		this.animationTileCords = animationCords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;
		this.isActivator();

	}

	@Override
	public void tick() {
			if(open == true){
				lastIterationTime = System.currentTimeMillis();
				currentAnimationIndex = (currentAnimationIndex +1) % animationTileCords.length;
				this.tileId = (animationTileCords[currentAnimationIndex][0] + animationTileCords[currentAnimationIndex][1] * 32);
			}
	}
}
