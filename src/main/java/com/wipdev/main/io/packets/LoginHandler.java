package com.wipdev.main.io.packets;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.Location;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.game.saves.PlayerStorage;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class LoginHandler extends PacketHandler{

	private static int nextId = 0;
	
	public LoginHandler(WebsocketManager manager, Game game) {
		super(manager, game);
	}

	@Override
	public void handle(WsSession session, JSONObject object) {
		nextId++;
		manager.addLoggedInSession(nextId, session);
		game.getMap(0).addPlayer(nextId, PlayerStorage.loadPlayer(object.getString("username"), game, session, manager));
		JSONObject loginResponse = new JSONObject();
		loginResponse.put("key", "loginResponse");
		loginResponse.put("success", true);
		loginResponse.put("id", nextId);
		WebsocketManager.send(loginResponse, session);
	}

	
	
}
