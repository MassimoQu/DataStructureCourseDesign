package coreAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Station extends Node{
	
	private boolean center = false;
	protected List<String>line;
	
	protected class NeighboringStation{
		public int id,distance;
		public NeighboringStation(Station station,int distance) {
			this.id = station.id;
			this.distance = distance;
		}
	}
	
	protected List<NeighboringStation> neighbor;

	protected Station() {
		this(-1,"","");
	}

	protected Station(int id,String name,String line) {
		super(id,name);
		this.line = new ArrayList<String>();
		this.line.add(line);
		this.neighbor = new ArrayList<NeighboringStation>();
	}
	protected Station(Node node,String line) {
		this(node.id,node.name,line);
	}
	public List<String> getLine() {
		return line;
	}
	protected void setCenter() {
		this.center = true;
	}
	protected boolean isCenter() {
		return center;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		System.out.println("Line: ");
		for(String i :line) {
			System.out.print(i+" ");
		}
		System.out.println();
		return new String("Name: "+name+"\nId: "+ id+"\ncenter:"+center);
	}
}
