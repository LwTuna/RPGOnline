package com.wipdev.main.game.commands;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class ChangeMapCommand implements CommandExecutor{

	@Override
	public void onCommand(Player player, String[] split, Game game, WebsocketManager wsManager, WsSession session,
			JSONObject object, String command) {
		if(split.length < 2) {
			wsManager.sendMessage("Pls specify the Map!", session);
			return;
		}
		int dest = Integer.parseInt(split[1]);
		game.changeMap(game.getMap(dest), player, session);
		wsManager.sendMessage("Changed the Map to Map"+dest, session);
	}

	@Override
	public String getHelpText() {
		return "Teleports to Map with id<-args[0]";
	}

}
