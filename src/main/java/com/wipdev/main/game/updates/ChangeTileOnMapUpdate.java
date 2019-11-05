package com.wipdev.main.game.updates;

import org.json.JSONObject;

public class ChangeTileOnMapUpdate extends GameUpdate{

	private int x,y,tileNew,layer;
	
	public ChangeTileOnMapUpdate(int x,int y,int tileNew,int layer) {
		super("changeTileOnMap");
		this.x=x;
		this.y=y;
		this.layer=layer;
		this.tileNew=tileNew;
	}
	
	@Override
	public JSONObject getAsJSON() {
		JSONObject obj =  super.getAsJSON();
		obj.put("x", x);
		obj.put("y", y);
		obj.put("tileNew", tileNew);
		obj.put("layer", layer);
		return obj;
	}

}
