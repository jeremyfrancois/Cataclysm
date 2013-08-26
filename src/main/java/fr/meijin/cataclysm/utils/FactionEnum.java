package fr.meijin.cataclysm.utils;

import org.apache.commons.lang.StringUtils;

public enum FactionEnum {

	ASKARI ("askari", "Les Askaris"),
	BAMAKAS ("bamakas", "Les Bamakas"),
	CONVOI ("convoi", "Le Convoi"),
	HORDE ("horde", "La Horde"),
	ISC ("isc", "I.S.C"),
	JOKERS ("jokers", "Les Gang Jokers"),
	MATRIARCAT ("matriarcat", "Le Matriarcat de Sybille"),
	RESISTANCE ("resistance", "La Résistance");
	
	private String code;
	private String label;
	
	private FactionEnum(){
		
	}

	private FactionEnum(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public static String getDisplayName(String code){
		for(FactionEnum faction : values()){
			if(StringUtils.equals(faction.code, code)){
				return faction.label;
			}
		}
		return null;
	}
}
