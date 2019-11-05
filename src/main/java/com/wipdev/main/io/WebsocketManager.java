package com.wipdev.main.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.websocket.api.CloseException;
import org.json.JSONObject;

import com.wipdev.main.game.Game;
import com.wipdev.main.io.packets.GetWorldHandler;
import com.wipdev.main.io.packets.InteractHandler;
import com.wipdev.main.io.packets.LoginHandler;
import com.wipdev.main.io.packets.MoveHandler;
import com.wipdev.main.io.packets.MsgHandler;
import com.wipdev.main.io.packets.PacketHandler;
import com.wipdev.main.io.packets.PlaceTileHandler;
import com.wipdev.main.io.packets.RequestPlayerIdHandler;
import com.wipdev.main.io.packets.TagNotFoundHandler;

import io.javalin.Javalin;
import io.javalin.websocket.ErrorHandler;
import io.javalin.websocket.WsSession;

public class WebsocketManager {

	private Map<Integer,WsSession> loggedInSessions = new HashMap<Integer,WsSession>();
	private Map<String,PacketHandler> executors = new HashMap<String,PacketHandler>();
	
	private Game game;
	private Javalin javalin;
	
	
	public WebsocketManager(Javalin javalin,Game game) {
		this.game = game;
		this.javalin = javalin;
		initExecutors();
		
		javalin.ws("/websocket", ws->{
			ws.onConnect(session -> {
				//OnConnect
			});
			ws.onMessage((session, message) -> {
					
			       JSONObject recieved = new JSONObject(message);
			       if(recieved.getString("key") != null) {
			    	   executors.getOrDefault(recieved.getString("key"),
			    			   new TagNotFoundHandler(this,game)).handle(session, recieved);
			       }   
			});
			ws.onClose((session,statusCode, reason) ->{
				removeSession(session);
				System.out.println("User disconnected. Reason : "+reason+" Code:"+statusCode);
			});
			ws.onError((session, throwable)->{
				if(throwable instanceof CloseException) {
					System.out.println("Timeout!");
				}else {
					throwable.printStackTrace();
				}
			});
		});
	}
	
	
	private void initExecutors() {
		executors.put("login", new LoginHandler(this, game));
		executors.put("getWorld",new GetWorldHandler(this, game));
		executors.put("keyPress", new MoveHandler(this, game));
		executors.put("msg",new MsgHandler(this, game));
		executors.put("placeTile", new PlaceTileHandler(this, game));
		executors.put("interact", new InteractHandler(this, game));
		executors.put("requestPlayerId", new RequestPlayerIdHandler(this, game));
	}

	public void addLoggedInSession(int id,WsSession session) {
		loggedInSessions.put(id, session);
	}
	

	public void send(JSONObject obj) {
		for(WsSession session:loggedInSessions.values()) {
			WebsocketManager.send(obj,session);
		}
	}
	
	//as server
	public void sendMessage(String msg,WsSession session) {
		JSONObject obj = new JSONObject();
		obj.put("key", "msg");
		obj.put("msg", msg);
		send(obj, session);
	}
	
	public static boolean send(JSONObject obj,WsSession session) {
		try {
			if(!session.isOpen()) return false;
			session.getRemote().sendString(obj.toString());
			return true;
		} catch (IllegalStateException e) {
			//If Socket is Blocking due Server Restart do nothing
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void removeSession(WsSession session) {
		Iterator<Entry<Integer,WsSession>> it = loggedInSessions.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer,WsSession> entry = (Entry<Integer,WsSession>)it.next();
	        if(entry.getValue().getId().equals(session.getId())) {
				game.getPlayer(getSessionId(session)).getLocation().getMap().removePlayer(getSessionId(session));
				it.remove();
				
			}
	    }
		
	}
	
	public int getSessionId(WsSession session) {
		for(Entry<Integer,WsSession> entry:loggedInSessions.entrySet()) {
			if(entry.getValue().getId() == session.getId()) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
}
