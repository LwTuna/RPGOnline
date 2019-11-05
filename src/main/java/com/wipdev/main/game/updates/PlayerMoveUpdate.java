package com.wipdev.main.game.updates;

import org.json.JSONObject;

import com.wipdev.main.game.entities.Player;

public class PlayerMoveUpdate extends GameUpdate{

	private Player p;
	private double xMove,yMove;
	
	public PlayerMoveUpdate(Player p,double xMove,double yMove) {
		super("playerMove");
		this.p = p;
		this.xMove = xMove;
		this.yMove = yMove;
	}

	
	@Override
	public JSONObject getAsJSON() {
		JSONObject obj =  super.getAsJSON();
		obj.put("player", p.getAsJSON());
		return obj;
	}
}
