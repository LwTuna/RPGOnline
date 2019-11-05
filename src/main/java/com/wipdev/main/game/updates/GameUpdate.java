package com.wipdev.main.game.updates;

import org.json.JSONObject;

public abstract class GameUpdate {

	private String gameUpdateKey;
	
	
	
	public GameUpdate(String gameUpdateKey) {
		this.gameUpdateKey = gameUpdateKey;
	}



	public JSONObject getAsJSON() {
		JSONObject obj = new JSONObject();
		obj.put("gameUpdateKey", gameUpdateKey);
		return obj;
	}
	
}
