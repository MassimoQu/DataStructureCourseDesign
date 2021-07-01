package coreAlgorithm;

public class Node {
	
	protected String name;
	protected int id;

	Node(){
		this(-1,"");
	}
	Node(int id,String name){
		this.id = id;
		this.name = name;
	}
	protected void setId(int id) {
		this.id = id;
	}
	protected int getId() {
		return id;
	}
	protected String getName() {
		return name;
	}
}
