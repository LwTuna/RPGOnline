package com.wipdev.main.game.commands;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;
import com.wipdev.main.io.packets.MsgHandler;

import io.javalin.websocket.WsSession;

public interface CommandExecutor {

	public String getHelpText();
	
	public void onCommand(Player player, String[] split, Game game, WebsocketManager wsManager, WsSession session, JSONObject object, String command);
	
}
