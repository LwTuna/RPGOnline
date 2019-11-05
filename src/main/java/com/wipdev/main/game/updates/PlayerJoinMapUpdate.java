package com.wipdev.main.game.updates;

import org.json.JSONObject;

import com.wipdev.main.game.entities.Player;

public class PlayerJoinMapUpdate extends GameUpdate{

	private Player p;
	
	public PlayerJoinMapUpdate(Player p) {
		super("addPlayer");
		this.p = p;
	}

	
	@Override
	public JSONObject getAsJSON() {
		JSONObject obj =  super.getAsJSON();
		obj.put("player", p.getAsJSON());
		return obj;
	}
}
