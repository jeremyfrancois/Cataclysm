package fr.meijin.cataclysm.converter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

public class RankingConverter implements TypeConverter{

	@Override
	public Object coerceToBean(Object arg0, Component arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object coerceToUi(Object o, Component c) {
		return String.valueOf((Integer) o + 1);
	}

}