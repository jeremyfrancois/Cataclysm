package fr.meijin.cataclysm.composer;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Grid;

import fr.meijin.cataclysm.model.Ranking;

public class RankingComposer extends GenericForwardComposer<Grid>{

	private static final long serialVersionUID = -1124776158818228201L;

	private Ranking ranking;
	
	private AnnotateDataBinder binder;
	
	public void doAfterCompose (Grid grid) throws Exception{
		super.doAfterCompose(grid);
		binder = new AnnotateDataBinder(grid);
		ranking = (Ranking) execution.getAttribute("ranking");
		
		
		page.setAttribute("playerRankings", ranking.playerRankings);
		binder.loadAll();
	}
}
