package graph;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SumoGraph implements Cloneable{

	public Graph graph;

	public Map<String, List<Edge>> routes = new HashMap<String,List<Edge>>();

	public SumoGraph() {
	}

	public SumoGraph(String nodesFile, String edgesFile) {
		List<Vertex> nodes = loadNodes(nodesFile);
		List<Edge> edges = loadEdges(nodes, edgesFile);
		graph = new Graph(nodes,edges);

		List<Edge> route1 = new ArrayList<Edge>();
		List<Edge> route2 = new ArrayList<Edge>();
		List<Edge> route3 = new ArrayList<Edge>();

		for(Edge e : edges)
		{
			if(e.getId().equals("e2") || e.getId().equals("e3"))
				route1.add(e);
			else 
				if(e.getId().equals("e4") || e.getId().equals("e5"))
					route3.add(e);
				else 
					if(e.getId().equals("e6"))
						route2.add(e);
		}

		routes.put("route0", route1);
		routes.put("route1", route2);
		routes.put("route2", route3);

		System.out.println("Graph Loaded");
	}

	private List<Edge> loadEdges(List<Vertex> nodes, String edgesFile) {

		List<Edge> edges = new ArrayList<Edge>();
		try {

			File fXmlFile = new File(edgesFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("edge");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					String nodeFrom = eElement.getAttribute("from");
					String nodeTo = eElement.getAttribute("to");

					Edge e = new Edge(eElement.getAttribute("id"),getNodeByID(nodes,nodeFrom), getNodeByID(nodes,nodeTo),0);
					//Num futuro usar o sumo para ir buscar a length da edge
					e.extension = Integer.parseInt(eElement.getAttribute("li_length"));
					e.toll = Boolean.parseBoolean(eElement.getAttribute("li_toll"));
					e.maxSpeed = (int) Double.parseDouble(eElement.getAttribute("speed"));
					edges.add(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return edges;
	}

	private Vertex getNodeByID(List<Vertex> nodes, String nodeID) {
		for(Vertex v: nodes)
			if(v.getId().equals(nodeID))
				return v;
		return null;
	}

	private List<Vertex> loadNodes(String nodesFile) {

		List<Vertex> nodes = new ArrayList<Vertex>();
		try {

			File fXmlFile = new File(nodesFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("node");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					nodes.add(new Vertex(eElement.getAttribute("id"), eElement.getAttribute("id")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodes;
	}
}