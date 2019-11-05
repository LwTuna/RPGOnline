package com.wipdev.main.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.wipdev.main.game.entities.Player;
import com.wipdev.main.io.WebsocketManager;

import io.javalin.websocket.WsSession;

public class Game implements Runnable{

	private List<GameMap> maps = new ArrayList<GameMap>();
	private final String mapFolderPath = "res/maps";
	private boolean running = false;
	
	
	
	public Game() {
		Tile.init();
		File folder = new File(mapFolderPath);
		for(int i=0;i<folder.listFiles().length;i++) {
			maps.add(new GameMap(mapFolderPath+"/map"+i,this));
		}
	}

	public int addMap(int width,int height) {
		maps.add(new GameMap(width, height, "map"+maps.size(), this,mapFolderPath));
		return maps.size()-1;
	}
	
	public GameMap getMap(int index) {
		return maps.get(index);
	}
	
	public void changeMap(GameMap dest,Player p,WsSession session) {
		GameMap loc = p.getLocation().getMap();
		loc.removePlayer(p.getPlayerId());
		WebsocketManager.send(dest.getTilesAsJSON(),session);
		WebsocketManager.send(dest.getPlayersAsJSON(p.getPlayerId()), session);
		dest.addPlayer(p.getPlayerId(), p);
		p.getLocation().setMap(dest);
		
	}
	
	public Player getPlayer(int sessionId) {
		for(GameMap map:maps) {
			Player p = map.getPlayer(sessionId);
			if(p!=null) return p;
		}
		return null;
	}
	
	public synchronized void start() {
		Thread thread = new Thread(this);
		running = true;
		thread.start();
	}
	@Override
	public void run() {
		while(running) {
			tick();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void tick() {
		for(GameMap m : maps) {
			m.tick();
		}
	}


	public String getMapFolderPath() {
		return mapFolderPath;
	}
	
	
}
