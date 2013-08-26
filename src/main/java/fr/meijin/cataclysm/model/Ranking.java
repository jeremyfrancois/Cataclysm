package fr.meijin.cataclysm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ranking implements Serializable{

	private static final long serialVersionUID = 7662106785797944945L;

	public int roundNumber;
	
	public List<PlayerRanking> playerRankings;

	public Ranking() {
		super();
		this.playerRankings = new ArrayList<PlayerRanking>();
	}
}
