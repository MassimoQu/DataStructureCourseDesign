package coreAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class Edge {
	
	private static List<Edge>edge;
	private static int totalNum;
	static {
		totalNum = 0;
		edge = new ArrayList<Edge>();
	}
	
	private int leftCenterStation,rightCenterStation;
	private ArrayList<String> stationBetween;
	
	public Edge() {
		// TODO Auto-generated constructor stub
		this(-1,-1);
		stationBetween = new ArrayList<String>();
	}
	public Edge(int leftCenterStation,int rightCenterStation) {
		this.leftCenterStation=leftCenterStation;
		this.rightCenterStation=rightCenterStation;
		stationBetween = new ArrayList<String>();
	}
}
