package fr.meijin.cataclysm.model;

import java.io.Serializable;

public class Result implements Serializable {

	private static final long serialVersionUID = 1392362184518917508L;

	public Integer resultTournoi;
	
	public Integer resultMission;
	
	public String mission;
	
	public Result(){
		resultTournoi = 0;
		resultMission = 0;
	}

	public Integer getResultTournoi() {
		return resultTournoi;
	}

	public Integer getResultMission() {
		return resultMission;
	}

	public String getMission() {
		return mission;
	}

}
