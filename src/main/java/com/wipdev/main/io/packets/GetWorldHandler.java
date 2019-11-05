package com.wipdev.main.io.packets;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class GetWorldHandler extends PacketHandler{

	public GetWorldHandler(WebsocketManager manager, Game game) {
		super(manager, game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(WsSession session, JSONObject object) {
		Player p = game.getPlayer(manager.getSessionId(session));
		WebsocketManager.send(p.getLocation().getMap().getTilesAsJSON(), session);
		WebsocketManager.send(p.getLocation().getMap().getPlayersAsJSON(p.getPlayerId()), session);
		
	}

}
