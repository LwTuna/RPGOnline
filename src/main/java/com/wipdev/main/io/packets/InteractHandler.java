package com.wipdev.main.io.packets;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class InteractHandler extends PacketHandler{

	public InteractHandler(WebsocketManager manager, Game game) {
		super(manager, game);
	}

	@Override
	public void handle(WsSession session, JSONObject object) {
		Player p = game.getPlayer(manager.getSessionId(session));
		double x = object.getDouble("x");
		double y = object.getDouble("y");
		p.shoot(x,y);
	}

}
