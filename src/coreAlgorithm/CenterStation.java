package coreAlgorithm;

import java.util.List;

public class CenterStation extends Station{
	
	protected CenterStation(Node node,List<String>line) {
		this.id = node.getId();
		this.name = node.getName();
		this.line = line;
	}
}
