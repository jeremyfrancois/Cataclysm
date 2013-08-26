package fr.meijin.cataclysm.composer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import fr.meijin.cataclysm.model.Game;
import fr.meijin.cataclysm.model.Player;
import fr.meijin.cataclysm.model.PlayerRanking;
import fr.meijin.cataclysm.model.Ranking;
import fr.meijin.cataclysm.model.Round;
import fr.meijin.cataclysm.model.Tournament;
import fr.meijin.cataclysm.utils.DocumentUtils;
import fr.meijin.cataclysm.utils.TournamentUtils;

public class IndexComposer extends GenericForwardComposer<Div> {

	private static final long serialVersionUID = 5120754564562369429L;
	
	private AnnotateDataBinder binder;
	private Tabbox singleTabbox;
	private Tabbox resultTabbox;
	private Listbox playersList;
	private Grid playersResultsGrid;

	public void doAfterCompose(Div div) throws Exception {
		super.doAfterCompose(div);
		binder = new AnnotateDataBinder(div);
		Tournament tournament = (Tournament) session.getAttribute("tournament");
		if(tournament == null){
			tournament = new Tournament();
			session.setAttribute("tournament",tournament);
		}else {
			reloadTabs(tournament);
			reloadPlayerRankingTab(tournament);
		}

		binder.loadAll();
	}
	
