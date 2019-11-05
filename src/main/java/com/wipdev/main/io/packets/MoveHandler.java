package com.wipdev.main.io.packets;

import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class MoveHandler extends PacketHandler{

	private final int wKC=87,dKC =68,aKC = 65,sKC =83;
	
	public MoveHandler(WebsocketManager manager, Game game) {
		super(manager, game);
	}

	@Override
	public void handle(WsSession session, JSONObject object) {
		double xMove =0;
		double yMove =0;
		int keyCode = object.getInt("code");
		if(keyCode == wKC) {
			yMove = -1;
		}else if(keyCode == sKC) {
			yMove = 1;
		}if(keyCode == aKC) {
			xMove = -1;
		}if(keyCode == dKC) {
			xMove = 1;
		}
		Player p = game.getPlayer(manager.getSessionId(session));
		p.move(xMove, yMove);
		
		
	}

	
	
}
