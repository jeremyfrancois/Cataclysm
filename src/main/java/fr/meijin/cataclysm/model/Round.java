package fr.meijin.cataclysm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Round implements Serializable {

	private static final long serialVersionUID = 2947399885019424641L;

	public int roundNumber;
	
	public List<Game> games;
	
	public List<Game> challenges;
	
	public Round (){
		super();
		this.games = new ArrayList<Game>();
		this.challenges = new ArrayList<Game>();
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public List<Game> getGames() {
		return games;
	}

	public List<Game> getChallenges() {
		return challenges;
	}

	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public void setChallenges(List<Game> challenges) {
		this.challenges = challenges;
	}
	
}
