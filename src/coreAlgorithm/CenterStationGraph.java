package coreAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class CenterStationGraph extends StationGraph{
	
	protected  List<Integer>transferFromStationId;
	
	protected CenterStationGraph(){
		transferFromStationId = new ArrayList<Integer>();
	}
	
	protected  CenterStation getOrInitialize(Station station) {
		String name = station.name;
		if(getId.containsKey(name)) {
			CenterStation target = (CenterStation) list.get(getId.get(name));
			return target;
		}
		transferFromStationId.add(station.getId());
		Station target = new CenterStation(super.getOrInitialize(name),station.line);
		list.add(target);
		return (CenterStation) target;
	}
	
	
	
	protected  CenterStation getInStation(int stationId) {
		for(int i = 0;i < totalNum;i++) {
			if(transferFromStationId.get(i) == stationId)return (CenterStation) list.get(i);
		}
		return null;
	}
}
