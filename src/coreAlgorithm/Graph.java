package coreAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class Graph {

	protected final static int INF = 0x3f3f3f3f;
	
	//抽象类中的 list 设成 private 不能被子类继承，子类建新的 list
	private List<Node>list;
	
	protected List<String>nameList;
	protected int [][] dis;//distance 
	protected int [][] minPath;
	protected Map<String,Integer>getId;
	
	protected  int totalNum;
	
	public Graph() {
		totalNum = 0;
		getId = new HashMap<String,Integer>();
		
		nameList = new ArrayList<String>();
		list = new ArrayList<Node>();
		
	}
	
	protected Node getOrInitialize(String name) {
		if(getId.containsKey(name)) {
			Node target = list.get(getId.get(name));
			return target;
		}
		getId.put(name,totalNum);
		Node target = new Node(totalNum++,name);
		nameList.add(name);
		list.add(target);
		return target;
	}
	
	
	
	protected  int getTotalNumber() {
		return totalNum;
	}
	protected  boolean exist(String name) {
		return getId.containsKey(name);
	}
	
	protected String getNodeName(int id) {
		return nameList.get(id);
	}
	protected  int getNodeId(String name) {
		return getId.get(name);
	}
	protected  boolean containNode(String name) {
		return getId.containsKey(name);
	}
	protected int getMinDistance(int from,int to) {
		return dis[from][to];
	}
	protected int getMinDistance(String from,String to) {
		return dis[getId.get(from)][getId.get(to)];
	}
	
	protected abstract void getGraph();
	
	protected void floydPretreatment() {
		getGraph();//在抽象类的方法中调用抽象方法
		for(int k = 0;k < totalNum;k++) {
			for(int i = 0;i < totalNum;i++) {
				for(int j = 0;j < totalNum;j++) {
					if(dis[i][k] + dis[k][j] < dis[i][j]) {
						dis[i][j] = dis[i][k] + dis[k][j];
						minPath[i][j] = k;
					}
				}
			}
		}
	}
	
	protected  List<String> getMinPath(String start,String destination){
		List<String> ans = new ArrayList<String>();
		if(!containNode(start)) {
			try{
				throw new Exception("Graph.java Line 84:  " + start + " 不存在，请检查输入");
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ans;
		}
		if(!containNode(destination)) {
			try{
				throw new Exception("Graph.java Line 88:  " + destination + " 不存在，请检查输入");
			}catch(Exception e) {
				e.printStackTrace();
			}
			return ans;
		}
		int from = getNodeId(start), to = getNodeId(destination);
		ans.add(getNodeName(from));
		findPath(ans,from,to);
		return ans;
	}
	private  void findPath(List<String> ans,int start,int destination) {
		int transfer = minPath[start][destination];
		if(transfer == -1) {
			ans.add(getNodeName(destination));
		}
		else {
			findPath(ans,start,transfer);
			findPath(ans,transfer,destination);
		}
	}
	
}

	

