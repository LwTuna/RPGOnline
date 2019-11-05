package com.wipdev.main.game.updates;

import org.json.JSONObject;

import com.wipdev.main.game.entities.MoveableEntity;

public class RemoveMoveableEntityUpdate<T extends MoveableEntity> extends GameUpdate{

	private T t;
	
	public RemoveMoveableEntityUpdate(T t) {
		super("removeEntity");
		this.t = t;
	}

	@Override
	public JSONObject getAsJSON() {
		JSONObject obj =  super.getAsJSON();
		obj.put("id", t.getId());
		return obj;
	}
	
}
