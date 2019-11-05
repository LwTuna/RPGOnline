package com.wipdev.main.game.entities;

import java.awt.geom.Rectangle2D;

import org.json.JSONObject;

import com.wipdev.main.game.Location;
import com.wipdev.main.game.updates.PlayerMoveUpdate;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class Player extends MoveableEntity{

	private String displayName;
	private WsSession session;
	private WebsocketManager manager;
	private double speed = 0.2d;
	
	
	public Player(Location location,String name,WsSession session,WebsocketManager manager) {
		super(location, 1d, 1d,1);
		this.session = session;
		this.manager = manager;
		this.displayName = name;
	}

	public String getDisplayName() {
		return displayName;
	}
	public void move(double dx,double dy) {
		xMove = dx*speed;
		yMove = dy*speed;
	}
	
	@Override
	public void tick() {
		doMovement();
		location.getMap().addUpdate(new PlayerMoveUpdate(this,xMove,yMove));
		resetMovement();
	}
	@Override
	public JSONObject getAsJSON() {
		JSONObject obj = super.getAsJSON();
		obj.put("name", displayName);
		
		obj.put("pid",manager.getSessionId(session));
		return obj;
	}

	public int getPlayerId() {
		return manager.getSessionId(session);
	}
	
	public WsSession getSession() {
		return session;
	}

	public WebsocketManager getManager() {
		return manager;
	}

	public void shoot(double x, double y) {
		double dx =  x-location.getX()-width/2;
		double dy = y-location.getY()-height/2 ;
		location.getMap().getProjectileManager().add(new Projectile(new Location(location.getMap(), location.getX()+(width/4), location.getY()+(height/4)), 0.5d, 0.5d, dx, dy, 0.2d, 1,3));
	}

	
}
