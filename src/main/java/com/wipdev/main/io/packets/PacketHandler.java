package com.wipdev.main.io.packets;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public abstract class PacketHandler {
	
	protected WebsocketManager manager;
	protected Game game;
	
	
	
	public PacketHandler(WebsocketManager manager, Game game) {
		this.manager = manager;
		this.game = game;
	}
	
	public abstract void handle(WsSession session,JSONObject object);
	
}
