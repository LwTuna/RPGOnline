package com.wipdev.main.game.updates;

import org.json.JSONObject;

import com.wipdev.main.game.entities.MoveableEntity;

public class MoveMoveableEntityUpdate<T extends MoveableEntity> extends GameUpdate {

	private T t;
	
	public MoveMoveableEntityUpdate(T t) {
		super("moveEntity");
		this.t = t;
	}

	@Override
	public JSONObject getAsJSON() {
		JSONObject obj =  super.getAsJSON();
		obj.put("entity",t.getAsJSON() );
		return obj;
	}
	
	public int getEntityId() {
		return t.getId();
	}
	
}
