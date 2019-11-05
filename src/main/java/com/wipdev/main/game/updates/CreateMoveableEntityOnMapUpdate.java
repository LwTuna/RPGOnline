package com.wipdev.main.game.updates;

import org.json.JSONObject;

import com.wipdev.main.game.entities.MoveableEntity;

public class CreateMoveableEntityOnMapUpdate<T extends MoveableEntity> extends GameUpdate{

	private T t;
	
	public CreateMoveableEntityOnMapUpdate(T t) {
		super("newMovableEntity");
		this.t = t;
	}
	
	@Override
	public JSONObject getAsJSON() {
		JSONObject obj= super.getAsJSON();
		obj.put("entity", t.getAsJSON());
		return obj;
	}
	
	
}
