package com.wipdev.main.game;

import java.util.Collection;
import java.util.Map.Entry;

import org.json.JSONObject;

public class Location {

	private GameMap map;
	private double x,y;
	
	public Location(GameMap map, double x, double y) {
		this.map = map;
		this.x = x;
		this.y = y;
	}

	public GameMap getMap() {
		return map;
	}

	public void setMap(GameMap map) {
		this.map = map;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public JSONObject getAsJSON() {
		JSONObject obj = new JSONObject();
		obj.put("map", map.getMapName());
		obj.put("x", x);
		obj.put("y", y);
		return obj;
	}
	
	public JSONObject addToJSON(JSONObject src) {
		JSONObject toAdd = this.getAsJSON();
		for(Entry<String, Object> e:toAdd.toMap().entrySet()) {
			src.put(e.getKey(), e.getValue());
		}
		return src;
	}
	
	
	
}
