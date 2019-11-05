package com.wipdev.main.game.entities;

import java.util.Collection;

import org.json.JSONObject;

import com.wipdev.main.game.GameMap;
import com.wipdev.main.game.Location;
import com.wipdev.main.game.updates.CreateMoveableEntityOnMapUpdate;
import com.wipdev.main.game.updates.GameUpdate;
import com.wipdev.main.game.updates.PlayerMoveUpdate;
import com.wipdev.main.game.updates.RemoveMoveableEntityUpdate;

public abstract class MoveableEntity extends Entity{

	protected double xMove = 0,yMove = 0;
	protected int textureId;
	
	private static int nextID = 1;
	
	protected int id;
	
	protected boolean toRemove = false;
	public MoveableEntity(Location location, double width, double height,int textureId) {
		super(location, width, height);
		this.id = nextID++;
		this.textureId = textureId;
		if( !(this instanceof Player)) {
			location.getMap().addUpdate(new CreateMoveableEntityOnMapUpdate<MoveableEntity>(this));
		}
		
	}
	
	
	
	/**
	 * Also have to change map in the Map itself
	 * @param newMap The new Map the Entity goes to
	 */
	public void changeMap(GameMap newMap) {
		location.setMap(newMap);
	}
	
	@Override
	public JSONObject getAsJSON() {
		JSONObject obj = super.getAsJSON();
		obj.put("xMove", xMove);
		obj.put("yMove", yMove);
		obj.put("textureId", textureId);
		obj.put("id", id);
		return obj;
		
	}

	public void remove() {
		toRemove = true;
		location.getMap().addUpdate(new RemoveMoveableEntityUpdate<MoveableEntity>(this));
	}

	public boolean isToRemove() {
		return toRemove;
	}



	public int getId() {
		return id;
	}



	protected void doMovement() {
		int ulTileX = (int) (location.getX()+xMove);
		int ulTileY = (int) (location.getY());
		
		int lrTileX = (int) (location.getX()+xMove+width);
		int lrTileY = (int) (location.getY()+height-0.01);
		
		int llTileX = (int) (location.getX()+xMove);
		int llTileY = (int) (location.getY()+height-0.01);
		
		int urTileX = (int) (location.getX()+xMove+width);
		int urTileY = (int) (location.getY());
		
		
		if(!location.getMap().isSolid(ulTileX, ulTileY) && !location.getMap().isSolid(llTileX, llTileY)&&
				!location.getMap().isSolid(lrTileX, lrTileY) && !location.getMap().isSolid(urTileX, urTileY)) {
			location.setX(location.getX() + xMove);
			if(location.getX() < 0) {
				location.setX(0);
			}else if(location.getX() + width >= location.getMap().getWidth()) {
				location.setX(location.getMap().getWidth() - width);
			}
		}
		
		
		
		ulTileX = (int) (location.getX());
		ulTileY = (int) (location.getY()+yMove);
		
		lrTileX = (int) (location.getX()+width);
		lrTileY = (int) (location.getY()+height+yMove);
		
		llTileX = (int) (location.getX());
		llTileY = (int) (location.getY()+height+yMove);
		
		urTileX = (int) (location.getX()+width);
		urTileY = (int) (location.getY()+yMove);
		
		
		if(!location.getMap().isSolid(ulTileX, ulTileY) && !location.getMap().isSolid(llTileX, llTileY)&&
				!location.getMap().isSolid(lrTileX, lrTileY) && !location.getMap().isSolid(urTileX, urTileY)) {
			location.setY(location.getY() + yMove);
			
			if(location.getY() < 0) {
				location.setY(0);
			}else if(location.getY() + height >= location.getMap().getHeight()) {
				location.setY(location.getMap().getHeight() - height);
			}
		}
		
		
		
		
		
		
		
	}
	
	protected void resetMovement() {
		if(xMove != 0 || yMove != 0) {
			yMove = 0;
			xMove = 0;
		}
	}
	
}
