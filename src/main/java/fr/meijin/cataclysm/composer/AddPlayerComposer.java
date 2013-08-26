package fr.meijin.cataclysm.composer;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import fr.meijin.cataclysm.model.Player;
import fr.meijin.cataclysm.model.Tournament;

public class AddPlayerComposer extends GenericForwardComposer<Window>{

	private static final long serialVersionUID = 4975753713510981075L;
	
	private AnnotateDataBinder binder;
	private Textbox firstNameTextbox;
	private Textbox lastNameTextbox;
	private Textbox nicknameTextbox;
	private Combobox factionCombobox;
	private Checkbox forfeitCheckbox;

	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		binder = new AnnotateDataBinder(comp);
		Player oldPlayer = (Player) arg.get("oldPlayer");
		if(oldPlayer != null){
			firstNameTextbox.setValue(oldPlayer.getFirstName());
			lastNameTextbox.setValue(oldPlayer.getLastName());
			nicknameTextbox.setValue(oldPlayer.nickname);
			factionCombobox.setValue(oldPlayer.faction);
			forfeitCheckbox.setChecked(oldPlayer.forfeit);
		}
		binder.loadAll();
	}
	
	public void onOK(){
		onClick$validateButton(null);
	}

	public void onClick$validateButton (Event e){
		Tournament tournament = (Tournament) session.getAttribute("tournament");
		
		Player p = (Player) arg.get("oldPlayer");
		if(p == null){
			p = new Player();
			
			for(Player player : tournament.players){
				if(StringUtils.equals(player.nickname, nicknameTextbox.getValue())){
					Messagebox.show("Ce pseudonyme est déjà utilisé, veuillez en saisir un autre.");
					return;
				}
			}
			
			p.id = tournament.playersCount++;
		}
		
		p.firstName = firstNameTextbox.getValue();
		p.lastName = lastNameTextbox.getValue();
		p.nickname = nicknameTextbox.getValue();
		if(factionCombobox.getSelectedItem() != null)
			p.faction = factionCombobox.getSelectedItem().getValue();
		else 
			p.faction = factionCombobox.getValue();
		

		p.forfeit = forfeitCheckbox.isChecked();
		
		page.setAttribute("newPlayer",p);
		System.out.println("New player : "+p.nickname+" | "+p.faction);
		self.detach();
	}
	
	public void onClick$cancelButton (Event e){
		self.detach();
	}
}
