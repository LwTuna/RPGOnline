package com.wipdev.main.io.packets;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class RequestPlayerIdHandler extends PacketHandler{

	public RequestPlayerIdHandler(WebsocketManager manager, Game game) {
		super(manager, game);
	}

	@Override
	public void handle(WsSession session, JSONObject object) {
		Player p = game.getPlayer(manager.getSessionId(session));
		if(p == null) return;
		JSONObject obj = new JSONObject();
		obj.put("id",p.getId());
		obj.put("key", "pid");
		manager.send(obj);
		
	}

}
