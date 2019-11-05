package com.wipdev.main.io.packets;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.GameMap;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class PlaceTileHandler extends PacketHandler{

	public PlaceTileHandler(WebsocketManager manager, Game game) {
		super(manager, game);
	}

	@Override
	public void handle(WsSession session, JSONObject object) {
		Player p = game.getPlayer(manager.getSessionId(session));
		GameMap map = p.getLocation().getMap();
		map.changeTile(object.getInt("layer"), (int)object.getDouble("x"),(int)object.getDouble("y"), object.getInt("id"));
		
	}

}
