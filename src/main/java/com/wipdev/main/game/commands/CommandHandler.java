package com.wipdev.main.game.commands;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class CommandHandler {

	private static Map<String,CommandExecutor> executors = new HashMap<>();
	
	public static void handleCommand(WebsocketManager wsManager,Game game,
			WsSession session,JSONObject object,Player player) {
		
		String msg = object.getString("msg");
		if(!msg.startsWith("/")) return;
		String withoutSlash = msg.substring(1);
		String[] split = withoutSlash.split(" ");
		
		if(split.length <=0) return;
		wsManager.sendMessage(msg, session);
		try {
			executors.getOrDefault(split[0], new CommandExecutor() {
				
				@Override
				public void onCommand(Player player, String[] split, Game game, WebsocketManager wsManager, WsSession session,
						JSONObject object, String command) {
					wsManager.sendMessage("Command: /"+command+" not found!", session);
					
				}

				@Override
				public String getHelpText() {
					return null;
				}
			}).onCommand(player,split,game,wsManager,session,object,split[0]);
		}catch(Exception e) {
			wsManager.sendMessage("Error:"+e.getMessage(), session);
		}
		
		
		
	}

	public static void init() {
		executors.put("ping", new PingCommandExecutor());
		executors.put("changeMap", new ChangeMapCommand());
		executors.put("createMap", new CreateMapCommand());
		executors.put("toggleBuildingMode", new ToggleBuildingModeCommand());
		executors.put("help", new HelpCommand());
	}
	
	public static Map<String, CommandExecutor> getExecutors() {
		return executors;
	}
}
