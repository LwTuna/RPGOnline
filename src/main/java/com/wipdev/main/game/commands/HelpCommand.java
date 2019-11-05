package com.wipdev.main.game.commands;

import java.util.Map.Entry;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class HelpCommand implements CommandExecutor{

	@Override
	public void onCommand(Player player, String[] split, Game game, WebsocketManager wsManager, WsSession session,
			JSONObject object, String command) {
		
		for(Entry<String,CommandExecutor> exec:CommandHandler.getExecutors().entrySet()) {
			wsManager.sendMessage(exec.getKey()+":"+exec.getValue().getHelpText()+"\n",session);
		}

		
		
	}

	@Override
	public String getHelpText() {
		return "Displays the Help Text.";
	}

}
