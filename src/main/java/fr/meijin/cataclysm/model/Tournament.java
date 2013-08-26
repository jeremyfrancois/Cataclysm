package fr.meijin.cataclysm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tournament implements Serializable { 

	private static final long serialVersionUID = 4795850745848913367L;

	public String name;
	
	public int playersCount;
	
	public int rounds;
	
	public List<Player> players;
	
	public List<Round> roundsList;
	
	public List<Ranking> rankings;

	public Tournament(){
		name = "Tournoi Cataclysm";
		players = new ArrayList<Player>();
		roundsList = new ArrayList<Round>();
		rankings = new ArrayList<Ranking>();
		playersCount = 1;
	}
}