	public void onClick$addRound (Event e) throws InterruptedException{
		Tournament tournament = (Tournament) session.getAttribute("tournament");
		TournamentUtils.updatePlayersRanking(tournament.roundsList, tournament.players);
		boolean addBye = false;
		
		Collections.sort(tournament.players);
		Collections.reverse(tournament.players);
		if(tournament.rounds >= 1){
			Ranking ranking = new Ranking();
			ranking.roundNumber = tournament.rounds;
			for(Player p : tournament.players){
				PlayerRanking pr = new PlayerRanking();
				pr.nickname = p.nickname;
				pr.goalAverage = p.getGoalAverage();
				pr.points = p.getPoints();
				ranking.playerRankings.add(pr);
			}
			
			addResultTab(ranking);
			
			tournament.rankings.add(ranking);
		}
		
		Collections.sort(tournament.players);
		
		List<Player> toMatch = new ArrayList<Player>(tournament.players);
		
		List<Player> forfeitPlayers = new ArrayList<Player>();
		for(Player p : toMatch){
			System.out.println("Player to match "+p.nickname);
			if (p.forfeit && p.id !=0)
				forfeitPlayers.add(p);
		}
		
		toMatch.removeAll(forfeitPlayers);

		if(toMatch.size()%2 == 1){
			int rep = Messagebox.show("Le nombre de joueurs est impair, un joueur BYE va être créé, êtes-vous sur de vouloir continuer ?", "Attention", Messagebox.YES | Messagebox.NO, Messagebox.EXCLAMATION);
			if(rep == Messagebox.NO){
				return;
			} else {
				addBye = true;
			}
		} else if (tournament.players.isEmpty()){
			Messagebox.show("Impossible de créer une ronde sans joueurs.", "Erreur", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		
		if(addBye){
			Player p = new Player();
			p.id=0;
			p.nickname = "BYE";
			p.forfeit = true;
			p.faction = "";
			toMatch.add(p);
		}
		
		tournament.rounds++;
		
		
		Collections.sort(toMatch);
		Collections.reverse(toMatch);
			
		if(tournament.rounds == 1)
			Collections.shuffle(toMatch);
		
		Round r = TournamentUtils.doMatch(tournament.rounds,tournament.roundsList, toMatch, new ArrayList<Game>());
		tournament.roundsList.add(r);
		
		Tab tab = addRoundTab(r);
		
		session.setAttribute("tournament", tournament);
		tab.setSelected(true);
		binder.loadAll();
	}

	

	public void onClick$resultTab (Event e){
		
		Tournament tournament = (Tournament) session.getAttribute("tournament");
		TournamentUtils.updatePlayersRanking(tournament.roundsList, tournament.players);
		Collections.sort(tournament.players);
		Collections.reverse(tournament.players);
		session.setAttribute("tournament",tournament);
		playersResultsGrid.invalidate();
		playersResultsGrid.renderAll();
		binder.loadComponent(playersResultsGrid);
	}
	
	public void onClick$exportData (Event e) throws Exception{
		Tournament tournament = (Tournament) session.getAttribute("tournament");
		File exportFile = DocumentUtils.exportTournamentData(tournament);
		Filedownload.save(exportFile, "text/plain");
	}

	public void onClick$addPlayerButton(Event e) throws Exception {
		Window window = (Window) Executions.createComponents("add_player.zul", null, null);
		window.doModal();
		onPlayerChange();
	}
	

	public void onClick$resetTournament (Event e){
		int ret = Messagebox.show("Etes-vous sûr(e) de vouloir supprimer toutes les données du tournoi ?", "Attention", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
		
		if(ret == Messagebox.YES){
			Tournament t = new Tournament();
			session.setAttribute("tournament", t);
			deleteTabs();
		}
		((Tab) page.getFellowIfAny("playerTab")).setSelected(true);
		
		binder.loadAll();
	}
	
	public void onClick$saveTournament (Event e) throws IOException {
		Tournament tournament = (Tournament) session.getAttribute("tournament");
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String date = format.format(new Date());
		
		String fileTitle = "Run4Win_"+tournament.name+"_"+date+".r4w";
		
		StringBuilder finalName = new StringBuilder();
		for (char c : fileTitle.toCharArray()) {
			if (c=='.' || Character.isJavaIdentifierPart(c)) {
				finalName.append(c);
			}
		}
		File file = new File(finalName.toString());
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(tournament);
		oos.flush();
		oos.close();
		fos.flush();
		fos.close();
		Filedownload.save(file, "text/plain");
		binder.loadAll();
	}
	
	public void onUpload$loadTournament (UploadEvent e) throws IOException, ClassNotFoundException {
		Media m = e.getMedia();
		if(m != null){
			ObjectInputStream ois = new ObjectInputStream(m.getStreamData());
			Tournament tournament = (Tournament) ois.readObject();
			deleteTabs();
			tournament = reloadTabs(tournament);
			tournament = reloadPlayerRankingTab(tournament);
			session.setAttribute("tournament",tournament);
			ois.close();
		}
		binder.loadAll();
	}

	public void onEditPlayer(Event e){
		Player oldPlayer = (Player) e.getData();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("oldPlayer", oldPlayer);
		Window window = (Window) Executions.createComponents("add_player.zul", null, map);
		window.doModal();
		onPlayerChange();
		page.removeAttribute("oldPlayer");
	}
	
	private void onPlayerChange() {
		Player newPlayer = (Player) page.getAttribute("newPlayer");
		
		if(newPlayer != null){
			Tournament tournament = (Tournament) session.getAttribute("tournament");
			if(tournament.players.isEmpty()){
				tournament.players.add(newPlayer);
			} else {
				
				int indexToremove = -1;
				for(Player p : tournament.players){
					if(p.id == newPlayer.id){
						indexToremove = tournament.players.indexOf(p);
					}
				}
				
				if(indexToremove >= 0){
					tournament.players.remove(indexToremove);
					tournament.players.add(indexToremove, newPlayer);
				} else {
					tournament.players.add(newPlayer);
				}
				
				page.removeAttribute("newPlayer");
				session.setAttribute("tournament", tournament);
				playersList.renderAll();
			}
		}
		binder.loadAll();
	}
	
	public void onDeletePlayer(Event e){
		Player p = (Player) e.getData();
		Tournament tournament = (Tournament) session.getAttribute("tournament");
		tournament.players.remove(p);
		tournament.playersCount--;
		session.setAttribute("tournament", tournament);
		playersList.renderAll();
		binder.loadAll();
	}
	
	public void onDeleteRound(Event e){
		Tournament tournament = (Tournament) session.getAttribute("tournament");
		
		Integer roundToRemove = (Integer) e.getData();
		if(roundToRemove > 0){
			
			int index = -1;
			for(Round r : tournament.roundsList){
				if (r.roundNumber == roundToRemove)
					index = tournament.roundsList.indexOf(r);
			}
			
			if (index != -1)
				tournament.roundsList.remove(index);
			tournament.rounds--;
			
			int i = 1;
			for(Round r : tournament.roundsList){
				r.roundNumber = i++;
				for(Game g : r.games){
					g.roundNumber = r.roundNumber;
				}
			}
			
			TournamentUtils.updatePlayersRanking(tournament.roundsList, tournament.players);

			
		}

		deleteTabs();
		reloadTabs(tournament);
		reloadRanking(tournament);
		reloadPlayerRankingTab(tournament);
		
		session.setAttribute("tournament", tournament);
		((Tab) page.getFellowIfAny("playerTab")).setSelected(true);
		
		binder.loadAll();
	}
	
	private Tournament reloadTabs (Tournament tournament){
		if(tournament.roundsList.size()  != 0){
			for(Round r : tournament.roundsList){
				addRoundTab(r);
			}
		}
		return tournament;
	}
	
	private Tab addRoundTab(Round r) {
		Tab tab = new Tab("Ronde "+r.roundNumber);
		tab.setId("round"+r.roundNumber+"Tab");
		tab.setParent(singleTabbox.getTabs());
		
		Tabpanel panel = new Tabpanel();
		panel.setId("round"+r.roundNumber+"Panel");
		panel.setParent(singleTabbox.getTabpanels());
		
		Include inc = new Include("round.zul");
		inc.setDynamicProperty("round", r);
		inc.setId("round"+r.roundNumber+"Include");
		inc.setParent(panel);
		
		return tab;
	}

	private void deleteTabs (){
		List<Component> componentsToRemove = new ArrayList<Component>();
		for(Component c : page.getFellows()){
			if(c.getId().matches("(round)?(ranking)?[0-9]+(Panel)?(Tab)?(Include)?")){
				componentsToRemove.add(c);
			}
		}
		
		for(Component c : componentsToRemove){
			c.setParent(null);
			c.detach();
			c.setId(null);
			c = null;
		}
	}
	
	private Tournament reloadPlayerRankingTab(Tournament tournament) {
		if(tournament.rankings.size() != 0){
			for(Ranking r : tournament.rankings){
				System.out.println("Reloading player ranking on round "+r.roundNumber);
				addResultTab(r);
			}
		}
		return tournament;
	}

	private void addResultTab(Ranking ranking){
		System.out.println("Adding result for round "+ranking.roundNumber);
		Tab tab = new Tab("Ronde "+ranking.roundNumber);
		tab.setId("ranking"+ranking.roundNumber+"Tab");
		
		tab.setParent(resultTabbox.getTabs());
		Tabpanel panel = new Tabpanel();
		panel.setId("ranking"+ranking.roundNumber+"Panel");
		panel.setParent(resultTabbox.getTabpanels());
		
		Include inc = new Include("ranking.zul");
		inc.setDynamicProperty("ranking", ranking);
		inc.setId("ranking"+ranking.roundNumber+"Include");
		inc.setParent(panel);
	}
	
	private void reloadRanking(Tournament tournament){
		tournament.rankings = new ArrayList<Ranking>();
		TournamentUtils.updatePlayersRanking(tournament.roundsList, tournament.players);
		if(tournament.rounds >= 1){
			for(int i = 1; i < tournament.roundsList.size(); i++){
				Ranking ranking = new Ranking();
				ranking.roundNumber = i;
				for(Player p : tournament.players){
					PlayerRanking pr = new PlayerRanking();
					pr.nickname = p.nickname;
					pr.goalAverage = p.getGoalAverage(i);
					pr.points = p.getPoints(i);
					ranking.playerRankings.add(pr);
				}
				
				tournament.rankings.add(ranking);
			}
		}
	}

}