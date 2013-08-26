package fr.meijin.cataclysm.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Comparable<Player>, Serializable {
	
	private static final long serialVersionUID = 2757430029692649285L;

	public int id;
	
	public String firstName;
	
	public String lastName;
	
	public String nickname;
	
	public String faction;
	
	public boolean forfeit;
	
	public Map<String, Game> games;
	
	public Player () {
		super();
		this.games = new HashMap<String,Game>();
		this.forfeit = false;
	}

	public int getPoints(){
		int ret = 0;
		
		if(this.id == 0)
			return 0;
		
		for(Game g : games.values()){
			if(g.player1.equals(this))
				ret += g.p1Result.resultTournoi;
			else
				ret += g.p2Result.resultTournoi;
		}
		return ret;
	}
	
	public int getPoints(int roundNumber){
		int ret = 0;
		for(int i=1;i <= games.values().size() && i < roundNumber; i++){
			Game g = games.get(String.valueOf(i));
			
			if(g.player1.equals(this))
				ret += g.p1Result.resultTournoi;
			else
				ret += g.p2Result.resultTournoi;
		}
		return ret;
	}
	
	public int getGoalAverage(){
		int ret = 0;
		
		if(this.id == 0)
			return 0;
		
		for(Game g : games.values()){
			if(g.player1.equals(this))
				ret += g.p1Result.resultMission - g.p2Result.resultMission;
			else
				ret += g.p2Result.resultMission - g.p1Result.resultMission;
		}
		return ret;
	}
	
	public int getGoalAverage(int roundNumber){
		int ret = 0;
		for(int i=1;i <= games.values().size() && i < roundNumber; i++){
			Game g = games.get(String.valueOf(i));
			
			if(g.player1.equals(this))
				ret += g.p1Result.resultMission - g.p2Result.resultMission;
			else
				ret += g.p2Result.resultMission - g.p1Result.resultMission;
		}
		return ret;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Player){
			Player p = (Player) obj;
			return p.id == this.id;
		}
		return false;
	}

	@Override
	public int compareTo(Player p) {
		int ret = 0;

		ret = (this.getPoints() - p.getPoints())*100000;
		if (ret == 0)
			ret = (this.getGoalAverage() - p.getGoalAverage())*10;
		
		return ret;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getNickname() {
		return nickname;
	}
	
	public String getFaction() {
		return faction;
	}

	public boolean isForfeit() {
		return forfeit;
	}

	public Map<String, Game> getGames() {
		return games;
	}

}

