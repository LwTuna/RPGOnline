package com.wipdev.main.game.commands;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class PingCommandExecutor implements CommandExecutor {


	@Override
	public void onCommand(Player player, String[] split, Game game, WebsocketManager wsManager, WsSession session,
			JSONObject object, String command) {
		wsManager.sendMessage("Pong!", session);
		
	}

	@Override
	public String getHelpText() {
		return "Pongs you back ;)";
	}

}
