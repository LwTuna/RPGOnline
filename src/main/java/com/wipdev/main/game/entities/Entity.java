package com.wipdev.main.game.entities;

import org.json.JSONObject;

import com.wipdev.main.game.Location;

public abstract class Entity {

	protected Location location;
	protected double width,height;
	
	public Entity(Location location, double width, double height) {
		this.location = location;
		this.width = width;
		this.height = height;
	}
	
	public abstract void tick();

	public JSONObject getAsJSON() {
		JSONObject obj = new JSONObject();
		obj = location.addToJSON(obj);
		obj.put("width", width);
		obj.put("height", height);
		
		return obj;
	}
	
	public Location getLocation() {
		return location;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
	
	
}
