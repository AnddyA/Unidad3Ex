/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import exceptions.EmptyListException;
import exceptions.IndexListException;
import exceptions.NonExistentElementException;
import java.io.IOException;
import model.Objetos;

/**
 *
 * @author ubuntu
 */
public class ObjetosDAO extends DaoAdap<Objetos> {

	private Objetos ob;

	public ObjetosDAO() {
		super(Objetos.class);
	}

	public Objetos getOb() {
		return ob;
	}

	public void setOb(Objetos ob) {
		this.ob = ob;
	}

	public void guardar(String nombre, Double X, Double Y) throws IOException {
		ob = new Objetos(X, Y, nombre);
		ob.setId(generateId());
		save(ob);
	}

	public Objetos searchName(String name) throws IllegalAccessException, EmptyListException, NonExistentElementException, IndexListException {

		var list = getAll();

		list.mergeSort("nombre", true);

		return list.binarySearch("nombre", name);
	}
}
