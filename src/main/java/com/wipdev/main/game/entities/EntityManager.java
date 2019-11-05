package com.wipdev.main.game.entities;

import java.util.ArrayList;
import java.util.List;

import com.wipdev.main.game.GameMap;

public class EntityManager<T extends MoveableEntity> {

	private GameMap map;
	
	private List<T> entities = new ArrayList<T>();
	
	
	public EntityManager(GameMap map){
		this.map = map;
	}
	
	public void tick() {
		for(int i=0;i<entities.size();i++) {
			T t = entities.get(i);
			if(t.isToRemove()) {
				entities.remove(t);
				continue;
			}else {
				t.tick();
			}
		}
	}
	
	public void add(T t) {
		entities.add(t);
	}
	
	
	
}
