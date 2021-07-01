package coreAlgorithm;

import java.util.ArrayList;
import java.util.List;


public class StationGraph extends Graph{
	
	protected final static  int MAX_STATION_NUMBER = 400;
	
	protected List<Station>list;
	protected int[][] graph;
	
	public StationGraph() {		
		list = new ArrayList<Station>();
		
		dis = new int[MAX_STATION_NUMBER][MAX_STATION_NUMBER];
		graph = new int[MAX_STATION_NUMBER][MAX_STATION_NUMBER];
		minPath = new int[MAX_STATION_NUMBER][MAX_STATION_NUMBER];
		for(int i=0;i<MAX_STATION_NUMBER;i++) {
			for(int j=0;j<MAX_STATION_NUMBER;j++) {
				dis[i][j] = graph[i][j] = INF;
				minPath[i][j] = -1;
			}
		}
	}
	
	
	
	protected  Station getOrInitialize(String name,String line) {
		if(getId.containsKey(name)) {
			Station target = list.get(getId.get(name));
			if(!target.line.contains(line))target.line.add(line);
			return target;
		}
		Station target = new Station(super.getOrInitialize(name),line);
		list.add(target);
		return target;
	}
	
	protected  void connect(Station start,Station destination,int distance) {
		if(start == null || destination == null || start.id == -1 || destination.id == -1) {
			System.out.println(Thread.getAllStackTraces() + " connect() 传进的参数有问题");
			return;
		}
		start.neighbor.add(start.new NeighboringStation(destination,distance));
//		destination.neighbor.add(destination.new NeighboringStation(start,distance));
		dis[start.id][destination.id] = graph[start.id][destination.id] = distance;
	}
	
	protected Station getStation(String name) {
		if(getId.containsKey(name)) {
			return list.get(getId.get(name));
		}
		return null;
	}

	protected  Station getStation(int id) {
		return list.get(id);
	}
	
	protected List<String> getLineNames(String name) {
		return list.get(getNodeId(name)).getLine();
	}

	@Override
	protected void getGraph() {
		for(int i = 0;i < totalNum;i++) {
			for(int j = 0;j < totalNum;j++) {
				if(i == j)dis[i][j] = 0;
				else dis[i][j] = graph[i][j];
			}
		}
	}
	
	
	
	
}
