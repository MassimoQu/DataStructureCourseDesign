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
	
	//三张图
	private StationGraph stationGraph = new StationGraph();
	private CenterStationGraph centerStationGraph = new CenterStationGraph();
	private LineGraph lineGraph = new LineGraph();
	
	//读数据，建各种 Graph
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
				//文件流操作
				String[] info = edge.split("\t");//重载String 函数中的 split
				String[] info1 = info[2].split(" ");
				int distance = Integer.parseInt(info1[0]);//转数字
				String from = info[0],to = info[1],line = info1[4];
				
				//建 Station 图
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
			
//			System.out.println("共 " + a + "个中枢站");
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
		
		  //建 CenterStation 图 
		boolean[] vis = new boolean[stationGraph.getTotalNumber()];
		dfsBuildGraphs(vis,0,-1,0,-1);
		
		
		
		  //建 Line 图
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
		
		
		
		  //Floyd 预处理 
		stationGraph.floydPretreatment();
		centerStationGraph.floydPretreatment();
		lineGraph.floydPretreatment();
		
	}
	// dfs 遍历 StationGraph 建 CenterStationGraph 
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
	
	//最短路
	public List<String> getMinPath(String start,String destination) {
		return stationGraph.getMinPath(start,destination);
	}
	
	protected List<String> getCenterStationMinPath(String start,String destination) {
		return centerStationGraph.getMinPath(start,destination);
	}
	
	//最小换乘
	//不需要换乘则返回为空！
	
	//不需要分两用啦，就第2点用处
	/*
	 * route 做两用：
	 * 1. 普通点到中枢点的转换 ： 
	 * 		list 存从普通站点走到左或右最近中枢点之间的普通站点
	 * 		distance 存从普通站点走到...中枢点的距离
	 * 2. 不同地铁线之间切换的不同组合
	 * 		list 存中枢点
	 * 		distance 存这个组合的转站的距离
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
	//如果不需要换站那么返回的 List<String> 为空,输入有误返回 null
	public List<List<String>> getMinTransfer(String start,String destination,int showNum) {
		
		
		List<List<String>> ans = new ArrayList<List<String>>();  
		
		
		if(!stationGraph.containNode(start)) {
			System.out.println(start + " 不存在，请检查输入");
			return null;
		}
		if(!stationGraph.containNode(destination)) {
			System.out.println(destination + " 不存在，请检查输入");
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
					System.out.println("共 " + t.centerStationId.size() + " 个换乘");
					for(int idd:t.centerStationId) {
						System.out.println(centerStationGraph.getNodeName(idd));
					}
					
					
					last = moment;
				}
				
				//把 Connection 中的中枢点递归展开
				//添加普通点，连接始末两段
				if(!sameLine)addAllCenterStationPathGoneBy(start, destination, lineListGoneBy, new ArrayList<String>(), connectionList, -1, 0, 0, connectionList.size());
				else {
					lineListGoneBy.add(new Route(stationGraph.getMinPath(start, destination),stationGraph.getMinDistance(start, destination)));
				}
				
			}

				
				
		}
		
		
		
		
		
		//从普通点转换到中枢点，中枢点就是 LineGraph 的边的构成，以 CenterStationGraph 计算
//		Route[] from = new Route[2],to = new Route[2];
//			
//		
//		//普通点走到中枢点
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
//								System.out.println("共 " + t.centerStationId.size() + " 个换乘");
//								for(int idd:t.centerStationId) {
//									System.out.println(centerStationGraph.getNodeName(idd));
//								}
//								
//								last = moment;
//							}
//							
//							//把 Connection 中的中枢点递归展开
//							//添加普通点，连接始末两段
//							addAllCenterStationPathGoneBy(i, j, lineListGoneBy, new ArrayList<String>(),connectionList, -1, i.distance + j.distance, 0, connectionList.size());
//							
//							
//						}
//					}
//				}
//			}
//		}
//		
		
		//按 distance 排序准备输出
		lineListGoneBy.sort((Route a,Route b)->{
			return a.distance-b.distance;
		});
		
		for(int i = 0;i < showNum && i < lineListGoneBy.size();i++) {
			ans.add(lineListGoneBy.get(i).list);
		}
		
		return ans;
	}
	
	// 递归展开 Line 的边 Connection 里面的中枢点
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
	
	// 有了最小换乘的 CenterStationGraph 之后添加普通站点转化为 StationGraph
	// 这里还传进了始点和终点的 route ，避免麻烦的同时提升耦合性
	// 性能min版
	private int addEdgeBetweenCenterStation(String startStationName, String endStationName, List<String>stationList) {
		if(stationList.size()==0) {
			try {
				throw new Exception("给中枢点添加 edge 的时候发现中枢点数组为空");
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
		
		// 合并
		// 这里还没有考虑 stationList 为空的情况
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
		
		// 去重
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
 * Collections.reverse public 内部类 
 * 内部代码没有执行出来结果没有问题
 * 调试的时候走到下一行就变回 reverse 前的样子 
 * 不懂 真的搞不懂 底层原理入土为安叭 先哭为敬
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
	 * 普通点转中枢点，用 centerStationGraph 的思路做之后，这些代码都用不到了
	 * 虽然用不到，还是留着祭奠一下死去的脑细胞，你们存在过
	 * 小tips ： 
	 * 写代码前确定号思路
	 * 不要为了要用什么思路而用什么思路
	 * 什么时候代码里面绿掉的注释代码比运行的部分还多
	 * 你就知道在细节讨论清楚之前应该把电脑藏起来这个真知灼见:)
	 * 
	 */
 
//	// 普通站点转换到中枢站点
//	// 注意本身就是中枢点这种情况的处理
//	private int routeCnt = 0;
//	private void dfsFindCenterStation(Route[] route, List<String> stationList, int lastId, int momentId, int distance) {
//		
//		if(!stationGraph.getStation(momentId).isCenter()) { 
//			
//			Station station = stationGraph.getStation(momentId);
//
//			int size = station.neighbor.size();
//			
//			// 按照我们建图的方式，不会出现这种情况
//			try {//到时候可以测试一下有没有可能出现这种 bug
//				if(station.neighbor.size()>2) {
//					System.out.println(station);
//					for(NeighboringStation i:station.neighbor) {
//						System.out.println(stationGraph.getNodeName(i.id));
//					}
//					throw new Exception("station 没标记为 center 但 相邻点的数量多于2");
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
