package com.wipdev.main.game.entities;

import org.json.JSONObject;

import com.wipdev.main.game.Location;
import com.wipdev.main.game.updates.CreateMoveableEntityOnMapUpdate;
import com.wipdev.main.game.updates.MoveMoveableEntityUpdate;

public class Projectile extends MoveableEntity{

	private double dx,dy;
	private double speed;
	
	private double ndx,ndy;
	
	private long timeStarted;
	private double duration;
	
	public Projectile(Location location, double width, double height,double dx,double dy,double speed,int textureId,double duration) {
		super(location, width, height,textureId);
		this.dx = dx;
		this.dy = dy;
		this.speed = speed;
		this.duration = duration;
		double length = Math.sqrt((Math.pow(dx, 2))+(Math.pow(dy, 2)));
		ndx = dx/length;
		ndy = dy/length;
		
		timeStarted = System.currentTimeMillis();
	}

	@Override
	public void tick() {
		long deltaTime = System.currentTimeMillis() - timeStarted;
		double asSeconds = deltaTime / 1000d;
		if(asSeconds > duration) {
			remove();
			return;
		}
		
		location.setX(location.getX()+(ndx*speed));
		location.setY(location.getY()+(ndy*speed));
		
		location.getMap().addUpdate(new MoveMoveableEntityUpdate<Projectile>(this));
	}
	
	

}
