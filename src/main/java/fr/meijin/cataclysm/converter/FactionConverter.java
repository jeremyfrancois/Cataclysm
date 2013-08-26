package fr.meijin.cataclysm.converter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import fr.meijin.cataclysm.utils.FactionEnum;

public class FactionConverter implements TypeConverter {

	@Override
	public Object coerceToBean(Object o, Component c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object coerceToUi(Object o, Component c) {
		if(o instanceof String){
			String id = (String) o;
			if (c instanceof Image) {
				return "/images/"+id+".png";
			} else if (c instanceof Label) {

				return FactionEnum.getDisplayName(id);
			}
		}
		return "";
	}
	
	public static String coerceToHTML(String id) {
		if(id == null)
			return "";

		return "http://meijin91.free.fr/factions/"+ id + ".png";
	}
}