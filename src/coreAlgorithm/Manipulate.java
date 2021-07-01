package coreAlgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import coreAlgorithm.LineGraph.Connection;
import coreAlgorithm.Station.NeighboringStation;


public class Manipulate {
	
	private final static int INF = 0x3f3f3f3f;
	
	//����ͼ
	private StationGraph stationGraph = new StationGraph();
	private CenterStationGraph centerStationGraph = new CenterStationGraph();
	private LineGraph lineGraph = new LineGraph();
	
	//�����ݣ������� Graph
	public void init() {
		File file = new File("src\\coreAlgorithm\\subwayData.txt");
		FileReader f = null;
		BufferedReader read = null;
		try {
			f = new FileReader(file);
			read = new BufferedReader(f);
			String edge = null;
			
//			int[] cnt = new int[StationGraph.MAX_STATION_NUMBER];
			
//			int a = 0;
			
			while((edge=read.readLine())!=null) {
				//�ļ�������
				String[] info = edge.split("\t");//����String �����е� split
				String[] info1 = info[2].split(" ");
				int distance = Integer.parseInt(info1[0]);//ת����
				String from = info[0],to = info[1],line = info1[4];
				
				//�� Station ͼ
				Station start = stationGraph.getOrInitialize(from,line);
				Station destination = stationGraph.getOrInitialize(to,line);
				stationGraph.connect(start,destination,distance);
//				int startCnt = ++cnt[start.getId()],destinationCnt = ++cnt[destination.getId()];
				if(!start.isCenter() && start.getLine().size()>1) {
//						startCnt > 4) {
//						
					start.setCenter();
//					System.out.println("Set "+start.getName()+" As Center");
//					a++;
				}
				if(!destination.isCenter() && destination.getLine().size()>1) {
//						destinationCnt > 4) {
//						
					destination.setCenter();
//					System.out.println("Set "+destination.getName()+" As Center");
//					a++;
				}
			}
			
//			System.out.println("�� " + a + "������վ");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				read.close();
				f.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		  //�� CenterStation ͼ 
		boolean[] vis = new boolean[stationGraph.getTotalNumber()];
		dfsBuildGraphs(vis,0,-1,0,-1);
		
		
		
		  //�� Line ͼ
		for(Station i:centerStationGraph.list) {
			for(String from:i.line) {
				for(String to:i.line) {
					if(from != to) {
						Line start = lineGraph.getOrInitialize(from);
						Line destination = lineGraph.getOrInitialize(to);
						lineGraph.connect(start,destination,i);
					}
				}
			}
		}
		
		
//		for(Line i:lineGraph.list) {
//			System.out.println(i.id + " " + i.name);
//		}
//		System.out.println();
//		for(int i = 0;i < lineGraph.totalNum;i++) {
//			for(int j = 0;j < lineGraph.totalNum;j++) {
//				if(i==j)continue;
//				System.out.print(lineGraph.getNodeName(i) + " -> " + lineGraph.getNodeName(j) + " :  ");
//				if(lineGraph.Edge_CenterStation[i][j] == null) {
//					System.out.println();
//					continue;
//				}
//				for(int t:lineGraph.Edge_CenterStation[i][j].centerStationId) {
//					System.out.print(centerStationGraph.getNodeName(t) + "  " );
//				}
//				System.out.println();
//			}
//		}
		
		
		
		  //Floyd Ԥ���� 
		stationGraph.floydPretreatment();
		centerStationGraph.floydPretreatment();
		lineGraph.floydPretreatment();
		
	}
	// dfs ���� StationGraph �� CenterStationGraph 
	private void dfsBuildGraphs(boolean[] vis,int moment,int lastCenter,int distance,int previous) {
		Station momentStation = stationGraph.getStation(moment); 
		if(momentStation.isCenter()) {
			CenterStation mom = centerStationGraph.getOrInitialize(momentStation);
			if(lastCenter == -1) lastCenter = momentStation.getId();
			else {
				CenterStation last = null;
				last = centerStationGraph.getInStation(lastCenter);
				centerStationGraph.connect(last,mom,distance);
				lastCenter = moment;
				distance = 0;
			}
		}
		if(vis[moment])return;
		vis[moment] = true;
		for(int i = 0;i < stationGraph.getTotalNumber();i++) {
			if(i != previous && moment != i && stationGraph.graph[moment][i] != INF) {
				dfsBuildGraphs(vis,i,lastCenter,distance + stationGraph.graph[moment][i],moment);
			}
		}
	}
	
	//���·
	public List<String> getMinPath(String start,String destination) {
		return stationGraph.getMinPath(start,destination);
	}
	
	protected List<String> getCenterStationMinPath(String start,String destination) {
		return centerStationGraph.getMinPath(start,destination);
	}
	
	//��С����
	//����Ҫ�����򷵻�Ϊ�գ�
	
	//����Ҫ�����������͵�2���ô�
	/*
	 * route �����ã�
	 * 1. ��ͨ�㵽������ת�� �� 
	 * 		list �����ͨվ���ߵ��������������֮�����ͨվ��
	 * 		distance �����ͨվ���ߵ�...�����ľ���
	 * 2. ��ͬ������֮���л��Ĳ�ͬ���
	 * 		list �������
	 * 		distance �������ϵ�תվ�ľ���
	 */
	
	public class Route{
//		String destination;
		public List<String> list;
		public int distance;
		public Route(){
			this(null,0);
		}
		public Route(List<String> lineList,int distance){
			this.list = lineList;
			this.distance = distance;
		}
//		public Route(List<String> lineList,int distance,String destination){
//			this(lineList,distance);
//			this.destination = destination;
//		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			System.out.print("list: ");
			for(String i:list) {
				System.out.print(i+ " ");
			}
			
			return new String("distance: "+distance);
		}
		
	}
	//�������Ҫ��վ��ô���ص� List<String> Ϊ��,�������󷵻� null
	public List<List<String>> getMinTransfer(String start,String destination,int showNum) {
		
		
		List<List<String>> ans = new ArrayList<List<String>>();  
		
		
		if(!stationGraph.containNode(start)) {
			System.out.println(start + " �����ڣ���������");
			return null;
		}
		if(!stationGraph.containNode(destination)) {
			System.out.println(destination + " �����ڣ���������");
			return null;
		}
		
		List<Route> lineListGoneBy = new ArrayList<Route>();
		
		for(String x :stationGraph.getStation(start).getLine()) {
			for(String y:stationGraph.getStation(destination).getLine()) {
				
				List<String> lineList = lineGraph.getMinPath(x, y);
				
				List<Connection> connectionList = new ArrayList<Connection>();
				
				String last = null;
				
				boolean sameLine = false;
				
				for(String moment:lineList) {
					if(last == null) {
						last = moment;
						continue;
					}
					int line1 = lineGraph.getNodeId(last),line2 = lineGraph.getNodeId(moment);
					if(line1 == line2) {
						sameLine = true;
						break;
					}
					else{
						connectionList.add(lineGraph.Edge_CenterStation[line1][line2]);
					}
					
					System.out.println("line tranfer: " + lineGraph.getNodeName(line1) + " -> " + lineGraph.getNodeName(line2));
					Connection t = connectionList.get(connectionList.size()-1);
					System.out.println("�� " + t.centerStationId.size() + " ������");
					for(int idd:t.centerStationId) {
						System.out.println(centerStationGraph.getNodeName(idd));
					}
					
					
					last = moment;
				}
				
				//�� Connection �е������ݹ�չ��
				//�����ͨ�㣬����ʼĩ����
				if(!sameLine)addAllCenterStationPathGoneBy(start, destination, lineListGoneBy, new ArrayList<String>(), connectionList, -1, 0, 0, connectionList.size());
				else {
					lineListGoneBy.add(new Route(stationGraph.getMinPath(start, destination),stationGraph.getMinDistance(start, destination)));
				}
				
			}

				
				
		}
		
		
		
		
		
		//����ͨ��ת��������㣬�������� LineGraph �ıߵĹ��ɣ��� CenterStationGraph ����
//		Route[] from = new Route[2],to = new Route[2];
//			
//		
//		//��ͨ���ߵ������
//		toCenterStation(from,start);
//		toCenterStation(to,destination);
//		
//		System.out.println(from[1]);
//		
//		List<Route> lineListGoneBy = new ArrayList<Route>();
//		
//		for(Route i:from) {
//			for(Route j:to) {
//				if(i != null && j != null) {
//					List<String> startLine = stationGraph.getLineNames(i.destination);
//					List<String> destinationLine = stationGraph.getLineNames(j.destination);
//					
//					
//					for(String x:startLine) {
//						for(String y:destinationLine) {
//							
//							List<String> lineList = lineGraph.getMinPath(x,y);
//							List<Connection> connectionList = new ArrayList<Connection>();
//							
//							String last = null;
//							
//							for(String moment:lineList) {
//								if(last == null) {
//									last = moment;
//									continue;
//								}
//								int line1 = lineGraph.getNodeId(last),line2 = lineGraph.getNodeId(moment);
//								if(line1 == line2) {
//									connectionList.add(lineGraph.new Connection());
//								}
//								else{
//									connectionList.add(lineGraph.Edge_CenterStation[line1][line2]);
//								}
//								
//								System.out.println("line tranfer: " + lineGraph.getNodeName(line1) + " -> " + lineGraph.getNodeName(line2));
//								Connection t = connectionList.get(connectionList.size()-1);
//								System.out.println("�� " + t.centerStationId.size() + " ������");
//								for(int idd:t.centerStationId) {
//									System.out.println(centerStationGraph.getNodeName(idd));
//								}
//								
//								last = moment;
//							}
//							
//							//�� Connection �е������ݹ�չ��
//							//�����ͨ�㣬����ʼĩ����
//							addAllCenterStationPathGoneBy(i, j, lineListGoneBy, new ArrayList<String>(),connectionList, -1, i.distance + j.distance, 0, connectionList.size());
//							
//							
//						}
//					}
//				}
//			}
//		}
//		
		
		//�� distance ����׼�����
		lineListGoneBy.sort((Route a,Route b)->{
			return a.distance-b.distance;
		});
		
		for(int i = 0;i < showNum && i < lineListGoneBy.size();i++) {
			ans.add(lineListGoneBy.get(i).list);
		}
		
		return ans;
	}
	
