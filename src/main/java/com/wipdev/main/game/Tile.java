package com.wipdev.main.game;

import java.util.HashMap;

public class Tile {

	private int id;
	private String texturePath;
	private boolean solid;
	
	private static HashMap<Integer, Tile> tiles = new HashMap<Integer,Tile>();
	
	
	public Tile(String texturePath, boolean solid,int id) {
		this.id = id;
		this.texturePath = texturePath;
		this.solid = solid;
		tiles.put(id, this);
	}

	
	public static void init() {
		new Tile("air", false, 0);
		new Tile("grass_tile1",false,1);
		new Tile("grass_tile2",false,2);
		new Tile("grass_tile3",false,3);
		new Tile("grass_tile4",false,4);
		new Tile("grass_tile5",false,5);
		new Tile("grass_tile6",false,6);
		new Tile("grass_tile7",false,7);
		new Tile("grass_tile8",false,8);
		new Tile("grass_tile9",false,9);
		new Tile("grass_tile10",false,10);
		new Tile("wall_1",true,11);
	}

	public int getId() {
		return id;
	}


	public String getTexturePath() {
		return texturePath;
	}


	public boolean isSolid() {
		return solid;
	}


	public static HashMap<Integer, Tile> getTiles() {
		return tiles;
	}
	
	
	
}
