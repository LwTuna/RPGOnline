package com.wipdev.main.io.packets;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class TagNotFoundHandler extends PacketHandler{

	public TagNotFoundHandler(WebsocketManager manager, Game game) {
		super(manager, game);
	}

	@Override
	public void handle(WsSession session, JSONObject object) {
		System.out.println("Tag : "+object.getString("tag")+" not found!");
		
	}

}
