package com.wipdev.main.io.packets;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.commands.CommandHandler;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class MsgHandler extends PacketHandler{

	public MsgHandler(WebsocketManager manager, Game game) {
		super(manager, game);
	}

	@Override
	public void handle(WsSession session, JSONObject object) {
		Player p=game.getPlayer(manager.getSessionId(session));
		if(object.getString("msg").startsWith("/")) {
			CommandHandler.handleCommand(manager, game, session, object, p);
		}else {
			object.put("username",p.getDisplayName() );
			manager.send(object);
		}
		
	}

}
