package fr.meijin.cataclysm.composer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import fr.meijin.cataclysm.model.Game;
import fr.meijin.cataclysm.model.Player;
import fr.meijin.cataclysm.model.Tournament;
import fr.meijin.cataclysm.utils.ReferentielMission;

public class AddResultComposer  extends GenericForwardComposer<Window>{

	private static final long serialVersionUID = -706570803908008843L;
	
	private AnnotateDataBinder binder;

	private Intbox intboxP1ResultMission;
	private Intbox intboxP1ResultTournoi;
	
	private Intbox intboxP2ResultMission;
	private Intbox intboxP2ResultTournoi;
	
	private Combobox comboboxP1Mission;
	private Combobox comboboxP2Mission;

	private Combobox player1Combobox;
	private Combobox player2Combobox;
	
	private Rows player1Rows;
	private Rows player2Rows;
	
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		binder = new AnnotateDataBinder(comp);
		Game g = (Game) arg.get("game");
		
		Tournament t = (Tournament) session.getAttribute("tournament");
		for(Player p : t.players){
			player1Combobox.appendItem(p.nickname);
			player2Combobox.appendItem(p.nickname);
		}
		
		player1Combobox.setValue(g.player1.nickname);
		player2Combobox.setValue(g.player2.nickname);
		
		comboboxP1Mission.setValue(g.p1Result.mission);
		comboboxP2Mission.setValue(g.p2Result.mission);
		
		page.setAttribute("player1Missions", excludePlayedMissions(g.player1, ReferentielMission.getMissions(g.player1.faction)));
		page.setAttribute("player2Missions", excludePlayedMissions(g.player2, ReferentielMission.getMissions(g.player2.faction)));
		page.setAttribute("game", g);
		binder.loadAll();
	}
	
	private List<String> excludePlayedMissions(Player p, List<String> missions) {
		for(Game g : p.games.values()){
			if(p.equals(g.player1))
				missions.remove(g.p1Result.mission);
			else
				missions.remove(g.p2Result.mission);
		}
		return missions;
	}

	public void onClick$validateButton (Event e){
		Game g = (Game) page.getAttribute("game");
		if(StringUtils.equals(player2Combobox.getText(), player1Combobox.getText())){
			Messagebox.show("Appariement impossible, un joueur ne peut pas jouer contre lui-même !");
			return;
		}
		
		g.p1Result.resultMission = intboxP1ResultMission.getValue();
		g.p1Result.resultTournoi = intboxP1ResultTournoi.getValue();
		g.p1Result.mission = (String) (comboboxP1Mission.getSelectedItem() != null ? comboboxP1Mission.getSelectedItem().getValue() : comboboxP1Mission.getValue());
		
		g.p2Result.resultMission = intboxP2ResultMission.getValue();
		g.p2Result.resultTournoi = intboxP2ResultTournoi.getValue();
		g.p2Result.mission = (String) (comboboxP2Mission.getSelectedItem() != null ? comboboxP2Mission.getSelectedItem().getValue() : comboboxP2Mission.getValue());

		Tournament t = (Tournament) session.getAttribute("tournament");
		
		for(Player p : t.players){
			if(!p.forfeit){
				if(StringUtils.equals(p.nickname, player1Combobox.getText())){
					System.out.println("Setting player 1 : "+p.nickname);
					g.setPlayer1(p);
				}
				
				if(StringUtils.equals(p.nickname, player2Combobox.getText())){
					System.out.println("Setting player 2 : "+p.nickname);
					g.setPlayer2(p);
				}
			}
		}
		desktop.setAttribute("newGame",g);
		self.detach();
	}
	
	public void onClick$player1Combobox(Event e){
		player1Rows.setVisible(false);
		player2Rows.setVisible(false);
		binder.loadAll();
	}
	
	public void onClick$player2Combobox(Event e){
		onClick$player1Combobox(null);
	}
	
	public void onClick$cancelButton (Event e){
		self.detach();
	}
}
