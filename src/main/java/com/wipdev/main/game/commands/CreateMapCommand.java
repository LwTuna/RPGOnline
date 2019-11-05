package com.wipdev.main.game.commands;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class CreateMapCommand implements CommandExecutor{

	@Override
	public void onCommand(Player player, String[] split, Game game, WebsocketManager wsManager, WsSession session,
			JSONObject object, String command) {
		int width = Integer.parseInt(split[1]);
		int height = Integer.parseInt(split[2]);
		int id=game.addMap(width,height);
		wsManager.sendMessage("Created Map ID:"+id+" Size:"+width+"*"+height, session);
	}

	@Override
	public String getHelpText() {
		return "Creates a map with width<-args[0] and height <-args[1] as Dimensions";
	}

}