	// �ݹ�չ�� Line �ı� Connection ����������
	private void addAllCenterStationPathGoneBy(String startStationName, String endStationName, List<Route> lineListGoneBy,List<String> stationList,
			List<Connection> connectionList, int lastCenterStationId, int distance, int moment, int total) {
		
		if(moment == total) {
		
			distance += addEdgeBetweenCenterStation(startStationName,endStationName,stationList);
			Route target = new Route(new ArrayList<String>(stationList), distance);
			lineListGoneBy.add(target);
			
			System.out.println(target);
			System.out.println();
			
			return ;
		
		}
		
		for(int i:connectionList.get(moment).centerStationId) {
			
			stationList.add(centerStationGraph.getNodeName(i));
			
			addAllCenterStationPathGoneBy(startStationName,endStationName, lineListGoneBy, stationList,connectionList, i, 
					distance + (lastCenterStationId==-1?0:centerStationGraph.getMinDistance(lastCenterStationId, i)),moment + 1,total);
			
			stationList.remove(stationList.size()-1);
		
		}
	}
	
	// ������С���˵� CenterStationGraph ֮�������ͨվ��ת��Ϊ StationGraph
	// ���ﻹ������ʼ����յ�� route �������鷳��ͬʱ���������
	// ����min��
	private int addEdgeBetweenCenterStation(String startStationName, String endStationName, List<String>stationList) {
		if(stationList.size()==0) {
			try {
				throw new Exception("���������� edge ��ʱ�������������Ϊ��");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		List<String> copyOfStationList = new ArrayList<String>(stationList);
		String last = copyOfStationList.get(0);
		int total = 0,distance = 0;
		stationList.clear();
		for(int i = 1;i < copyOfStationList.size();i++) {
			String moment = copyOfStationList.get(i);
//			List<String> temp = new ArrayList<String>(stationList);
//			stationList.clear();
//			for(int j = 0;j < total;j++) {
//				stationList.add(temp.get(j));
//			}
//			List<String> normalStationBetween = stationGraph.getMinPath(last, moment);
			distance += stationGraph.getMinDistance(last, moment);
			stationList.addAll(stationGraph.getMinPath(last, moment));
//			total += normalStationBetween.size();
//			for(String normalStation:normalStationBetween) {
//				stationList.add(normalStation);
//			}
//			for(int j = i + 1;j < copyOfStationList.size();j++) {
//				stationList.add(copyOfStationList.get(j));
//			}
			last = moment;
//			total++;
		}
		
		// �ϲ�
		// ���ﻹû�п��� stationList Ϊ�յ����
		List<String> startStationList = null, endStationList = null;
		if(stationList.size() == 0) {
			startStationList = stationGraph.getMinPath(startStationName,endStationName);
			endStationList = new ArrayList<String>();
		}
		else {
			startStationList = stationGraph.getMinPath(startStationName, stationList.get(0));
			endStationList = stationGraph.getMinPath(stationList.get(stationList.size()-1), endStationName);
		}
		List<String> temp = new ArrayList<String>(startStationList);
//		temp.addAll(stationGraph.getMinPath(startStationList.destination, stationList.get(0)));
		temp.addAll(stationList);
//		temp.addAll(stationGraph.getMinPath(stationList.get(stationList.size()-1), endStationList.destination));
		
//		List<String> temp1 = new ArrayList<String>();
//		for(int i = endStationList.size()-1;i>=0;i--) {
//			temp1.add(endStationList.get(i));
//		}
//		
//		temp.addAll(temp1);
		
		temp.addAll(endStationList);
		
		List<String> temp2 = new ArrayList<String>(temp);
		
		temp.clear();
		
		// ȥ��
		String templast = null;
		for(int i = 0;i < temp2.size();i++) {
			if(templast == null) {
				templast = temp2.get(i);
				temp.add(templast);
				continue;
			}
			if(temp2.get(i) != templast) {
				temp.add(temp2.get(i));
				templast = temp2.get(i);
			}
		}
		
/*
 * Collections.reverse public �ڲ��� 
 * �ڲ�����û��ִ�г������û������
 * ���Ե�ʱ���ߵ���һ�оͱ�� reverse ǰ������ 
 * ���� ��ĸ㲻�� �ײ�ԭ������Ϊ���� �ȿ�Ϊ��
 * 
 */
		
//		Collections.reverse(endStationList.list);
		
//		int size = endStationList.list.size();
//		for(int i=0;i<size/2;i++) {
//			String t = endStationList.list.get(i);
//			endStationList.list.set(i, endStationList.list.get(size-1-i));
//			endStationList.list.set(size-1-i, t);
//		}
//		temp.addAll(endStationList.list);

		
		
		
		stationList.clear();
		for(String str:temp) {
			stationList.add(str);
		}
		
		
		return distance;
	}
	/*
	 * ��ͨ��ת����㣬�� centerStationGraph ��˼·��֮����Щ���붼�ò�����
	 * ��Ȼ�ò������������ż���һ����ȥ����ϸ�������Ǵ��ڹ�
	 * Сtips �� 
	 * д����ǰȷ����˼·
	 * ��ҪΪ��Ҫ��ʲô˼·����ʲô˼·
	 * ʲôʱ����������̵���ע�ʹ�������еĲ��ֻ���
	 * ���֪����ϸ���������֮ǰӦ�ðѵ��Բ����������֪�Ƽ�:)
	 * 
	 */
 
//	// ��ͨվ��ת��������վ��
//	// ע�Ȿ������������������Ĵ���
//	private int routeCnt = 0;
//	private void dfsFindCenterStation(Route[] route, List<String> stationList, int lastId, int momentId, int distance) {
//		
//		if(!stationGraph.getStation(momentId).isCenter()) { 
//			
//			Station station = stationGraph.getStation(momentId);
//
//			int size = station.neighbor.size();
//			
//			// �������ǽ�ͼ�ķ�ʽ����������������
//			try {//��ʱ����Բ���һ����û�п��ܳ������� bug
//				if(station.neighbor.size()>2) {
//					System.out.println(station);
//					for(NeighboringStation i:station.neighbor) {
//						System.out.println(stationGraph.getNodeName(i.id));
//					}
//					throw new Exception("station û���Ϊ center �� ���ڵ����������2");
//				}
//			}
//			catch(Exception e) {
//				e.printStackTrace();
//				return;
//			}
//			
//			
//			
//			for(int i = 0; i < size;i++) {
//				int id = station.neighbor.get(i).id;
//				if(lastId == id)continue;
//				stationList.add(stationGraph.getNodeName(id));
//				dfsFindCenterStation(route, stationList, momentId, id, distance + stationGraph.getMinDistance(momentId, id));
//				stationList.remove(stationList.size()-1);
//			}
//		} else {
//			
//			route[routeCnt++] = new Route(new ArrayList<String>(stationList), distance,stationGraph.getNodeName(momentId));
//			
//		}
//	}
//	private void toCenterStation(Route[] route,String stationName) {
//		
//		if(stationGraph.getStation(stationName).isCenter()) {
//			route[0] = new Route(new ArrayList<String>(),0,stationName);
//			route[1] = null;
//			return;
//		}
//			
//		List<String>stationList = new ArrayList<String>();
//		stationList.add(stationName);
//		
//		routeCnt = 0;
//		dfsFindCenterStation(route, stationList, -1, stationGraph.getNodeId(stationName), 0);
//		
//	}
		

}
