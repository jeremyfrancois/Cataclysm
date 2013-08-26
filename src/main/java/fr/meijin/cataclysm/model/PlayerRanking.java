package fr.meijin.cataclysm.model;

import java.io.Serializable;

public class PlayerRanking implements Serializable {

	private static final long serialVersionUID = 6728654137356869659L;

	public String nickname;
	
	public int points;
	public int goalAverage;
	
	public String getNickname() {
		return nickname;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getGoalAverage() {
		return goalAverage;
	}
	
}