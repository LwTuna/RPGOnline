package com.wipdev.main.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;

import com.wipdev.main.files.FileUtils;

public class TileLayer {

	public final int WIDTH,HEIGHT;
	
	private Tile[][] tiles;
	
	

	public TileLayer(int wIDTH, int hEIGHT) {
		WIDTH = wIDTH;
		HEIGHT = hEIGHT;
		tiles = new Tile[WIDTH][HEIGHT];
		
		loadEmptyMap();
	}
	
	private void loadEmptyMap() {
		for(int y=0;y<HEIGHT;y++) {
			for(int x=0;x<WIDTH;x++) {
				tiles[x][y] = Tile.getTiles().get((int)(Math.random()*Tile.getTiles().size()));
			}
		}
	}

	public void setTile(int x,int y,Tile tile) {
		tiles[x][y] = tile;
	}
	
	public boolean isSolid(int x,int y) {
		return tiles[x][y].isSolid();
	}
	
	public TileLayer(int wIDTH, int hEIGHT,String mapFilePath) {
		WIDTH = wIDTH;
		HEIGHT = hEIGHT;
		tiles = new Tile[WIDTH][HEIGHT];
		
		loadTileLayer(mapFilePath);
	}

	private void loadTileLayer(String mapFilePath) {
		File file = new File(mapFilePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int currentLine = 0;
			while((line = br.readLine())!=null) {
				if(!line.isBlank()) {
					String[] splited = line.split(" ");
					for(int x=0;x<splited.length;x++) {
						int id= Integer.parseInt(splited[x]);
						tiles[x][currentLine] = Tile.getTiles().get(id);
					}
				}
				currentLine ++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void safeToFile(String path) {
		File file = new File(path);
		FileUtils.createIfNotExistent(file);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int y=0;y<HEIGHT;y++) {
				String line = "";
				for(int x=0;x<WIDTH;x++) {
					line+=tiles[x][y].getId();
					if(x!=WIDTH-1) {
						line+=" ";
					}
					
				}
				bw.append(line+"\n");
			}
			bw.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JSONArray toJSONArray() {
		JSONArray columns = new JSONArray();
		for(int x=0;x<WIDTH;x++) {
			JSONArray column = new JSONArray();
			for(int y=0;y<HEIGHT;y++) {
				column.put(tiles[x][y].getId());
			}
			columns.put(column);
		}
		return columns;
	}
}
