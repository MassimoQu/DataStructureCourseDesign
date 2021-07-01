package coreAlgorithm;

import java.util.ArrayList;
import java.util.List;


public class LineGraph extends Graph{
	
	private final static int MAX_LINE_NUMBER = 30;
	
	class Connection{
		public List<Integer> centerStationId;
		public Connection() {
			centerStationId = new ArrayList<Integer>();
		}
		
	}
	protected  Connection[][] Edge_CenterStation;
	protected  List<Line> list;
	
	public LineGraph() {
		Edge_CenterStation = new Connection[MAX_LINE_NUMBER][MAX_LINE_NUMBER];
		
		list = new ArrayList<Line>();
		
		dis = new int[MAX_LINE_NUMBER][MAX_LINE_NUMBER];
		minPath = new int[MAX_LINE_NUMBER][MAX_LINE_NUMBER];
		for(int i=0;i<MAX_LINE_NUMBER;i++) {
			for(int j=0;j<MAX_LINE_NUMBER;j++) {
				dis[i][j] = INF;
				minPath[i][j] = -1;
			}
		}
		
	}
	
	protected Line getOrInitialize(String name) {
		if(getId.containsKey(name)) {
			Line target = list.get(getId.get(name));
			return target;
		}
		Line target = new Line(super.getOrInitialize(name));
		list.add(target);
		return target;
	}
	
	protected void connect(Line start,Line destination,Station station) {//此处的 station 为 CenterStation
		if(start == null || destination == null || start.id == -1 || destination.id == -1) {
			System.out.println(Thread.getAllStackTraces() + " connect() 传进的参数有问题");
			return;
		}
		if(Edge_CenterStation[start.id][destination.id] == null) {
			Edge_CenterStation[start.id][destination.id] = new Connection();
		}
		Edge_CenterStation[start.id][destination.id].centerStationId.add(station.id);
	}
	
	@Override
	protected void getGraph() {
		for(int i = 0;i < totalNum;i++) {
			for(int j = 0;j < totalNum;j++) {
				if(i == j)dis[i][j] = 0;
				else if(Edge_CenterStation[i][j] != null)dis[i][j] = 1;
			}
		}
	}
}
