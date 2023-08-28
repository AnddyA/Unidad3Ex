/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ObjetosDAO;
import exceptions.EmptyListException;
import exceptions.GraphSizeException;
import exceptions.IndexListException;
import exceptions.NonExistentElementException;
import graph.DirectedLabelGraph;
import graph.UndirectedLabelGraph;
import graph.Util;
import list.MyLinkedList;
import model.Objetos;

/**
 *
 * @author ubuntu
 */
public class ControladorGraafo {

	private DirectedLabelGraph<Objetos> grafosObj;
	MyLinkedList<Objetos> ob;

	public ControladorGraafo() {
		labelGraph();
	}

	public void labelGraph() {
		ob = new ObjetosDAO().getAll();

		grafosObj = new UndirectedLabelGraph<>(ob.size());

		var tmp = ob.toArray();

		for (int i = 0; i < tmp.length; i++) {
			grafosObj.labelVertex(i + 1, tmp[i]);
		}

		try {
			loadEdges();
		} catch (GraphSizeException ex) {
			System.out.println("Error: " + ex.getMessage());
		}

	}

	public void loadEdges() throws GraphSizeException {

		var adj = new ObjetosDAO().getAll().toArray();

		for (var a : adj) {

			for (var b : adj) {
				if (b.getNombre().equals(a.getNombre())) {
					continue;
				}
				var d = Util.distancia(a, b);
				grafosObj.addEdge(a.getId(), b.getId(), d);

			}

		}

		for (int i = 0; i < adj.length; i++) {
			for (int j = 0; j < adj.length; j++) {
				if (i == j) {
					continue;
				}
				var d = Util.distancia(adj[i], adj[j]);
				grafosObj.addEdge(adj[i].getId(), adj[j].getId(), d);
			}
		}

	}

	public DirectedLabelGraph<Objetos> getGrafosObj() {
		return grafosObj;
	}

	public void setGrafosObj(DirectedLabelGraph<Objetos> grafosObj) {
		this.grafosObj = grafosObj;
	}

	public MyLinkedList<Objetos> getOb() {
		return ob;
	}

	public void setOb(MyLinkedList<Objetos> ob) {
		this.ob = ob;
	}

	public DirectedLabelGraph<Objetos> path(String n1, String n2) {
		return bellman(n1, n2);
	}

	private DirectedLabelGraph<Objetos> showPath(MyLinkedList path, int dest) {

		if (path == null) {
			return null;
		}

		path.add(dest);

		var pathGraph = new DirectedLabelGraph<Objetos>(path.size());

		System.out.println(pathGraph.numVertices());

		var tmp = path.toArray();

		for (int i = 0; i < tmp.length; i++) {

			var label = grafosObj.getLabel((int) tmp[i]);

			pathGraph.labelVertex((i + 1), label);

			if ((i + 1) == tmp.length) {
				break;
			}

			try {
				pathGraph.addEdge((i + 1), (i + 2), grafosObj.edgeWeight((int) tmp[i], (int) tmp[i + 1]));
			} catch (GraphSizeException ex) {
				System.out.println("Error : " + ex.getMessage());
			}

		}

		return pathGraph;
	}

	private DirectedLabelGraph<Objetos> bellman(String n1, String n2) {

		try {

			int i = new dao.ObjetosDAO().searchName(n1).getId();

			int j = new dao.ObjetosDAO().searchName(n2).getId();

			return showPath(grafosObj.bellmanFord(i, j), j);

		} catch (IllegalAccessException | EmptyListException | NonExistentElementException | IndexListException ex) {
			System.out.println("Error: " + ex.getMessage());
		}
		return null;
	}

}
