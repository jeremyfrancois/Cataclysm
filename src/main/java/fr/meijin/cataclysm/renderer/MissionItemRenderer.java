package fr.meijin.cataclysm.renderer;

import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;

public class MissionItemRenderer implements ComboitemRenderer<String> {

	@Override
	public void render(Comboitem item, String data, int index) throws Exception {
		item.setLabel(data);
		item.setValue(data);
	}

}