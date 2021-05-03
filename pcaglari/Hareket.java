/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pcaglari;

import java.util.LinkedList;

public class Hareket { //taşların hareketleri
	
	private LinkedList<Taslar> Rebackgammon = new LinkedList<Taslar>();

	public void push(Taslar o) {
		Rebackgammon.addFirst(o);
	}

	public Taslar pop() {
		return Rebackgammon.removeFirst();
	}

	public Taslar peek() {
		return Rebackgammon.getFirst();
	}

	public boolean empty() {
		return Rebackgammon.isEmpty();
	}

	public void clear() {
		Rebackgammon.clear();
	}

}

