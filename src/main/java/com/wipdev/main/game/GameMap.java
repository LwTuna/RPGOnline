package com.wipdev.main.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wipdev.main.files.FileUtils;
import com.wipdev.main.game.entities.EntityManager;
import com.wipdev.main.game.entities.MoveableEntity;
import com.wipdev.main.game.entities.Player;
import com.wipdev.main.game.entities.Projectile;
import com.wipdev.main.game.updates.ChangeTileOnMapUpdate;
import com.wipdev.main.game.updates.GameUpdate;
import com.wipdev.main.game.updates.MoveMoveableEntityUpdate;
import com.wipdev.main.game.updates.PlayerJoinMapUpdate;
import com.wipdev.main.game.updates.PlayerLeaveMapUpdate;
import com.wipdev.main.io.WebsocketManager;

public class GameMap {

	private int width = 0,height = 0;
	private String mapName;
	
	private TileLayer ground,belowPlayer,abovePlayer;
	//Entities
	private Map<Integer,Player> players;
	private EntityManager<Projectile> projectileManager = new EntityManager<Projectile>(this);
	private List<GameUpdate> updates = new ArrayList<>();
	private Game game;
	
	public GameMap(int width, int height,String name,Game game,String folderPath) {
		this.width = width;
		this.height = height;
		this.mapName = name;
		this.game = game;
		ground = new TileLayer(width, height);
		belowPlayer = new TileLayer(width, height);
		abovePlayer = new TileLayer(width, height);
		
		players = new HashMap<Integer,Player>();
		safeMap(folderPath+"/"+mapName);
	}
	
	public boolean isSolid(int x,int y) {
		if(ground.isSolid(x, y)) {
			return true;
		}else if(belowPlayer.isSolid(x, y)) {
			return true;
		}else if(abovePlayer.isSolid(x, y)) {
			return true;
		}else {
			return false;
		}
	}
	
	
	public void tick() {
		for(Player p:players.values()) {
			p.tick();
		}
		projectileManager.tick();
		
		if(!updates.isEmpty()) {
			List<GameUpdate> toSend = List.copyOf(updates);
			JSONObject obj =updatesToJSON(toSend);
			sendToEveryone(obj);
			updates.clear();
		}
	}
	
	private JSONObject updatesToJSON(List<GameUpdate> toSend) {
		JSONObject obj = new JSONObject();
		obj.put("key", "updates");
		JSONArray arr = new JSONArray();
		for(GameUpdate update:toSend) {
			arr.put(update.getAsJSON());
		}
		obj.put("updates", arr);
		return obj;
	}

	private void sendToEveryone(JSONObject obj) {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(Player p:players.values()) {
					int id = p.getManager().getSessionId(p.getSession());
					WebsocketManager.send(obj, p.getSession());
				}
				
			}
		});
		thread.start();
	}
	

	public GameMap(String folderPath,Game game) {
		loadMapSettings(folderPath+"/settings.txt");
		this.game = game;
		ground = new TileLayer(width, height,folderPath+"/ground.txt");
		belowPlayer = new TileLayer(width, height,folderPath+"/belowPlayer.txt");
		abovePlayer = new TileLayer(width, height,folderPath+"/abovePlayer.txt");
		
		players = new HashMap<Integer,Player>();
	}
	
	private void loadMapSettings(String settingsPath) {
		File file = new File(settingsPath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			JSONObject obj = new JSONObject(br.readLine());
			br.close();
			mapName = obj.getString("name");
			width = obj.getInt("width");
			height = obj.getInt("height");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void safeMap(String folderPath) {
		safeMapSettings(folderPath+"/settings.txt");
		ground.safeToFile(folderPath+"/ground.txt");
		belowPlayer.safeToFile(folderPath+"/belowPlayer.txt");
		abovePlayer.safeToFile(folderPath+"/abovePlayer.txt");
	}
	

	
	public void changeTile(int layer,int x,int y,int tileId) {
		String folderPath = game.getMapFolderPath()+"/"+mapName;
		switch(layer) {
		case 0:
			ground.setTile(x, y, Tile.getTiles().get(tileId));
			ground.safeToFile(folderPath+"/ground.txt");
			break;
		case 1:
			belowPlayer.setTile(x, y, Tile.getTiles().get(tileId));
			belowPlayer.safeToFile(folderPath+"/belowPlayer.txt");
			break;
		case 2:
			abovePlayer.setTile(x, y, Tile.getTiles().get(tileId));
			abovePlayer.safeToFile(folderPath+"/abovePlayer.txt");
			break;
		}
		
		updates.add(new ChangeTileOnMapUpdate(x, y, tileId, layer));
	}
	
	
	private void safeMapSettings(String settingsPath) {
		JSONObject toSave = new JSONObject();
		toSave.put("height", height);
		toSave.put("width", width);
		toSave.put("name", mapName);
		
		File file = new File(settingsPath);
		FileUtils.createIfNotExistent(file);
		
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(file));
			br.write(toSave.toString());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getTilesAsJSON() {
		JSONObject packet = new JSONObject();
		packet.put("key", "tiles");
		packet.put("height", height);
		packet.put("width", width);
		packet.put("name", mapName);
		packet.put("ground", ground.toJSONArray());
		packet.put("belowPlayer", belowPlayer.toJSONArray());
		packet.put("abovePlayer", abovePlayer.toJSONArray());
		return packet;
		
	}
	
	public JSONObject getPlayersAsJSON(int playerID) {
		JSONObject packet = new JSONObject();
		packet.put("key", "players");
		JSONArray players = new JSONArray();
		for(Player p:this.players.values()) {
			players.put(p.getAsJSON());
		}
		packet.put("players", players);
		packet.put("playerId", playerID);
		return packet;
	}


	public String getMapName() {
		return mapName;
	}
	
	public Player getPlayer(int sessionId) {
		return players.get(sessionId);
	}
	
	public void addPlayer(int sessionId,Player player) {
		players.put(sessionId, player);
		updates.add(new PlayerJoinMapUpdate(player));
	}
	
	public void addUpdate(GameUpdate update) {
		updates.add(update);
	}

	public void removePlayer(int sessionId) {
		updates.add(new PlayerLeaveMapUpdate(players.get(sessionId).getPlayerId()));
		players.remove(sessionId);
	}



	public EntityManager<Projectile> getProjectileManager() {
		return projectileManager;
	}



	public int getWidth() {
		return width;
	}



	public int getHeight() {
		return height;
	}

	

	
	
	
}
