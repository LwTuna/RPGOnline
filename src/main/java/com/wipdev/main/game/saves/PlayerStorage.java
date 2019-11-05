package com.wipdev.main.game.saves;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.Location;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class PlayerStorage {

	public static Player loadPlayer(String username,Game game,WsSession session,WebsocketManager manager) {
		return new Player(new Location(	game.getMap(0), 8, 8),username,session,manager);
	}
	
}
