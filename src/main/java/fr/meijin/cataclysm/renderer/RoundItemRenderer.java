package fr.meijin.cataclysm.renderer;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import fr.meijin.cataclysm.model.Game;

public class RoundItemRenderer implements ListitemRenderer<Game>{

	@Override
	public void render(Listitem item, Game g, int i) throws Exception {
		createLabelCell(String.valueOf(g.id)).setParent(item);
		createLabelCell(g.player1.nickname).setParent(item);
		createLabelCell(g.p1Result.mission).setParent(item);
		createLabelCell(g.p1Result.resultMission.toString()).setParent(item);
		createLabelCell(g.p1Result.resultTournoi.toString()).setParent(item);
		
		createLabelCell(g.player2.nickname).setParent(item);
		createLabelCell(g.p2Result.mission).setParent(item);
		createLabelCell(g.p2Result.resultMission.toString()).setParent(item);
		createLabelCell(g.p2Result.resultTournoi.toString()).setParent(item);

		Listcell cell = createListcellButtons(g);
		
		if(g.p1Result.resultMission == 0 && g.p1Result.resultTournoi == 0 
			&& g.p2Result.resultMission == 0 && g.p2Result.resultTournoi == 0){
			cell.setStyle("background : #FFADAD;");
		}
		cell.setParent(item);
	}

	private Listcell createLabelCell(String label) {
		if (label != null)
			return new Listcell(label);

		return new Listcell();
	}
	
	private Listcell createListcellButtons(final Game g){
		Listcell listcell = new Listcell();
		
		Button editButton = new Button("Modifier");
		editButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event e) throws Exception {
				Events.postEvent("onEditGame", (Component) e.getTarget().getParent().getParent().getParent(),  g);
			}
			
			
		});
		editButton.setMold("trendy");
		editButton.setParent(listcell);
		
		return listcell;
	}
}