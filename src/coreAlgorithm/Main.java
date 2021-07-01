package coreAlgorithm;

import java.util.Scanner;
import java.util.List;

/**
 * 
 * @author Massimo
 * 
 * 
 *	
 */
public class Main {
	
	public static void main(String[] args) {
		
	Manipulate system = new Manipulate();

	Scanner keyBoard = new Scanner(System.in); 
	String start = keyBoard.nextLine(),destination = keyBoard.nextLine();
	 	
	system.init();
	
//	List<String> minPath = system.getMinPath(start,destination);
//	for(String station:minPath) {
//		System.out.print(station + "->");
//	}
	
//	List<String> centerStationMinPath = system.getCenterStationMinPath(start, destination);
//	for(String station:centerStationMinPath) {
//		System.out.print(station + "->");
//	}
	
	List<List<String>> minTransfer = system.getMinTransfer(start,destination,3);
	for(List<String> list:minTransfer) {
		for(String station:list) {//不需要换乘则返回空List
			System.out.print(station + "->");
		}
		System.out.println();
	}
	
	
//		system.TestCenterStationMinPath(start, destination);
		
		
		/*
		 * StationGraph test = new StationGraph();
		 * 
		 * for(Station i:test.list) { if(i.isCenter()) {
		 * System.out.println(i.getName()); } }
		 */		
		/*
		 * int fit = 0,unfit = 0; for(int i=0;i<Station.totalNum;i++) { for(int
		 * j=0;j<Station.totalNum;j++) {
		 * if(Station.graph[i][j]!=Station.graph[j][i])unfit++; else fit++; } }
		 * System.out.println(Station.totalNum); System.out.println("fit: "+fit);
		 * System.out.println("unfit: "+unfit);
		 */
	
	
	keyBoard.close();
	
	
	}
	
	
	
}
