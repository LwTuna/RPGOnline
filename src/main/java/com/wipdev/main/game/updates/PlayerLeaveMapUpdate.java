package com.wipdev.main.game.updates;

import org.json.JSONObject;

import com.wipdev.main.game.entities.Player;

public class PlayerLeaveMapUpdate extends GameUpdate{

	private int pId;
	
	public PlayerLeaveMapUpdate(int i) {
		super("removePlayer");
		this.pId = i;
	}

	
	@Override
	public JSONObject getAsJSON() {
		JSONObject obj =  super.getAsJSON();
		obj.put("playerId", pId);
		return obj;
	}
}
